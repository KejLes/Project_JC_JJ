package proyecto.coleccion_soundtrack;

import proyecto.interfaces.Exportable;
import proyecto.interfaces.Valorable;

/**
 * Representa un soundtrack (banda sonora) de un videojuego.
 * A diferencia de Videojuego, Soundtrack no usa herencia porque
 * no hay subtipos distintos: todos los soundtracks comparten
 * la misma estructura.
 *
 * Implementa {@link Valorable} para puntuacion y {@link Exportable}
 * para serializacion a JSON (formato principal) y CSV.
 *
 * La duracion se almacena internamente en segundos, pero se muestra
 * en formato mm:ss mediante {@link #duracionEnFormato()}.
 */
public class Soundtrack implements Valorable, Exportable {

    private int id;
    private String nombre;
    private String compositor;
    private String videojuegoAsociado;
    private boolean estadoDisponible;
    private double valoracion;
    private int duracion; // Duracion en segundos

    /**
     * Constructor de Soundtrack.
     *
     * @param id                  identificador numerico unico
     * @param nombre              titulo del soundtrack
     * @param compositor          nombre del compositor
     * @param videojuegoAsociado  videojuego al que pertenece
     * @param estadoDisponible    true si esta disponible
     * @param duracion            duracion en segundos (debe ser positiva)
     * @throws IllegalArgumentException si el nombre o compositor estan vacios,
     *                                  o si la duracion es negativa
     */
    public Soundtrack(int id, String nombre, String compositor,
                      String videojuegoAsociado, boolean estadoDisponible,
                      int duracion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (compositor == null || compositor.isBlank()) {
            throw new IllegalArgumentException("El compositor no puede estar vacio");
        }
        if (duracion < 0) {
            throw new IllegalArgumentException("La duracion no puede ser negativa");
        }
        this.id = id;
        this.nombre = nombre;
        this.compositor = compositor;
        this.videojuegoAsociado = (videojuegoAsociado != null) ? videojuegoAsociado : "";
        this.estadoDisponible = estadoDisponible;
        this.duracion = duracion;
        this.valoracion = 0.0;
    }

    /**
     * Convierte la duracion de segundos a formato mm:ss.
     * Ejemplo: 185 segundos -> "03:05"
     *
     * @return cadena con la duracion en formato mm:ss
     */
    public String duracionEnFormato() {
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /**
     * Asigna una valoracion al soundtrack.
     *
     * @param nota puntuacion entre 0.0 y 5.0
     * @throws IllegalArgumentException si la nota esta fuera del rango
     */
    @Override
    public void valorar(double nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("La valoracion debe estar entre 0 y 5");
        }
        this.valoracion = nota;
    }

    /** {@inheritDoc} */
    @Override
    public double getValoracion() {
        return this.valoracion;
    }

    /**
     * Establece la valoracion directamente (usado al cargar desde fichero).
     *
     * @param valoracion valor entre 0.0 y 5.0
     */
    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    /**
     * Serializa el soundtrack a formato JSON manual.
     * Se usa StringBuilder en lugar de librerias externas.
     *
     * @return cadena JSON con todos los atributos
     */
    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\": ").append(id).append(", ");
        sb.append("\"nombre\": \"").append(nombre).append("\", ");
        sb.append("\"compositor\": \"").append(compositor).append("\", ");
        sb.append("\"videojuegoAsociado\": \"").append(videojuegoAsociado).append("\", ");
        sb.append("\"estadoDisponible\": ").append(estadoDisponible).append(", ");
        sb.append("\"valoracion\": ").append(String.format(java.util.Locale.US, "%.1f", valoracion)).append(", ");
        sb.append("\"duracion\": ").append(duracion);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Serializa el soundtrack a formato CSV.
     * Campos separados por punto y coma.
     *
     * @return cadena CSV
     */
    @Override
    public String toCsv() {
        return String.format(java.util.Locale.US, "%d;%s;%s;%s;%b;%.1f;%d",
                id, nombre, compositor, videojuegoAsociado,
                estadoDisponible, valoracion, duracion);
    }

    /**
     * Muestra por consola toda la informacion del soundtrack.
     */
    public void mostrarInformacion() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Compositor: " + compositor);
        System.out.println("Videojuego asociado: " + videojuegoAsociado);
        System.out.println("Disponible: " + (estadoDisponible ? "Si" : "No"));
        System.out.println("Valoracion: " + valoracion);
        System.out.println("Duracion: " + duracionEnFormato());
    }

    /**
     * Devuelve una descripcion formateada en una sola linea.
     *
     * @return cadena con la descripcion resumida
     */
    public String obtenerFormatoDescripcion() {
        return String.format("Soundtrack - ID: %d, Nombre: %s, Compositor: %s, "
                        + "Videojuego: %s, Disponible: %s, Valoracion: %.1f, "
                        + "Duracion: %s",
                id, nombre, compositor, videojuegoAsociado,
                estadoDisponible ? "Si" : "No", valoracion, duracionEnFormato());
    }

    // --- Getters ---

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getCompositor() {
        return this.compositor;
    }

    public String getVideojuegoAsociado() {
        return this.videojuegoAsociado;
    }

    public boolean isEstadoDisponible() {
        return this.estadoDisponible;
    }

    public int getDuracion() {
        return this.duracion;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | %s | %s | Val: %.1f | %s",
                id, nombre, compositor, videojuegoAsociado,
                estadoDisponible ? "Disponible" : "No disponible",
                valoracion, duracionEnFormato());
    }
}
