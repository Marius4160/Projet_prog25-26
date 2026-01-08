
import java.util.List;

public record EtatJeu(TileType[][] grid, Player.Position playerPos, List<Player.Position> blocs, int energie, int bombes, List<String> actions) {

    public EtatJeu      {
        grid = copieGrid(grid);
        blocs = List.copyOf(blocs);
        actions = List.copyOf(actions);
    }

    private static TileType[][] copieGrid(TileType[][] grid) {  // clone la grille pour ne pas faire de modification direct et pouvoir faire les tests equals et hashcode
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }

    @Override
    public boolean equals(Object obj) { // réecrit le equals initialiser par le record EtatJeu
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EtatJeu other)) { // test si l'objet passé en parametre est un etat EtatJeu et si il est le meme que other <=> EtatJeu other = (EtatJeu) obj; --> cast
            return false;
        }
        if (energie != other.energie) { //test si l'energie du record est la meme que celle de celle avec laquelle elle est comparée
            return false;
        }
        if (bombes != other.bombes) {//test si le nombre de bombe du record est le meme que celui avec lequel il est comparé
            return false;
        }
        if (!playerPos.equals(other.playerPos)) { //test si la position du Player du record est la meme que celle avec laquelle elle est comparée
            return false;
        }
        if (!blocs.equals(other.blocs)) { // test si la liste des blocs du record est la meme que celle avec laquelle elle est comparée
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
    public int hashCode() { //reecrit le hashcode initial du record EtatJeu
        int result = energie;
        result = 31 * result + bombes; //test pour chaque etat si il a déja été visité en 
        result = 31 * result + playerPos.hashCode(); //ajoutant à la varaible résulte le nombre de bombes 
        result = 31 * result + blocs.hashCode(); //mais aussi la valeur renvoyée par le hashCode de la position joueur et blocs

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                result = 31 * result + grid[i][j].ordinal();
            }
        }
        return result;
    }
}
