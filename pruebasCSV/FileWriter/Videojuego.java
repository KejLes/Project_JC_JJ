
import java.util.ArrayList;

public class Videojuego{
	String						ID;
	String						Nombre;
	String						Desarrolladora;
	ArrayList<String>			Genero;
	boolean						estadoDisponible;

	public Videojuego(String ID, String Nombre, String Desarrolladora, ArrayList<String> Genero, boolean estadoDisponible) {
		this.ID = ID;
		this.Nombre = Nombre;
		this.Desarrolladora = Desarrolladora;
		this.Genero = Genero;
		this.estadoDisponible = estadoDisponible;
	}

	@Override
	public String toString(){
		String toReturn = (
			this.ID + "," +
			this.Nombre + "," +
			this.Desarrolladora + "," +
			this.Genero + "," +
			this.estadoDisponible
		);
		return toReturn;
	}

	public String getId() { return ID; }
    public String getJuego() { return Nombre; }
    public String getDesarrolladora() { return Desarrolladora; }
    public ArrayList<String> getGenero() { return Genero; }
    public boolean isEstado_disponible() { return estadoDisponible; }
}
