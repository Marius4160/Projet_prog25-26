
public class Grid {

    private TileType[][] grid;
    private int rows;
    private int cols;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new TileType[rows][cols];
    }

    public Grid(TileType[][] t) {
        this.grid = t;
    }

    public void affichage() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                switch (grid[i][j]) {

                    case Mur ->
                        System.out.print("#");

                    case BlocDestructible ->
                        System.out.print("X");

                    case SolVide ->
                        System.out.print(" ");

                    case Joueur ->
                        System.out.print("@");

                    case Objectif ->
                        System.out.print(".");

                    case BlocLeger ->
                        System.out.print("o");

                    case BlocGlissant ->
                        System.out.print("O");

                    case BlocEnergivore ->
                        System.out.print("*");

                    case PileEnergie ->
                        System.out.print("+");

                    case Explosif ->
                        System.out.print("E");

                    default ->
                        System.out.print("?");
                }
            }
            System.out.println();
        }
    }
    public boolean pousserBloc(Player.Position blocPos, Player.Direction dir) {

        if (Grid.estBloc(grid, blocPos)==false) {
            return false;
        }
        Player.Position nextPos = blocPos.translate(dir.dRow(), dir.dCol());
        if (isInside(nextPos)== false) {
            return false;
        }
        if (grid[nextPos.row()][nextPos.col()] != TileType.SolVide) {
            return false;
        }
        grid[nextPos.row()][nextPos.col()] = grid[blocPos.row()][blocPos.col()];
        grid[blocPos.row()][blocPos.col()] = TileType.SolVide;

        
        return true;
    }
}


