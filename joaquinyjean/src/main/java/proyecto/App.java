package proyecto;

import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import proyecto.colecciones_videojuego.Videojuego;
import proyecto.colecciones_soundtrack.SoundtrackVideojuego;
import proyecto.persistencia.gestorFicherosCSV;
import proyecto.persistencia.gestorFicherosJSON;
import proyecto.consola.MenuConsola;
import proyecto.gui.VentanaGUI;

/**
 * Clase principal de GameVault.
 * Carga los videojuegos del CSV y los soundtracks del JSON,
 * y permite elegir entre la interfaz grafica o el menu de consola.
 */
public class App {
    private static final String RUTA_CSV = "src/main/resources/biblioteca_juegos.csv";
    private static final String RUTA_JSON = "src/main/resources/soundtracks.json";

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       GAMEVAULT - TU BIBLIOTECA PERSONAL        ");
        System.out.println("=================================================\n");

        // Cargar videojuegos del CSV
        System.out.println("[+] Cargando videojuegos (CSV)...");
        TreeMap<Integer, Videojuego> videojuegos = gestorFicherosCSV.leerCSV(RUTA_CSV);
        System.out.println("    Videojuegos cargados: " + videojuegos.size());

        // Cargar soundtracks del JSON
        System.out.println("[+] Cargando soundtracks (JSON)...");
        List<SoundtrackVideojuego> soundtracks;
        try {
            soundtracks = gestorFicherosJSON.leerJSON(RUTA_JSON);
            System.out.println("    Soundtracks cargados: " + soundtracks.size());
        } catch (Exception e) {
            System.err.println("Error al cargar soundtracks: " + e.getMessage());
            soundtracks = new java.util.ArrayList<>();
        }

        // Elegir modo
        System.out.println("\nElige el modo:");
        System.out.println("  1. Interfaz grafica (GUI)");
        System.out.println("  2. Consola");
        System.out.print("Opcion: ");

        Scanner sc = new Scanner(System.in);
        String opcion = sc.nextLine().trim();

        if (opcion.equals("2")) {
            // Modo consola
            MenuConsola menu = new MenuConsola(videojuegos, soundtracks, RUTA_CSV, RUTA_JSON);
            menu.iniciar();
        } else {
            // Modo GUI (por defecto)
            final List<SoundtrackVideojuego> stFinal = soundtracks;
            final TreeMap<Integer, Videojuego> vjFinal = videojuegos;
            javax.swing.SwingUtilities.invokeLater(() -> {
                new VentanaGUI(vjFinal, stFinal, RUTA_CSV, RUTA_JSON);
            });
        }
    }
}
