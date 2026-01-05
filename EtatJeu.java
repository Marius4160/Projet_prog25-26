
import java.util.ArrayList;
import java.util.List;


public record EtatJeu(TileType[][] grid,Player.Position playerPos,List<Player.Position> blocs,int energie,int bombes,List<String> actions) {

    public EtatJeu {
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
        if (!(obj instanceof EtatJeu other)){
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


    public EtatJeu ajouterAction(String action) {
        List<String> newActions = new ArrayList<>(actions);
        newActions.add(action);
        return new EtatJeu(grid, playerPos, blocs, energie, bombes, newActions);
    }

    public EtatJeu consommerBombe() {
        if (bombes <= 0) return null;
        return new EtatJeu(grid, playerPos, blocs, energie, bombes - 1, actions);
    }
    public EtatJeu AjouterEnergie(int ajout) {
        return new EtatJeu(grid, playerPos, blocs, energie+ajout, bombes, actions);
    }

    public EtatJeu ConsommerEnergie(int consommation) {
        return new EtatJeu(grid, playerPos, blocs, energie-consommation, bombes, actions);
    }
    public EtatJeu deplacerBloc(Player.Position ancienne, Player.Position nouvelle) {
        List<Player.Position> newBlocs = new ArrayList<>(blocs);
        newBlocs.remove(ancienne);
        newBlocs.add(nouvelle);
        // consomme 1 d'energie pour bloc leger, 3 pour energivore, 0 pour glissant
        TileType typeBloc = grid[ancienne.row()][ancienne.col()];
        int coutEnergie = switch (typeBloc) {
            case BlocLeger ->
                1;
            case BlocEnergivore ->
                3;
            case BlocGlissant ->
                0;
            default ->
                0;
        };
        return new EtatJeu(grid, playerPos, newBlocs, energie - coutEnergie, bombes, actions);
    }

}

