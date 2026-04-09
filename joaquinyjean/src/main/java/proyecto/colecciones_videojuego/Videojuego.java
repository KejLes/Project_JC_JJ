package proyecto.colecciones_videojuego;

import java.util.ArrayList;
import proyecto.Valorable;
import proyecto.Exportable;

/**
 * Clase abstracta que representa un videojuego.
 */
public abstract class Videojuego implements Accionar, Valorable, Exportable {
	int							id;
	String						nombre;
	String						desarrolladora;
	ArrayList<String>			genero;
	boolean						estadoDisponible;
    double                      valoracion;

	public Videojuego(int id, String nombre,
			String desarrolladora, ArrayList<String> genero,
			boolean estadoDisponible) {
		this.id = id;
		this.nombre = nombre;
		this.desarrolladora = desarrolladora;
		this.genero = genero;
		this.estadoDisponible = estadoDisponible;
        this.valoracion = 0.0;
	}

    @Override
    public double getValoracion() { return valoracion; }

    @Override
    public void setValoracion(double puntuacion) { this.valoracion = puntuacion; }

    @Override
    public String toCSV() {
        return id + "," + nombre + "," + desarrolladora + "," + getStringGenero('.') + "," + estadoDisponible;
    }

    @Override
    public abstract String toJSON();
	/*Los getters son... pues eso, getters,
	metodos para obtener los valores de los atributos
	 */
	public int getId() {
		return this.id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public String getDesarrolladora() {
		return this.desarrolladora;
	}

	/**
	 * Devuelve genero en ArrayList
	 * @return
	 */
	public ArrayList<String> getALGenero() {
		return this.genero;
	}

	/**
	 * Devuelve genero en String
	 * @return
	 */
	public String getStringGenero(char caracter) {
		String toReturn;

		toReturn = "";	// Es para inicializarlo
		for (int i = 0; i < genero.size(); i++)
		{
			toReturn += genero.get(i);
			if (i < genero.size() - 1)
				toReturn += caracter;
		}
		return (toReturn);
	}

	public boolean getDisponible() {
		return this.estadoDisponible;
	}

	/**
	 * Retornará una lista de videojuegos, por ahora sólo una lista vacia
	 */
	@Override
	public String listarVideojuegos() {
		String lista = " ";
		return lista;
	}

	/**
	 * El metodo devolverá más que solo el nombre pero por ahora...
	 */
	@Override
	public String buscarVideojuego() {
		return this.nombre;
	}

	/**
	 * Agregar al carrito si está estadoDisponible
	 */
	@Override
	public boolean agregarAlCarrito(boolean estadoDisponible) {
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
