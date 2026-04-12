package proyecto.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.*;

import proyecto.coleccion_videojuego.Digital;
import proyecto.coleccion_videojuego.Fisico;
import proyecto.coleccion_videojuego.Videojuego;
import proyecto.colecciones.Colecciones;

/**
 * Dialogo modal para anadir un nuevo videojuego a la coleccion.
 * Permite elegir entre Fisico y Digital, mostrando campos especificos
 * segun la seleccion:
 * <ul>
 *   <li>Fisico: campo "Estado" y checkbox "Incluye caja".</li>
 *   <li>Digital: checkbox "Requiere conexion a internet".</li>
 * </ul>
 *
 * Contiene tres botones:
 * <ul>
 *   <li>Anadir: solo funciona si los campos obligatorios estan rellenos.</li>
 *   <li>Limpiar: vacia todos los campos.</li>
 *   <li>Volver: cierra el dialogo sin aplicar cambios.</li>
 * </ul>
 */
public class DialogoVideojuego extends JDialog {

    private final Colecciones<Videojuego> coleccion;

    // Campos comunes
    private JTextField txtNombre;
    private JTextField txtDesarrolladora;
    private JTextField txtGeneros;
    private JCheckBox chkDisponible;
    private JSpinner spnValoracion;

    // Selector de tipo
    private JRadioButton rbFisico;
    private JRadioButton rbDigital;

    // Panel que cambia segun el tipo seleccionado
    private JPanel panelTipoEspecifico;
    private CardLayout cardTipo;

    // Campos de Fisico
    private JTextField txtEstado;
    private JCheckBox chkCaja;

    // Campos de Digital
    private JCheckBox chkConexion;

    /**
     * Constructor del dialogo.
     *
     * @param padre     ventana padre (la ventana principal)
     * @param coleccion coleccion de videojuegos donde se anadira el nuevo elemento
     */
    public DialogoVideojuego(JFrame padre, Colecciones<Videojuego> coleccion) {
        super(padre, "Anadir Videojuego", true); // true = modal
        this.coleccion = coleccion;

        setSize(480, 450);
        setLocationRelativeTo(padre);
        setResizable(false);

        initComponentes();
    }

    /**
     * Construye todos los componentes del dialogo.
     * Se construye a mano, sin generadores automaticos del IDE.
     */
    private void initComponentes() {
        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        // ID (auto-generado, solo informativo)
        int nuevoId = coleccion.getLista().stream()
                .mapToInt(v -> ((Videojuego) v).getId())
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

        // Desarrolladora
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Desarrolladora: *"), gbc);
        gbc.gridx = 1;
        txtDesarrolladora = new JTextField(25);
        contenido.add(txtDesarrolladora, gbc);

        // Generos
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Generos (coma): *"), gbc);
        gbc.gridx = 1;
        txtGeneros = new JTextField(25);
        contenido.add(txtGeneros, gbc);

        // Disponible
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Disponible:"), gbc);
        gbc.gridx = 1;
        chkDisponible = new JCheckBox("", true);
        contenido.add(chkDisponible, gbc);

        // Valoracion con JSpinner (0.0 a 5.0, paso 0.1)
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Valoracion (0-5):"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(0.0, 0.0, 5.0, 0.1);
        spnValoracion = new JSpinner(modeloSpinner);
        contenido.add(spnValoracion, gbc);

        // Selector de tipo: Fisico o Digital
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        contenido.add(new JLabel("Tipo: *"), gbc);
        gbc.gridx = 1;
        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rbFisico = new JRadioButton("Fisico", true);
        rbDigital = new JRadioButton("Digital");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbFisico);
        grupoTipo.add(rbDigital);
        panelRadio.add(rbFisico);
        panelRadio.add(rbDigital);
        contenido.add(panelRadio, gbc);

        // Panel de campos especificos (cambia con CardLayout)
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 2;
        cardTipo = new CardLayout();
        panelTipoEspecifico = new JPanel(cardTipo);

        // -- Panel Fisico --
        JPanel panelFisico = new JPanel(new GridBagLayout());
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.insets = new Insets(4, 4, 4, 4);
        gbcF.fill = GridBagConstraints.HORIZONTAL;

