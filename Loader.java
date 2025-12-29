import com.google.gson.*;
import java.nio.file.*;

class Loader {
    static Level load(String filename) throws Exception {
    var json = Files.readString(Path.of(filename));
    return new Gson().fromJson(json, Level.class);
    }
}
