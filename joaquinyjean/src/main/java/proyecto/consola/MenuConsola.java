package proyecto.consola;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import proyecto.colecciones_videojuego.Digital;
import proyecto.colecciones_videojuego.Fisico;
import proyecto.colecciones_videojuego.Videojuego;
import proyecto.colecciones_soundtrack.SoundtrackVideojuego;
import proyecto.persistencia.gestorFicherosCSV;
import proyecto.persistencia.gestorFicherosJSON;

/**
 * Menu de consola para gestionar videojuegos y soundtracks.
 * Usa Scanner para leer, printf para tablas alineadas,
 * streams para ordenar y contar, y regex para validar entradas.
 */
public class MenuConsola {

    private TreeMap<Integer, Videojuego> videojuegos;
    private List<SoundtrackVideojuego> soundtracks;
    private String rutaCSV;
    private String rutaJSON;
    private Scanner sc;

    // HashMap secundario: agrupa videojuegos por desarrolladora
    // para busquedas rapidas sin recorrer toda la coleccion
    private HashMap<String, List<Videojuego>> porDesarrolladora;

    /**
     * Crea el menu de consola con los datos cargados.
     * Construye el HashMap secundario a partir del TreeMap.
     * @param videojuegos mapa de videojuegos cargados del CSV
     * @param soundtracks lista de soundtracks cargados del JSON
     * @param rutaCSV ruta del fichero CSV
     * @param rutaJSON ruta del fichero JSON
     */
    public MenuConsola(TreeMap<Integer, Videojuego> videojuegos,
                       List<SoundtrackVideojuego> soundtracks,
                       String rutaCSV, String rutaJSON) {
        this.videojuegos = videojuegos;
        this.soundtracks = soundtracks;
        this.rutaCSV = rutaCSV;
        this.rutaJSON = rutaJSON;
        this.sc = new Scanner(System.in);

        // Construir HashMap secundario por desarrolladora
        this.porDesarrolladora = new HashMap<>();
        for (Videojuego v : videojuegos.values()) {
            porDesarrolladora
                .computeIfAbsent(v.getDesarrolladora(), k -> new ArrayList<>())
                .add(v);
        }
    }

    /**
     * Muestra el menu principal y ejecuta la opcion elegida.
     * Se repite hasta que el usuario elija salir.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opcion: ", 0, 8);
            switch (opcion) {
                case 1 -> mostrarVideojuegos();
                case 2 -> mostrarSoundtracks();
                case 3 -> buscarVideojuegoPorId();
                case 4 -> buscarSoundtrackPorId();
                case 5 -> anadirVideojuego();
                case 6 -> anadirSoundtrack();
                case 7 -> borrarVideojuego();
                case 8 -> borrarSoundtrack();
                case 0 -> System.out.println("Saliendo...");
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n  GAMEVAULT");
        System.out.println("  1. Ver videojuegos");
        System.out.println("  2. Ver soundtracks");
        System.out.println("  3. Buscar videojuego por ID");
        System.out.println("  4. Buscar soundtrack por ID");
        System.out.println("  5. Anadir videojuego");
        System.out.println("  6. Anadir soundtrack");
        System.out.println("  7. Borrar videojuego");
        System.out.println("  8. Borrar soundtrack");
        System.out.println("  0. Salir");
    }

    // =====================================================================
    //  LISTAR
    // =====================================================================

    /**
     * Muestra todos los videojuegos en tabla alineada con printf.
     * Usa stream para contar los disponibles.
     */
    private void mostrarVideojuegos() {
        if (videojuegos.isEmpty()) {
            System.out.println("\nNo hay videojuegos registrados.");
            return;
        }

        System.out.printf("%n%-5s %-8s %-30s %-20s %-20s %-10s%n",
                "ID", "TIPO", "NOMBRE", "DESARROLLADORA", "GENERO", "DISP.");

        for (Videojuego v : videojuegos.values()) {
            String tipo = (v instanceof Fisico) ? "Fisico" : "Digital";
            System.out.printf("%-5d %-8s %-30.30s %-20.20s %-20.20s %-10s%n",
                    v.getId(), tipo, v.getNombre(),
                    v.getDesarrolladora(), v.getStringGenero('.'),
                    v.getDisponible() ? "Si" : "No");
        }


        // Stream: contar cuantos estan disponibles
        long disponibles = videojuegos.values().stream()
                .filter(Videojuego::getDisponible)
                .count();
        System.out.printf("Total: %d videojuegos (%d disponibles)%n",
                videojuegos.size(), disponibles);

        // Mostrar resumen por desarrolladora usando el HashMap secundario
        System.out.println("\nPor desarrolladora:");
        for (Map.Entry<String, List<Videojuego>> entry : porDesarrolladora.entrySet()) {
            System.out.printf("  %-25s: %d juego(s)%n", entry.getKey(), entry.getValue().size());
        }
    }

