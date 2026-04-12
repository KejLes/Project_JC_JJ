package proyecto.interfaces;

/**
 * Interfaz que obliga a las clases que la implementen a saber
 * convertirse tanto a formato JSON como a formato CSV.
 * 
 * Videojuego usara principalmente CSV para persistencia,
 * mientras que Soundtrack usara JSON. Ambos implementan los dos
 * metodos para mantener la flexibilidad.
 */
public interface Exportable {

    /**
     * Devuelve una representacion JSON del objeto.
     * No se usan librerias externas: se construye manualmente con StringBuilder.
     *
     * @return cadena con el objeto serializado en formato JSON
     */
    String toJson();

    /**
     * Devuelve una representacion CSV del objeto.
     * Los campos se separan con punto y coma (;).
     * Si un campo contiene multiples valores (como generos),
     * estos se separan con punto (.) dentro del mismo campo.
     *
     * @return cadena con el objeto serializado en formato CSV
     */
    String toCsv();
}
