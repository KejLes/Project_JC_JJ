
//parA jean E:\jf\Programacion\unidad5\proyecto again\Project_JC_JJ\pruebajson\soundtracks.json
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce la ruta del archivo JSON: ");
        String ruta = scanner.nextLine();

        GestorJSON gestor = new GestorJSON(ruta);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║   🎮 Biblioteca Soundtracks  ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Listar todos             ║");
            System.out.println("║  2. Buscar por ID            ║");
            System.out.println("║  3. Buscar por nombre        ║");
            System.out.println("║  4. Agregar nuevo            ║");
            System.out.println("║  5. Salir                    ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine();

            try {
                switch (opcion) {

                    case "1":
                        gestor.listar();
                        break;

                    case "2":
                        System.out.print("ID a buscar: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        gestor.buscarPorId(id);
                        break;

                    case "3":
                        System.out.print("Nombre o videojuego: ");
                        String texto = scanner.nextLine();
                        gestor.buscarPorNombre(texto);
                        break;

                    case "4":
                        System.out.print("ID: ");
                        int nId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nombre: ");
                        String nNombre = scanner.nextLine();
                        System.out.print("Compositor: ");
                        String nComp = scanner.nextLine();
                        System.out.print("Videojuego asociado: ");
                        String nJuego = scanner.nextLine();
                        System.out.print("Duración (ej: 3:45): ");
                        String nDur = scanner.nextLine();
                        System.out.print("¿Disponible? (true/false): ");
                        boolean nDisp = Boolean.parseBoolean(scanner.nextLine());

                        gestor.agregar(new SoundtrackVideojuego(
                            nId, nNombre, nComp, nJuego, nDur, nDisp
                        ));
                        break;

                    case "5":
                        salir = true;
                        System.out.println("👋 Hasta luego.");
                        break;

                    default:
                        System.out.println("⚠️ Opción no válida.");
                }

            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
