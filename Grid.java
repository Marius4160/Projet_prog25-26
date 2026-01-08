
import java.util.ArrayList;
import java.util.List;

public class Grid {

    private final int rows;
    private final int cols;

    public Grid(TileType[][] tiles) {
        this.rows = tiles.length;
        this.cols = tiles[0].length;
    }

    public int rows() {//guetters
        return rows;
    }

    public int cols() {//guetters
        return cols;
    }

    public boolean isInside(Player.Position p) { // verifie que la position p est bien dans la grille
        return p.row() >= 0 && p.row() < rows
                && p.col() >= 0 && p.col() < cols;
    }

    public boolean estBloc(TileType t) { //renvoie true si le bloc est un bloc peut import lequel
        return t == TileType.BlocGlissant
                || t == TileType.BlocEnergivore
                || t == TileType.BlocLeger;
    }

    public static Player.Position findPlayer(TileType[][] grid) { // renvoie la position du player dans une grid donnée
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == TileType.Joueur) {
                    return new Player.Position(i, j);
                }
            }
        }
        return null;
    }

    private static TileType[][] copieGrid(TileType[][] grid) { // clone la grille pour ne pas faire de modification direct 
        TileType[][] copie = new TileType[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copie[i] = grid[i].clone();
        }
        return copie;
    }

    public EtatJeu etatSuivant(EtatJeu etat, Player.Direction dir) { // renvoie le record apres modifications de chaque element

        Player.Position cible = etat.playerPos().translate(dir.dRow(), dir.dCol()); // calcul la position de la case devant le joueur grace à la direction

        if (!isInside(cible)) { // empeche la sortie de grille
            return null;
        }

        TileType[][] grid = etat.grid();
        TileType contenu = grid[cible.row()][cible.col()];

        if (contenu == TileType.Mur || contenu == TileType.BlocDestructible) { //impossible d'avancer
            return null;
        }
        TileType[][] newGrid = copieGrid(grid); 
        int newEnergie = etat.energie();
        int newBombes = etat.bombes();
        List<Player.Position> newBlocs = new ArrayList<>(etat.blocs());
        //copie de la grille dans l'etat pour respecter l'immuabilité 

        // cas où le bloc est poussable
        if (estBloc(contenu)) {
            //position où le bloc doit etre déplacé
            Player.Position nextBlocPos = cible.translate(dir.dRow(), dir.dCol());

            if (!isInside(nextBlocPos)) {//test si la poussé est possible avec les contraintes imposées 
                return null;
            }
            if (grid[nextBlocPos.row()][nextBlocPos.col()] != TileType.SolVide) {
                return null;
            }
            // cout energie selon le bloc 
            int cout = switch (contenu) {
                case BlocLeger ->
                    1;
                case BlocEnergivore ->
                    3;
                case BlocGlissant ->
                    0;
                default ->
                    0;
            };

            if (newEnergie < cout) {
                return null;
            }
            newEnergie -= cout;
            //déplace le bloc et remplace la poistion initiale par un SolVide
            newGrid[nextBlocPos.row()][nextBlocPos.col()] = contenu;
            newGrid[cible.row()][cible.col()] = TileType.SolVide;

            newBlocs.remove(cible);
            newBlocs.add(nextBlocPos);
        }
        //Recuperation d'énergie
        if (contenu == TileType.PileEnergie) {
            newEnergie += 5;
            newGrid[cible.row()][cible.col()] = TileType.SolVide;
        }
        //récupération d'explosif
        if (contenu == TileType.Explosif) {
            newBombes++;
            newGrid[cible.row()][cible.col()] = TileType.SolVide;
        }
        newGrid[etat.playerPos().row()][etat.playerPos().col()] = TileType.SolVide;
        //test si l'objectif est atteint pour ne pas le remplacer par un sol vide si le joueur passe dessus
        if (contenu == TileType.Objectif) {
            newGrid[cible.row()][cible.col()] = TileType.Objectif;
        } else {
            newGrid[cible.row()][cible.col()] = TileType.Joueur;
        }
        //Ajout l'action à l'hisorique
        List<String> nouvAction = new ArrayList<>(etat.actions());
        nouvAction.add("MOVE " + dir);
        //renvoie le nouvel EtatJeu
        return new EtatJeu(newGrid, cible, newBlocs, newEnergie, newBombes, nouvAction);
    }

    public EtatJeu explose(EtatJeu etat, Player.Direction dir) { // controle l'explosion des blocs

        if (etat.bombes() <= 0) {//pas de bombe
            return null;
        }
        //case ciblée par l'exploqion//
        Player.Position cible = etat.playerPos().translate(dir.dRow(), dir.dCol()); 
        
        if (!isInside(cible)) {
            return null;
        }

        TileType[][] newGrid = copieGrid(etat.grid());
        TileType t = newGrid[cible.row()][cible.col()];

        if (t != TileType.BlocDestructible ) {
            return null;
        }

        newGrid[cible.row()][cible.col()] = TileType.SolVide;
       
        List<Player.Position> newBlocs = new ArrayList<>(etat.blocs());  //liste des blocs mises à jour//
        newBlocs.remove(cible);
        
        List<String> newActions = new ArrayList<>(etat.actions()); /*//ajoute l'action à la liste du record */
        newActions.add("BOMB " + dir);
        /*renvoie le nouveau record */
        return new EtatJeu(newGrid, etat.playerPos(), newBlocs, etat.energie(), etat.bombes() - 1, newActions);
    }
    /*test si le jeu est gagné  */
    public boolean isWin(EtatJeu etat) {
        TileType t = etat.grid()[etat.playerPos().row()][etat.playerPos().col()];
        return t == TileType.Objectif;
    }
}
