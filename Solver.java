
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
                EtatJeu move = grid.etatSuivant(courant, dir);
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

    // Convertir un TileType en caractère pour l'affichage
    private static char tileToChar(TileType t) {
        return switch (t) {
            case Mur ->
                '#';
            case Joueur ->
                '@';
            case Objectif ->
                '.';
            case PileEnergie ->
                '+';
            case Explosif ->
                'E';
            case BlocEnergivore ->
                '*';
            case BlocDestructible ->
                'X';
            case BlocLeger ->
                'o';
            case BlocGlissant ->
                'O';
            case SolVide ->
                ' ';
        };
    }

    // Afficher la grille
    private static void afficherGrille(TileType[][] grid, Player.Position playerPos) {
        for (int i = 0; i < grid.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < grid[0].length; j++) {
                if (i == playerPos.row() && j == playerPos.col()) {
                    sb.append('@');
                } else {
                    sb.append(tileToChar(grid[i][j]));
                }
            }
            System.out.println(sb.toString());
        }
    }

    // main de test et démonstration
    public static void loop() {
        try {
            // Charger un niveau depuis le fichier JSON sans dépendance externe
            java.util.List<String> linesRaw = java.nio.file.Files.readAllLines(java.nio.file.Path.of("niveau2.json"));
            String nom = "";
            int energieInitiale = 0;
            java.util.List<String> grilleLines = new java.util.ArrayList<>();
            boolean inGrille = false;

            for (String l : linesRaw) {
                String s = l.trim();
                if (s.startsWith("\"nom\"")) {
                    nom = s.substring(s.indexOf(':') + 1).replaceAll("[\",]", "").trim();
                }
                if (s.startsWith("\"energieInitiale\"")) {
                    String num = s.substring(s.indexOf(':') + 1).replaceAll("[^0-9]", "");
                    energieInitiale = Integer.parseInt(num);
                }
                if (s.startsWith("\"grille\"")) {
                    inGrille = true;
                    continue;
                }
                if (inGrille) {
                    if (s.startsWith("]")) {
                        break;
                    }
                    String row = s.replaceAll("[\",]", "").trim();
                    if (!row.isEmpty()) {
                        grilleLines.add(row);
                    }
                }
            }

            System.out.println("=== RÉSOLUTION DU PUZZLE ===\n");
            System.out.println("Niveau : " + nom);
            System.out.println("Énergie initiale : " + energieInitiale);
            System.out.println("Grille initiale:");
            for (String line : grilleLines) {
                System.out.println(line);
            }

            // Construire la grille et l'état initial
            String[] lines = grilleLines.toArray(new String[0]);
            int rows = lines.length;
            int cols = lines[0].length();
            TileType[][] tiles = new TileType[rows][cols];
            java.util.List<Player.Position> blocs = new java.util.ArrayList<>();
            Player.Position playerPos = null;

            for (int i = 0; i < rows; i++) {
                String line = lines[i];
                for (int j = 0; j < cols; j++) {
                    char c = j < line.length() ? line.charAt(j) : ' ';
                    TileType t;
                    switch (c) {
                        case '#' ->
                            t = TileType.Mur;
                        case '@' -> {
                            t = TileType.Joueur;
                            playerPos = new Player.Position(i, j);
                        }
                        case '.' ->
                            t = TileType.Objectif;
                        case '+' ->
                            t = TileType.PileEnergie;
                        case '*' -> {
                            t = TileType.BlocEnergivore;
                            blocs.add(new Player.Position(i, j));
                        }
                        case 'E' ->
                            t = TileType.Explosif;
                        case 'O' -> {
                            t = TileType.BlocLeger;
                            blocs.add(new Player.Position(i, j));
                        }
                        case 'X' -> {
                            t = TileType.BlocDestructible;
                            blocs.add(new Player.Position(i, j));
                        }
                        default ->
                            t = TileType.SolVide;
                    }
                    tiles[i][j] = t;
                }
            }

            if (playerPos == null) {
                System.err.println("Aucun joueur trouvé dans la grille.");
                return;
            }

            // Résoudre le puzzle
            Grid grid = new Grid(tiles);
            EtatJeu etatInitial = new EtatJeu(tiles, playerPos, blocs, energieInitiale, 0, new java.util.ArrayList<>());

            // Test manuel des commandes
            Player.jouerManuel(grid, etatInitial); // Décommenter pour jouer manuellement

            /*  // ligne a enlever pour activer le solver automatique
            Solver solver = new Solver(grid);
            EtatJeu solution = solver.solve(etatInitial);
            
            // Afficher le résultat
            if (solution != null) {
                System.out.println("\n✓ PUZZLE RÉSOLU !\n");
                System.out.println("Statistiques finales:");
                System.out.println("  • Actions : " + solution.actions().size());
                System.out.println("  • Énergie : " + solution.energie());
                System.out.println("  • Bombes : " + solution.bombes());

                System.out.println("\nGrille finale:");
                afficherGrille(solution.grid(), solution.playerPos());

                // Replay étape par étape
                System.out.println("\n=== REPLAY ÉTAPE PAR ÉTAPE ===\n");
                EtatJeu current = etatInitial;

                System.out.println("ÉTAPE 0 (Initiale)");
                System.out.println("Énergie: " + current.energie() + " | Bombes: " + current.bombes());
                afficherGrille(current.grid(), current.playerPos());

                int step = 1;
                for (String act : solution.actions()) {
                    System.out.println("\n" + "─".repeat(20));
                    System.out.println("ÉTAPE " + step + " - Action: " + act);

                    String[] parts = act.split(" ");
                    Player.Direction dir = Player.Direction.valueOf(parts[1]);
                    EtatJeu next = parts[0].equals("MOVE")
                            ? grid.etatSuivant(current, dir)
                            : grid.explose(current, dir);

                    if (next == null) {
                        System.out.println("Erreur : action invalide");
                        break;
                    }

                    System.out.println("Énergie: " + next.energie() + " | Bombes: " + next.bombes());
                    afficherGrille(next.grid(), next.playerPos());
                    current = next;
                    step++;
                }
            } else {
                System.out.println("\n✗ PUZZLE NON RÉSOLVABLE !");
            }
            // ligne a enlever pour activer le solver automatique ou ajouter  si on veut le garder désactivé
             */
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du niveau :");
            e.printStackTrace();
        }
    }

}
