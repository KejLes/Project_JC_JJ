package proyecto.consola;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import proyecto.coleccion_soundtrack.Soundtrack;
import proyecto.coleccion_videojuego.Digital;
import proyecto.coleccion_videojuego.Fisico;
import proyecto.coleccion_videojuego.Videojuego;
import proyecto.colecciones.Colecciones;

/**
 * Menu de consola para interactuar con las colecciones de videojuegos
 * y soundtracks. Usa {@code printf} para alinear tablas y valida todas
 * las entradas del usuario con bucles y expresiones regulares.
 *
 * Esta clase es independiente de la GUI; ambas pueden coexistir.
 */
public class Consola {

    private final Colecciones<Videojuego> colVideojuegos;
    private final Colecciones<Soundtrack> colSoundtracks;
    private final Scanner sc;

    /**
     * Constructor de la consola.
     *
     * @param colVideojuegos coleccion de videojuegos
     * @param colSoundtracks coleccion de soundtracks
     */
    public Consola(Colecciones<Videojuego> colVideojuegos,
                   Colecciones<Soundtrack> colSoundtracks) {
        this.colVideojuegos = colVideojuegos;
        this.colSoundtracks = colSoundtracks;
        this.sc = new Scanner(System.in);
    }

    /**
     * Punto de entrada del menu principal. Muestra las opciones
     * y gestiona la navegacion entre submenus.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Opcion: ", 0, 2);
            switch (opcion) {
                case 1 -> menuVideojuegos();
                case 2 -> menuSoundtracks();
                case 0 -> System.out.println("Saliendo del programa...");
            }
        } while (opcion != 0);
    }

    // =========================================================================
    //  MENU PRINCIPAL
    // =========================================================================

    private void mostrarMenuPrincipal() {
        System.out.println("\n+==============================+");
        System.out.println("|        G A M E V A U L T     |");
        System.out.println("+==============================+");
        System.out.println("|  1. Videojuegos              |");
        System.out.println("|  2. Soundtracks              |");
        System.out.println("|  0. Salir                    |");
        System.out.println("+==============================+");
    }

    // =========================================================================
    //  SUBMENU VIDEOJUEGOS
    // =========================================================================

    private void menuVideojuegos() {
        int opcion;
        do {
            System.out.println("\n+==============================+");
            System.out.println("|      V I D E O J U E G O S   |");
            System.out.println("+==============================+");
            System.out.println("|  1. Ver lista                |");
            System.out.println("|  2. Buscar                   |");
            System.out.println("|  3. Filtrar                  |");
            System.out.println("|  4. Ordenar                  |");
            System.out.println("|  5. Anadir                   |");
            System.out.println("|  6. Destruir todo            |");
            System.out.println("|  0. Volver                   |");
            System.out.println("+==============================+");
            opcion = leerEntero("Opcion: ", 0, 6);

            switch (opcion) {
                case 1 -> mostrarTablaVideojuegos(colVideojuegos.getLista());
                case 2 -> buscarVideojuegos();
                case 3 -> filtrarVideojuegos();
                case 4 -> ordenarVideojuegos();
                case 5 -> anadirVideojuego();
                case 6 -> destruirVideojuegos();
            }
        } while (opcion != 0);
    }

    /**
     * Muestra la tabla de videojuegos con columnas alineadas usando printf.
     * La anchura fija de cada columna mantiene la tabla legible
     * independientemente de la longitud del texto.
     *
     * @param lista lista de videojuegos a mostrar
     */
    private void mostrarTablaVideojuegos(List<Videojuego> lista) {
        if (lista.isEmpty()) {
            System.out.println("\n  No hay videojuegos para mostrar.");
            return;
        }
        System.out.printf("%n%-5s %-8s %-30s %-20s %-25s %-12s %-6s%n",
                "ID", "TIPO", "NOMBRE", "DESARROLLADORA", "GENERO", "DISPONIBLE", "VAL.");
        System.out.println("-".repeat(110));
        for (Videojuego v : lista) {
            System.out.printf("%-5d %-8s %-30s %-20s %-25s %-12s %-6.1f%n",
                    v.getId(), v.getTipo(),
                    truncar(v.getNombre(), 28),
                    truncar(v.getDesarrolladora(), 18),
                    truncar(v.getGeneroTexto(), 23),
                    v.isEstadoDisponible() ? "Si" : "No",
                    v.getValoracion());
        }
        System.out.println("-".repeat(110));
        System.out.printf("Total: %d videojuegos%n", lista.size());
    }

