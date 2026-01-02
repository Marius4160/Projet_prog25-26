
public record EtatJeu(TileType[][] grid, Player.Position playerPos, int energie) {

    public EtatJeu   {
        grid = copieGrid(grid);
    }

    private static TileType[][] copieGrid(TileType[][] grid) {
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }

    @Override
    public boolean equals(Object obj) { // Override le equals car celui du record de base ne convient pas
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EtatJeu other)) { // vérifie que obj est bien un EtatJeu, créer la variable other 
            return false;
        }

        if (energie != other.energie) { //test pour savoir si l'energie est différente 
            return false;
        }
        if (playerPos.equals(other.playerPos) == false) { // test pour les positions si elles sont égales
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
    public int hashCode() { // même chose que pour le equals --> on cherche à savoir si deux état sont différents 
        int result = energie;                        // set l'energie pour pouvoir tester si l'energie est la meme entre deux état
        result = 31 * result + playerPos.hashCode(); //nombre premirer + simple avec l'addition de la référence de la position du Player
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                result = 31 * result + grid[i][j].ordinal(); // donne le type de la case
            }
        }
        return result;
    }
}
