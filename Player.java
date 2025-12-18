
public class Player {

    private int row;
    private int col;

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static record Position(int row, int col) {

        public Position translate(int dr, int dc) {
            return new Position(row + dr, col + dc);
        }
    }

    public static record ActionResult(Position newPosition, boolean win) {

    }

    public static enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

        private final int dr, dc;

        Direction(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }

        public int dRow() {
            return dr;
        }

        public int dCol() {
            return dc;
        }

        public static Direction fromChar(char c) {
            return switch (c) {
                case 'z' ->
                    UP;
                case 's' ->
                    DOWN;
                case 'q' ->
                    LEFT;
                case 'd' ->
                    RIGHT;
                default ->
                    null;
            };
        }
    }

    public Player movedTo(Position newPosition) {
        return new Player(newPosition.row, newPosition.col);
    }
}
