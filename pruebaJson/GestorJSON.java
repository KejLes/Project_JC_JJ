

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorJSON {

    private String ruta;

    public GestorJSON(String ruta) {
        this.ruta = ruta;
    }

    // ── LEER todos los soundtracks del JSON ───────────────────────────────
    public List<SoundtrackVideojuego> leerSoundtracks() throws IOException {
        List<SoundtrackVideojuego> lista = new ArrayList<>();

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println(" Archivo no encontrado: " + archivo.getAbsolutePath());
            return lista;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {

            // Variables temporales para cada objeto
            int     id                 = -1;
            String  nombre             = null;
            String  compositor         = null;
            String  videojuegoAsociado = null;
            String  duracion           = null;
            boolean estadoDisponible   = false;

            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.equals("{")) {
                    // Nuevo objeto: resetear variables
                    id                 = -1;
                    nombre             = null;
                    compositor         = null;
                    videojuegoAsociado = null;
                    duracion           = null;
                    estadoDisponible   = false;

                } else if (linea.startsWith("\"id\"")) {
                    id = Integer.parseInt(extraerValor(linea));

                } else if (linea.startsWith("\"nombre\"")) {
                    nombre = extraerValor(linea);

                } else if (linea.startsWith("\"compositor\"")) {
                    compositor = extraerValor(linea);

                } else if (linea.startsWith("\"videojuegoAsociado\"")) {
                    videojuegoAsociado = extraerValor(linea);

                } else if (linea.startsWith("\"duracion\"")) {
                    duracion = extraerValor(linea);

                } else if (linea.startsWith("\"estadoDisponible\"")) {
                    estadoDisponible = Boolean.parseBoolean(extraerValor(linea));

                } else if (linea.startsWith("}")) {
                    // Fin del objeto: crear el soundtrack si tiene lo mínimo
                    if (id != -1 && nombre != null) {
                        lista.add(new SoundtrackVideojuego(
                            id, nombre, compositor,
                            videojuegoAsociado, duracion,
                            estadoDisponible
                        ));
                    }
                }
            }
        }

        return lista;
    }

    // ── LISTAR todos los soundtracks ──────────────────────────────────────
    public void listar() throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        System.out.println("\n🎵 Biblioteca de Soundtracks");
        System.out.println("══════════════════════════════════════════");

        if (lista.isEmpty()) {
            System.out.println("No hay soundtracks registrados.");
            return;
        }

        for (SoundtrackVideojuego s : lista) {
            // Usamos el método de la interfaz AccionSound
            System.out.println(s.obtenerFormatoDescripcion());
        }

        System.out.println("══════════════════════════════════════════");
        System.out.println("Total: " + lista.size() + " soundtracks\n");
    }

    // ── BUSCAR por ID ─────────────────────────────────────────────────────
    public void buscarPorId(int id) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        System.out.println("\n🔍 Buscando ID " + id + "...");

        boolean encontrado = false;
        for (SoundtrackVideojuego s : lista) {
            if (s.getID() == id) {
                s.mostrarInfo(); // Usamos el método abstracto implementado
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("❌ No existe ningún soundtrack con ID: " + id + "\n");
        }
    }

    // ── BUSCAR por nombre (búsqueda parcial) ──────────────────────────────
    public void buscarPorNombre(String texto) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();
        String textoBuscar = texto.toLowerCase();

        System.out.println("\n🔍 Resultados para: \"" + texto + "\"");
        System.out.println("──────────────────────────────────────────");

        boolean hayResultados = false;
        for (SoundtrackVideojuego s : lista) {
            // Buscamos en nombre y en videojuego asociado
            if (s.getNombre().toLowerCase().contains(textoBuscar) ||
                s.getVideojuegoAsociado().toLowerCase().contains(textoBuscar)) {
                System.out.println(s.obtenerFormatoDescripcion());
                hayResultados = true;
            }
        }

        if (!hayResultados) {
            System.out.println("❌ Sin resultados para: " + texto);
        }
        System.out.println();
    }

    // ── AGREGAR un soundtrack nuevo ───────────────────────────────────────
    public void agregar(SoundtrackVideojuego nuevo) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        // Usar agregarAlCarrito de la interfaz para verificar disponibilidad
        if (!nuevo.agregarAlCarrito(nuevo.getDisponible())) {
            System.out.println("⚠️ El soundtrack no está disponible.");
            return;
        }

        lista.add(nuevo);
        escribirJSON(lista);
        System.out.println("✅ Soundtrack agregado: " + nuevo.getNombre() + "\n");
    }

    // ── Escribir la lista entera como JSON ────────────────────────────────
    private void escribirJSON(List<SoundtrackVideojuego> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ruta, false), "UTF-8"))) {

            bw.write("[");
            bw.newLine();

            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toJSON());
                if (i < lista.size() - 1) bw.write(",");
                bw.newLine();
            }

            bw.write("]");
            bw.newLine();
        }
    }

    // ── Extraer el valor de una línea JSON ────────────────────────────────
    // "nombre": "Megalovania",  →  Megalovania
    // "id": 1,                 →  1
    private String extraerValor(String linea) {
        if (linea.endsWith(",")) {
            linea = linea.substring(0, linea.length() - 1);
        }

        String[] partes = linea.split(": ", 2);
        String valor = partes[1].trim();

        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }

        return valor;
    }
}