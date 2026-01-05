
public class Grid {

    private final int rows, cols;

    public Grid(TileType[][] tiles) {
        this.rows = tiles.length;
        this.cols = tiles[0].length;
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    public boolean isInside(Player.Position p) {
        return p.row() >= 0 && p.row() < rows
                && p.col() >= 0 && p.col() < cols;
    }

    public boolean estBloc(TileType t) {
        return t == TileType.BlocGlissant
                || t == TileType.BlocEnergivore
                || t == TileType.BlocLeger;
    }

    public static Player.Position findPlayer(TileType[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == TileType.Joueur) {
                    return new Player.Position(i, j);
                }
            }
        }
        return null;
    }

    public boolean pousserBloc(TileType[][] grid, Player.Position blocPos, Player.Direction dir) {
        Player.Position nextPos
                = blocPos.translate(dir.dRow(), dir.dCol());

        if (isInside(nextPos) == false) {
            return false;
        }
        if (grid[nextPos.row()][nextPos.col()] != TileType.SolVide) {
            return false;
        }

        return true;
    }

    private static TileType[][] copieGrid(TileType[][] grid) {
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }

    public EtatJeu EtatSuivant(EtatJeu etat, Player.Direction dir) {

        Player.Position newPos
                = etat.playerPos().translate(dir.dRow(), dir.dCol());

        if (!isInside(newPos)) {
            return null;
        }

        TileType[][] grid = etat.grid();
        TileType cible = grid[newPos.row()][newPos.col()];

        // Mur impossible 
        if (cible == TileType.Mur) {
            return null;
        }

        TileType[][] newGrid = copieGrid(grid);
        int newEnergie = etat.energie();

        // Pile d'énergie
        if (cible == TileType.PileEnergie) {
            newEnergie += 5;
            newGrid[newPos.row()][newPos.col()] = TileType.SolVide;
        }

        // Bloc énergivore
        if (cible == TileType.BlocEnergivore) {
            newEnergie -= 5;
            if (newEnergie < 0) {
                return null;
            }
        }

        return new EtatJeu(newGrid, newPos, newEnergie);
    }

    public EtatJeu explose(EtatJeu etat, Player.Direction dir) {

        if (etat.energie() <= 0) {
            return null;
        }

        Player.Position cible
                = etat.playerPos().translate(dir.dRow(), dir.dCol());

        if (!isInside(cible)) {
            return null;
        }

        TileType[][] newGrid = copieGrid(etat.grid());
        TileType t = newGrid[cible.row()][cible.col()];

        if (t != TileType.BlocDestructible && t != TileType.Explosif) {
            return null;
        }

        newGrid[cible.row()][cible.col()] = TileType.SolVide;

        return new EtatJeu(newGrid, etat.playerPos(), etat.energie() - 1);
    }

    public boolean isWin(EtatJeu etat) {
        TileType t = etat.grid()[etat.playerPos().row()][etat.playerPos().col()];
        return t == TileType.Objectif;
    }
}

