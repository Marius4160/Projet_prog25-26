
public class Game {

    public record GameState(TileType[][] grid, Player.Position pos, int energie, boolean result) {

    }

}
