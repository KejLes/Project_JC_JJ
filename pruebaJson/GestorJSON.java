

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorJSON {

    private String ruta;

    public GestorJSON(String ruta) {
        this.ruta = ruta;
    }

    // lee todos los soundtracks del jsson 
    public List<SoundtrackVideojuego> leerSoundtracks() throws IOException {
        List<SoundtrackVideojuego> lista = new ArrayList<>();

        File archivo = new File(ruta); //(bob esponja demacrado)
        if (!archivo.exists()) {
            System.out.println(" Archivo no encontrado: " + archivo.getAbsolutePath());//asi poder copiar la ruta y agregar lo que falta
            return lista;
        }

        try (BufferedReader br = new BufferedReader(//br es nombre común para un bufferedreader, no es importante
                new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {//recomendado para las tildes

            // vsariables temporales para cada objeto para ir guardando los datos a medida que se leen las líneas
            int id = -1;
            String nombre = null;
            String compositor = null;
            String videojuegoAsociado = null;
            String duracion = null;
            boolean estadoDisponible = false;

            String linea;//cada línea del archivo se lee aquí, y se procesa según su contenido
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();//trim sirve para eliminar espacios al inicio y al final de la línea

                if (linea.equals("{")) {
                    // nuevo objeto: resetear variables
                    id = -1;
                    nombre = null;
                    compositor = null;
                    videojuegoAsociado = null;
                    duracion = null;
                    estadoDisponible = false;

                } else if (linea.startsWith("\"id\"")) {//si la línea empieza con id, entonces se extrae el valor y se guarda en la variable id
                    id = Integer.parseInt(extraerValor(linea));

                } else if (linea.startsWith("\"nombre\"")) {//igualmente para el nombre, compositor, videojuego asociado, duración y estado disponible
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
                    // fin del objeto, crear el soundtrack si tiene lo mínimo
                    if (id != -1 && nombre != null) {
                        lista.add(new SoundtrackVideojuego(id,nombre,compositor,videojuegoAsociado,duracion,estadoDisponible));
                    }
                }
            }
        }

        return lista;
    }

    // listar todos los soundtracks
    public void listar() throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        System.out.println("\n Biblioteca de Soundtracks");
        System.out.println("");

        if (lista.isEmpty()) {
            System.out.println("No hay soundtracks registrados.");
            return;
        }

        for (SoundtrackVideojuego s : lista) { //s es el nombre común para un objeto de la clase SoundtrackVideojuego, no es importante, se puede cambiar por cualquier otro nombre, pero s de soundtrack es fácil de entender
            // Usamos el método de la interfaz AccionSound
            System.out.println(s.obtenerFormatoDescripcion());
        }

        System.out.println("");
        System.out.println("Total: " + lista.size() + " soundtracks\n");//dice la cantidad de msuicas
    }
    // busca por id 
    public void buscarPorId(int id) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        System.out.println("\n Buscando id" + id + "..."); //xd que de la ilusión que es una pantalla de carga
        boolean encontrado = false;
        for (SoundtrackVideojuego s : lista) {//por cada soundtrack de la lista, se compara su id con el id buscado, si coincide, se muestra su información usando el método mostrarinfo
            if (s.getID() == id) {
                s.mostrarInfo(); // usamos el método abstracto implementado
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {//si no fue encontrado
            System.out.println("No existe ningún soundtrack con id: " + id + " pipipi \n");
        }
    }
    // burcar por nombre (búsqueda parcial) 
    public void buscarPorNombre(String texto) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();
        String textoBuscar = texto.toLowerCase();

        System.out.println("\n Resultados para: \"" + texto + "\"");
        System.out.println("");

        boolean hayResultados = false;
        for (SoundtrackVideojuego s : lista) {
            // buscamos en nombre y en videojuego asociado
            if (s.getNombre().toLowerCase().contains(textoBuscar) ||
                s.getVideojuegoAsociado().toLowerCase().contains(textoBuscar)) {
                System.out.println(s.obtenerFormatoDescripcion());
                hayResultados = true;
            }
        }

        if (!hayResultados) {
            System.out.println(" NoN hay resultados para: " + texto);
        }
        System.out.println();
    }

    // agregar un soundtrack nuevo
    public void agregar(SoundtrackVideojuego nuevo) throws IOException {
        List<SoundtrackVideojuego> lista = leerSoundtracks();

        // usar agregaralcarrito de la interfaz para verificar disponibilidad, con el boolean ese del principio
        if (!nuevo.agregarAlCarrito(nuevo.getDisponible())) {
            System.out.println(" El soundtrack no está disponible.");
            return;
        }
        lista.add(nuevo); // agrega a la lista y luego se escribe la lista entera como json, sobreescribiendo 
        escribirJSON(lista);
        System.out.println(" Soundtrack agregado: " + nuevo.getNombre() + "\n");
    }

    //escribir la lista entera como json
    private void escribirJSON(List<SoundtrackVideojuego> lista) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(//bw es nombre para bufferedwriter
                new OutputStreamWriter(new FileOutputStream(ruta, false), "UTF-8"))) {

            bw.write("[");//esto escribe un formato json basico
            bw.newLine();

            for (int i = 0; i < lista.size(); i++) {//esto recorre la lista de soundtracks y escrribe
                bw.write(lista.get(i).toJSON());
                if (i < lista.size() - 1) bw.write(",");
                bw.newLine();
            }

            bw.write("]");//esto cierra el fromato jason
            bw.newLine();
        }
    }

    // extraer el valor de una línea json
    private String extraerValor(String linea) {//extrae el valor de una linea json
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