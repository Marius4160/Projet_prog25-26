import com.google.*;
import com.google.gson.Gson;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Loader {

    public static Level load(String fileName) throws Exception {
        String json = Files.readString(Path.of(fileName));
        return new Gson().fromJson(json, Level.class);
    }
    /*public static void main(String[] args) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("level/leveldemo.json");
            Level p = gson.fromJson(reader, Level.class);

            System.out.println("Nom " + p.nom);
            System.out.println("EnergieInitiale  "+p.energieInitiale);
            System.out.println("Grille ");
            for (String ligne : p.grille) {
                System.out.println(ligne);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }*/
}
