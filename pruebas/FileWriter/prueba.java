
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class prueba {
    public static void main(String[] args) {
        try (FileWriter fw = new FileWriter("prueba.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Hola, mundo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
