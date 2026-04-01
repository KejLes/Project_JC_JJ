package proyecto.colecciones_videojuego;

import java.util.ArrayList;

/**
 * Clase abstracta que representa un videojuego. Contiene atributos comunes a todos los
 * videojuegos y métodos que pueden ser implementados por las clases hijas (Fisico y Digital).
 * Implementa la interfaz Accionar para definir acciones comunes como listar videojuegos,
 * buscar videojuegos y agregar al carrito de compras.
 *
 * No han sido comentadas los metodos get, porque se entienden por sí mismos.
 */
public abstract class Videojuego implements Accionar {
	int							id;
	String						nombre;
	String						desarrolladora;
	ArrayList<String>			genero;
	boolean						estadoDisponible; // Por si se implementa lo de adquirir, la disponibilidad para meterlo al carrito

	public Videojuego(int id, String nombre,
			String desarrolladora, ArrayList<String> genero,
			boolean estadoDisponible) {
		this.id = id;
		this.nombre = nombre;
		this.desarrolladora = desarrolladora;
		this.genero = genero;
		this.estadoDisponible = estadoDisponible;
	}
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
