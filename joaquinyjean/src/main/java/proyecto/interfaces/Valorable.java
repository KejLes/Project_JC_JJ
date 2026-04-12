package proyecto.interfaces;

/**
 * Interfaz que permite puntuar elementos de la coleccion.
 * Tanto Videojuego como Soundtrack son valorables con una nota del 0 al 5.
 *
 * Se usa como capacidad transversal: cualquier clase futura
 * (por ejemplo, un DLC o una Resenia) podria implementarla
 * sin necesidad de heredar de Videojuego o Soundtrack.
 */
public interface Valorable {

    /**
     * Asigna una valoracion al elemento.
     *
     * @param nota puntuacion entre 0.0 y 5.0
     * @throws IllegalArgumentException si la nota esta fuera del rango [0, 5]
     */
    void valorar(double nota);

    /**
     * Obtiene la valoracion actual del elemento.
     *
     * @return valoracion actual (0.0 si no ha sido valorado)
     */
    double getValoracion();
}
