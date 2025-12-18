public class Grid {

    private final TileType [][] tiles;
    private int rows, cols;

    public Grid (TileType [][] tiles){
        this.tiles = tiles;
        this.rows= tiles.length;
        this.cols = tiles[0].length;
    }
    public TileType[][] getTiles() {
        return tiles;
    }
    
    public int rows() { return rows; }
    public int cols() { return cols; }

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
        Player.Position nextPos = blocPos.translate(dir.dRow(), dir.dCol());
        if (isInside(nextPos)== false) {
            return false;
        }
        if (tiles[nextPos.row()][nextPos.col()] != TileType.SolVide) {
            return false;
        }
        tiles[nextPos.row()][nextPos.col()] = tiles[blocPos.row()][blocPos.col()];
        tiles[blocPos.row()][blocPos.col()] = TileType.SolVide;

        return true;
    }

}

