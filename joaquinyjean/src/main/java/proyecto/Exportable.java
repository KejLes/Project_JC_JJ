package proyecto;

/**
 * Interfaz para objetos que pueden exportarse a diferentes formatos de texto.
 */
public interface Exportable {
    /**
     * Devuelve la representación del objeto en formato CSV.
     * @return Una cadena formateada en CSV.
     */
    String toCSV();

    /**
     * Devuelve la representación del objeto en formato JSON.
     * @return Una cadena formateada en JSON.
     */
    String toJSON();
}
