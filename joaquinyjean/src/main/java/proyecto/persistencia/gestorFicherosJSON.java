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
import java.util.List;

import proyecto.colecciones_soundtrack.SoundtrackVideojuego;

public class GestorFicherosJSON {

    private String ruta;

    public GestorFicherosJSON(String ruta) {
        this.ruta = ruta;
    }

    // Leer todos los soundtracks del JSON
    public List<SoundtrackVideojuego> leerSoundtracks() throws IOException {
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

    // Listar todos los soundtracks
    public void listar() throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        System.out.println("\nBiblioteca de Soundtracks");
        System.out.println("══════════════════════════════════════════");

        if (lista.isEmpty()) {
            System.out.println("No hay soundtracks registrados.");
            return;
        }

        for (SoundtrackVideojuego s : lista) {
            System.out.println(s.obtenerFormatoDescripcion());
        }

        System.out.println("══════════════════════════════════════════");
        System.out.println("Total: " + lista.size() + " soundtracks\n");
    }

    // Buscar por ID
    public void buscarPorId(int idBuscado) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        System.out.println("\nBuscando ID " + idBuscado + "...");

        boolean encontrado = false;
        for (SoundtrackVideojuego s : lista) {
            if (s.getID() == idBuscado) {
                s.mostrarInfo();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No existe ningún soundtrack con ID: " + idBuscado + "\n");
        }
    }

    // Buscar por nombre (búsqueda parcial)
    public void buscarPorNombre(String texto) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();
        String textoBuscar = texto.toLowerCase();

        System.out.println("\nResultados para: \"" + texto + "\"");
        System.out.println("──────────────────────────────────────────");

        boolean hayResultados = false;
        for (SoundtrackVideojuego s : lista) {
            if (s.getNombre().toLowerCase().contains(textoBuscar) ||
                s.getVideojuegoAsociado().toLowerCase().contains(textoBuscar)) {
                System.out.println(s.obtenerFormatoDescripcion());
                hayResultados = true;
            }
        }

        if (!hayResultados) {
            System.out.println("Sin resultados para: " + texto);
        }
        System.out.println();
    }

    // Agregar un soundtrack nuevo
    public void agregar(SoundtrackVideojuego nuevo) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        if (!nuevo.agregarAlCarrito(nuevo.getDisponible())) {
            System.out.println("El soundtrack no está disponible.");
            return;
        }

        lista.add(nuevo);
        escribirJSON(lista);
        System.out.println("Soundtrack agregado: " + nuevo.getNombre() + "\n");
    }

    // Escribir la lista entera como JSON
    private void escribirJSON(List<SoundtrackVideojuego> lista) throws IOException {
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

    // Extraer el valor de una línea JSON
    private String extraerValor(String linea) {
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
