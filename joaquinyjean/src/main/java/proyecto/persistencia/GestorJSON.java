package proyecto.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proyecto.coleccion_soundtrack.Soundtrack;

/**
 * Gestor de ficheros JSON para la coleccion de soundtracks.
 * Usa tres mecanismos de E/S distintos:
 *
 * <ul>
 *   <li>{@link BufferedReader}: lectura secuencial del fichero JSON completo.</li>
 *   <li>{@link FileWriter} (via BufferedWriter): escritura completa del fichero.</li>
 *   <li>{@link RandomAccessFile}: acceso directo a un soundtrack por posicion
 *       de byte, sin releer todo el fichero.</li>
 * </ul>
 *
 * El JSON se escribe con un objeto por linea (entre los corchetes del array)
 * para facilitar el uso de RandomAccessFile:
 * <pre>
 * [
 * {"id": 1, "nombre": "...", ...},
 * {"id": 2, "nombre": "...", ...}
 * ]
 * </pre>
 *
 * El parseo es manual (sin librerias externas como Gson o Jackson).
 */
public class GestorJSON {

    private final String ruta;

    // Indice de posiciones de byte para RandomAccessFile
    private Map<String, Long> indicePosiciones;

    /**
     * Crea un gestor JSON apuntando a la ruta indicada.
     *
     * @param ruta ruta del fichero JSON (por ejemplo, "resources/soundtracks.json")
     */
    public GestorJSON(String ruta) {
        this.ruta = ruta;
        this.indicePosiciones = new HashMap<>();
    }

    // =========================================================================
    //  BufferedReader - Lectura secuencial del JSON
    // =========================================================================

