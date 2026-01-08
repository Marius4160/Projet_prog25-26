
import com.google.*;
import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;

public class Loader {

    // Charge un niveau depuis un fichier JSON et le retourne sous forme d'un objet Level
    public static Level load(String fileName) throws Exception {
        String json = Files.readString(Path.of(fileName));
        return new Gson().fromJson(json, Level.class);
    }

    public static void afficherGrille(EtatJeu etat) { // affiche la grille dans la console
        System.out.println("Grille ");
        for (TileType[] row : etat.grid()) {
            StringBuilder sb = new StringBuilder();
            for (TileType t : row) {
                sb.append(t.toSymbol());
            }
            System.out.println(sb);
        }
    }
}
