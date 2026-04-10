package proyecto.persistencia;

import proyecto.colecciones_soundtrack.SoundtrackVideojuego;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Buscar automaticamente el JSON en la carpeta resources
        String ruta = buscarJSON("joaquinyjean/src/main/java/proyecto/resources");

        if (ruta == null) {
            System.out.println("No se encontro ningun archivo .json en la carpeta resources.");
            return;
        }

        System.out.println("Archivo cargado: " + ruta);

        gestorFicherosJSON gestor = new gestorFicherosJSON(ruta);
        Scanner scanner   = new Scanner(System.in);
        boolean salir     = false;

        while (!salir) {
            System.out.println("\n+------------------------------+");
            System.out.println("|   Biblioteca Soundtracks     |");
            System.out.println("+------------------------------+");
            System.out.println("|  1. Listar todos             |");
            System.out.println("|  2. Buscar por ID            |");
            System.out.println("|  3. Buscar por nombre        |");
            System.out.println("|  4. Agregar nuevo            |");
            System.out.println("|  5. Salir                    |");
            System.out.println("+------------------------------+");
            System.out.print("Opcion: ");

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
                        System.out.print("Duracion (ej: 3:45): ");
                        String nDur = scanner.nextLine();
                        System.out.print("Disponible? (true/false): ");
                        boolean nDisp = Boolean.parseBoolean(scanner.nextLine());

                        gestor.agregar(new SoundtrackVideojuego(
                            nId, nNombre, nComp, nJuego, nDur, nDisp
                        ));
                        break;

                    case "5":
                        salir = true;
                        System.out.println("Hasta luego.");
                        break;

                    default:
                        System.out.println("Opcion no valida.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Busca el primer archivo .json dentro de la carpeta indicada.
     * Devuelve la ruta como String, o null si no encuentra ninguno.
     */
    private static String buscarJSON(String carpeta) {
        File directorio = new File(carpeta);

        // Verificar que la carpeta existe y es un directorio
        if (!directorio.exists() || !directorio.isDirectory()) {
            System.out.println("La carpeta no existe: " + directorio.getAbsolutePath());
            return null;
        }

        // Listar solo los archivos que terminen en .json
        File[] archivos = directorio.listFiles(
            (dir, nombre) -> nombre.endsWith(".json")
        );

        if (archivos == null || archivos.length == 0) {
            return null;
        }

        // Devolver la ruta del primero (y unico) que encuentre
        return archivos[0].getPath();
    }
}