        gbcF.gridx = 0; gbcF.gridy = 0;
        panelFisico.add(new JLabel("Estado: *"), gbcF);
        gbcF.gridx = 1;
        txtEstado = new JTextField(15);
        panelFisico.add(txtEstado, gbcF);

        gbcF.gridx = 0; gbcF.gridy = 1;
        panelFisico.add(new JLabel("Incluye caja:"), gbcF);
        gbcF.gridx = 1;
        chkCaja = new JCheckBox("", false);
        panelFisico.add(chkCaja, gbcF);

        panelTipoEspecifico.add(panelFisico, "FISICO");

        // -- Panel Digital --
        JPanel panelDigital = new JPanel(new GridBagLayout());
        GridBagConstraints gbcD = new GridBagConstraints();
        gbcD.insets = new Insets(4, 4, 4, 4);
        gbcD.fill = GridBagConstraints.HORIZONTAL;

        gbcD.gridx = 0; gbcD.gridy = 0;
        panelDigital.add(new JLabel("Requiere conexion:"), gbcD);
        gbcD.gridx = 1;
        chkConexion = new JCheckBox("", false);
        panelDigital.add(chkConexion, gbcD);

        panelTipoEspecifico.add(panelDigital, "DIGITAL");

        contenido.add(panelTipoEspecifico, gbc);
        gbc.gridwidth = 1;

        // Cambiar panel al seleccionar tipo
        rbFisico.addActionListener(e -> cardTipo.show(panelTipoEspecifico, "FISICO"));
        rbDigital.addActionListener(e -> cardTipo.show(panelTipoEspecifico, "DIGITAL"));

        // --- BOTONES ---
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAnadir = new JButton("Anadir");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnAnadir);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        contenido.add(panelBotones, gbc);

        // --- LISTENERS DE BOTONES ---

        // Boton Anadir: valida campos obligatorios antes de crear
        btnAnadir.addActionListener(e -> {
            if (!validarCampos()) {
                JOptionPane.showMessageDialog(this,
                        "Rellena todos los campos obligatorios (*)",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String nombre = txtNombre.getText().trim();
                String desarrolladora = txtDesarrolladora.getText().trim();
                ArrayList<String> generos = Arrays.stream(
                                txtGeneros.getText().trim().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toCollection(ArrayList::new));
                boolean disponible = chkDisponible.isSelected();
                double valoracion = (double) spnValoracion.getValue();

                Videojuego v;
                if (rbFisico.isSelected()) {
                    String estado = txtEstado.getText().trim();
                    boolean caja = chkCaja.isSelected();
                    v = new Fisico(nuevoId, nombre, desarrolladora, generos,
                            disponible, estado, caja);
                } else {
                    boolean conexion = chkConexion.isSelected();
                    v = new Digital(nuevoId, nombre, desarrolladora, generos,
                            disponible, conexion);
                }
                v.setValoracion(valoracion);
                coleccion.anadir(v);

                JOptionPane.showMessageDialog(this,
                        "Videojuego anadido con ID " + nuevoId,
                        "Anadido", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error de validacion",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Boton Limpiar: resetea todos los campos
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Boton Volver: cierra sin hacer nada
        btnVolver.addActionListener(e -> dispose());

        setContentPane(contenido);
    }

    /**
     * Valida que los campos obligatorios esten rellenos.
     * Si el tipo es Fisico, el campo "Estado" tambien es obligatorio.
     *
     * @return true si todos los campos obligatorios tienen contenido
     */
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) return false;
        if (txtDesarrolladora.getText().trim().isEmpty()) return false;
        if (txtGeneros.getText().trim().isEmpty()) return false;
        if (rbFisico.isSelected() && txtEstado.getText().trim().isEmpty()) return false;
        return true;
    }

    /**
     * Limpia todos los campos del formulario, devolviendolos
     * a su estado inicial.
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtDesarrolladora.setText("");
        txtGeneros.setText("");
        chkDisponible.setSelected(true);
        spnValoracion.setValue(0.0);
        rbFisico.setSelected(true);
        cardTipo.show(panelTipoEspecifico, "FISICO");
        txtEstado.setText("");
        chkCaja.setSelected(false);
        chkConexion.setSelected(false);
    }
}
