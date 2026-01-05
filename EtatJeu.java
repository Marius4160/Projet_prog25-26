
import java.util.List;

public record EtatJeu(TileType[][] grid, Player.Position playerPos, List<Player.Position> blocs, int energie, int bombes, List<String> actions) {

    public EtatJeu      {
        grid = copieGrid(grid);
        blocs = List.copyOf(blocs);
        actions = List.copyOf(actions);
    }

    private static TileType[][] copieGrid(TileType[][] grid) {
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EtatJeu other)) {
            return false;
        }
        if (energie != other.energie) {
            return false;
        }
        if (bombes != other.bombes) {
            return false;
        }
        if (!playerPos.equals(other.playerPos)) {
            return false;
        }
        if (!blocs.equals(other.blocs)) {
            return false;
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != other.grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = energie;
        result = 31 * result + bombes;
        result = 31 * result + playerPos.hashCode();
        result = 31 * result + blocs.hashCode();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                result = 31 * result + grid[i][j].ordinal();
            }
        }
        return result;
    }
}
