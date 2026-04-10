package proyecto.colecciones_soundtrack;

public class SoundtrackVideojuego extends Soundtrack {

    private String duracion; // Ej: "2:36"

    public SoundtrackVideojuego(int ID, String Nombre, String Compositor,
                                 String VideojuegoAsociado, String duracion,
                                 boolean estadoDisponible) {
        // Llamamos al constructor del padre (Soundtrack)
        super(ID, Nombre, Compositor, VideojuegoAsociado, estadoDisponible);
        this.duracion = duracion;
    }

    public String getDuracion() {
        return duracion;
    }

    // ── Métodos abstractos obligatorios del padre ─────────────────────────

    @Override
    public void mostrarInfo() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("  ID:          " + ID);
        System.out.println("  Nombre:      " + Nombre);
        System.out.println("  Compositor:  " + Compositor);
        System.out.println("  Videojuego:  " + VideojuegoAsociado);
        System.out.println("  Duración:    " + duracion);
        System.out.println("  Disponible:  " + (estadoDisponible ? "Sí" : "No"));
        System.out.println("└─────────────────────────────────────────┘");
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
