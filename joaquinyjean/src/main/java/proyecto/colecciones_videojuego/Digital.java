package proyecto.colecciones_videojuego;

import java.util.ArrayList;

/**
 * Clase que representa un videojuego digital. Hereda de la clase abstracta
 * Videojuego e implementa el método mostrarInfo() para mostrar información
 * específica de los videojuegos digitales, como si requieren conexión a
 * internet para jugar. Contiene un atributo booleano conexionRequerida para
 * indicar si el juego necesita conexión a internet. 
 */
public class Digital extends Videojuego{
    boolean conexionRequerida; //si el juego requiere conexión a internet para jugarlo


    public Digital (
            int ID,
            String Nombre,
            String Desarrolladora,
            ArrayList<String> Genero,
            boolean estadoDisponible,
            boolean conexionRequerida
        ){
        super(ID, Nombre, Desarrolladora, Genero, estadoDisponible);
        this.conexionRequerida = conexionRequerida;
    }

    /**
     * El metodo abstracto que necesita. Hereda de videojuego
     */
    @Override
    public void mostrarInfo(){
        System.out.printf("""
        Información del videojuego digital:
        - ID: %d
        - Nombre: %s
        - Desarrolladora: %s
        - Género: %s
        %s\n
                """, id, nombre, desarrolladora, getStringGenero(','), obtenerFormatoDescripcion());
    }

    /**
     * El metodo abstracto que devuelve información del campo de Digital
     */
    @Override
    public String obtenerFormatoDescripcion() {
        return ("- Conexión requerida: " + conexionRequerida);
    }

    public boolean getConexionRequerida() {
        return (this.conexionRequerida);
    }

    @Override
    public String toJSON() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"nombre\": \"" + nombre + "\",\n" +
                "  \"desarrolladora\": \"" + desarrolladora + "\",\n" +
                "  \"generos\": \"" + getStringGenero('.') + "\",\n" +
                "  \"conexionRequerida\": " + conexionRequerida + "\n" +
                "}";
    }
}