    /**
     * Carga todos los soundtracks del fichero JSON.
     * Lee linea a linea con BufferedReader. Cada linea (excepto la primera
     * y la ultima) contiene un objeto JSON que se parsea manualmente.
     *
     * @return lista de soundtracks cargados desde el fichero
     * @throws IOException si hay error de lectura
     */
    public List<Soundtrack> cargar() throws IOException {
        List<Soundtrack> lista = new ArrayList<>();
        indicePosiciones.clear();

        File fichero = new File(ruta);
        if (!fichero.exists()) {
            return lista;
        }

        // Leer todo el contenido con BufferedReader y UTF-8 explicito
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new java.io.InputStreamReader(
                        new java.io.FileInputStream(ruta), "UTF-8"))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                // Eliminar BOM si existe
                if (primeraLinea) {
                    if (linea.startsWith("\uFEFF")) {
                        linea = linea.substring(1);
                    }
                    primeraLinea = false;
                }
                contenido.append(linea).append("\n");
            }
        }

        // Parsear el JSON manualmente
        String json = contenido.toString().trim();
        if (json.startsWith("[")) {
            json = json.substring(1); // Quitar el '['
        }
        if (json.endsWith("]")) {
            json = json.substring(0, json.length() - 1); // Quitar el ']'
        }

        // Separar por objetos: cada objeto va entre { y }
        List<String> objetos = extraerObjetos(json);
        for (String obj : objetos) {
            Soundtrack st = parsearObjeto(obj.trim());
            if (st != null) {
                lista.add(st);
            }
        }

        // Construir el indice de posiciones para RandomAccessFile
        construirIndice();

        return lista;
    }

    // =========================================================================
    //  FileWriter - Escritura completa del fichero JSON
    // =========================================================================

    /**
     * Guarda toda la lista de soundtracks en el fichero JSON.
     * Sobreescribe el contenido anterior.
     * Cada soundtrack ocupa una linea para facilitar el acceso con RandomAccessFile.
     *
     * @param soundtracks lista completa de soundtracks a guardar
     * @throws IOException si hay error de escritura
     */
    public void guardar(List<Soundtrack> soundtracks) throws IOException {
        File fichero = new File(ruta);
        if (fichero.getParentFile() != null) {
            fichero.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(
                new java.io.OutputStreamWriter(
                        new java.io.FileOutputStream(ruta), "UTF-8"))) {
            bw.write("[");
            bw.newLine();
            for (int i = 0; i < soundtracks.size(); i++) {
                bw.write(soundtracks.get(i).toJson());
                if (i < soundtracks.size() - 1) {
                    bw.write(",");
                }
                bw.newLine();
            }
            bw.write("]");
            bw.newLine();
        }

        // Reconstruir indice despues de guardar
        construirIndice();
    }

    // =========================================================================
    //  RandomAccessFile - Acceso directo por posicion de byte
    // =========================================================================

    /**
     * Busca un soundtrack por su ID usando RandomAccessFile.
     * Salta directamente a la posicion de byte registrada en el indice.
     *
     * @param id identificador del soundtrack a buscar
     * @return el soundtrack encontrado, o null si no existe
     * @throws IOException si hay error de lectura
     */
    public Soundtrack buscarPorId(String id) throws IOException {
        Long posicion = indicePosiciones.get(id);
        if (posicion == null) {
            return null;
        }

        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            raf.seek(posicion);
            String linea = raf.readLine();
            if (linea != null) {
                // Limpiar posibles comas al final de la linea
                linea = linea.trim();
                if (linea.endsWith(",")) {
                    linea = linea.substring(0, linea.length() - 1);
                }
                return parsearObjeto(linea);
            }
        }
        return null;
    }

    // =========================================================================
    //  Metodos auxiliares de parseo manual
    // =========================================================================

    /**
     * Construye el indice de posiciones de byte para cada soundtrack
     * dentro del fichero. Lee con RandomAccessFile linea a linea.
     *
     * @throws IOException si hay error de lectura
     */
    private void construirIndice() throws IOException {
        indicePosiciones.clear();
        File fichero = new File(ruta);
        if (!fichero.exists()) return;

        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            long posicion;
            String linea;
            while (true) {
                posicion = raf.getFilePointer();
                linea = raf.readLine();
                if (linea == null) break;

                linea = linea.trim();
                // Solo procesar lineas que parecen objetos JSON
                if (linea.startsWith("{")) {
                    // Extraer el ID del objeto
                    String id = extraerValor(linea, "id");
                    if (id != null) {
                        indicePosiciones.put(id, posicion);
                    }
                }
            }
        }
    }

    /**
     * Extrae todos los objetos JSON individuales de una cadena.
     * Busca pares de llaves { } y devuelve cada objeto como cadena.
     *
     * @param json cadena JSON sin los corchetes del array
     * @return lista de cadenas, cada una con un objeto JSON
     */
    private List<String> extraerObjetos(String json) {
        List<String> objetos = new ArrayList<>();
        int nivel = 0;
        int inicio = -1;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (nivel == 0) inicio = i;
                nivel++;
            } else if (c == '}') {
                nivel--;
                if (nivel == 0 && inicio >= 0) {
                    objetos.add(json.substring(inicio, i + 1));
                    inicio = -1;
                }
            }
        }
        return objetos;
    }

    /**
     * Parsea un objeto JSON (como cadena) y construye un Soundtrack.
     * El parseo es completamente manual, sin librerias externas.
     *
     * @param objetoJson cadena con un objeto JSON entre llaves
     * @return objeto Soundtrack, o null si el formato es invalido
     */
    private Soundtrack parsearObjeto(String objetoJson) {
        try {
            int id = Integer.parseInt(extraerValor(objetoJson, "id"));
            String nombre = extraerValorTexto(objetoJson, "nombre");
            String compositor = extraerValorTexto(objetoJson, "compositor");
            String videojuego = extraerValorTexto(objetoJson, "videojuegoAsociado");
            boolean disponible = Boolean.parseBoolean(extraerValor(objetoJson, "estadoDisponible"));
            double valoracion = Double.parseDouble(extraerValor(objetoJson, "valoracion"));
            int duracion = Integer.parseInt(extraerValor(objetoJson, "duracion"));

            Soundtrack st = new Soundtrack(id, nombre, compositor, videojuego, disponible, duracion);
            st.setValoracion(valoracion);
            return st;

        } catch (Exception e) {
            System.err.println("Error al parsear objeto JSON: " + objetoJson);
            System.err.println("Detalle: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extrae el valor de un campo numerico o booleano de un objeto JSON.
     * Busca el patron "clave": valor y devuelve el valor como cadena.
     *
     * @param json  objeto JSON como cadena
     * @param clave nombre del campo a buscar
     * @return valor del campo como cadena, o null si no se encuentra
     */
    private String extraerValor(String json, String clave) {
        String patron = "\"" + clave + "\":";
        int inicio = json.indexOf(patron);
        if (inicio < 0) {
            // Intentar con espacio despues de los dos puntos
            patron = "\"" + clave + "\": ";
            inicio = json.indexOf(patron);
        }
        if (inicio < 0) return null;

        inicio += patron.length();
        // Saltar espacios
        while (inicio < json.length() && json.charAt(inicio) == ' ') {
            inicio++;
        }

        int fin = inicio;
        while (fin < json.length() && json.charAt(fin) != ','
                && json.charAt(fin) != '}' && json.charAt(fin) != ' ') {
            fin++;
        }
        return json.substring(inicio, fin).trim();
    }

    /**
     * Extrae el valor de un campo de texto (entre comillas) de un objeto JSON.
     *
     * @param json  objeto JSON como cadena
     * @param clave nombre del campo a buscar
     * @return valor del campo sin comillas, o cadena vacia si no se encuentra
     */
    private String extraerValorTexto(String json, String clave) {
        String patron = "\"" + clave + "\":";
        int inicio = json.indexOf(patron);
        if (inicio < 0) {
            patron = "\"" + clave + "\": ";
            inicio = json.indexOf(patron);
        }
        if (inicio < 0) return "";

        inicio += patron.length();
        // Buscar la primera comilla de apertura
        int comillaAbrir = json.indexOf("\"", inicio);
        if (comillaAbrir < 0) return "";

        // Buscar la comilla de cierre
        int comillaCerrar = json.indexOf("\"", comillaAbrir + 1);
        if (comillaCerrar < 0) return "";

        return json.substring(comillaAbrir + 1, comillaCerrar);
    }

    /**
     * Devuelve el indice de posiciones actual.
     *
     * @return mapa con ID como clave y posicion de byte como valor
     */
    public Map<String, Long> getIndicePosiciones() {
        return indicePosiciones;
    }
}
