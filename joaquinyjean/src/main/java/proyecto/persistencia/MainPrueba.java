package proyecto.persistencia;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import proyecto.colecciones_videojuego.Videojuego;

public class MainPrueba {
	public static void main(String[] args) {
		TreeMap<Integer, Videojuego>	videojuegoMap;
		String						nombreArchivoOrigen;
		String						nombreArchivoDestino;

		System.out.println(new File(".").getAbsolutePath());
		nombreArchivoOrigen = "target/classes/resources/biblioteca_juegos.csv";
		// nombreArchivoOrigen = System.getProperty("user.dir") +
		// 	File.separator +
		// 	"target" +
		// 	File.separator +
		// 	"classess" +
		// 	File.separator +
		// 	"biblioteca_juegos.csv";
			System.out.println("---------------\n" + nombreArchivoOrigen);
		videojuegoMap = GestorFicherosCSV.leerCSV(nombreArchivoOrigen);
		System.out.println("\nSe ha leido y guardado a un HashMap\n");

		for (Map.Entry<Integer, Videojuego> entry : videojuegoMap.entrySet())
		{
			System.out.print("\n");
			entry.getValue().mostrarInfo();
		}

		nombreArchivoDestino = "target/classes/resources/biblioteca_prueba_escritura.csv";
		GestorFicherosCSV.escribirCSV(videojuegoMap, nombreArchivoDestino);
		System.out.println("Se ha escrito el archivo a:\n\tbiblioteca_prueba_escritura.csv");
	}
}
