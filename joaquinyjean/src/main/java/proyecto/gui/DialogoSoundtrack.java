package proyecto.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import proyecto.coleccion_soundtrack.Soundtrack;
import proyecto.colecciones.Colecciones;

/**
 * Dialogo modal para anadir un nuevo soundtrack a la coleccion.
 * Al introducir la duracion en segundos, se muestra en tiempo real
 * la conversion al formato mm:ss al lado del campo.
 *
 * Contiene tres botones:
 * <ul>
 *   <li>Anadir: solo funciona si los campos obligatorios estan rellenos.</li>
 *   <li>Limpiar: vacia todos los campos.</li>
 *   <li>Volver: cierra el dialogo sin aplicar cambios.</li>
 * </ul>
 */
public class DialogoSoundtrack extends JDialog {

    private final Colecciones<Soundtrack> coleccion;

    // Campos del formulario
    private JTextField txtNombre;
    private JTextField txtCompositor;
    private JTextField txtVideojuego;
    private JCheckBox chkDisponible;
    private JSpinner spnValoracion;
    private JTextField txtDuracion;
    private JLabel lblDuracionFormato; // Muestra la conversion mm:ss

    /**
     * Constructor del dialogo.
     *
     * @param padre     ventana padre
     * @param coleccion coleccion de soundtracks donde se anadira el nuevo elemento
     */
    public DialogoSoundtrack(JFrame padre, Colecciones<Soundtrack> coleccion) {
        super(padre, "Anadir Soundtrack", true);
        this.coleccion = coleccion;

        setSize(480, 400);
        setLocationRelativeTo(padre);
        setResizable(false);

        initComponentes();
    }

    /**
     * Construye todos los componentes del dialogo a mano.
     */
    private void initComponentes() {
        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        // ID (auto-generado)
        int nuevoId = coleccion.getLista().stream()
                .mapToInt(st -> ((Soundtrack) st).getId())
                .max().orElse(0) + 1;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        JLabel lblId = new JLabel(String.valueOf(nuevoId));
        lblId.setFont(new Font("SansSerif", Font.BOLD, 14));
        contenido.add(lblId, gbc);

        // Nombre
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Nombre: *"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(25);
        contenido.add(txtNombre, gbc);

        // Compositor
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Compositor: *"), gbc);
        gbc.gridx = 1;
        txtCompositor = new JTextField(25);
        contenido.add(txtCompositor, gbc);

        // Videojuego asociado
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Videojuego: *"), gbc);
        gbc.gridx = 1;
        txtVideojuego = new JTextField(25);
        contenido.add(txtVideojuego, gbc);

        // Disponible
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Disponible:"), gbc);
        gbc.gridx = 1;
        chkDisponible = new JCheckBox("", true);
        contenido.add(chkDisponible, gbc);

        // Valoracion con JSpinner
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Valoracion (0-5):"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(0.0, 0.0, 5.0, 0.1);
        spnValoracion = new JSpinner(modeloSpinner);
        contenido.add(spnValoracion, gbc);

        // Duracion en segundos + vista previa mm:ss
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Duracion (seg): *"), gbc);
        gbc.gridx = 1;
        JPanel panelDuracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtDuracion = new JTextField(8);
        lblDuracionFormato = new JLabel("= 00:00");
        lblDuracionFormato.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblDuracionFormato.setForeground(new Color(100, 100, 100));
        panelDuracion.add(txtDuracion);
        panelDuracion.add(lblDuracionFormato);
        contenido.add(panelDuracion, gbc);

        // Actualizar vista previa mm:ss al escribir
        txtDuracion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarVistaPrevia();
            }
        });

        // --- BOTONES ---
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnAnadir = new JButton("Anadir");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnAnadir);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        contenido.add(panelBotones, gbc);

        // --- LISTENERS ---

        // Boton Anadir: valida campos antes de crear
        btnAnadir.addActionListener(e -> {
            if (!validarCampos()) {
                JOptionPane.showMessageDialog(this,
                        "Rellena todos los campos obligatorios (*)\n"
                                + "La duracion debe ser un numero positivo.",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String nombre = txtNombre.getText().trim();
                String compositor = txtCompositor.getText().trim();
                String videojuego = txtVideojuego.getText().trim();
                boolean disponible = chkDisponible.isSelected();
                double valoracion = (double) spnValoracion.getValue();
                int duracion = Integer.parseInt(txtDuracion.getText().trim());

                Soundtrack st = new Soundtrack(nuevoId, nombre, compositor,
                        videojuego, disponible, duracion);
                st.setValoracion(valoracion);
                coleccion.anadir(st);

                JOptionPane.showMessageDialog(this,
                        "Soundtrack anadido con ID " + nuevoId
                                + "\nDuracion: " + st.duracionEnFormato(),
                        "Anadido", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "La duracion debe ser un numero entero en segundos.",
                        "Error de formato",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error de validacion",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Boton Limpiar
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Boton Volver
        btnVolver.addActionListener(e -> dispose());

        setContentPane(contenido);
    }

    /**
     * Actualiza la etiqueta de vista previa con el formato mm:ss
     * basandose en el valor actual del campo de duracion.
     * Si el valor no es un numero valido, muestra "= --:--".
     */
    private void actualizarVistaPrevia() {
        try {
            int segundos = Integer.parseInt(txtDuracion.getText().trim());
            if (segundos >= 0) {
                int min = segundos / 60;
                int seg = segundos % 60;
                lblDuracionFormato.setText(String.format("= %02d:%02d", min, seg));
            } else {
                lblDuracionFormato.setText("= --:--");
            }
        } catch (NumberFormatException e) {
            lblDuracionFormato.setText("= --:--");
        }
    }

    /**
     * Valida que los campos obligatorios esten rellenos
     * y que la duracion sea un numero positivo.
     *
     * @return true si la validacion pasa
     */
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) return false;
        if (txtCompositor.getText().trim().isEmpty()) return false;
        if (txtVideojuego.getText().trim().isEmpty()) return false;
        try {
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            if (duracion <= 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtCompositor.setText("");
        txtVideojuego.setText("");
        chkDisponible.setSelected(true);
        spnValoracion.setValue(0.0);
        txtDuracion.setText("");
        lblDuracionFormato.setText("= 00:00");
    }
}