    private void buscarVideojuegos() {
        System.out.print("Buscar (id, nombre, desarrolladora o genero): ");
        String texto = sc.nextLine().trim().toLowerCase();
        List<Videojuego> resultados = colVideojuegos.buscar(v ->
                String.valueOf(v.getId()).contains(texto)
                        || v.getNombre().toLowerCase().contains(texto)
                        || v.getDesarrolladora().toLowerCase().contains(texto)
                        || v.getGeneroTexto().toLowerCase().contains(texto)
        );
        mostrarTablaVideojuegos(resultados);
    }

    private void filtrarVideojuegos() {
        System.out.println("  1. Disponibles");
        System.out.println("  2. No disponibles");
        System.out.println("  3. Fisico");
        System.out.println("  4. Digital");
        System.out.println("  5. Mejor valorado (5.0)");
        System.out.println("  6. Peor valorado (0.0)");
        int filtro = leerEntero("Filtro: ", 1, 6);

        List<Videojuego> resultado = switch (filtro) {
            case 1 -> colVideojuegos.filtrar(Videojuego::isEstadoDisponible);
            case 2 -> colVideojuegos.filtrar(v -> !v.isEstadoDisponible());
            case 3 -> colVideojuegos.filtrarPorTipo(Fisico.class)
                    .stream().map(f -> (Videojuego) f).collect(Collectors.toList());
            case 4 -> colVideojuegos.filtrarPorTipo(Digital.class)
                    .stream().map(d -> (Videojuego) d).collect(Collectors.toList());
            case 5 -> colVideojuegos.filtrarMejorValorado();
            case 6 -> colVideojuegos.filtrarPeorValorado();
            default -> colVideojuegos.getLista();
        };
        mostrarTablaVideojuegos(resultado);
    }

    private void ordenarVideojuegos() {
        System.out.println("  1. Por ID");
        System.out.println("  2. Alfabeticamente (nombre)");
        System.out.println("  3. Valoracion ascendente");
        System.out.println("  4. Valoracion descendente");
        int orden = leerEntero("Orden: ", 1, 4);

        List<Videojuego> resultado = switch (orden) {
            case 1 -> colVideojuegos.ordenar(Comparator.comparingInt(Videojuego::getId));
            case 2 -> colVideojuegos.ordenar(Comparator.comparing(
                    Videojuego::getNombre, String.CASE_INSENSITIVE_ORDER));
            case 3 -> colVideojuegos.ordenarPorValoracionAsc();
            case 4 -> colVideojuegos.ordenarPorValoracionDesc();
            default -> colVideojuegos.getLista();
        };
        mostrarTablaVideojuegos(resultado);
    }

    private void anadirVideojuego() {
        System.out.println("\n--- Anadir videojuego ---");

        // Calcular siguiente ID disponible
        int nuevoId = colVideojuegos.getLista().stream()
                .mapToInt(Videojuego::getId)
                .max().orElse(0) + 1;
        System.out.println("ID asignado: " + nuevoId);

        String nombre = leerTexto("Nombre: ");
        String desarrolladora = leerTexto("Desarrolladora: ");

        System.out.print("Generos (separados por coma): ");
        String generosStr = sc.nextLine().trim();
        ArrayList<String> generos = Arrays.stream(generosStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));

        boolean disponible = leerBoolean("Disponible (s/n): ");
        double valoracion = leerDouble("Valoracion (0.0 - 5.0): ", 0.0, 5.0);

        System.out.println("Tipo: 1. Fisico  2. Digital");
        int tipo = leerEntero("Tipo: ", 1, 2);

