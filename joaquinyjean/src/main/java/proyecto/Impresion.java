package proyecto;
import java.util.Scanner;



public class Impresion {
    Scanner lee = new Scanner(System.in);
    public int Interfaz() {
        System.out.println("-----BIENVENIDO A LA BIBLIOTECA DE VIDEOJUEGOS-----");
        System.out.println("Escriba el número de la acción que desea realizar:");
        System.out.println("1. Mostrar todos los títulos");
        System.out.println("2. Buscar videojuego (ID o título)");
        System.out.println("3. Ver carrito de compras");
        System.out.println("4. Salir");
        int opcionInterfaz = lee.nextInt();
        verificarOpcion(opcionInterfaz);
        return opcionInterfaz;
    }

    public boolean verificarOpcion(int opcionInterfaz) {
        boolean reiniciarOpciones = false;
        switch (opcionInterfaz) {
            case 1:
                System.out.println("Mostrando todos los títulos...");
                // Lógica para mostrar todos los títulos
                imprimirTitulos();
                break;
            case 2:
                System.out.println("Buscando videojuego...");
                // Lógica para buscar videojuego por ID o título
                buscarVideojuego();
                break;
            case 3:
                System.out.println("Mostrando carrito de compras...");
                // Lógica para mostrar el carrito de compras
                mostrarCarrito();
                break;
            case 4:
                System.out.println("Saliendo del programa. ¡Gracias por visitar la biblioteca de videojuegos!");
                System.exit(0);
                break;
            default:
                System.out.println("Opción no válida. Por favor, elija una opción del 1 al 4.");
                reiniciarOpciones = true;
                break;
        }

        volveraInterfaz(reiniciarOpciones);
        return reiniciarOpciones;
    }

    private void volveraInterfaz(boolean reiniciarOpciones) {
        if (reiniciarOpciones) {
            Interfaz();
        }
    }

    //estos nomas para que no se muestren errores, estos no vendran aqui luego se rar
    public void imprimirTitulos() {
        // Lógica para mostrar todos los títulos
    }
    public void buscarVideojuego() {
        // Lógica para buscar videojuego por ID o título
    }
    public void mostrarCarrito() {
        // Lógica para mostrar el carrito de compras
    }

}