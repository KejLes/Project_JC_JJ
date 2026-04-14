package proyecto.colecciones_videojuego;

/**
 * Esta interfaz define las acciones que pueden realizar los videojuegos, como listar, buscar y agregar al carrito. Es implementada por la clase Videojuego, lo que garantiza que todos los videojuegos tendrán estas funcionalidades básicas.
 */
public interface Accionar {	// si revisas, videojuego tiene los metodos de esta interfaz. Lo reviso don Jean

    /**
     * Lista los videojuegos disponibles en la biblioteca.
     * @return
     */
    public String listarVideojuegos();

    /**
     * Busca un videojuego por su ID o título.
     * @return
     */
    public String buscarVideojuego();

    /**
     * Agrega un videojuego al carrito de compras si está disponible.
     * @param estadoDisponible
     * @return
     */
    public boolean agregarAlCarrito (boolean estadoDisponible);
}
