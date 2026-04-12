package proyecto.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import proyecto.coleccion_videojuego.Digital;
import proyecto.coleccion_videojuego.Fisico;
import proyecto.coleccion_videojuego.Videojuego;

/**
 * Gestor de ficheros CSV para la coleccion de videojuegos.
 * Usa tres mecanismos de E/S distintos, cada uno con un proposito concreto:
 *
 * <ul>
 *   <li>{@link BufferedReader}: lectura secuencial eficiente de todas las lineas.</li>
 *   <li>{@link FileWriter} (via BufferedWriter): escritura completa del fichero.</li>
 *   <li>{@link RandomAccessFile}: acceso directo por posicion de byte
 *       para buscar un videojuego por ID sin releer todo el fichero.</li>
 * </ul>
 *
 * Formato CSV (campos separados por ;):
 * <pre>
 *   FISICO;id;nombre;desarrolladora;genero1.genero2;disponible;valoracion;estado;caja
 *   DIGITAL;id;nombre;desarrolladora;genero1.genero2;disponible;valoracion;conexionRequerida
 * </pre>
 */
public class GestorCSV {

    private final String ruta;

    // Indice de posiciones de byte para RandomAccessFile.
    // Clave: ID del videojuego, Valor: posicion en bytes donde comienza la linea
    private Map<String, Long> indicePosiciones;

    /**
     * Crea un gestor CSV apuntando a la ruta indicada.
     *
     * @param ruta ruta del fichero CSV (por ejemplo, "resources/videojuegos.csv")
     */
    public GestorCSV(String ruta) {
        this.ruta = ruta;
        this.indicePosiciones = new HashMap<>();
    }

    // =========================================================================
    //  BufferedReader - Lectura secuencial linea a linea
    // =========================================================================

    /**
     * Carga todos los videojuegos del fichero CSV.
     * Usa BufferedReader para leer linea a linea de forma secuencial
     * y construir los objetos correspondientes.
     * Despues, construye el indice de posiciones con RandomAccessFile
     * para futuras busquedas directas por ID.
     *
     * @return lista de videojuegos cargados desde el fichero
     * @throws IOException si hay error de lectura
     */
    public List<Videojuego> cargar() throws IOException {
        List<Videojuego> lista = new ArrayList<>();
        indicePosiciones.clear();

        File fichero = new File(ruta);
        if (!fichero.exists()) {
            return lista;
        }

        // PASO 1: BufferedReader — lectura secuencial eficiente de todas las lineas
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isBlank()) {
                    Videojuego v = parsear(linea);
                    if (v != null) {
                        lista.add(v);
                    }
                }
            }
        }

        // PASO 2: RandomAccessFile — construir el indice de posiciones de byte
        // para poder saltar directamente a cualquier linea en busquedas por ID
        reconstruirIndice();

        return lista;
    }

    // =========================================================================
    //  FileWriter - Escritura completa del inventario
    // =========================================================================

    /**
     * Guarda toda la lista de videojuegos en el fichero CSV.
     * Sobreescribe el contenido anterior.
     * Usa BufferedWriter sobre FileWriter para eficiencia.
     *
     * @param videojuegos lista completa de videojuegos a guardar
     * @throws IOException si hay error de escritura
     */
    public void guardar(List<Videojuego> videojuegos) throws IOException {
        // Crear directorios padre si no existen
        File fichero = new File(ruta);
        if (fichero.getParentFile() != null) {
            fichero.getParentFile().mkdirs();
        }

        // FileWriter con false: sobreescribe el fichero completo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, false))) {
            for (Videojuego v : videojuegos) {
                bw.write(v.toCsv());
                bw.newLine();
            }
        }

        // Reconstruir el indice despues de guardar
        reconstruirIndice();
    }

    // =========================================================================
    //  RandomAccessFile - Acceso directo por posicion de byte
    // =========================================================================

    /**
     * Busca un videojuego por su ID usando RandomAccessFile.
     * Salta directamente a la posicion de byte registrada en el indice,
     * sin tener que releer todo el fichero.
     *
     * @param id identificador del videojuego a buscar
     * @return el videojuego encontrado, o null si no existe
     * @throws IOException si hay error de lectura
     */
    public Videojuego buscarPorId(String id) throws IOException {
        Long posicion = indicePosiciones.get(id);
        if (posicion == null) {
            return null; // ID no encontrado en el indice
        }

        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            raf.seek(posicion); // Saltar directamente a la posicion
            String linea = raf.readLine();
            if (linea != null) {
                return parsear(linea);
            }
        }
        return null;
    }

    // =========================================================================
    //  Metodos auxiliares
    // =========================================================================

    /**
     * Reconstruye el indice de posiciones leyendo el fichero con RandomAccessFile.
     * Se invoca despues de guardar para mantener el indice actualizado.
     *
     * @throws IOException si hay error de lectura
     */
    private void reconstruirIndice() throws IOException {
        indicePosiciones.clear();
        File fichero = new File(ruta);
        if (!fichero.exists()) return;

        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            long posicion = 0;
            String linea;
            while ((linea = raf.readLine()) != null) {
                if (!linea.isBlank()) {
                    String[] campos = linea.split(";");
                    if (campos.length >= 2) {
                        indicePosiciones.put(campos[1], posicion); // campos[1] es el ID
                    }
                }
                posicion = raf.getFilePointer();
            }
        }
    }

    /**
     * Convierte una linea CSV en el objeto Videojuego correspondiente.
     * Determina si es Fisico o Digital por el primer campo (tipo).
     *
     * Formato FISICO:  FISICO;id;nombre;desarrolladora;generos;disponible;valoracion;estado;caja
     * Formato DIGITAL: DIGITAL;id;nombre;desarrolladora;generos;disponible;valoracion;conexion
     *
     * @param linea linea CSV a parsear
     * @return objeto Videojuego (Fisico o Digital), o null si el formato es invalido
     */
    private Videojuego parsear(String linea) {
        try {
            String[] campos = linea.split(";");
            String tipo = campos[0].trim().toUpperCase();

            int id = Integer.parseInt(campos[1].trim());
            String nombre = campos[2].trim();
            String desarrolladora = campos[3].trim();

            // Los generos estan separados por punto dentro de su campo
            ArrayList<String> generos = Arrays.stream(campos[4].trim().split("\\."))
                    .map(String::trim)
                    .collect(Collectors.toCollection(ArrayList::new));

            boolean disponible = Boolean.parseBoolean(campos[5].trim());
            double valoracion = Double.parseDouble(campos[6].trim());

            Videojuego v;
            if (tipo.equals("FISICO")) {
                String estado = campos[7].trim();
                boolean caja = Boolean.parseBoolean(campos[8].trim());
                v = new Fisico(id, nombre, desarrolladora, generos, disponible, estado, caja);
            } else {
                boolean conexion = Boolean.parseBoolean(campos[7].trim());
                v = new Digital(id, nombre, desarrolladora, generos, disponible, conexion);
            }

            // Establecer la valoracion leida del fichero
            v.setValoracion(valoracion);
            return v;

        } catch (Exception e) {
            System.err.println("Error al parsear linea CSV: " + linea);
            System.err.println("Detalle: " + e.getMessage());
            return null;
        }
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
