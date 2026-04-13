package proyecto.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import proyecto.coleccion_soundtrack.Soundtrack;
import proyecto.coleccion_videojuego.Digital;
import proyecto.coleccion_videojuego.Fisico;
import proyecto.coleccion_videojuego.Videojuego;
import proyecto.colecciones.Colecciones;

/**
 * Ventana principal de GameVault.
 * Usa {@link CardLayout} para navegar entre cuatro pantallas
 * sin abrir multiples ventanas:
 * <ol>
 *   <li>INICIO: titulo y boton "Iniciar".</li>
 *   <li>MENU: botones "Videojuegos" y "Soundtracks".</li>
 *   <li>VIDEOJUEGOS: lista con busqueda, filtros, ordenacion, anadir y destruir.</li>
 *   <li>SOUNDTRACKS: idem para soundtracks.</li>
 * </ol>
 *
 * Toda la GUI se construye a mano mediante codigo, sin generadores automaticos.
 */
public class GameVaultGUI extends JFrame {

    // -- Colecciones --
    private final Colecciones<Videojuego> colVideojuegos;
    private final Colecciones<Soundtrack> colSoundtracks;

    // -- Navegacion con CardLayout --
    private final CardLayout cardLayout;
    private final JPanel panelPrincipal;

    // -- Componentes de la pantalla de videojuegos --
    private DefaultTableModel modeloTablaVj;
    private JTable tablaVj;
    private JTextField buscadorVj;

    // -- Componentes de la pantalla de soundtracks --
    private DefaultTableModel modeloTablaSt;
    private JTable tablaSt;
    private JTextField buscadorSt;

    // -- Callback para guardar al cerrar --
    private Runnable accionGuardar;

    /**
     * Constructor de la ventana principal.
     *
     * @param colVideojuegos coleccion de videojuegos
     * @param colSoundtracks coleccion de soundtracks
     * @param accionGuardar  accion a ejecutar al cerrar (guarda los ficheros)
     */
    public GameVaultGUI(Colecciones<Videojuego> colVideojuegos,
                        Colecciones<Soundtrack> colSoundtracks,
                        Runnable accionGuardar) {
        this.colVideojuegos = colVideojuegos;
        this.colSoundtracks = colSoundtracks;
        this.accionGuardar = accionGuardar;

        // Configuracion basica de la ventana
        setTitle("GameVault");
        setSize(1000, 620);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Lo gestiona WindowListener
        setLocationRelativeTo(null);

        // CardLayout para cambiar entre pantallas
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // Construir las cuatro pantallas
        panelPrincipal.add(crearPanelInicio(), "INICIO");
        panelPrincipal.add(crearPanelMenu(), "MENU");
        panelPrincipal.add(crearPanelVideojuegos(), "VIDEOJUEGOS");
        panelPrincipal.add(crearPanelSoundtracks(), "SOUNDTRACKS");

        add(panelPrincipal);

        // WindowListener: pregunta si guardar antes de cerrar
        configurarCierre();

        // Mostrar la pantalla de inicio
        cardLayout.show(panelPrincipal, "INICIO");
        setVisible(true);
    }

    // =========================================================================
    //  PANTALLA 1: INICIO
    // =========================================================================

    /**
     * Crea el panel de bienvenida con el titulo "GameVault"
     * y un boton "Iniciar" centrado.
     *
     * @return panel de inicio
     */
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();

        // Titulo
        JLabel titulo = new JLabel("GameVault");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        titulo.setForeground(new Color(205, 214, 244));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(titulo, gbc);

