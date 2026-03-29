// Imprimir únicamente la lista de nombres de los vdeojuegos

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class prueba2_BufferedReader {
    public static void main(String[] args) {
        String fileName = "prueba_BufferedReader.csv";
        ArrayList<String> nombresJuegoList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String[] splittedLine;
            while ((line = br.readLine()) != null) {
                splittedLine = line.split(",");
                nombresJuegoList.add(splittedLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : nombresJuegoList)
            System.out.printf("%s\n", s);
    }
}
