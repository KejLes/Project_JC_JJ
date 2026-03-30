package proyecto.persistencia;

/*
 * (No es javadoc)
 * Para leer y escribir en csv
 * La clase debería devolver la lista con todos los videojuegos, ya sea digital o fisico
 *
 * Implementará metodos para guardar en la lista si es digital o fisico, osea, dos metodos
 * Para leer será lo mismo que la linea anterior, entonces debería de haber 3 métodos más
 *
 * Métodos:
 * 	1 - Leer CSV
 * 	2 . Pasar contenido a Fisico
 * 	3 . Pasar contenido a Digital
 * 	4 - Escribir en CSV
 * 	5 - Pasar Fisico a String
 * 	6 - Pasar Digital a String
 */

import proyecto.colecciones_videojuego.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class gestorFicherosCSV {
	public static void main(String[] args) {
		String	nombreArchivo	= "prueba.csv";
		ArrayList<Videojuego> videojuegos = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
			String line;
			String[] splittedLine;
			String[] generos;
			while ((line = br.readLine()) != null) {
				if (line.contains("ID"))	// Ignora la primera linea
					continue;
				splittedLine = line.split(",");
				generos = splittedLine[3].split("\\.");
				videojuegos.add(new Videojuego(
					splittedLine[0],
					splittedLine[1],
					splittedLine[2],
					new ArrayList<>(Arrays.asList(generos)),
					splittedLine[4].contains("true")
				));
			}
		} catch (IOException e) {
			System.out.printf("""
					Error durante la lectura del archivo.
					Mensaje
					%s
					""", e.getMessage());
		}

		System.out.println("\nSe ha leido el archivo, ahora voy a impimirlo:\n");

		for (Videojuego v: videojuegos)
			System.out.println(v.toString());

		System.out.println("\nSe guardado en listas, ahora lo guardo en un CSV\n");
		guardarEnCSV(videojuegos, "guardarA.csv");
	}

	public static void guardarEnCSV(ArrayList<Videojuego> videojuegos, String nombreArchivo) {
        try (FileWriter fw = new FileWriter(nombreArchivo);
             BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write("ID,juego,desarrolladora,genero,estado_disponible");
				bw.newLine();
            for (Videojuego v: videojuegos){
				bw.write(v.toString());
				bw.newLine();
			}
            System.out.println("Archivo escrito exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
