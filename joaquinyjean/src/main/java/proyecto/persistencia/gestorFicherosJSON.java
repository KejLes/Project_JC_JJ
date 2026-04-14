package proyecto.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import proyecto.colecciones_soundtrack.SoundtrackVideojuego;

/**
 * Lee y escribe soundtracks en formato JSON.
 * Usa BufferedReader para leer y BufferedWriter para escribir.
 * Todos los metodos son estaticos, igual que gestorFicherosCSV.
 * El parseo del JSON es manual, sin librerias externas.
 */
public class gestorFicherosJSON {

    /**
     * Lee el fichero JSON y devuelve todos los soundtracks.
     * Parsea el JSON linea a linea de forma manual.
     * @param ruta ruta del fichero JSON
     * @return lista de soundtracks
     * @throws IOException si el fichero no existe o hay error de lectura
     */
    public static List<SoundtrackVideojuego> leerJSON(String ruta) throws IOException {
        List<SoundtrackVideojuego> lista = new ArrayList<>();

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado: " + archivo.getAbsolutePath());
            return lista;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), "UTF-8"))) {

            int id = -1;
            String nombre = null;
            String compositor = null;
            String videojuegoAsociado = null;
            String duracion = null;
            boolean estadoDisponible = false;

            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.equals("{")) {
                    id = -1;
                    nombre = null;
                    compositor = null;
                    videojuegoAsociado = null;
                    duracion = null;
                    estadoDisponible = false;

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

                } else if (linea.startsWith("}") || linea.startsWith("},")) {
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

    /**
     * Escribe toda la lista de soundtracks en el fichero JSON.
     * Sobreescribe el contenido anterior.
     * @param lista lista de soundtracks a guardar
     * @param ruta ruta del fichero JSON
     * @throws IOException si hay error de escritura
     */
    public static void escribirJSON(List<SoundtrackVideojuego> lista, String ruta) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ruta, false), "UTF-8"))) {

            bw.write("[");
            bw.newLine();

            for (int i = 0; i < lista.size(); i++) {
                bw.write(lista.get(i).toJSON());
                if (i < lista.size() - 1) {
                    bw.write(",");
                }
                bw.newLine();
            }

            bw.write("]");
            bw.newLine();
        }
    }

    /**
     * Anade un soundtrack nuevo al fichero JSON.
     * Lee la lista actual, anade el nuevo y reescribe todo.
     * @param nuevo soundtrack a anadir
     * @param ruta ruta del fichero JSON
     * @throws IOException si hay error de lectura o escritura
     */
    public static void agregarSoundtrack(SoundtrackVideojuego nuevo, String ruta) throws IOException {
        List<SoundtrackVideojuego> lista = leerJSON(ruta);
        lista.add(nuevo);
        escribirJSON(lista, ruta);
    }

    /**
     * Elimina un soundtrack por ID y reescribe el JSON.
     * Usa un Iterator explicito para recorrer y eliminar de forma segura.
     * @param id identificador del soundtrack a borrar
     * @param ruta ruta del fichero JSON
     * @return true si se encontro y elimino, false si no existia
     * @throws IOException si hay error de lectura o escritura
     */
    public static boolean eliminarPorId(int id, String ruta) throws IOException {
        List<SoundtrackVideojuego> lista = leerJSON(ruta);

        // Iterator explicito
        Iterator<SoundtrackVideojuego> it = lista.iterator();
        boolean encontrado = false;
        while (it.hasNext()) {
            if (it.next().getID() == id) {
                it.remove();
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            escribirJSON(lista, ruta);
        }
        return encontrado;
    }

    /**
     * Muestra todos los soundtracks por consola.
     * @param lista lista de soundtracks a mostrar
     */
    public static void listarSoundtracks(List<SoundtrackVideojuego> lista) {
        System.out.println("\nBiblioteca de Soundtracks");
        System.out.println("==========================================");

        if (lista.isEmpty()) {
            System.out.println("No hay soundtracks registrados.");
            return;
        }

        for (SoundtrackVideojuego s : lista) {
            System.out.println(s.obtenerFormatoDescripcion());
        }

        System.out.println("==========================================");
        System.out.println("Total: " + lista.size() + " soundtracks\n");
    }

    /**
     * Extrae el valor de una linea JSON del tipo "clave": valor.
     * @param linea linea del JSON a parsear
     * @return valor extraido como String
     */
    private static String extraerValor(String linea) {
        if (linea.endsWith(",")) {
            linea = linea.substring(0, linea.length() - 1);
        }

        int colonIndex = linea.indexOf(":");
        if (colonIndex == -1) {
            return "";
        }

        String valor = linea.substring(colonIndex + 1).trim();

        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }

        return valor;
    }
}
