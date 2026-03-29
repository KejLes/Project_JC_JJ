import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner lee = new Scanner(System.in);

        System.out.print("Introduce la ruta del archivo JSON: ");
        String ruta = lee.nextLine();

        GestorJSON gestor = new GestorJSON(ruta);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n");
            System.out.println("   Biblioteca Soundtracks  ");
            System.out.println("");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por nombre");
            System.out.println("4. Agregar nuevo");
            System.out.println("5. Salir");
            System.out.println("");
            System.out.print("Opción: ");

            String opcion = lee.nextLine();

            try {
                switch (opcion) {

                    case "1":
                        gestor.listar();
                        break;

                    case "2":
                        System.out.print("ID a buscar: ");
                        int id = Integer.parseInt(lee.nextLine());
                        gestor.buscarPorId(id);
                        break;

                    case "3":
                        System.out.print("Nombre o videojuego: ");
                        String texto = lee.nextLine();
                        gestor.buscarPorNombre(texto);
                        break;

                    case "4":
                        System.out.print("ID: ");
                        int nId = Integer.parseInt(lee.nextLine());
                        System.out.print("Nombre: ");
                        String nNombre = lee.nextLine();
                        System.out.print("Compositor: ");
                        String nComp = lee.nextLine();
                        System.out.print("Videojuego asociado: ");
                        String nJuego = lee.nextLine();
                        System.out.print("Duración (ej: 3:45): ");
                        String nDur = lee.nextLine();
                        System.out.print("¿Disponible? (true/false): ");//si pones false no se podrá agregar
                        boolean nDisp = Boolean.parseBoolean(lee.nextLine());

                        gestor.agregar(new SoundtrackVideojuego(
                            nId, nNombre, nComp, nJuego, nDur, nDisp
                        ));
                        break;

                    case "5":
                        salir = true;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        lee.close();
    }
}
