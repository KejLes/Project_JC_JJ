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
	String[]					plataformas; // Array de plataformas (PS5, PC, Switch, etc.)

	public Videojuego(int id, String nombre,
			String desarrolladora, ArrayList<String> genero,
			boolean estadoDisponible) {
		this.id = id;
		this.nombre = nombre;
		this.desarrolladora = desarrolladora;
		this.genero = genero;
		this.estadoDisponible = estadoDisponible;
        this.valoracion = 0.0;
		this.plataformas = new String[0]; // Array vacio por defecto
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
	 * @return lista de generos
	 */
	public ArrayList<String> getALGenero() {
		return this.genero;
	}

	/**
	 * Devuelve genero en String separado por el caracter indicado.
	 * @param caracter separador entre generos
	 * @return generos como texto
	 */
	public String getStringGenero(char caracter) {
		String toReturn;

		toReturn = "";
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
	 * Devuelve el array de plataformas del videojuego.
	 * @return array de plataformas
	 */
	public String[] getPlataformas() {
		return this.plataformas;
	}

	/**
	 * Establece las plataformas del videojuego.
	 * @param plataformas array con los nombres de las plataformas
	 */
	public void setPlataformas(String[] plataformas) {
		this.plataformas = plataformas;
	}

	@Override
	public String listarVideojuegos() {
		String lista = " ";
		return lista;
	}

	@Override
	public String buscarVideojuego() {
		return this.nombre;
	}

	@Override
	public boolean agregarAlCarrito(boolean estadoDisponible) {
		return estadoDisponible;
	}

	public abstract void mostrarInfo();

	public abstract String obtenerFormatoDescripcion();
}
