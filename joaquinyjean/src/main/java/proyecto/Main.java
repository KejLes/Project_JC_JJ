package proyecto;

import java.util.ArrayList;

import proyecto.colecciones_videojuego.Digital;
import proyecto.colecciones_videojuego.Fisico;

public class Main {
    // public <T extends Videojuego> void buscarporID (int ID, T[] videojuegos){//metodo generico que  busca el videojuego por id
    //     for (T videojuego : videojuegos){ //indistintanmente si es fisico o digital
    //         if (videojuego.getID() == ID){
    //             System.out.println(videojuego.buscarVideojuego());
    //         }
    //     }
    // }
    public static void main(String[] args) {
        // Aqui deberia de ir impresion.interfaz()
        //Impresion impresion = new Impresion();
        // Impresion.interfaz();

        ArrayList<String> generosKH = new ArrayList<>();
        generosKH.add("RPG");
        generosKH.add("Acción");

        System.out.println("Hello videogame library!\n");
<<<<<<< HEAD
        Fisico kingdomHearts = new Fisico(
            1
            , "Kingdom Hearts"
            , "Square Enix"
            , generosKH
            , true
            , "Bueno"
            , true
        );
=======
        Fisico kingdomHearts = new Fisico("1", "Kingdom Hearts", "Square Enix", generosKH, true, "Bueno", true);
>>>>>>> d718f0394815024c41cf3e94ca5fd62b2ab5c163
        kingdomHearts.mostrarInfo();

        ArrayList<String> generosFortnite = new ArrayList<>();
        generosFortnite.add("Battle Royale");
        generosFortnite.add("Shooter");

        Digital Fortnite = new Digital(
            2
            , "Fortnite"
            , "Epic Games"
            , generosFortnite
            , true
            , true
        );
        Fortnite.mostrarInfo();
    }
}
