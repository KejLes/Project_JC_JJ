package proyecto.coleccion_videojuego;

import java.util.ArrayList;

/**
 * Representa un videojuego en formato digital.
 * Extiende {@link Videojuego} anadiendo un atributo propio:
 * si requiere conexion a internet para funcionar.
 *
 * Formato CSV (campos separados por ;):
 * DIGITAL;id;nombre;desarrolladora;genero1.genero2;disponible;valoracion;conexionRequerida
 */
public class Digital extends Videojuego {

    private boolean conexionRequerida;

    /**
     * Constructor de videojuego digital.
     *
     * @param id                 identificador numerico unico
     * @param nombre             titulo del videojuego
     * @param desarrolladora     empresa desarrolladora
     * @param genero             lista de generos
     * @param estadoDisponible   true si esta disponible
     * @param conexionRequerida  true si necesita conexion a internet
     */
    public Digital(int id, String nombre, String desarrolladora,
                   ArrayList<String> genero, boolean estadoDisponible,
                   boolean conexionRequerida) {
        super(id, nombre, desarrolladora, genero, estadoDisponible);
        this.conexionRequerida = conexionRequerida;
    }

    /** {@inheritDoc} */
    @Override
    public void mostrarInformacion() {
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Desarrolladora: " + getDesarrolladora());
        System.out.println("Genero: " + getGeneroTexto());
        System.out.println("Disponible: " + (isEstadoDisponible() ? "Si" : "No"));
        System.out.println("Valoracion: " + getValoracion());
        System.out.println("Conexion requerida: " + (conexionRequerida ? "Si" : "No"));
    }

    /** {@inheritDoc} */
    @Override
    public String obtenerFormatoDescripcion() {
        return String.format("Digital - ID: %d, Nombre: %s, Desarrolladora: %s, "
                        + "Genero: %s, Disponible: %s, Valoracion: %.1f, "
                        + "Conexion requerida: %s",
                getId(), getNombre(), getDesarrolladora(), getGeneroTexto(),
                isEstadoDisponible() ? "Si" : "No", getValoracion(),
                conexionRequerida ? "Si" : "No");
    }

    /** {@inheritDoc} */
    @Override
    public String getTipo() {
        return "DIGITAL";
    }

    /**
     * Serializa el videojuego digital a formato JSON manual.
     *
     * @return cadena JSON con todos los atributos
     */
    @Override
    public String toJson() {
        return String.format(
                "{\"tipo\": \"DIGITAL\", \"id\": %d, \"nombre\": \"%s\", "
                        + "\"desarrolladora\": \"%s\", \"genero\": \"%s\", "
                        + "\"estadoDisponible\": %b, \"valoracion\": %.1f, "
                        + "\"conexionRequerida\": %b}",
                getId(), getNombre(), getDesarrolladora(), getGeneroTexto(),
                isEstadoDisponible(), getValoracion(), conexionRequerida);
    }

    /**
     * Serializa el videojuego digital a formato CSV.
     *
     * @return cadena CSV con campos separados por punto y coma
     */
    @Override
    public String toCsv() {
        return String.format("DIGITAL;%d;%s;%s;%s;%b;%.1f;%b",
                getId(), getNombre(), getDesarrolladora(), generosParaCsv(),
                isEstadoDisponible(), getValoracion(), conexionRequerida);
    }

    // --- Getter ---

    /**
     * @return true si el juego requiere conexion a internet
     */
    public boolean getConexionRequerida() {
        return this.conexionRequerida;
    }
}
