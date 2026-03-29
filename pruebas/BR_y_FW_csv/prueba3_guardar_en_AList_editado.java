/*
 * prueba3_AList.java
 * Lee el CSV y lo guarda en un ArrayList de Videojuegos que luego se imprime
 * Funciona correctamente
 *
 * La idea aquí es crear otra clase que pase de arrayList a CSV sin StringBuilder
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class prueba3_guardar_en_AList_editado {
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
