package proyecto;

public abstract class Soundtrack implements AccionSound{
    /**
 * Clase abstracta que representa un soundtrack. Contiene atributos comunes a todos los
 * soundtracks y métodos que pueden ser implementados por las clases hijas.
 * Implementa la interfaz AccionSound para definir acciones comunes como listar soundtracks,
 * buscar soundtracks y agregar al carrito de compras.
 *
 * No han sido comentadas los metodos get, porque se entienden por sí mismos.
 */

    int ID;
    String Nombre;
    String Compositor;
    String VideojuegoAsociado;
    boolean estadoDisponible; // Por si se implementa lo de adquirir, la disponibilidad para meterlo al carrito

    public Soundtrack(int ID, String Nombre, String Compositor, String VideojuegoAsociado, boolean estadoDisponible) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Compositor = Compositor;
        this.VideojuegoAsociado = VideojuegoAsociado;
        this.estadoDisponible = estadoDisponible;

    }

    public int getID() {
        return this.ID;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public String getCompositor() {
        return this.Compositor;
    }

    public String getVideojuegoAsociado() {
        return this.VideojuegoAsociado;
    }

    public boolean getDisponible() {
        return this.estadoDisponible;
    }

    @Override
    public String listarSoundtracks(){
        String lista = " ";
		return lista;
    }
    @Override
    public String buscarSoundtrack(){
        return this.Nombre;
    }
    @Override
    public boolean agregarAlCarrito (boolean estadoDisponible){
        return estadoDisponible;
    }

    /**
	 * Metodo abstracto obligatorio para los metodos hijos
	 */
    public abstract void mostrarInfo();

    /**
	 * Metodo abstracto obligatorio para los metodos hijos
	 */
	public abstract String obtenerFormatoDescripcion();
}
