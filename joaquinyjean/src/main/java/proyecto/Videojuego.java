package proyecto;

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
	int							ID;
	String						Nombre;
	String						Desarrolladora;
	ArrayList<String>			Genero;
	boolean						estadoDisponible; // Por si se implementa lo de adquirir, la disponibilidad para meterlo al carrito

	public Videojuego(int ID, String Nombre, String Desarrolladora, ArrayList<String> Genero, boolean estadoDisponible) {
		this.ID = ID;
		this.Nombre = Nombre;
		this.Desarrolladora = Desarrolladora;
		this.Genero = Genero;
		this.estadoDisponible = estadoDisponible;

	}

	public int getID() {
		return this.ID;
	}

	public String getNombre() {
		return this.Nombre;
	}

	public String getDesarrolladora() {
		return this.Desarrolladora;
	}

	public ArrayList<String> getGenero() {
		return this.Genero;
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
		return this.Nombre;
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
}
