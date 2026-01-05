
import java.util.Scanner; // utilisation scanner pour les touches

public class Player {

    private final int row;
    private final int col;
    private int energie;
    private int explosif;

    public Player(int row, int col) {
        this.row = row;
        this.col = col;

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getEnergie() {
        return energie;
    }

    public int getExplosif() {
        return explosif;
    }

    public void setEnergy(int energie) {
        this.energie = energie;
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

    /**
     * Lance une session de jeu manuel dans la console. Permet de tester les
     * déplacements (z,q,s,d) et les bombes (b).
     *
     * @param grid La grille contenant la logique du jeu (collisions, etc.).
     * @param etat L'état initial de la partie.
     */
    public static void jouerManuel(Grid grid, EtatJeu etat) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== MODE MANUEL ===");
        System.out.println("Commandes : z (haut), q (gauche), s (bas), d (droite)");
        System.out.println("Explosion : b puis direction");
        System.out.println("Quitter   : exit");

        while (true) {
            System.out.println("\n--- TOUR ---");
            System.out.println("Énergie: " + etat.energie() + " | Bombes: " + etat.bombes());
            afficherGrille(etat.grid(), etat.playerPos());

            if (grid.isWin(etat)) {
                System.out.println("VICTOIRE !");
                break;
            }

            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.equals("exit")) {
                break;
            }
            if (input.isEmpty()) {
                continue;
            }

            EtatJeu next = null;
            // Gestion de l'explosion (touche 'b' suivie d'une direction)
            if (input.startsWith("b")) {
                System.out.print("Direction explosion > ");
                String dirStr = scanner.nextLine().trim();
                if (!dirStr.isEmpty()) {
                    Direction dir = Direction.fromChar(dirStr.charAt(0));
                    if (dir != null) {
                        next = grid.explose(etat, dir);
                    }
                }
            } else {
                // Gestion du déplacement standard
                Direction dir = Direction.fromChar(input.charAt(0));
                if (dir != null) {
                    next = grid.etatSuivant(etat, dir);
                }
            }

            if (next != null) {
                etat = next;
            } else {
                System.out.println("Action impossible !");
            }
        }
    }

    /**
     * Affiche la grille dans la console pour le mode manuel.
     */
    private static void afficherGrille(TileType[][] grid, Position playerPos) {
        for (int i = 0; i < grid.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < grid[0].length; j++) {
                if (i == playerPos.row() && j == playerPos.col()) {
                    sb.append("@"); // Représentation du joueur
                } else {
                    sb.append(grid[i][j].toSymbol());
                }
            }
            System.out.println(sb.toString());
        }
    }

}