    /**
     * Muestra todos los soundtracks en tabla alineada con printf.
     * Usa stream para ordenar por nombre.
     */
    private void mostrarSoundtracks() {
        if (soundtracks.isEmpty()) {
            System.out.println("\nNo hay soundtracks registrados.");
            return;
        }

        // Stream: ordenar la lista por nombre antes de mostrar
        List<SoundtrackVideojuego> ordenados = soundtracks.stream()
                .sorted(Comparator.comparing(SoundtrackVideojuego::getNombre))
                .collect(Collectors.toList());

        System.out.printf("%n%-5s %-25s %-22s %-25s %-8s %-10s%n",
                "ID", "NOMBRE", "COMPOSITOR", "VIDEOJUEGO", "DURACION", "DISP.");

        for (SoundtrackVideojuego st : ordenados) {
            System.out.printf("%-5d %-25.25s %-22.22s %-25.25s %-8s %-10s%n",
                    st.getID(), st.getNombre(), st.getCompositor(),
                    st.getVideojuegoAsociado(), st.getDuracion(),
                    st.getDisponible() ? "Si" : "No");
        }

        System.out.printf("Total: %d soundtracks%n", soundtracks.size());
    }

    // =====================================================================
    //  BUSCAR POR ID
    // =====================================================================

    /**
     * Busca un videojuego por su ID usando RandomAccessFile.
     */
    private void buscarVideojuegoPorId() {
        System.out.print("ID del videojuego a buscar: ");
        String input = sc.nextLine().trim();

        // Regex: validar que sea un numero
        if (!input.matches("\\d+")) {
            System.out.println("ID no valido. Debe ser un numero.");
            return;
        }

        int id = Integer.parseInt(input);

        // Buscar usando RandomAccessFile
        Videojuego encontrado = gestorFicherosCSV.buscarPorIdRAF(id, rutaCSV);
        if (encontrado != null) {
            mostrarDetalle(encontrado);
        } else {
            System.out.println("No existe videojuego con ID: " + id);
        }
    }

    /**
     * Busca un soundtrack por su ID recorriendo la lista.
     */
    private void buscarSoundtrackPorId() {
        System.out.print("ID del soundtrack a buscar: ");
        String input = sc.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println("ID no valido. Debe ser un numero.");
            return;
        }

        int id = Integer.parseInt(input);

        // Stream: buscar por ID con filter y findFirst
        SoundtrackVideojuego encontrado = soundtracks.stream()
                .filter(st -> st.getID() == id)
                .findFirst()
                .orElse(null);

