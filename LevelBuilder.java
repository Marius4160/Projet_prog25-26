import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {

    public static TileType charToTile(char c) {
        return switch (c) {
            case '#' -> TileType.Mur;
            case '@' -> TileType.Joueur;
            case '.' -> TileType.SolVide;
            case 'o' -> TileType.Objectif;
            case 'E' -> TileType.PileEnergie;
            case 'D' -> TileType.BlocDestructible;
            case 'G' -> TileType.BlocGlissant;
            case 'L' -> TileType.BlocLeger;
            case 'X' -> TileType.Explosif;
            default -> throw new IllegalArgumentException("Caractère inconnu : " + c);
        };
    }

    public static TileType[][] buildGrid(Level level) {
        int rows = level.grille.length;
        int cols = level.grille[0].length();

        TileType[][] grid = new TileType[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] line = level.grille[i].toCharArray();
            for (int j = 0; j < cols; j++) {
                grid[i][j] = charToTile(line[j]);
            }
        }
        return grid;
    }

    public static List<Player.Position> findBlocs(TileType[][] grid) {
        List<Player.Position> blocs = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                TileType t = grid[i][j];
                if (t == TileType.BlocDestructible
                || t == TileType.BlocGlissant
                || t == TileType.BlocEnergivore
                || t == TileType.BlocLeger) {
                    blocs.add(new Player.Position(i, j));
                }
            }
        }
        return blocs;
    }

    public static EtatJeu buildEtatInitiale(Level level) {

        int rows = level.grille.length;
        int cols = level.grille[0].length();

        TileType[][] grid = new TileType[rows][cols];
        Player.Position playerPos = null;
        List<Player.Position> blocs = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        int bombes = 0;

        for (int i = 0; i < rows; i++) {
            char[] line = level.grille[i].toCharArray();

            for (int j = 0; j < cols; j++) {
                TileType t = charToTile(line[j]);

                if (t == TileType.Joueur) {
                    playerPos = new Player.Position(i, j);
                    grid[i][j] = TileType.SolVide;
                }
                else {
                    grid[i][j] = t;
                }

                if (t == TileType.BlocDestructible) {
                    blocs.add(new Player.Position(i, j));
                }
            }
        }
        return new EtatJeu(grid,playerPos,blocs,level.energieInitiale,bombes,actions);
    }
}
