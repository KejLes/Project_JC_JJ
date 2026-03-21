package proyecto;

import java.util.ArrayList;

public class Fisico extends Videojuego{
    String Estado;//el estado del juego, si es nuevo o usado
    boolean Caja; //el juego viene en caja o sólo posee el disco/cartucho

    public Fisico (int ID, String Nombre, String Desarrolladora, ArrayList<String> Genero, boolean Disponible, String Estado, boolean Caja){
        super(ID, Nombre, Desarrolladora, Genero, Disponible);
        this.Estado=Estado;
        this.Caja=Caja;
    }

    @Override
    public void mostrarInfo(){
        //el metodo abstracto que necesita
    }
}
