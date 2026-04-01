package proyecto.persistencia;

/*
 *
 * Para leer y escribir en csv
 * El metodo devuelve un TreeMap con todos los videojuegos, ya sea digital o fisico
 * Otro metodo recibe el TreeMap y lo guarda en un CSV
 *
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import proyecto.colecciones_videojuego.Digital;
import proyecto.colecciones_videojuego.Fisico;
import proyecto.colecciones_videojuego.Videojuego;

/**
 * Para leer y escribir en csv.
 * El metodo devuelve un TreeMap con todos los videojuegos, ya sea digital o fisico.
 * Otro metodo recibe el TreeMap y lo guarda en un CSV.
 * Cada uno tiene dos metodos auxiliares.
 *
 * Métodos:
 * 	1 - Leer CSV
 * 	2 . Pasar contenido a Fisico
 * 	3 . Pasar contenido a Digital
 * 	4 - Escribir en CSV
 * 	5 - Pasar Fisico a String
 * 	6 - Pasar Digital a String
 */
public class GestorFicherosCSV {

	/**
	 * Lee el CSV, puede o leer el de videojuegos fisicos o digitales.
	 * Se ayuda de dos metodos auxiliares arrayADigital y arrayAFisico para pasarlo todo a HashMap
	 * @param nombreArchivo
	 * @param opcion
	 * @return
	 */
	public static TreeMap<Integer, Videojuego> leerCSV(
			String nombreArchivo
		) {
		TreeMap<Integer, Videojuego> videojuegosMap;
		String		omitirCabecera;
		String		linea;
		String[]	splittedLine;
		Videojuego	videojuego;

		videojuegosMap = new TreeMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
			omitirCabecera = br.readLine();	// Las tres primeras lineas se omite
			omitirCabecera = br.readLine();
			omitirCabecera = br.readLine();
			while ((linea = br.readLine()) != null) {
				splittedLine = linea.split(",");
				if (splittedLine[1].equals("fisico"))
					videojuego = arrayAFisico(splittedLine);
				else	//DIGITAL
					videojuego = arrayADigital(splittedLine);
				videojuegosMap.put(videojuego.getId(), videojuego);
			}
		} catch (IOException e) {
			System.out.printf("""
					Error durante la lectura del archivo.
					Mensaje
					%s
					""", e.getMessage());
			e.printStackTrace();
		}

		return (videojuegosMap);
		// System.out.println("\nSe ha leido el archivo, ahora voy a impimirlo:\n");

		// for (Videojuego v: videojuegos)
		// System.out.println(v.toString());

		// System.out.println("\nSe guardado en listas, ahora lo guardo en un CSV\n");
		// guardarEnCSV(videojuegos, "guardarA.csv");
	}

	/**
	 * Metodo auxiliar que toma un array de Strings y lo pasa al objeto Fisico
	 * @param campos
	 * @return
	 */
	public static Videojuego arrayAFisico(String[] campos) {
		Videojuego fisico;
		ArrayList<String> generos;

		generos = new ArrayList<>(Arrays.asList(
				campos[4].split("\\.")));
		fisico = new Fisico(
				Integer.parseInt(campos[0]),
				campos[2],
				campos[3],
				generos,
				campos[5].contains("true"),
				campos[6],
				campos[7].contains("true"));
		return (fisico);
	}

	/**
	 * Metodo auxiliar que toma un array de Strings y lo pasa al objeto Digital
	 * @param campos
	 * @return
	 */
	public static Videojuego arrayADigital(String[] campos) {
		Videojuego digital;
		ArrayList<String> generos;

		generos = new ArrayList<>(Arrays.asList(
				campos[4].split("\\.")));
		digital = new Digital(
				Integer.parseInt(campos[0]),
				campos[2],
				campos[3],
				generos,
				campos[5].contains("true"),
				campos[6].contains("true"));
		return (digital);
	}

	/**
	 * Recibe como argumento un HashMap, se le indica si es Digital o Fisico y guarda todo a CSV
	 * @param videojuegos
	 * @param nombreArchivo
	 * @param opcion
	 */
	public static void escribirCSV(
			TreeMap<Integer, Videojuego> videojuegos,
			String nombreArchivo
		) {
		String	lineaAEscribir;

		try (FileWriter fw = new FileWriter(nombreArchivo, true);
				BufferedWriter bw = new BufferedWriter(fw)
			) {
			escribirCabeceraCSV(nombreArchivo);
			for (Map.Entry<Integer, Videojuego> entry : videojuegos.entrySet()) {
				if (entry.getValue() instanceof Digital)
					lineaAEscribir = DigitalToString(entry.getValue());
				else
					lineaAEscribir = FisicoToString(entry.getValue());
				bw.newLine();
				bw.write(lineaAEscribir);
			}
		} catch (IOException e) {
			System.out.printf("""
					Error durante la escritura del archivo.
					Mensaje
					%s
					""", e.getMessage());
			e.printStackTrace();
		}
			// System.out.println("Archivo escrito exitosamente.");
	}

	/**
	 * Los campos de Digital los pasa a String para escribirlo al CSV
	 * @param Digital
	 * @return
	 */
	public static String DigitalToString (Videojuego Digital) {
		String	lineaAEscribir;
		Digital	videojuego;

		videojuego = (Digital) Digital;
		lineaAEscribir = (
			videojuego.getId() + "," +
			"digital," +
			videojuego.getNombre() + "," +
			videojuego.getDesarrolladora() + "," +
			videojuego.getStringGenero('.') + "," +
			videojuego.getDisponible() + "," +
			videojuego.getConexionRequerida()
		);
		return (lineaAEscribir);
	}

	/**
	 * Los campos de Fisico los pasa a String para escribirlo al CSV
	 * @param Fisico
	 * @return
	 */
	public static String FisicoToString (Videojuego Fisico) {
		String lineaAEscribir;
		Fisico videojuego;

		videojuego = (Fisico) Fisico;
		lineaAEscribir = (
			videojuego.getId() + "," +
			"fisico," +
			videojuego.getNombre() + "," +
			videojuego.getDesarrolladora() + "," +
			videojuego.getStringGenero('.') + "," +
			videojuego.getDisponible() + "," +
			videojuego.getEstado() + "," +
			videojuego.getCaja()
		);
		return (lineaAEscribir);
	}

	/**
	 * Simplemente escribe la cabecera del CSV
	 * @param nombreArchivo
	 */
	public static void escribirCabeceraCSV(String nombreArchivo) {
		try (FileWriter fw = new FileWriter(nombreArchivo);
				BufferedWriter bw = new BufferedWriter(fw)
			) {
			bw.write("ID,juego,desarrolladora,genero,estado_disponible");
			bw.newLine();
			bw.write("ID,fisico,juego,desarrolladora,genero,estado_disponible,estado,caja");
			bw.newLine();
			bw.write("ID,digital,juego,desarrolladora,genero,estado_disponible,conexionRequerida");
		} catch (IOException e) {
			System.err.println("Error al escribir el archivo al fichero: " + e.getMessage());
		}
	}
}
