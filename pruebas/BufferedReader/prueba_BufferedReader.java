
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class prueba_BufferedReader {
    public static void main(String[] args) {
        String fileName = "prueba_BufferedReader.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
