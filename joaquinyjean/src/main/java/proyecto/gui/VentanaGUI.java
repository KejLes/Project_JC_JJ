package proyecto.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import proyecto.colecciones_soundtrack.SoundtrackVideojuego;
import proyecto.colecciones_videojuego.Digital;
import proyecto.colecciones_videojuego.Fisico;
import proyecto.colecciones_videojuego.Videojuego;
import proyecto.persistencia.gestorFicherosCSV;
import proyecto.persistencia.gestorFicherosJSON;

/**
 * Ventana principal de GameVault.
 * Muestra videojuegos o soundtracks en una JTable
 * y permite anadir, borrar y buscar por ID.
 * Construida a mano, sin editor visual.
 */
public class VentanaGUI extends JFrame {

    private TreeMap<Integer, Videojuego> videojuegos;
    private List<SoundtrackVideojuego> soundtracks;
    private String rutaCSV;
    private String rutaJSON;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField campoBuscar;

    // Controla si estamos viendo videojuegos o soundtracks
    private String modoActual;

    /**
     * Crea la ventana con los datos cargados.
     * @param videojuegos mapa de videojuegos
     * @param soundtracks lista de soundtracks
     * @param rutaCSV ruta del fichero CSV
     * @param rutaJSON ruta del fichero JSON
     */
    public VentanaGUI(TreeMap<Integer, Videojuego> videojuegos,
                      List<SoundtrackVideojuego> soundtracks,
                      String rutaCSV, String rutaJSON) {
        this.videojuegos = videojuegos;
        this.soundtracks = soundtracks;
        this.rutaCSV = rutaCSV;
        this.rutaJSON = rutaJSON;
        this.modoActual = "";

        setTitle("GameVault");
        setSize(900, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        construirInterfaz();
        configurarCierre();

        setVisible(true);
    }

    /**
     * Construye todos los componentes de la interfaz.
     */
    private void construirInterfaz() {
        setLayout(new BorderLayout(5, 5));

        // --- NORTE: botones de seleccion y busqueda ---
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton btnVideojuegos = new JButton("Videojuegos");
        JButton btnSoundtracks = new JButton("Soundtracks");
        panelNorte.add(btnVideojuegos);
        panelNorte.add(btnSoundtracks);

        panelNorte.add(new JLabel("   Buscar ID:"));
        campoBuscar = new JTextField(8);
        panelNorte.add(campoBuscar);
        JButton btnBuscar = new JButton("Buscar");
        panelNorte.add(btnBuscar);

        add(panelNorte, BorderLayout.NORTH);

        // --- CENTRO: tabla ---
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- SUR: botones de accion ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnAnadir = new JButton("Anadir");
        JButton btnBorrar = new JButton("Borrar seleccionado");
        panelSur.add(btnAnadir);
        panelSur.add(btnBorrar);
        add(panelSur, BorderLayout.SOUTH);

        // === LISTENERS ===

        // Boton Videojuegos: ActionListener con lambda
        btnVideojuegos.addActionListener(e -> {
            modoActual = "videojuegos";
            cargarTablaVideojuegos(videojuegos);
        });

        // Boton Soundtracks: ActionListener con lambda
        btnSoundtracks.addActionListener(e -> {
            modoActual = "soundtracks";
            cargarTablaSoundtracks(soundtracks);
        });

        // Boton Buscar: ActionListener con clase anonima
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPorId();
            }
        });

        // Boton Anadir
        btnAnadir.addActionListener(e -> {
            if (modoActual.equals("videojuegos")) {
                dialogoAnadirVideojuego();
            } else if (modoActual.equals("soundtracks")) {
                dialogoAnadirSoundtrack();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero Videojuegos o Soundtracks.");
            }
        });

        // Boton Borrar: borra el seleccionado sin confirmacion
        btnBorrar.addActionListener(e -> borrarSeleccionado());
    }

    // =====================================================================
    //  CARGAR TABLAS
    // =====================================================================

    /**
     * Llena la tabla con los videojuegos del mapa.
     * @param datos mapa de videojuegos a mostrar
     */
    private void cargarTablaVideojuegos(TreeMap<Integer, Videojuego> datos) {
        String[] columnas = {"ID", "Tipo", "Nombre", "Desarrolladora", "Genero", "Disponible"};
        modeloTabla.setColumnIdentifiers(columnas);
        modeloTabla.setRowCount(0);

        for (Videojuego v : datos.values()) {
            String tipo = (v instanceof Fisico) ? "Fisico" : "Digital";
            modeloTabla.addRow(new Object[]{
                    v.getId(), tipo, v.getNombre(),
                    v.getDesarrolladora(), v.getStringGenero('.'),
                    v.getDisponible() ? "Si" : "No"
            });
        }
    }

    /**
     * Llena la tabla con los soundtracks de la lista.
     * @param datos lista de soundtracks a mostrar
     */
    private void cargarTablaSoundtracks(List<SoundtrackVideojuego> datos) {
        String[] columnas = {"ID", "Nombre", "Compositor", "Videojuego", "Duracion", "Disponible"};
        modeloTabla.setColumnIdentifiers(columnas);
        modeloTabla.setRowCount(0);

        for (SoundtrackVideojuego st : datos) {
            modeloTabla.addRow(new Object[]{
                    st.getID(), st.getNombre(), st.getCompositor(),
                    st.getVideojuegoAsociado(), st.getDuracion(),
                    st.getDisponible() ? "Si" : "No"
            });
        }
    }

    // =====================================================================
    //  BUSCAR POR ID
    // =====================================================================

    /**
     * Busca en la tabla actual por el ID introducido en el campo de texto.
     * Si no encuentra resultados, muestra un mensaje.
     */
    private void buscarPorId() {
        String texto = campoBuscar.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un ID para buscar.");
            return;
        }

        if (!texto.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un numero.");
            return;
        }

        int id = Integer.parseInt(texto);

        if (modoActual.equals("videojuegos")) {
            Videojuego v = videojuegos.get(id);
            if (v != null) {
                // Mostrar solo ese en la tabla
                TreeMap<Integer, Videojuego> resultado = new TreeMap<>();
                resultado.put(id, v);
                cargarTablaVideojuegos(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "No existe videojuego con ID: " + id);
            }
        } else if (modoActual.equals("soundtracks")) {
            List<SoundtrackVideojuego> resultado = new ArrayList<>();
            for (SoundtrackVideojuego st : soundtracks) {
                if (st.getID() == id) {
                    resultado.add(st);
                    break;
                }
            }
            if (!resultado.isEmpty()) {
                cargarTablaSoundtracks(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "No existe soundtrack con ID: " + id);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona primero Videojuegos o Soundtracks.");
        }
    }

    // =====================================================================
    //  BORRAR
    // =====================================================================

    /**
     * Borra el elemento seleccionado en la tabla.
     * Lee el ID de la primera columna, lo elimina del fichero
     * y recarga la tabla. Sin confirmacion.
     */
    private void borrarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para borrar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        if (modoActual.equals("videojuegos")) {
            gestorFicherosCSV.eliminarPorId(id, rutaCSV);
            videojuegos = gestorFicherosCSV.leerCSV(rutaCSV);
            cargarTablaVideojuegos(videojuegos);
        } else if (modoActual.equals("soundtracks")) {
            try {
                gestorFicherosJSON.eliminarPorId(id, rutaJSON);
                soundtracks = gestorFicherosJSON.leerJSON(rutaJSON);
                cargarTablaSoundtracks(soundtracks);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
            }
        }
    }

    // =====================================================================
    //  DIALOGOS PARA ANADIR
    // =====================================================================

    /**
     * Abre un JDialog para introducir los datos de un videojuego nuevo.
     * Pide el tipo (fisico/digital) y muestra los campos correspondientes.
     */
    private void dialogoAnadirVideojuego() {
        JDialog dialogo = new JDialog(this, "Anadir Videojuego", true);
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(0, 2, 5, 5));

        // Campos comunes
        dialogo.add(new JLabel("Nombre:"));
        JTextField txtNombre = new JTextField();
        dialogo.add(txtNombre);

        dialogo.add(new JLabel("Desarrolladora:"));
        JTextField txtDesarrolladora = new JTextField();
        dialogo.add(txtDesarrolladora);

        dialogo.add(new JLabel("Generos (con punto):"));
        JTextField txtGeneros = new JTextField();
        dialogo.add(txtGeneros);

        dialogo.add(new JLabel("Disponible:"));
        JCheckBox chkDisponible = new JCheckBox("", true);
        dialogo.add(chkDisponible);

        dialogo.add(new JLabel("Tipo:"));
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"fisico", "digital"});
        dialogo.add(comboTipo);

        // Campos de fisico
        dialogo.add(new JLabel("Estado (nuevo/usado):"));
        JTextField txtEstado = new JTextField();
        dialogo.add(txtEstado);

        dialogo.add(new JLabel("Incluye caja:"));
        JCheckBox chkCaja = new JCheckBox();
        dialogo.add(chkCaja);

        // Campo de digital
        dialogo.add(new JLabel("Requiere conexion:"));
        JCheckBox chkConexion = new JCheckBox();
        dialogo.add(chkConexion);

        // Boton aceptar
        JButton btnAceptar = new JButton("Aceptar");
        dialogo.add(new JLabel()); // espacio vacio
        dialogo.add(btnAceptar);

        btnAceptar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String desarrolladora = txtDesarrolladora.getText().trim();

            if (nombre.isEmpty() || desarrolladora.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Rellena nombre y desarrolladora.");
                return;
            }

            ArrayList<String> generos = new ArrayList<>(
                    Arrays.asList(txtGeneros.getText().trim().split("\\.")));

            int nuevoId = videojuegos.isEmpty() ? 1 : videojuegos.lastKey() + 1;
            boolean disponible = chkDisponible.isSelected();
            String tipo = (String) comboTipo.getSelectedItem();

            Videojuego nuevo;
            if (tipo.equals("fisico")) {
                String estado = txtEstado.getText().trim();
                if (estado.isEmpty()) estado = "nuevo";
                nuevo = new Fisico(nuevoId, nombre, desarrolladora, generos,
                        disponible, estado, chkCaja.isSelected());
            } else {
                nuevo = new Digital(nuevoId, nombre, desarrolladora, generos,
                        disponible, chkConexion.isSelected());
            }

            videojuegos.put(nuevoId, nuevo);
            gestorFicherosCSV.escribirCSV(videojuegos, rutaCSV);
            cargarTablaVideojuegos(videojuegos);
            dialogo.dispose();
        });

        dialogo.setVisible(true);
    }

    /**
     * Abre un JDialog para introducir los datos de un soundtrack nuevo.
     */
    private void dialogoAnadirSoundtrack() {
        JDialog dialogo = new JDialog(this, "Anadir Soundtrack", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(0, 2, 5, 5));

        dialogo.add(new JLabel("Nombre:"));
        JTextField txtNombre = new JTextField();
        dialogo.add(txtNombre);

        dialogo.add(new JLabel("Compositor:"));
        JTextField txtCompositor = new JTextField();
        dialogo.add(txtCompositor);

        dialogo.add(new JLabel("Videojuego asociado:"));
        JTextField txtVideojuego = new JTextField();
        dialogo.add(txtVideojuego);

        dialogo.add(new JLabel("Duracion (mm:ss):"));
        JTextField txtDuracion = new JTextField();
        dialogo.add(txtDuracion);

        dialogo.add(new JLabel("Disponible:"));
        JCheckBox chkDisponible = new JCheckBox("", true);
        dialogo.add(chkDisponible);

        JButton btnAceptar = new JButton("Aceptar");
        dialogo.add(new JLabel());
        dialogo.add(btnAceptar);

        btnAceptar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String compositor = txtCompositor.getText().trim();

            if (nombre.isEmpty() || compositor.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Rellena nombre y compositor.");
                return;
            }

            int nuevoId = soundtracks.stream()
                    .mapToInt(SoundtrackVideojuego::getID)
                    .max().orElse(0) + 1;

            SoundtrackVideojuego nuevo = new SoundtrackVideojuego(
                    nuevoId, nombre, compositor,
                    txtVideojuego.getText().trim(),
                    txtDuracion.getText().trim(),
                    chkDisponible.isSelected());

            soundtracks.add(nuevo);

            try {
                gestorFicherosJSON.escribirJSON(soundtracks, rutaJSON);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error al guardar: " + ex.getMessage());
            }

            cargarTablaSoundtracks(soundtracks);
            dialogo.dispose();
        });

        dialogo.setVisible(true);
    }

    // =====================================================================
    //  CIERRE DE VENTANA
    // =====================================================================

    /**
     * Configura el WindowListener para preguntar antes de cerrar.
     * Usa WindowAdapter (clase anonima).
     */
    private void configurarCierre() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int r = JOptionPane.showConfirmDialog(
                        VentanaGUI.this,
                        "Deseas salir de GameVault?",
                        "Salir",
                        JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }
}
