package proyecto.coleccion_videojuego;

import java.util.ArrayList;

/**
 * Representa un videojuego en formato fisico.
 * Extiende {@link Videojuego} anadiendo dos atributos propios:
 * el estado del disco/cartucho y si incluye caja original.
 *
 * Formato CSV (campos separados por ;):
 * FISICO;id;nombre;desarrolladora;genero1.genero2;disponible;valoracion;estado;caja
 */
public class Fisico extends Videojuego {

    private String estado;  // "Nuevo", "Usado", "Como nuevo", etc.
    private boolean caja;   // true si el juego viene con caja original

    /**
     * Constructor de videojuego fisico.
     *
     * @param id               identificador numerico unico
     * @param nombre           titulo del videojuego
     * @param desarrolladora   empresa desarrolladora
     * @param genero           lista de generos
     * @param estadoDisponible true si esta disponible
     * @param estado           estado fisico del producto ("Nuevo", "Usado", etc.)
     * @param caja             true si incluye caja original
     * @throws IllegalArgumentException si el estado esta vacio
     */
    public Fisico(int id, String nombre, String desarrolladora,
                  ArrayList<String> genero, boolean estadoDisponible,
                  String estado, boolean caja) {
        super(id, nombre, desarrolladora, genero, estadoDisponible);
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado del juego fisico no puede estar vacio");
        }
        this.estado = estado;
        this.caja = caja;
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
        System.out.println("Estado del juego: " + estado);
        System.out.println("Caja incluida: " + (caja ? "Si" : "No"));
    }

    /** {@inheritDoc} */
    @Override
    public String obtenerFormatoDescripcion() {
        return String.format("Fisico - ID: %d, Nombre: %s, Desarrolladora: %s, "
                        + "Genero: %s, Disponible: %s, Valoracion: %.1f, "
                        + "Estado: %s, Caja: %s",
                getId(), getNombre(), getDesarrolladora(), getGeneroTexto(),
                isEstadoDisponible() ? "Si" : "No", getValoracion(),
                estado, caja ? "Si" : "No");
    }

    /** {@inheritDoc} */
    @Override
    public String getTipo() {
        return "FISICO";
    }

    /**
     * Serializa el videojuego fisico a formato JSON manual.
     * No se usan librerias externas.
     *
     * @return cadena JSON con todos los atributos
     */
    @Override
    public String toJson() {
        return String.format(java.util.Locale.US,
                "{\"tipo\": \"FISICO\", \"id\": %d, \"nombre\": \"%s\", "
                        + "\"desarrolladora\": \"%s\", \"genero\": \"%s\", "
                        + "\"estadoDisponible\": %b, \"valoracion\": %.1f, "
                        + "\"estado\": \"%s\", \"caja\": %b}",
                getId(), getNombre(), getDesarrolladora(), getGeneroTexto(),
                isEstadoDisponible(), getValoracion(), estado, caja);
    }

    /**
     * Serializa el videojuego fisico a formato CSV.
     * Los generos se separan con punto (.) dentro de su campo.
     *
     * @return cadena CSV con campos separados por punto y coma
     */
    @Override
    public String toCsv() {
        // Locale.US fuerza el punto decimal (4.8) en vez de coma (4,8)
        // que usaria el locale espanol por defecto
        return String.format(java.util.Locale.US, "FISICO;%d;%s;%s;%s;%b;%.1f;%s;%b",
                getId(), getNombre(), getDesarrolladora(), generosParaCsv(),
                isEstadoDisponible(), getValoracion(), estado, caja);
    }

    // --- Getters ---

    /**
     * @return estado fisico del juego ("Nuevo", "Usado", etc.)
     */
    public String getEstado() {
        return this.estado;
    }

    /**
     * @return true si incluye caja original
     */
    public boolean getCaja() {
        return this.caja;
    }
}
