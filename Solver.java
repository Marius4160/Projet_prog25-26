import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Solver {

    private final Grid grid;

    public Solver(Grid grid) {
        this.grid = grid;
    }
    public EtatJeu solve(EtatJeu initial) {

        Queue<EtatJeu> queue = new ArrayDeque<>();
        Set<EtatJeu> visited = new HashSet<>();

        queue.add(initial);
        visited.add(initial);

        while (!queue.isEmpty()) {

            EtatJeu courant = queue.poll();

            if (grid.isWin(courant)) {//teste la condition de victoire
                return courant;
            }

            for (Player.Direction dir : Player.Direction.values()) { //teste pur toute les directions et vérifie 
                EtatJeu move = grid.EtatSuivant(courant, dir);
                if (move != null && !visited.contains(move)) {
                    visited.add(move);
                    queue.add(move);
                }
                EtatJeu exp = grid.explose(courant, dir);
                if (exp != null && !visited.contains(exp)) {
                    visited.add(exp);
                    queue.add(exp);
                }
            }
        }
        return null;
    }

}
