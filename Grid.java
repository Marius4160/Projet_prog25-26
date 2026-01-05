
import java.util.ArrayList;
import java.util.List;

public class Grid {

    private final int rows;
    private final int cols;

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

    private static TileType[][] copieGrid(TileType[][] grid) {
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }

    public EtatJeu etatSuivant(EtatJeu etat, Player.Direction dir) {

        Player.Position cible
                = etat.playerPos().translate(dir.dRow(), dir.dCol());

        if (!isInside(cible)) {
            return null;
        }

        TileType[][] grid = etat.grid();
        TileType contenu = grid[cible.row()][cible.col()];

        if (contenu == TileType.Mur || contenu == TileType.BlocDestructible) {
            return null;
        }
        TileType[][] newGrid = copieGrid(grid);
        int newEnergie = etat.energie();
        int newBombes = etat.bombes();
        List<Player.Position> newBlocs = new ArrayList<>(etat.blocs());

        if (estBloc(contenu)) {

            Player.Position nextBlocPos
                    = cible.translate(dir.dRow(), dir.dCol());

            if (!isInside(nextBlocPos)) {
                return null;
            }
            if (grid[nextBlocPos.row()][nextBlocPos.col()] != TileType.SolVide) {
                return null;
            }

            int cout = switch (contenu) {
                case BlocLeger ->
                    1;
                case BlocEnergivore ->
                    3;
                case BlocGlissant ->
                    0;
                default ->
                    0;
            };

            if (newEnergie < cout) {
                return null;
            }
            newEnergie -= cout;

            newGrid[nextBlocPos.row()][nextBlocPos.col()] = contenu;
            newGrid[cible.row()][cible.col()] = TileType.SolVide;

            newBlocs.remove(cible);
            newBlocs.add(nextBlocPos);
        }

        if (contenu == TileType.PileEnergie) {
            newEnergie += 5;
            newGrid[cible.row()][cible.col()] = TileType.SolVide;
        }

        if (contenu == TileType.Explosif) {
            newBombes++;
            newGrid[cible.row()][cible.col()] = TileType.SolVide;
        }

        newGrid[etat.playerPos().row()][etat.playerPos().col()] = TileType.SolVide;
        // Si la cible est un objectif, conserver la tuile Objectif plutôt que d'écraser par Joueur
        // anciennement cette ligne était :
        // newGrid[cible.row()][cible.col()] = TileType.Joueur;
        if (contenu == TileType.Objectif) {
            newGrid[cible.row()][cible.col()] = TileType.Objectif;
        } else {
            newGrid[cible.row()][cible.col()] = TileType.Joueur;
        }

        List<String> nouvAction = new ArrayList<>(etat.actions());
        nouvAction.add("MOVE " + dir);

        return new EtatJeu(newGrid, cible, newBlocs, newEnergie, newBombes, nouvAction);
    }

    public EtatJeu explose(EtatJeu etat, Player.Direction dir) {

        if (etat.bombes() <= 0) {
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

        List<Player.Position> newBlocs = new ArrayList<>(etat.blocs());
        newBlocs.remove(cible);

        List<String> newActions = new ArrayList<>(etat.actions());
        newActions.add("BOMB " + dir);

        return new EtatJeu(newGrid, etat.playerPos(), newBlocs, etat.energie(), etat.bombes() - 1, newActions);
    }

    public boolean isWin(EtatJeu etat) {
        TileType t
                = etat.grid()[etat.playerPos().row()][etat.playerPos().col()];
        return t == TileType.Objectif;
    }
}
