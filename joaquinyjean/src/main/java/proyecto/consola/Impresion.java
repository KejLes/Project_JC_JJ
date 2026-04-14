package proyecto.consola;

import java.util.Scanner;

/**
 * Por el momento este es el avance de Jean para la interfaz de la terminal
 */
public class Impresion {
    Scanner lee = new Scanner(System.in);

    public Impresion() {
    }

    /**
     * Imprime el menu, y con ayuda de otros metodos, lee la entrada y devuelve
     * la opcion elegida
     * @return
     */
    public int interfaz() {
        int opcionInterfaz;

        System.out.print("""
            -----BIENVENIDO A LA BIBLIOTECA DE VIDEOJUEGOS-----
            Escriba el número de la acción que desea realizar:
            1. Mostrar todos los títulos
            2. Buscar videojuego (ID o título)
            3. Ver carrito de compras
            4. Salir

                """);
        opcionInterfaz = lee.nextInt();
        verificarOpcion(opcionInterfaz);
        return (opcionInterfaz);
    }

    /**
     * Llamada por interfaz, verifica la entrada del usuario y devuleve la
     * opcion escogida.
     * Si introduce una opcion no valida, se le indicará al usuario y se
     * volverá a mostrar el menu
     * @param opcionInterfaz
     * @return
     */
    public boolean verificarOpcion(int opcionInterfaz) {
        boolean reiniciarOpciones;

        reiniciarOpciones = false;
        switch (opcionInterfaz) {
            case 1 -> {
                System.out.println("Mostrando todos los títulos...");
                // Lógica para mostrar todos los títulos
                imprimirTitulos();
            } case 2 -> {
                System.out.println("Buscando videojuego...");
                // Lógica para buscar videojuego por ID o título
                buscarVideojuego();
            } case 3 -> {
                System.out.println("Mostrando carrito de compras...");
                // Lógica para mostrar el carrito de compras
                mostrarCarrito();
            } case 4 -> {
                System.out.println("Saliendo del programa. ¡Gracias por visitar la biblioteca de videojuegos!");
                System.exit(0);
            }
            default -> {
                System.out.println("Opción no válida. Por favor, elija una opción del 1 al 4.");
                reiniciarOpciones = true;
            }
        }
        volverAInterfaz(reiniciarOpciones);
        return (reiniciarOpciones);
    }

    /**
     * Si se introduce una opcion no valida se muestra nuevamente el menu
     * @param reiniciarOpciones
     */
    private void volverAInterfaz(boolean reiniciarOpciones) {
        if (reiniciarOpciones) {
            interfaz();
        }
    }

    /**
     * Para comprobrar que funciona bien.
     * En un futuro estara la logica implementada
     */
    public void imprimirTitulos() {
        // Lógica para mostrar todos los títulos
    }

    /**
     * Para comprobrar que funciona bien.
     * En un futuro estara la logica implementada
     */
    public void buscarVideojuego() {
        // Lógica para buscar videojuego por ID o título
    }

    /**
     * Para comprobrar que funciona bien.
     * En un futuro estara la logica implementada
     */
    public void mostrarCarrito() {
        // Lógica para mostrar el carrito de compras
    }

}