        if (encontrado != null) {
            encontrado.mostrarInfo();
        } else {
            System.out.println("No existe soundtrack con ID: " + id);
        }
    }

    // =====================================================================
    //  METODO GENERICO CON BOUNDED TYPE
    // =====================================================================

    /**
     * Muestra la informacion de cualquier tipo de videojuego.
     * Metodo generico: T puede ser Fisico o Digital, pero como minimo
     * es un Videojuego, asi que podemos llamar a sus metodos.
     * @param juego videojuego a mostrar (Fisico o Digital)
     */
    public static <T extends Videojuego> void mostrarDetalle(T juego) {
        System.out.println("\n--- Detalle del videojuego ---");
        juego.mostrarInfo();
    }

    // =====================================================================
    //  ANADIR
    // =====================================================================

    /**
     * Pide los datos de un videojuego por teclado y lo anade.
     * Usa regex para validar que el nombre no este vacio y sea valido.
     * Despues reescribe el CSV.
     */
    private void anadirVideojuego() {
        System.out.println("\n--- Anadir videojuego ---");

        // Stream: calcular siguiente ID disponible
        int nuevoId = videojuegos.values().stream()
                .mapToInt(Videojuego::getId)
                .max()
                .orElse(0) + 1;
        System.out.println("ID asignado: " + nuevoId);

        // Pedir nombre con validacion regex
        String nombre;
        do {
            System.out.print("Nombre: ");
            nombre = sc.nextLine().trim();
            // Regex: letras, numeros, espacios, dos puntos, guiones y apostrofes
            if (!nombre.matches("[a-zA-Z0-9\\u00C0-\\u00FF :.'\\-]+")) {
                System.out.println("Nombre no valido. Usa letras, numeros y espacios.");
            }
        } while (!nombre.matches("[a-zA-Z0-9\\u00C0-\\u00FF :.'\\-]+"));

        System.out.print("Desarrolladora: ");
        String desarrolladora = sc.nextLine().trim();

        System.out.print("Generos (separados por punto, ej: Accion.RPG): ");
        String generosStr = sc.nextLine().trim();
        ArrayList<String> generos = new ArrayList<>(Arrays.asList(generosStr.split("\\.")));

        System.out.print("Disponible (true/false): ");
        boolean disponible = sc.nextLine().trim().equalsIgnoreCase("true");

        System.out.print("Tipo (fisico/digital): ");
        String tipo = sc.nextLine().trim().toLowerCase();

        Videojuego nuevo;
        if (tipo.equals("fisico")) {
            System.out.print("Estado (nuevo/usado): ");
            String estado = sc.nextLine().trim();
            System.out.print("Incluye caja (true/false): ");
            boolean caja = sc.nextLine().trim().equalsIgnoreCase("true");
            nuevo = new Fisico(nuevoId, nombre, desarrolladora, generos, disponible, estado, caja);
        } else {
            System.out.print("Requiere conexion (true/false): ");
            boolean conexion = sc.nextLine().trim().equalsIgnoreCase("true");
            nuevo = new Digital(nuevoId, nombre, desarrolladora, generos, disponible, conexion);
        }

        videojuegos.put(nuevoId, nuevo);

        // Actualizar HashMap secundario
        porDesarrolladora
                .computeIfAbsent(desarrolladora, k -> new ArrayList<>())
                .add(nuevo);

        // Guardar en CSV
        gestorFicherosCSV.escribirCSV(videojuegos, rutaCSV);
        System.out.println("Videojuego anadido con ID " + nuevoId);
    }

    /**
     * Pide los datos de un soundtrack por teclado y lo anade.
     * Despues reescribe el JSON.
     */
    private void anadirSoundtrack() {
        System.out.println("\n--- Anadir soundtrack ---");

        // Calcular siguiente ID
        int nuevoId = soundtracks.stream()
                .mapToInt(SoundtrackVideojuego::getID)
                .max()
                .orElse(0) + 1;
        System.out.println("ID asignado: " + nuevoId);

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Compositor: ");
        String compositor = sc.nextLine().trim();

        System.out.print("Videojuego asociado: ");
        String videojuego = sc.nextLine().trim();

        System.out.print("Duracion (mm:ss): ");
        String duracion = sc.nextLine().trim();

        System.out.print("Disponible (true/false): ");
        boolean disponible = sc.nextLine().trim().equalsIgnoreCase("true");

        SoundtrackVideojuego nuevo = new SoundtrackVideojuego(
                nuevoId, nombre, compositor, videojuego, duracion, disponible);

        soundtracks.add(nuevo);

        // Guardar en JSON
        try {
            gestorFicherosJSON.escribirJSON(soundtracks, rutaJSON);
            System.out.println("Soundtrack anadido con ID " + nuevoId);
        } catch (Exception e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // =====================================================================
    //  BORRAR
    // =====================================================================

    /**
     * Pide un ID y borra el videojuego correspondiente.
     * Usa el metodo eliminarPorId que internamente usa Iterator explicito.
     */
    private void borrarVideojuego() {
        System.out.print("ID del videojuego a borrar: ");
        String input = sc.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println("ID no valido.");
            return;
        }

        int id = Integer.parseInt(input);

        if (gestorFicherosCSV.eliminarPorId(id, rutaCSV)) {
            // Recargar el TreeMap desde el fichero actualizado
            videojuegos = gestorFicherosCSV.leerCSV(rutaCSV);
            // Reconstruir HashMap secundario
            reconstruirHashMap();
            System.out.println("Videojuego con ID " + id + " eliminado.");
        } else {
            System.out.println("No existe videojuego con ID: " + id);
        }
    }

    /**
     * Pide un ID y borra el soundtrack correspondiente.
     */
    private void borrarSoundtrack() {
        System.out.print("ID del soundtrack a borrar: ");
        String input = sc.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println("ID no valido.");
            return;
        }

        int id = Integer.parseInt(input);

        try {
            if (gestorFicherosJSON.eliminarPorId(id, rutaJSON)) {
                // Recargar la lista desde el fichero actualizado
                soundtracks = gestorFicherosJSON.leerJSON(rutaJSON);
                System.out.println("Soundtrack con ID " + id + " eliminado.");
            } else {
                System.out.println("No existe soundtrack con ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
    }

    // =====================================================================
    //  UTILIDADES
    // =====================================================================

    /**
     * Reconstruye el HashMap secundario por desarrolladora
     * a partir del TreeMap actual.
     */
    private void reconstruirHashMap() {
        porDesarrolladora.clear();
        for (Videojuego v : videojuegos.values()) {
            porDesarrolladora
                .computeIfAbsent(v.getDesarrolladora(), k -> new ArrayList<>())
                .add(v);
        }
    }

    /**
     * Lee un numero entero del usuario, validando que este en rango.
     * Repite hasta obtener un valor valido.
     * @param prompt mensaje a mostrar
     * @param min valor minimo aceptado
     * @param max valor maximo aceptado
     * @return entero validado
     */
    private int leerEntero(String prompt, int min, int max) {
        int valor;
        do {
            System.out.print(prompt);
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.print(prompt);
            }
            valor = sc.nextInt();
            sc.nextLine();
        } while (valor < min || valor > max);
        return valor;
    }
}
