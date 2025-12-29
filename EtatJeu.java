
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EtatJeu)) {
            return false;
        }
        EtatJeu other = (EtatJeu) obj;
        if (this.energie != other.energie) {
            return false;
        }
        if (this.playerPos.equals(other.playerPos) != false) { //teste la position du joueur 
            return false;
        }
        if (this.grid.length != other.grid.length) {
            return false;
        }
        if (this.grid[0].length != other.grid[0].length) {
            return false;
        }
        for (int i = 0; i < grid.length; i++) {  //comparaison case pas case de différent elemenet
            for (int j = 0; j < grid[0].length; j++) {
                if (this.grid[i][j] != other.grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
