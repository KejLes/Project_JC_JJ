package proyecto.colecciones_soundtrack;

import proyecto.Valorable;
import proyecto.Exportable;

public abstract class Soundtrack implements AccionSound, Valorable, Exportable {
    /**
 * Clase abstracta que representa un soundtrack.
 */

    int ID;
    String Nombre;
    String Compositor;
    String VideojuegoAsociado;
    boolean estadoDisponible;
    double valoracion;

    public Soundtrack(int ID, String Nombre, String Compositor, String VideojuegoAsociado, boolean estadoDisponible) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Compositor = Compositor;
        this.VideojuegoAsociado = VideojuegoAsociado;
        this.estadoDisponible = estadoDisponible;
        this.valoracion = 0.0;
    }

    @Override
    public double getValoracion() { return valoracion; }

    @Override
    public void setValoracion(double puntuacion) { this.valoracion = puntuacion; }

    @Override
    public String toCSV() {
        return ID + "," + Nombre + "," + Compositor + "," + VideojuegoAsociado + "," + estadoDisponible;
    }

    @Override
    public abstract String toJSON();

    /**
     * Los getters son... pues eso, getters, metodos para obtener los valores de los atributos
	 */
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
    /**
	 * Retornará una lista de soundtracks, por ahora sólo una lista vacia
	 */
    @Override
    public String listarSoundtracks(){
        String lista = " ";
		return lista;
    }

    /**
	 * El metodo devolverá más que solo el nombre pero por ahora...
	 */
    @Override
    public String buscarSoundtrack(){
        return this.Nombre;
    }

    /**
	 * Agregar al carrito si está estadoDisponible
	 */
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