        Videojuego v;
        if (tipo == 1) {
            String estado = leerTexto("Estado (Nuevo/Usado/Como nuevo): ");
            boolean caja = leerBoolean("Incluye caja (s/n): ");
            v = new Fisico(nuevoId, nombre, desarrolladora, generos, disponible, estado, caja);
        } else {
            boolean conexion = leerBoolean("Requiere conexion a internet (s/n): ");
            v = new Digital(nuevoId, nombre, desarrolladora, generos, disponible, conexion);
        }
        v.setValoracion(valoracion);
        colVideojuegos.anadir(v);
        System.out.println("Videojuego anadido correctamente con ID " + nuevoId);
    }

    private void destruirVideojuegos() {
        System.out.print("Si aceptas, la lista se borrara por completo. Deseas continuar? (s/n): ");
        String respuesta = sc.nextLine().trim().toLowerCase();
        if (respuesta.equals("s")) {
            colVideojuegos.destruir();
            System.out.println("Lista de videojuegos destruida.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // =========================================================================
    //  SUBMENU SOUNDTRACKS
    // =========================================================================

    private void menuSoundtracks() {
        int opcion;
        do {
            System.out.println("\n+==============================+");
            System.out.println("|     S O U N D T R A C K S    |");
            System.out.println("+==============================+");
            System.out.println("|  1. Ver lista                |");
            System.out.println("|  2. Buscar                   |");
            System.out.println("|  3. Filtrar                  |");
            System.out.println("|  4. Ordenar                  |");
            System.out.println("|  5. Anadir                   |");
            System.out.println("|  6. Destruir todo            |");
            System.out.println("|  0. Volver                   |");
            System.out.println("+==============================+");
            opcion = leerEntero("Opcion: ", 0, 6);

            switch (opcion) {
                case 1 -> mostrarTablaSoundtracks(colSoundtracks.getLista());
                case 2 -> buscarSoundtracks();
                case 3 -> filtrarSoundtracks();
                case 4 -> ordenarSoundtracks();
                case 5 -> anadirSoundtrack();
                case 6 -> destruirSoundtracks();
            }
        } while (opcion != 0);
    }

    /**
     * Muestra la tabla de soundtracks con columnas alineadas usando printf.
     *
     * @param lista lista de soundtracks a mostrar
     */
    private void mostrarTablaSoundtracks(List<Soundtrack> lista) {
        if (lista.isEmpty()) {
            System.out.println("\n  No hay soundtracks para mostrar.");
            return;
        }
        System.out.printf("%n%-5s %-28s %-22s %-30s %-12s %-6s %-8s%n",
                "ID", "NOMBRE", "COMPOSITOR", "VIDEOJUEGO", "DISPONIBLE", "VAL.", "DURACION");
        System.out.println("-".repeat(115));
        for (Soundtrack st : lista) {
            System.out.printf("%-5d %-28s %-22s %-30s %-12s %-6.1f %-8s%n",
                    st.getId(),
                    truncar(st.getNombre(), 26),
                    truncar(st.getCompositor(), 20),
                    truncar(st.getVideojuegoAsociado(), 28),
                    st.isEstadoDisponible() ? "Si" : "No",
                    st.getValoracion(),
                    st.duracionEnFormato());
        }
        System.out.println("-".repeat(115));
        System.out.printf("Total: %d soundtracks%n", lista.size());
    }

    private void buscarSoundtracks() {
        System.out.print("Buscar (id, nombre, compositor o videojuego): ");
        String texto = sc.nextLine().trim().toLowerCase();
        List<Soundtrack> resultados = colSoundtracks.buscar(st ->
                String.valueOf(st.getId()).contains(texto)
                        || st.getNombre().toLowerCase().contains(texto)
                        || st.getCompositor().toLowerCase().contains(texto)
                        || st.getVideojuegoAsociado().toLowerCase().contains(texto)
        );
        mostrarTablaSoundtracks(resultados);
    }

    private void filtrarSoundtracks() {
        System.out.println("  1. Disponibles");
        System.out.println("  2. No disponibles");
        System.out.println("  3. Mejor valorado (5.0)");
        System.out.println("  4. Peor valorado (0.0)");
        int filtro = leerEntero("Filtro: ", 1, 4);

        List<Soundtrack> resultado = switch (filtro) {
            case 1 -> colSoundtracks.filtrar(Soundtrack::isEstadoDisponible);
            case 2 -> colSoundtracks.filtrar(st -> !st.isEstadoDisponible());
            case 3 -> colSoundtracks.filtrarMejorValorado();
            case 4 -> colSoundtracks.filtrarPeorValorado();
            default -> colSoundtracks.getLista();
        };
        mostrarTablaSoundtracks(resultado);
    }

    private void ordenarSoundtracks() {
        System.out.println("  1. Por ID");
        System.out.println("  2. Alfabeticamente (nombre)");
        System.out.println("  3. Valoracion ascendente");
        System.out.println("  4. Valoracion descendente");
        System.out.println("  5. Duracion ascendente");
        System.out.println("  6. Duracion descendente");
        int orden = leerEntero("Orden: ", 1, 6);

        List<Soundtrack> resultado = switch (orden) {
            case 1 -> colSoundtracks.ordenar(Comparator.comparingInt(Soundtrack::getId));
            case 2 -> colSoundtracks.ordenar(Comparator.comparing(
                    Soundtrack::getNombre, String.CASE_INSENSITIVE_ORDER));
            case 3 -> colSoundtracks.ordenarPorValoracionAsc();
            case 4 -> colSoundtracks.ordenarPorValoracionDesc();
            case 5 -> colSoundtracks.ordenar(Comparator.comparingInt(Soundtrack::getDuracion));
            case 6 -> colSoundtracks.ordenar(
                    Comparator.comparingInt(Soundtrack::getDuracion).reversed());
            default -> colSoundtracks.getLista();
        };
        mostrarTablaSoundtracks(resultado);
    }

    private void anadirSoundtrack() {
        System.out.println("\n--- Anadir soundtrack ---");

        int nuevoId = colSoundtracks.getLista().stream()
                .mapToInt(Soundtrack::getId)
                .max().orElse(0) + 1;
        System.out.println("ID asignado: " + nuevoId);

        String nombre = leerTexto("Nombre: ");
        String compositor = leerTexto("Compositor: ");
        String videojuego = leerTexto("Videojuego asociado: ");
        boolean disponible = leerBoolean("Disponible (s/n): ");
        int duracion = leerEntero("Duracion en segundos: ", 1, 99999);
        double valoracion = leerDouble("Valoracion (0.0 - 5.0): ", 0.0, 5.0);

        Soundtrack st = new Soundtrack(nuevoId, nombre, compositor,
                videojuego, disponible, duracion);
        st.setValoracion(valoracion);
        colSoundtracks.anadir(st);
        System.out.println("Soundtrack anadido correctamente con ID " + nuevoId);
        System.out.println("Duracion: " + st.duracionEnFormato());
    }

    private void destruirSoundtracks() {
        System.out.print("Si aceptas, la lista se borrara por completo. Deseas continuar? (s/n): ");
        String respuesta = sc.nextLine().trim().toLowerCase();
        if (respuesta.equals("s")) {
            colSoundtracks.destruir();
            System.out.println("Lista de soundtracks destruida.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // =========================================================================
    //  METODOS DE VALIDACION
    // =========================================================================

    /**
     * Lee un numero entero del usuario, validando que este dentro del rango.
     * Repite la peticion hasta obtener un valor valido.
     *
     * @param prompt mensaje a mostrar
     * @param min    valor minimo aceptado
     * @param max    valor maximo aceptado
     * @return entero validado dentro del rango
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
            sc.nextLine(); // Consumir salto de linea
        } while (valor < min || valor > max);
        return valor;
    }

    /**
     * Lee un numero decimal del usuario, validando que este dentro del rango.
     *
     * @param prompt mensaje a mostrar
     * @param min    valor minimo aceptado
     * @param max    valor maximo aceptado
     * @return double validado dentro del rango
     */
    private double leerDouble(String prompt, double min, double max) {
        double valor;
        do {
            System.out.print(prompt);
            while (!sc.hasNextDouble()) {
                sc.next();
                System.out.print(prompt);
            }
            valor = sc.nextDouble();
            sc.nextLine();
        } while (valor < min || valor > max);
        return valor;
    }

    /**
     * Lee una cadena de texto no vacia del usuario.
     * Valida con expresion regular que contenga al menos 1 caracter alfanumerico.
     *
     * @param prompt mensaje a mostrar
     * @return cadena validada (no vacia)
     */
    private String leerTexto(String prompt) {
        String texto;
        do {
            System.out.print(prompt);
            texto = sc.nextLine().trim();
            // Regex: al menos un caracter alfanumerico o con acentos
        } while (!texto.matches(".*[a-zA-Z0-9\\u00C0-\\u00FF].*"));
        return texto;
    }

    /**
     * Lee una respuesta booleana del usuario (s/n).
     *
     * @param prompt mensaje a mostrar
     * @return true si la respuesta es "s", false si es "n"
     */
    private boolean leerBoolean(String prompt) {
        String respuesta;
        do {
            System.out.print(prompt);
            respuesta = sc.nextLine().trim().toLowerCase();
        } while (!respuesta.equals("s") && !respuesta.equals("n"));
        return respuesta.equals("s");
    }

    /**
     * Trunca una cadena a una longitud maxima, anadiendo "..." si es necesario.
     *
     * @param texto  cadena original
     * @param maxLen longitud maxima
     * @return cadena truncada o la original si no supera el limite
     */
    private String truncar(String texto, int maxLen) {
        if (texto.length() <= maxLen) return texto;
        return texto.substring(0, maxLen - 3) + "...";
    }
}
