package proyecto;


/**
 * Esta interfaz define las acciones que pueden realizar los videojuegos, como listar, buscar y agregar al carrito. Es implementada por la clase Videojuego, lo que garantiza que todos los videojuegos tendrán estas funcionalidades básicas.
 */
public interface AccionSound {
    
    /**
     * Lista los soundtracks de videojuegos disponibles en la biblioteca.
     * @return
     */
    public String listarSoundtracks();

    /**
     * Busca un soundtrack por su ID o título.
     * @return
     */
    public String buscarSoundtrack();
    
    /**
     * Agrega un soundtrack al carrito de compras si está disponible.
     * @param estadoDisponible
     * @return
     */
    public boolean agregarAlCarrito (boolean estadoDisponible);


}
