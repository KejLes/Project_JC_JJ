/*
 * prueba3_AList.java
 * Lee el CSV y lo guarda en un ArrayList de Videojuegos que luego se imprime
 * Funciona correctamente
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class prueba3_guardar_en_AList {
	public static void main(String[] args) {
		String	fileName	= "prueba_BufferedReader.csv";
		ArrayList<Vprueba3_guardar_en_AList_editado new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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
		{
			System.out.println(v.toString());
		}
	}
}
