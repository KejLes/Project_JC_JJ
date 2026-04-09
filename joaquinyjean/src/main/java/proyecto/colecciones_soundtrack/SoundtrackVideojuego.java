package proyecto.colecciones_soundtrack;

public class SoundtrackVideojuego extends Soundtrack {

    private String duracion; //se escribealgo como 2:00

    public SoundtrackVideojuego(int ID, String Nombre, String Compositor, String VideojuegoAsociado, String duracion, boolean estadoDisponible) {
        // llamamos al constructor del padre
        super(ID, Nombre, Compositor, VideojuegoAsociado, estadoDisponible);
        this.duracion = duracion;
    }

    public String getDuracion() {
        return duracion;
    }

    // métodos abstractos obligatorios del padre

    @Override
    public void mostrarInfo() {
        System.out.println("");
        System.out.println("  ID: " + ID);
        System.out.println("  Nombre: " + Nombre);
        System.out.println("  Compositor: " + Compositor);
        System.out.println("  Videojuego: " + VideojuegoAsociado);
        System.out.println("  Duración: " + duracion);
        System.out.println("  Disponible: " + (estadoDisponible ? "Sí" : "No"));
        System.out.println("");
    }

    @Override
    public String obtenerFormatoDescripcion() {
        return "[" + ID + "] " + Nombre + " - " + Compositor +
               " (" + VideojuegoAsociado + ") " + duracion;
    }

    // convierte el objeto a bloque json para escribirlo
    @Override
    public String toJSON() {
        return  "    {\n" +
                "        \"id\": "                  + ID                          + ",\n" +
                "        \"nombre\": \""            + Nombre                      + "\",\n" +
                "        \"compositor\": \""        + Compositor                  + "\",\n" +
                "        \"videojuegoAsociado\": \"" + VideojuegoAsociado         + "\",\n" +
                "        \"duracion\": \""          + duracion                    + "\",\n" +
                "        \"estadoDisponible\": "    + estadoDisponible            + "\n" +
                "    }";
    }
}
