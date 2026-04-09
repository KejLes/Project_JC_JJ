package proyecto.persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import proyecto.colecciones_soundtrack.SoundtrackVideojuego;

/**
 * Gestor de persistencia para los soundtracks en formato JSON.
 * Realiza la lectura y escritura de forma manual sin librerías externas.
 * Sigue el diseño de GestorFicherosCSV para mantener la consistencia.
 */
public class GestorFicherosJSON {

    /**
     * Lee la lista de soundtracks desde un archivo JSON.
     * @param ruta Ruta del archivo JSON.
     * @return Lista de soundtracks cargados.
     * @throws IOException Si ocurre un error de E/S.
     */
    public static List<SoundtrackVideojuego> leerJSON(String ruta) throws IOException {
        List<SoundtrackVideojuego> lista = new ArrayList<>();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"))) {
            String linea;
            int id = -1;
            String nombre = null;
            String compositor = null;
            String videojuegoAsociado = null;
            String duracion = null;
            boolean estadoDisponible = false;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.equals("{")) {
                    id = -1;
                    nombre = null;
                    compositor = null;
                    videojuegoAsociado = null;
                    duracion = null;
                    estadoDisponible = false;
                } else if (linea.startsWith("\"id\":")) {
                    id = Integer.parseInt(extraerValor(linea));
                } else if (linea.startsWith("\"nombre\":")) {
                    nombre = extraerValor(linea);
                } else if (linea.startsWith("\"compositor\":")) {
                    compositor = extraerValor(linea);
                } else if (linea.startsWith("\"videojuegoAsociado\":")) {
                    videojuegoAsociado = extraerValor(linea);
                } else if (linea.startsWith("\"duracion\":")) {
                    duracion = extraerValor(linea);
                } else if (linea.startsWith("\"estadoDisponible\":")) {
                    estadoDisponible = Boolean.parseBoolean(extraerValor(linea));
                } else if (linea.startsWith("}") || linea.startsWith("},")) {
                    if (id != -1 && nombre != null) {
                        lista.add(new SoundtrackVideojuego(id, nombre, compositor, videojuegoAsociado, duracion, estadoDisponible));
                    }
                }
            }
        }
        return lista;
    }

    /**
     * Escribe la lista de soundtracks en un archivo JSON.
     * @param lista Lista de soundtracks a persistir.
     * @param ruta Ruta del archivo JSON.
     * @throws IOException Si ocurre un error de E/S.
     */
    public static void escribirJSON(List<SoundtrackVideojuego> lista, String ruta) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta, false), "UTF-8"))) {
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
     * Extrae el valor de una línea JSON (simplificado).
     * @param linea Línea del JSON ("clave": "valor" o "clave": valor).
     * @return El valor extraído sin comillas ni comas finales.
     */
    private static String extraerValor(String linea) {
        // Eliminar coma final si existe
        if (linea.endsWith(",")) {
            linea = linea.substring(0, linea.length() - 1);
        }

        int colonIndex = linea.indexOf(":");
        if (colonIndex == -1) return "";

        String valor = linea.substring(colonIndex + 1).trim();

        // Si es una cadena entre comillas, quitarlas
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }

        return valor;
    }

    /**
     * Muestra la lista de soundtracks en formato tabular por consola.
     * @param lista Lista de soundtracks a mostrar.
     */
    public static void listarSoundtracks(List<SoundtrackVideojuego> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay soundtracks en la biblioteca.");
            return;
        }

        System.out.printf("%-5s | %-25s | %-20s | %-20s | %-10s%n", "ID", "Nombre", "Compositor", "Videojuego", "Duración");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (SoundtrackVideojuego s : lista) {
            System.out.printf("%-5d | %-25.25s | %-20.20s | %-20.20s | %-10s%n",
                    s.getID(), s.getNombre(), s.getCompositor(), s.getVideojuegoAsociado(), s.getDuracion());
        }
    }
}
