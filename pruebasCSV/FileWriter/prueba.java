
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class prueba {
    public static void main(String[] args) {
        try (FileWriter fw = new FileWriter("prueba.csv");
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("""
Hola\nMundo
            """);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
