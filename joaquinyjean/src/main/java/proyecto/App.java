package proyecto;

import java.util.TreeMap;
import java.util.List;
import java.io.IOException;

import proyecto.colecciones_videojuego.Videojuego;
import proyecto.colecciones_soundtrack.SoundtrackVideojuego;
import proyecto.persistencia.GestorFicherosCSV;
import proyecto.persistencia.GestorFicherosJSON;

/**
 * Clase principal que ensambla todo el proyecto GameVault.
 * Carga el catálogo de videojuegos (CSV) y soundtracks (JSON)
 * y los muestra en un formato tabular por consola.
 */
public class App {
    private static final String RUTA_CSV = "src/main/resources/biblioteca_juegos.csv";
    private static final String RUTA_JSON = "src/main/resources/soundtracks.json";

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       GAMEVAULT - TU BIBLIOTECA PERSONAL        ");
        System.out.println("=================================================\n");

        try {
            // 1. Cargar y mostrar Videojuegos
            System.out.println("[+] Cargando Videojuegos (CSV)...");
            TreeMap<Integer, Videojuego> videojuegos = GestorFicherosCSV.leerCSV(RUTA_CSV);
            mostrarTablaVideojuegos(videojuegos);

            System.out.println("\n");

            // 2. Cargar y mostrar Soundtracks
            System.out.println("[+] Cargando Soundtracks (JSON)...");
            List<SoundtrackVideojuego> soundtracks = GestorFicherosJSON.leerJSON(RUTA_JSON);
            GestorFicherosJSON.listarSoundtracks(soundtracks);

            System.out.println("\n=================================================");
            System.out.println("   Carga de datos finalizada correctamente.      ");
            System.out.println("=================================================");

        } catch (IOException e) {
            System.err.println("Error al cargar la persistencia: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Muestra la lista de videojuegos en un formato tabular legible.
     * @param videojuegos Mapa de videojuegos indexados por ID.
     */
    private static void mostrarTablaVideojuegos(TreeMap<Integer, Videojuego> videojuegos) {
        if (videojuegos == null || videojuegos.isEmpty()) {
            System.out.println("No hay videojuegos registrados en el sistema.");
            return;
        }

        System.out.printf("%-5s | %-25s | %-20s | %-15s | %-10s%n", "ID", "Nombre", "Desarrolladora", "Géneros", "Disp.");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (Videojuego v : videojuegos.values()) {
            System.out.printf("%-5d | %-25.25s | %-20.20s | %-15.15s | %-10s%n",
                    v.getId(),
                    v.getNombre(),
                    v.getDesarrolladora(),
                    v.getStringGenero('.'),
                    v.getDisponible() ? "SÍ" : "NO"
            );
        }
    }
}
