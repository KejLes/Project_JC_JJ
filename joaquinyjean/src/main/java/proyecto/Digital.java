package proyecto;
import java.util.ArrayList;
public class Digital extends Videojuego{
    boolean conexionRequerida; //si el juego requiere conexión a internet para jugarlo
    

    public Digital (int ID, String Nombre, String Desarrolladora, ArrayList<String> Genero, boolean Disponible, boolean conexionRequerida){
        super(ID, Nombre, Desarrolladora, Genero, Disponible);
        this.conexionRequerida=conexionRequerida;
    }

    @Override
    public void mostrarInfo(){
        //el metodo abstracto que necesita
    }
}
