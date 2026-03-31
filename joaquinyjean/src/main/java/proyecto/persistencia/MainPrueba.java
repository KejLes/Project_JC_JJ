package proyecto.persistencia;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import proyecto.colecciones_videojuego.Videojuego;

public class MainPrueba {
	public static void main(String[] args) {
		HashMap<String,Videojuego>	videojuegoMap;
		String						nombreArchivoOrigen;
		String						nombreArchivoDestino;

		System.out.println(new File(".").getAbsolutePath());
		nombreArchivoOrigen = "target/classes/resources/biblioteca_juegos.csv";
		videojuegoMap = GestorFicherosCSV.leerCSV(nombreArchivoOrigen);
		System.out.println("\nSe ha leido y guardado a un HashMap\n");

		for (Map.Entry<String,Videojuego> entry : videojuegoMap.entrySet())
		{
			System.out.print("\n");
			entry.getValue().mostrarInfo();
		}

		nombreArchivoDestino = "target/classes/resources/biblioteca_prueba_escritura.csv";
		GestorFicherosCSV.escribirCSV(videojuegoMap, nombreArchivoDestino);
		System.out.println("Se ha escrito el archivo a:\n\tbiblioteca_prueba_escritura.csv");
	}
}
