package proyecto;

import java.util.ArrayList;

public abstract class Videojuego implements Accionar{
    int ID;
    String Nombre;
    String Desarrolladora;
    ArrayList<String> Genero= new ArrayList<>();
    boolean Disponible;// por si se implementa lo de adquirir, la disponibilidad para meterlo al carrito

    public Videojuego (int ID, String Nombre, String Desarrolladora, ArrayList<String> Genero, boolean Disponible) {
        this.ID=ID;
        this.Nombre=Nombre;
        this.Desarrolladora= Desarrolladora;
        this.Genero = Genero;
        this.Disponible= Disponible;

    }
    public int getID() {
        return this.ID;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public String getDesarrolladora() {
        return this.Desarrolladora;
    }

    public ArrayList<String> getGenero() {
        return this.Genero;
    }
    
    public boolean getDisponible(){
        return this.Disponible;
    }

    @Override
    public String listarVideojuegos (){
        String lista= " ";
        return lista;
    }
    @Override
    public String BuscarVideojuego (){
        return this.Nombre;//el metodo devolverá más que solo el nombre pero por ahora...
    }
    @Override
    public boolean AgregarAlCarrito(boolean Disponible){//agregar al carrito si está disponible
        return Disponible;
    }

    public abstract void mostrarInfo();
        //metodo abstracto obligatorio para los metodos hijos
}
