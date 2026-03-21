package proyecto;
import java.util.ArrayList;
public class Main {
    public <T extends Videojuego> void buscarporID (int ID, T[] videojuegos){//metodo generico que  busca el videojuego por id
        for (T videojuego : videojuegos){ //indistintanmente si es fisico o digital
            if (videojuego.getID()==ID){
                System.out.println(videojuego.BuscarVideojuego());
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Fisico kingdomHearts = new Fisico(1, "Kingdom Hearts", "Square Enix", new ArrayList<String>(), true, "Bueno", true);
        Digital Fornite = new Digital(2, "Fortnite", "Epic Games", new ArrayList<String>(), true, true);
        
    
    }
}