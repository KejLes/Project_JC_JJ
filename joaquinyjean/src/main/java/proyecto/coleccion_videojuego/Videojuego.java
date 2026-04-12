package proyecto.coleccion_videojuego;

import java.util.ArrayList;

import proyecto.interfaces.Exportable;
import proyecto.interfaces.Valorable;

/**
 * Clase abstracta que representa un videojuego generico.
 * No se puede instanciar directamente: se debe usar una de sus subclases
 * ({@link Fisico} o {@link Digital}).
 *
 * Implementa {@link Valorable} para permitir puntuacion y
 * {@link Exportable} para serializacion a CSV y JSON.
 *
 * Atributos comunes: id, nombre, desarrolladora, genero (lista), 
 * estado de disponibilidad y valoracion.
 */
public abstract class Videojuego implements Valorable, Exportable {

    private int id;
    private String nombre;
    private String desarrolladora;
    private ArrayList<String> genero;
    private boolean estadoDisponible;
    private double valoracion;

    /**
     * Constructor de Videojuego.
     *
     * @param id              identificador numerico unico
     * @param nombre          titulo del videojuego
     * @param desarrolladora  empresa desarrolladora
     * @param genero          lista de generos (puede tener varios)
     * @param estadoDisponible true si esta disponible, false en caso contrario
     * @throws IllegalArgumentException si el nombre o la desarrolladora estan vacios
     */
    public Videojuego(int id, String nombre, String desarrolladora,
                      ArrayList<String> genero, boolean estadoDisponible) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (desarrolladora == null || desarrolladora.isBlank()) {
            throw new IllegalArgumentException("La desarrolladora no puede estar vacia");
        }
        this.id = id;
        this.nombre = nombre;
        this.desarrolladora = desarrolladora;
        this.genero = (genero != null) ? genero : new ArrayList<>();
        this.estadoDisponible = estadoDisponible;
        this.valoracion = 0.0;
    }

    /**
     * Asigna una valoracion al videojuego.
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
     * Muestra por consola toda la informacion del videojuego.
     * Cada subclase anade sus atributos especificos.
     */
    public abstract void mostrarInformacion();

    /**
     * Devuelve una descripcion formateada en una sola linea.
     *
     * @return cadena con la descripcion resumida
     */
    public abstract String obtenerFormatoDescripcion();

    /**
     * Devuelve el tipo de videojuego como cadena ("FISICO" o "DIGITAL").
     * Cada subclase implementa este metodo.
     *
     * @return tipo del videojuego
     */
    public abstract String getTipo();

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDesarrolladora() {
        return desarrolladora;
    }

    public ArrayList<String> getGenero() {
        return genero;
    }

    /**
     * Devuelve los generos unidos por coma, util para mostrar en pantalla.
     *
     * @return cadena con los generos separados por coma
     */
    public String getGeneroTexto() {
        return String.join(", ", genero);
    }

    public boolean isEstadoDisponible() {
        return estadoDisponible;
    }

    /**
     * Convierte la lista de generos al formato CSV (separados por punto).
     * Ejemplo: "Aventura.RPG.Mundo abierto"
     *
     * @return cadena con generos separados por punto
     */
    protected String generosParaCsv() {
        return String.join(".", genero);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | %s | %s | Val: %.1f",
                id, nombre, desarrolladora, getGeneroTexto(),
                estadoDisponible ? "Disponible" : "No disponible", valoracion);
    }
}
