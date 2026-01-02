public class Grid {

    private final TileType [][] tiles;
    private int rows, cols;

    public Grid (TileType [][] tiles){
        this.tiles = tiles;
        this.rows= tiles.length;
        this.cols = tiles[0].length;
    }
    public int rows() { return rows; }
    public int cols() { return cols; }

    public TileType[][] getTiles() {
        return tiles;
    }

    public boolean isInside(Player.Position p) {
        return p.row() >= 0 && p.row() < rows &&
               p.col() >= 0 && p.col() < cols;
    }

    public static boolean estBloc(TileType[][]tile, Player.Position p){
        if(tile[p.row()][p.col()]==TileType.BlocDestructible){
            return true;
        }else if(tile[p.row()][p.col()]==TileType.BlocEnergivore){
            return true;
        }else if(tile[p.row()][p.col()]==TileType.BlocGlissant){
            return true;
        }else if(tile[p.row()][p.col()]==TileType.BlocLeger){
            return true;
        }
        return false;
    }
    
    public TileType typeBloc(TileType[][]tile ,Player.Position p){
        if(isInside(p)){
            if(tile [p.row()][p.col()] == TileType.BlocDestructible){
                return TileType.BlocDestructible;
            }else if(tile [p.row()][p.col()] == TileType.BlocGlissant){
                return TileType.BlocGlissant;
            }else if(tile [p.row()][p.col()] == TileType.BlocEnergivore){
                return TileType.BlocEnergivore;
            }else if(tile [p.row()][p.col()] == TileType.BlocLeger){
                return TileType.BlocLeger;
            }
        }
        return null;
    }
    public boolean pousserBloc(Player.Position blocPos, Player.Direction dir) {

        if (Grid.estBloc(tiles, blocPos)==false) {
            return false;
        }
        Player.Position nextPos = blocPos.translate(dir.dRow(), dir.dCol());//clacul la prochaine position du joueur en fonction de sa direction
        if (isInside(nextPos)== false) {
            return false;
        }
        if (tiles[nextPos.row()][nextPos.col()] != TileType.SolVide) {
            return false;
        }
        tiles[nextPos.row()][nextPos.col()] = tiles[blocPos.row()][blocPos.col()];//la nouvelle position du joueur devient la position du bloc
        tiles[blocPos.row()][blocPos.col()] = TileType.SolVide;

        return true;
    }
    private static TileType[][] copieGrid(TileType[][] grid) {
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }
    public EtatJeu nextState(EtatJeu etat, Player.Direction dir) {

        Player.Position newPos = etat.playerPos().translate(dir.dRow(), dir.dCol());

        if (isInside(newPos)==false) return null; // Hors grille

        TileType[][] newGrid = copieGrid(etat.grid());
        TileType bloc = newGrid[newPos.row()][newPos.col()];

        if (bloc == TileType.Mur) {// verifie si le type à la position bloc est Mur
            return null;
        }

        int newEnergie = etat.energie();
        if (bloc == TileType.PileEnergie) {
            newEnergie += 5;
            newGrid[newPos.row()][newPos.col()] = TileType.SolVide;
        } else if (bloc == TileType.BlocEnergivore) {
            newEnergie -= 1; // Bloc énergivore
            if (newEnergie < 0) return null; // Pas assez d'énergie
        }
        return new EtatJeu(newGrid, newPos, newEnergie);
    }

    public EtatJeu explose(EtatJeu etat, Player.Direction dir) {

        if (etat.energie() <= 0) {
            return null;
            } 
        Player.Position target = etat.playerPos().translate(dir.dRow(), dir.dCol());

            if (!isInside(target)){ 
                return null;
            }
        TileType[][] newGrid = copieGrid(etat.grid());
        TileType t = newGrid[target.row()][target.col()];

        if (t != TileType.BlocDestructible && t != TileType.Explosif) {
            return null;
        }
        newGrid[target.row()][target.col()] = TileType.SolVide;
        return new EtatJeu(newGrid, etat.playerPos(), etat.energie() - 1);
    }
    public boolean isWin(EtatJeu etat) {
        TileType t = etat.grid()[etat.playerPos().row()][etat.playerPos().col()];
        return t == TileType.Objectif;
    }

}

