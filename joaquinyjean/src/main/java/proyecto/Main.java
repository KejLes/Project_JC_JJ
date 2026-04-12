package proyecto;

import java.io.IOException;
import java.util.List;

import proyecto.coleccion_soundtrack.Soundtrack;
import proyecto.coleccion_videojuego.Videojuego;
import proyecto.colecciones.Colecciones;
import proyecto.consola.Consola;
import proyecto.gui.GameVaultGUI;
import proyecto.persistencia.GestorCSV;
import proyecto.persistencia.GestorJSON;

/**
 * Clase principal de GameVault.
 * Punto de entrada del programa que:
 * <ol>
 *   <li>Carga los datos desde los ficheros (CSV para videojuegos, JSON para soundtracks).</li>
 *   <li>Crea las colecciones con sus indices secundarios.</li>
 *   <li>Lanza la interfaz grafica (GUI) o el menu de consola segun el argumento.</li>
 *   <li>Al cerrar, guarda los cambios en los ficheros correspondientes.</li>
 * </ol>
 *
 * Uso:
 * <pre>
 *   java proyecto.Main         -> lanza la GUI (por defecto)
 *   java proyecto.Main consola -> lanza el menu de consola
 * </pre>
 */
public class Main {

    // Rutas de los ficheros de datos
    private static final String RUTA_CSV = "resources/videojuegos.csv";
    private static final String RUTA_JSON = "resources/soundtracks.json";

    /**
     * Metodo principal.
     *
     * @param args argumentos de linea de comandos.
     *             Si contiene "consola", lanza el menu de consola.
     *             En caso contrario, lanza la GUI.
     */
    public static void main(String[] args) {

        // --- 1. Crear los gestores de ficheros ---
        GestorCSV gestorCsv = new GestorCSV(RUTA_CSV);
        GestorJSON gestorJson = new GestorJSON(RUTA_JSON);

        // --- 2. Crear las colecciones ---
        // El indice secundario del HashMap usa la desarrolladora para videojuegos
        // y el compositor para soundtracks
        Colecciones<Videojuego> colVideojuegos = new Colecciones<>(Videojuego::getDesarrolladora);
        Colecciones<Soundtrack> colSoundtracks = new Colecciones<>(Soundtrack::getCompositor);

        // --- 3. Cargar datos desde ficheros ---
        try {
            List<Videojuego> videojuegos = gestorCsv.cargar();
            colVideojuegos.cargarTodos(videojuegos);
            System.out.println("Videojuegos cargados: " + videojuegos.size());
        } catch (IOException e) {
            System.err.println("Error al cargar videojuegos: " + e.getMessage());
        }

        try {
            List<Soundtrack> soundtracks = gestorJson.cargar();
            colSoundtracks.cargarTodos(soundtracks);
            System.out.println("Soundtracks cargados: " + soundtracks.size());
        } catch (IOException e) {
            System.err.println("Error al cargar soundtracks: " + e.getMessage());
        }

        // --- 4. Accion de guardado (se reutiliza al cerrar) ---
        Runnable guardar = () -> {
            try {
                gestorCsv.guardar(colVideojuegos.getLista());
                System.out.println("Videojuegos guardados correctamente.");
            } catch (IOException e) {
                System.err.println("Error al guardar videojuegos: " + e.getMessage());
            }
            try {
                gestorJson.guardar(colSoundtracks.getLista());
                System.out.println("Soundtracks guardados correctamente.");
            } catch (IOException e) {
                System.err.println("Error al guardar soundtracks: " + e.getMessage());
            }
        };

        // --- 5. Lanzar la interfaz ---
        if (args.length > 0 && args[0].equalsIgnoreCase("consola")) {
            // Modo consola
            Consola consola = new Consola(colVideojuegos, colSoundtracks);
            consola.iniciar();
            // Al salir de la consola, guardar automaticamente
            guardar.run();
        } else {
            // Modo GUI (por defecto)
            // SwingUtilities.invokeLater garantiza que la GUI se cree
            // en el hilo de despacho de eventos de Swing (EDT)
            javax.swing.SwingUtilities.invokeLater(() -> {
                new GameVaultGUI(colVideojuegos, colSoundtracks, guardar);
            });
        }
    }
}
