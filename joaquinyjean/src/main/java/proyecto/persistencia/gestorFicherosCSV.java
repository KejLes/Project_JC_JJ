package proyecto.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import proyecto.colecciones_videojuego.Digital;
import proyecto.colecciones_videojuego.Fisico;
import proyecto.colecciones_videojuego.Videojuego;

/**
 * Lee y escribe videojuegos en formato CSV.
 * Usa BufferedReader para leer, FileWriter para escribir
 * y RandomAccessFile para buscar por posicion de byte.
 */
public class gestorFicherosCSV {

	// Indice de posiciones para RandomAccessFile
	// Clave: ID del videojuego, Valor: posicion en bytes de su linea
	private static Map<Integer, Long> indicePosiciones = new HashMap<>();

	/**
	 * Lee el CSV y devuelve todos los videojuegos en un TreeMap.
	 * Omite las 3 primeras lineas (cabeceras).
	 * @param nombreArchivo ruta del fichero CSV
	 * @return mapa con los videojuegos indexados por ID
	 */
	public static TreeMap<Integer, Videojuego> leerCSV(String nombreArchivo) {
		TreeMap<Integer, Videojuego> videojuegosMap;
		String		omitirCabecera;
		String		linea;
		String[]	splittedLine;
		Videojuego	videojuego;

		videojuegosMap = new TreeMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
			omitirCabecera = br.readLine();
			omitirCabecera = br.readLine();
			omitirCabecera = br.readLine();
			while ((linea = br.readLine()) != null) {
				splittedLine = linea.split(",");
				if (splittedLine[1].equals("fisico"))
					videojuego = arrayAFisico(splittedLine);
				else
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

		// Construir el indice de posiciones para RandomAccessFile
		construirIndice(nombreArchivo);

		return (videojuegosMap);
	}

	/**
	 * Toma un array de Strings y lo convierte en objeto Fisico.
	 * @param campos campos de la linea CSV
	 * @return videojuego fisico
	 */
	public static Videojuego arrayAFisico(String[] campos) {
		Videojuego fisico;
		ArrayList<String> generos;

		generos = new ArrayList<>(Arrays.asList(campos[4].split("\\.")));
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
	 * Toma un array de Strings y lo convierte en objeto Digital.
	 * @param campos campos de la linea CSV
	 * @return videojuego digital
	 */
	public static Videojuego arrayADigital(String[] campos) {
		Videojuego digital;
		ArrayList<String> generos;

		generos = new ArrayList<>(Arrays.asList(campos[4].split("\\.")));
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
	 * Guarda todos los videojuegos del TreeMap en el fichero CSV.
	 * Primero escribe las cabeceras y luego los datos.
	 * @param videojuegos mapa de videojuegos a guardar
	 * @param nombreArchivo ruta del fichero CSV
	 */
	public static void escribirCSV(
			TreeMap<Integer, Videojuego> videojuegos,
			String nombreArchivo) {
		String	lineaAEscribir;

		try (FileWriter fw = new FileWriter(nombreArchivo, true);
				BufferedWriter bw = new BufferedWriter(fw)) {
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

		// Reconstruir el indice despues de escribir
		construirIndice(nombreArchivo);
	}

	/**
	 * Convierte un videojuego Digital a linea CSV.
	 * @param Digital videojuego digital
	 * @return linea CSV
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
	 * Convierte un videojuego Fisico a linea CSV.
	 * @param Fisico videojuego fisico
	 * @return linea CSV
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
	 * Escribe las 3 lineas de cabecera del CSV.
	 * @param nombreArchivo ruta del fichero CSV
	 */
	public static void escribirCabeceraCSV(String nombreArchivo) {
		try (FileWriter fw = new FileWriter(nombreArchivo);
				BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write("ID,juego,desarrolladora,genero,estado_disponible");
			bw.newLine();
			bw.write("ID,fisico,juego,desarrolladora,genero,estado_disponible,estado,caja");
			bw.newLine();
			bw.write("ID,digital,juego,desarrolladora,genero,estado_disponible,conexionRequerida");
		} catch (IOException e) {
			System.err.println("Error al escribir cabecera: " + e.getMessage());
		}
	}

	// =====================================================================
	//  METODOS NUEVOS: RandomAccessFile, Iterator y eliminacion
	// =====================================================================

	/**
	 * Construye un indice de posiciones de byte para cada videojuego en el CSV.
	 * Usa RandomAccessFile para registrar donde empieza cada linea de datos.
	 * Esto permite luego saltar directamente a un registro sin leer todo el fichero.
	 * @param ruta ruta del fichero CSV
	 */
	public static void construirIndice(String ruta) {
		indicePosiciones.clear();
		try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
			// Saltar las 3 lineas de cabecera
			raf.readLine();
			raf.readLine();
			raf.readLine();

			long posicion;
			String linea;
			while (true) {
				posicion = raf.getFilePointer();
				linea = raf.readLine();
				if (linea == null) break;
				if (!linea.isBlank()) {
					String[] campos = linea.split(",");
					int id = Integer.parseInt(campos[0]);
					indicePosiciones.put(id, posicion);
				}
			}
		} catch (IOException e) {
			System.err.println("Error al construir indice RAF: " + e.getMessage());
		}
	}

	/**
	 * Busca un videojuego por ID usando RandomAccessFile.
	 * Salta directamente a la posicion de byte del registro
	 * sin leer todo el fichero linea a linea.
	 * @param id identificador del videojuego
	 * @param ruta ruta del fichero CSV
	 * @return el videojuego encontrado, o null si no existe
	 */
	public static Videojuego buscarPorIdRAF(int id, String ruta) {
		Long posicion = indicePosiciones.get(id);
		if (posicion == null) {
			return null;
		}

		try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
			raf.seek(posicion);
			String linea = raf.readLine();
			if (linea != null) {
				String[] campos = linea.split(",");
				if (campos[1].equals("fisico"))
					return arrayAFisico(campos);
				else
					return arrayADigital(campos);
			}
		} catch (IOException e) {
			System.err.println("Error al buscar con RAF: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Elimina un videojuego por ID y reescribe el CSV.
	 * Usa un Iterator explicito para recorrer y eliminar de forma segura.
	 * @param id identificador del videojuego a borrar
	 * @param ruta ruta del fichero CSV
	 * @return true si se encontro y elimino, false si no existia
	 */
	public static boolean eliminarPorId(int id, String ruta) {
		TreeMap<Integer, Videojuego> mapa = leerCSV(ruta);

		// Iterator explicito: recorre las entradas y elimina de forma segura
		Iterator<Map.Entry<Integer, Videojuego>> it = mapa.entrySet().iterator();
		boolean encontrado = false;
		while (it.hasNext()) {
			Map.Entry<Integer, Videojuego> entry = it.next();
			if (entry.getKey() == id) {
				it.remove();
				encontrado = true;
				break;
			}
		}

		if (encontrado) {
			escribirCSV(mapa, ruta);
		}
		return encontrado;
	}
}