        // Subtitulo
        JLabel subtitulo = new JLabel("Tu biblioteca de videojuegos y soundtracks");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(166, 173, 200));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        panel.add(subtitulo, gbc);

        // Boton Iniciar
        JButton btnIniciar = new JButton("Iniciar");
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnIniciar.setPreferredSize(new Dimension(200, 50));
        btnIniciar.setBackground(new Color(137, 180, 250));
        btnIniciar.setForeground(new Color(30, 30, 46));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ActionListener con lambda
        btnIniciar.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(btnIniciar, gbc);

        return panel;
    }

    // =========================================================================
    //  PANTALLA 2: MENU (elegir Videojuegos o Soundtracks)
    // =========================================================================

    /**
     * Crea el panel de menu con dos botones lado a lado:
     * "Videojuegos" y "Soundtracks".
     *
     * @return panel de menu
     */
    private JPanel crearPanelMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 46));
        GridBagConstraints gbc = new GridBagConstraints();

        // Titulo
        JLabel titulo = new JLabel("Elige una coleccion");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(205, 214, 244));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 50, 0);
        panel.add(titulo, gbc);

        // Boton Videojuegos
        JButton btnVj = crearBotonMenu("Videojuegos", new Color(166, 227, 161));
        btnVj.addActionListener(e -> {
            cargarTablaVideojuegos(colVideojuegos.getLista());
            cardLayout.show(panelPrincipal, "VIDEOJUEGOS");
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 20, 0, 20);
        panel.add(btnVj, gbc);

        // Boton Soundtracks
        JButton btnSt = crearBotonMenu("Soundtracks", new Color(245, 194, 231));
        btnSt.addActionListener(e -> {
            cargarTablaSoundtracks(colSoundtracks.getLista());
            cardLayout.show(panelPrincipal, "SOUNDTRACKS");
        });
        gbc.gridx = 1;
        panel.add(btnSt, gbc);

        return panel;
    }

    /**
     * Crea un boton estilizado para el menu de seleccion.
     *
     * @param texto texto del boton
     * @param color color de fondo
     * @return boton configurado
     */
    private JButton crearBotonMenu(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(220, 80));
        btn.setBackground(color);
        btn.setForeground(new Color(30, 30, 46));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =========================================================================
    //  PANTALLA 3: VIDEOJUEGOS
    // =========================================================================

    /**
     * Crea el panel completo de videojuegos con:
     * - Titulo "Videojuegos" arriba.
     * - Barra de busqueda.
     * - Tabla con scroll.
     * - Filtros y ordenacion.
     * - Botones Anadir, Destruir, Volver.
     *
     * @return panel de videojuegos
     */
    private JPanel crearPanelVideojuegos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- NORTE: titulo y barra de busqueda ---
        JPanel panelNorte = new JPanel(new BorderLayout(5, 5));

        JLabel titulo = new JLabel("Videojuegos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        panelNorte.add(titulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar: "));
        buscadorVj = new JTextField(30);
        panelBusqueda.add(buscadorVj);
        panelNorte.add(panelBusqueda, BorderLayout.CENTER);

        panel.add(panelNorte, BorderLayout.NORTH);

        // --- CENTRO: tabla de videojuegos ---
        String[] columnas = {"ID", "Tipo", "Nombre", "Desarrolladora",
                "Genero", "Disponible", "Valoracion"};
        modeloTablaVj = new DefaultTableModel(columnas, 0) {
            // La tabla no es editable directamente
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVj = new JTable(modeloTablaVj);
        tablaVj.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVj.setRowHeight(25);

        // JScrollPane: barra deslizadora para la tabla
        JScrollPane scroll = new JScrollPane(tablaVj);
        panel.add(scroll, BorderLayout.CENTER);

        // --- ESTE: filtros y ordenacion ---
        JPanel panelFiltrosOrden = new JPanel();
        panelFiltrosOrden.setLayout(new BoxLayout(panelFiltrosOrden, BoxLayout.Y_AXIS));
        panelFiltrosOrden.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        // Seccion Filtrar por:
        panelFiltrosOrden.add(new JLabel("Filtrar por:"));
        panelFiltrosOrden.add(Box.createVerticalStrut(5));

        String[] opcionesFiltro = {"-- Sin filtro --", "Disponible", "No disponible",
                "Fisico", "Digital", "Mejor valorado (5)", "Peor valorado (0)"};
        JComboBox<String> comboFiltro = new JComboBox<>(opcionesFiltro);
        comboFiltro.setMaximumSize(new Dimension(200, 30));
        panelFiltrosOrden.add(comboFiltro);

        panelFiltrosOrden.add(Box.createVerticalStrut(20));

        // Seccion Ordenar por:
        panelFiltrosOrden.add(new JLabel("Ordenar por:"));
        panelFiltrosOrden.add(Box.createVerticalStrut(5));

        String[] opcionesOrden = {"-- Sin orden --", "ID", "Nombre (A-Z)",
                "Valoracion ascendente", "Valoracion descendente"};
        JComboBox<String> comboOrden = new JComboBox<>(opcionesOrden);
        comboOrden.setMaximumSize(new Dimension(200, 30));
        panelFiltrosOrden.add(comboOrden);

        panelFiltrosOrden.add(Box.createVerticalStrut(10));

        JButton btnAplicar = new JButton("Aplicar");
        btnAplicar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFiltrosOrden.add(btnAplicar);

        panel.add(panelFiltrosOrden, BorderLayout.EAST);

        // --- SUR: botones de accion ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAnadir = new JButton("Anadir");
        JButton btnDestruir = new JButton("Destruir");
        JButton btnVolver = new JButton("Volver");

        btnDestruir.setBackground(new Color(243, 139, 168));
        btnDestruir.setForeground(Color.WHITE);

        panelSur.add(btnAnadir);
        panelSur.add(btnDestruir);
        panelSur.add(btnVolver);

        panel.add(panelSur, BorderLayout.SOUTH);

        // --- LISTENERS ---

        // Buscador: filtra al presionar Enter
        buscadorVj.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String texto = buscadorVj.getText().trim().toLowerCase();
                    if (texto.isEmpty()) {
                        cargarTablaVideojuegos(colVideojuegos.getLista());
                    } else {
                        List<Videojuego> resultados = colVideojuegos.buscar(v ->
                                String.valueOf(v.getId()).contains(texto)
                                        || v.getNombre().toLowerCase().contains(texto)
                                        || v.getDesarrolladora().toLowerCase().contains(texto)
                                        || v.getGeneroTexto().toLowerCase().contains(texto)
                        );
                        cargarTablaVideojuegos(resultados);
                    }
                }
            }
        });

        // Boton Aplicar filtro y orden
        btnAplicar.addActionListener(e -> {
            List<Videojuego> lista = aplicarFiltroVideojuegos(
                    comboFiltro.getSelectedIndex());
            lista = aplicarOrdenVideojuegos(lista, comboOrden.getSelectedIndex());
            cargarTablaVideojuegos(lista);
        });

        // Boton Anadir: abre dialogo con lambda
        btnAnadir.addActionListener(e -> {
            DialogoVideojuego dialogo = new DialogoVideojuego(this, colVideojuegos);
            dialogo.setVisible(true);
            // Al cerrar el dialogo, refrescar la tabla
            cargarTablaVideojuegos(colVideojuegos.getLista());
        });

        // Boton Destruir: usa clase anonima (requisito del proyecto)
        btnDestruir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "Si aceptas, la lista se borrara por completo,\n"
                                + "quedara vacia. Deseas continuar?",
                        "Advertencia - Destruir",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    colVideojuegos.destruir();
                    cargarTablaVideojuegos(colVideojuegos.getLista());
                }
            }
        });

        // Boton Volver
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));

        return panel;
    }

    /**
     * Aplica un filtro a la lista de videojuegos segun el indice del combo.
     *
     * @param indiceFiltro indice seleccionado en el JComboBox
     * @return lista filtrada
     */
    private List<Videojuego> aplicarFiltroVideojuegos(int indiceFiltro) {
        return switch (indiceFiltro) {
            case 1 -> colVideojuegos.filtrar(Videojuego::isEstadoDisponible);
            case 2 -> colVideojuegos.filtrar(v -> !v.isEstadoDisponible());
            case 3 -> colVideojuegos.filtrarPorTipo(Fisico.class)
                    .stream().map(f -> (Videojuego) f).collect(Collectors.toList());
            case 4 -> colVideojuegos.filtrarPorTipo(Digital.class)
                    .stream().map(d -> (Videojuego) d).collect(Collectors.toList());
            case 5 -> colVideojuegos.filtrarMejorValorado();
            case 6 -> colVideojuegos.filtrarPeorValorado();
            default -> new ArrayList<>(colVideojuegos.getLista());
        };
    }

    /**
     * Aplica un orden a la lista proporcionada segun el indice del combo.
     *
     * @param lista       lista a ordenar
     * @param indiceOrden indice seleccionado en el JComboBox
     * @return lista ordenada
     */
    private List<Videojuego> aplicarOrdenVideojuegos(List<Videojuego> lista,
                                                      int indiceOrden) {
        return switch (indiceOrden) {
            case 1 -> lista.stream()
                    .sorted(Comparator.comparingInt(Videojuego::getId))
                    .collect(Collectors.toList());
            case 2 -> lista.stream()
                    .sorted(Comparator.comparing(Videojuego::getNombre,
                            String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
            case 3 -> lista.stream()
                    .sorted(Comparator.comparingDouble(Videojuego::getValoracion))
                    .collect(Collectors.toList());
            case 4 -> lista.stream()
                    .sorted(Comparator.comparingDouble(Videojuego::getValoracion).reversed())
                    .collect(Collectors.toList());
            default -> lista;
        };
    }

    /**
     * Carga la tabla de videojuegos con la lista proporcionada.
     * Limpia las filas anteriores y rellena con los datos nuevos.
     *
     * @param lista lista de videojuegos a mostrar en la tabla
     */
    public void cargarTablaVideojuegos(List<Videojuego> lista) {
        modeloTablaVj.setRowCount(0); // Limpiar tabla
        for (Videojuego v : lista) {
            modeloTablaVj.addRow(new Object[]{
                    v.getId(),
                    v.getTipo(),
                    v.getNombre(),
                    v.getDesarrolladora(),
                    v.getGeneroTexto(),
                    v.isEstadoDisponible() ? "Si" : "No",
                    String.format(java.util.Locale.US, "%.1f", v.getValoracion())
            });
        }
    }

    // =========================================================================
    //  PANTALLA 4: SOUNDTRACKS
    // =========================================================================

    /**
     * Crea el panel completo de soundtracks, con la misma estructura
     * que el de videojuegos pero adaptado a los atributos de Soundtrack.
     *
     * @return panel de soundtracks
     */
    private JPanel crearPanelSoundtracks() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- NORTE: titulo y barra de busqueda ---
        JPanel panelNorte = new JPanel(new BorderLayout(5, 5));

        JLabel titulo = new JLabel("Soundtracks", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        panelNorte.add(titulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar: "));
        buscadorSt = new JTextField(30);
        panelBusqueda.add(buscadorSt);
        panelNorte.add(panelBusqueda, BorderLayout.CENTER);

        panel.add(panelNorte, BorderLayout.NORTH);

        // --- CENTRO: tabla de soundtracks ---
        String[] columnas = {"ID", "Nombre", "Compositor", "Videojuego",
                "Disponible", "Valoracion", "Duracion"};
        modeloTablaSt = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSt = new JTable(modeloTablaSt);
        tablaSt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSt.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaSt);
        panel.add(scroll, BorderLayout.CENTER);

        // --- ESTE: filtros y ordenacion ---
        JPanel panelFiltrosOrden = new JPanel();
        panelFiltrosOrden.setLayout(new BoxLayout(panelFiltrosOrden, BoxLayout.Y_AXIS));
        panelFiltrosOrden.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        panelFiltrosOrden.add(new JLabel("Filtrar por:"));
        panelFiltrosOrden.add(Box.createVerticalStrut(5));

        String[] opcionesFiltro = {"-- Sin filtro --", "Disponible", "No disponible",
                "Mejor valorado (5)", "Peor valorado (0)"};
        JComboBox<String> comboFiltro = new JComboBox<>(opcionesFiltro);
        comboFiltro.setMaximumSize(new Dimension(200, 30));
        panelFiltrosOrden.add(comboFiltro);

        panelFiltrosOrden.add(Box.createVerticalStrut(20));

        panelFiltrosOrden.add(new JLabel("Ordenar por:"));
        panelFiltrosOrden.add(Box.createVerticalStrut(5));

        String[] opcionesOrden = {"-- Sin orden --", "ID", "Nombre (A-Z)",
                "Valoracion ascendente", "Valoracion descendente",
                "Duracion ascendente", "Duracion descendente"};
        JComboBox<String> comboOrden = new JComboBox<>(opcionesOrden);
        comboOrden.setMaximumSize(new Dimension(200, 30));
        panelFiltrosOrden.add(comboOrden);

        panelFiltrosOrden.add(Box.createVerticalStrut(10));

        JButton btnAplicar = new JButton("Aplicar");
        btnAplicar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFiltrosOrden.add(btnAplicar);

        panel.add(panelFiltrosOrden, BorderLayout.EAST);

        // --- SUR: botones ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAnadir = new JButton("Anadir");
        JButton btnDestruir = new JButton("Destruir");
        JButton btnVolver = new JButton("Volver");

        btnDestruir.setBackground(new Color(243, 139, 168));
        btnDestruir.setForeground(Color.WHITE);

        panelSur.add(btnAnadir);
        panelSur.add(btnDestruir);
        panelSur.add(btnVolver);

        panel.add(panelSur, BorderLayout.SOUTH);

        // --- LISTENERS ---

        // Buscador: filtra al presionar Enter
        buscadorSt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String texto = buscadorSt.getText().trim().toLowerCase();
                    if (texto.isEmpty()) {
                        cargarTablaSoundtracks(colSoundtracks.getLista());
                    } else {
                        List<Soundtrack> resultados = colSoundtracks.buscar(st ->
                                String.valueOf(st.getId()).contains(texto)
                                        || st.getNombre().toLowerCase().contains(texto)
                                        || st.getCompositor().toLowerCase().contains(texto)
                                        || st.getVideojuegoAsociado().toLowerCase().contains(texto)
                        );
                        cargarTablaSoundtracks(resultados);
                    }
                }
            }
        });

        // Boton Aplicar
        btnAplicar.addActionListener(e -> {
            List<Soundtrack> lista = aplicarFiltroSoundtracks(
                    comboFiltro.getSelectedIndex());
            lista = aplicarOrdenSoundtracks(lista, comboOrden.getSelectedIndex());
            cargarTablaSoundtracks(lista);
        });

        // Boton Anadir con lambda
        btnAnadir.addActionListener(e -> {
            DialogoSoundtrack dialogo = new DialogoSoundtrack(this, colSoundtracks);
            dialogo.setVisible(true);
            cargarTablaSoundtracks(colSoundtracks.getLista());
        });

        // Boton Destruir con clase anonima
        btnDestruir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "Si aceptas, la lista se borrara por completo,\n"
                                + "quedara vacia. Deseas continuar?",
                        "Advertencia - Destruir",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    colSoundtracks.destruir();
                    cargarTablaSoundtracks(colSoundtracks.getLista());
                }
            }
        });

        // Boton Volver
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));

        return panel;
    }

    /**
     * Aplica un filtro a la lista de soundtracks.
     *
     * @param indiceFiltro indice seleccionado
     * @return lista filtrada
     */
    private List<Soundtrack> aplicarFiltroSoundtracks(int indiceFiltro) {
        return switch (indiceFiltro) {
            case 1 -> colSoundtracks.filtrar(Soundtrack::isEstadoDisponible);
            case 2 -> colSoundtracks.filtrar(st -> !st.isEstadoDisponible());
            case 3 -> colSoundtracks.filtrarMejorValorado();
            case 4 -> colSoundtracks.filtrarPeorValorado();
            default -> new ArrayList<>(colSoundtracks.getLista());
        };
    }

    /**
     * Aplica un orden a la lista de soundtracks.
     *
     * @param lista       lista a ordenar
     * @param indiceOrden indice seleccionado
     * @return lista ordenada
     */
    private List<Soundtrack> aplicarOrdenSoundtracks(List<Soundtrack> lista,
                                                      int indiceOrden) {
        return switch (indiceOrden) {
            case 1 -> lista.stream()
                    .sorted(Comparator.comparingInt(Soundtrack::getId))
                    .collect(Collectors.toList());
            case 2 -> lista.stream()
                    .sorted(Comparator.comparing(Soundtrack::getNombre,
                            String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
            case 3 -> lista.stream()
                    .sorted(Comparator.comparingDouble(Soundtrack::getValoracion))
                    .collect(Collectors.toList());
            case 4 -> lista.stream()
                    .sorted(Comparator.comparingDouble(Soundtrack::getValoracion).reversed())
                    .collect(Collectors.toList());
            case 5 -> lista.stream()
                    .sorted(Comparator.comparingInt(Soundtrack::getDuracion))
                    .collect(Collectors.toList());
            case 6 -> lista.stream()
                    .sorted(Comparator.comparingInt(Soundtrack::getDuracion).reversed())
                    .collect(Collectors.toList());
            default -> lista;
        };
    }

    /**
     * Carga la tabla de soundtracks con la lista proporcionada.
     *
     * @param lista lista de soundtracks a mostrar
     */
    public void cargarTablaSoundtracks(List<Soundtrack> lista) {
        modeloTablaSt.setRowCount(0);
        for (Soundtrack st : lista) {
            modeloTablaSt.addRow(new Object[]{
                    st.getId(),
                    st.getNombre(),
                    st.getCompositor(),
                    st.getVideojuegoAsociado(),
                    st.isEstadoDisponible() ? "Si" : "No",
                    String.format(java.util.Locale.US, "%.1f", st.getValoracion()),
                    st.duracionEnFormato()
            });
        }
    }

    // =========================================================================
    //  CIERRE DE VENTANA
    // =========================================================================

    /**
     * Configura el WindowListener para guardar los datos al cerrar.
     * Usa WindowAdapter (clase anonima que extiende WindowAdapter)
     * para gestionar el evento windowClosing.
     */
    private void configurarCierre() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        GameVaultGUI.this,
                        "Deseas guardar los cambios antes de salir?",
                        "Guardar y salir",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (respuesta == JOptionPane.YES_OPTION) {
                    if (accionGuardar != null) {
                        accionGuardar.run();
                    }
                    dispose();
                } else if (respuesta == JOptionPane.NO_OPTION) {
                    dispose();
                }
                // Si es CANCEL, no hace nada (la ventana sigue abierta)
            }
        });
    }
}
