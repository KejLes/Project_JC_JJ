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
 *
 * Creo debería añadir más
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import proyecto.colecciones_videojuego.Fisico;
import proyecto.colecciones_videojuego.Videojuego;

public class GestorFicherosCSV {

	/**
	 * Llama a leerFisicoODigital con ambas opciones y junta ambos HashMap en uno
	 */
	public static HashMap<String, Videojuego> leerCSV () {
		//leerFisicoODigital
		return null;
	}

	/**
	 * Lee el CSV, puede o leer el de videojuegos fisicos o digitales
	 */
	public static HashMap<String, Videojuego> leerFisicoODigital (String nombreArchivo, eVideojuego opcion) {
		HashMap<String, Videojuego>	videojuegosMap;
		String						line;
		String[]					splittedLine;
		Videojuego					videojuego;

		videojuegosMap = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
			line = br.readLine();	// La primera linea se omite
			while ((line = br.readLine()) != null) {
				splittedLine = line.split(",");
				if (opcion == eVideojuego.FISICO)
					videojuego = guardarFisico(splittedLine);
				else	//	Opcion es DIGITAL
					videojuego = guardarDigital(splittedLine);
				videojuegosMap.put(videojuego.getID(), videojuego);
			}
		} catch (IOException e) {
			System.out.printf("""
					Error durante la lectura del archivo.
					Mensaje
					%s
					""", e.getMessage());
		}

		return (videojuegosMap);
		// System.out.println("\nSe ha leido el archivo, ahora voy a impimirlo:\n");

		// for (Videojuego v: videojuegos)
		// 	System.out.println(v.toString());

		// System.out.println("\nSe guardado en listas, ahora lo guardo en un CSV\n");
		// guardarEnCSV(videojuegos, "guardarA.csv");
	}

	public static Videojuego guardarFisico(String [] campos){
		Videojuego			fisico;
		ArrayList<String>	generos;

		generos = new ArrayList<>(Arrays.asList(
			campos[3].split("\\.")
		));
		fisico = new Fisico(
			campos[0],
			campos[1],
			campos[2],
			generos,
			campos[4].contains("true"),
			campos[5],
			campos[6].contains("true")
		);
		return (fisico);
	}

	public static Videojuego guardarDigital(String [] campos){
		return null;
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
