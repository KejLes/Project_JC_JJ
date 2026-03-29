package proyecto;

import java.util.ArrayList;

/**
 * Clase que representa un videojuego físico. Hereda de la clase abstracta
 * Videojuego e implementa el método mostrarInfo() para mostrar información
 * específica de los videojuegos físicos, como su estado y si viene en caja.
 * Contiene un atributo String estado para indicar si el juego es nuevo o usado,
 * y un atributo boolean caja para indicar si el juego viene en caja o solo posee el disco/cartucho.
 */
public class Fisico extends Videojuego{
    String  estado;//el estado del juego, si es nuevo o usado
    boolean caja; //el juego viene en caja o sólo posee el disco/cartucho

    public Fisico (String ID, String Nombre, String Desarrolladora, ArrayList<String> Genero, boolean estadoDisponible, String estado, boolean caja){
        super(ID, Nombre, Desarrolladora, Genero, estadoDisponible);
        this.estado = estado;
        this.caja = caja;
    }

    /**
     * El metodo abstracto que hereda de videojuego
     */
    @Override
    public void mostrarInfo(){
        System.out.printf("""
        Información del videojuego físico:
        - ID: %d
        - Nombre: %s
        - Desarrolladora: %s
        - Género: %s
        %s\n
                """, ID, Nombre, Desarrolladora, Genero, obtenerFormatoDescripcion());
    }

    /**
     * El metodo abstracto que devuelve información de los campos de Fisico
     */
    @Override
    public String obtenerFormatoDescripcion() {
        return ("- Estado: " + estado + "\n- Viene en caja: " + caja);
    }
}
