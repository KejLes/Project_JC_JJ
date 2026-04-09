package proyecto;

/**
 * Interfaz para objetos que pueden recibir una valoración de 0 a 5 estrellas.
 */
public interface Valorable {
    /**
     * Devuelve la valoración media del objeto.
     * @return Puntuación de 0 a 5.
     */
    double getValoracion();

    /**
     * Establece una nueva valoración.
     * @param puntuacion Puntuación de 0 a 5.
     */
    void setValoracion(double puntuacion);
}
