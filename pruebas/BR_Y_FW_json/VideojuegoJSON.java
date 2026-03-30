import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoJSON {

    public static void guardarJSON(List<Videojuego> videojuegos, String ruta) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            String json = writer.writeValueAsString(videojuegos);

            try (BufferedWriter bw = Files.newBufferedWriter(
                    Path.of(ruta),
                    StandardCharsets.UTF_8)) {
                bw.write(json);
            }

            System.out.println("JSON guardado en: " + ruta);

        } catch (IOException e) {
            System.err.println("Error al guardar JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Videojuego> lista = new ArrayList<>();

        lista.add(new Videojuego(1, "The Legend of Zelda", "Nintendo", "Aventura", true));
        lista.add(new Videojuego(2, "God of War", "Santa Monica Studio", "Acción", true));
        lista.add(new Videojuego(3, "Cyberpunk 2077", "CD Projekt Red", "RPG", false));

        guardarJSON(lista, "videojuegos.json");
    }
}
