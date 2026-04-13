'''mermaid
classDiagram
    direction TB

    %% ==================== INTERFACES ====================
    class Valorable {
        <<interface>>
        +valorar(nota : double) void
        +getValoracion() double
    }

    class Exportable {
        <<interface>>
        +toJson() String
        +toCsv() String
    }

    %% ==================== MODELO - VIDEOJUEGOS ====================
    class Videojuego {
        <<abstract>>
        -id : int
        -nombre : String
        -desarrolladora : String
        -genero : ArrayList~String~
        -estadoDisponible : boolean
        -valoracion : double
        +Videojuego(id, nombre, desarrolladora, genero, estadoDisponible)
        +valorar(nota : double) void
        +getValoracion() double
        +setValoracion(valoracion : double) void
        +mostrarInformacion()* void
        +obtenerFormatoDescripcion()* String
        +getTipo()* String
        +getId() int
        +getNombre() String
        +getDesarrolladora() String
        +getGenero() ArrayList~String~
        +getGeneroTexto() String
        +isEstadoDisponible() boolean
        #generosParaCsv() String
        +toString() String
    }

    class Fisico {
        -estado : String
        -caja : boolean
        +Fisico(id, nombre, desarrolladora, genero, estadoDisponible, estado, caja)
        +mostrarInformacion() void
        +obtenerFormatoDescripcion() String
        +getTipo() String
        +toJson() String
        +toCsv() String
        +getEstado() String
        +getCaja() boolean
    }

    class Digital {
        -conexionRequerida : boolean
        +Digital(id, nombre, desarrolladora, genero, estadoDisponible, conexionRequerida)
        +mostrarInformacion() void
        +obtenerFormatoDescripcion() String
        +getTipo() String
        +toJson() String
        +toCsv() String
        +getConexionRequerida() boolean
    }

    %% ==================== MODELO - SOUNDTRACK ====================
    class Soundtrack {
        -id : int
        -nombre : String
        -compositor : String
        -videojuegoAsociado : String
        -estadoDisponible : boolean
        -valoracion : double
        -duracion : int
        +Soundtrack(id, nombre, compositor, videojuegoAsociado, estadoDisponible, duracion)
        +duracionEnFormato() String
        +valorar(nota : double) void
        +getValoracion() double
        +setValoracion(valoracion : double) void
        +toJson() String
        +toCsv() String
        +mostrarInformacion() void
        +obtenerFormatoDescripcion() String
        +getId() int
        +getNombre() String
        +getCompositor() String
        +getVideojuegoAsociado() String
        +isEstadoDisponible() boolean
        +getDuracion() int
        +toString() String
    }

    %% ==================== COLECCIONES ====================
    class Colecciones~T extends Valorable~ {
        -lista : List~T~
        -indiceSecundario : Map~String, List~T~~
        -extractorClave : Function~T, String~
        +Colecciones(extractorClave : Function~T, String~)
        +anadir(elemento : T) void
        +cargarTodos(elementos : List~T~) void
        +eliminar(elemento : T) boolean
        +destruir() void
        +buscarPorIndice(clave : String) List~T~
        +buscar(filtro : Predicate~T~) List~T~
        +filtrar(filtro : Predicate~T~) List~T~
        +filtrarMejorValorado() List~T~
        +filtrarPeorValorado() List~T~
        +filtrarPorTipo(tipo : Class~S~) List~S~
        +ordenar(comparador : Comparator~T~) List~T~
        +ordenarPorValoracionAsc() List~T~
        +ordenarPorValoracionDesc() List~T~
        +toJson(conversor : Function~T, String~) String
        +toCsv(conversor : Function~T, String~) String
        +getLista() List~T~
        +getIndiceSecundario() Map~String, List~T~~
        +tamano() int
        +estaVacia() boolean
    }

    %% ==================== PERSISTENCIA ====================
    class GestorCSV {
        -ruta : String
        -indicePosiciones : Map~String, Long~
        +GestorCSV(ruta : String)
        +cargar() List~Videojuego~
        +guardar(videojuegos : List~Videojuego~) void
        +buscarPorId(id : String) Videojuego
        -reconstruirIndice() void
        -parsear(linea : String) Videojuego
        +getIndicePosiciones() Map~String, Long~
    }

    class GestorJSON {
        -ruta : String
        -indicePosiciones : Map~String, Long~
        +GestorJSON(ruta : String)
        +cargar() List~Soundtrack~
        +guardar(soundtracks : List~Soundtrack~) void
        +buscarPorId(id : String) Soundtrack
        -construirIndice() void
        -extraerObjetos(json : String) List~String~
        -parsearObjeto(objetoJson : String) Soundtrack
        -extraerValor(json : String, clave : String) String
        -extraerValorTexto(json : String, clave : String) String
        +getIndicePosiciones() Map~String, Long~
    }

    %% ==================== CONSOLA ====================
    class Consola {
        -colVideojuegos : Colecciones~Videojuego~
        -colSoundtracks : Colecciones~Soundtrack~
        -sc : Scanner
        +Consola(colVideojuegos, colSoundtracks)
        +iniciar() void
        -mostrarMenuPrincipal() void
        -menuVideojuegos() void
        -menuSoundtracks() void
        -mostrarTablaVideojuegos(lista : List~Videojuego~) void
        -mostrarTablaSoundtracks(lista : List~Soundtrack~) void
        -buscarVideojuegos() void
        -buscarSoundtracks() void
        -filtrarVideojuegos() void
        -filtrarSoundtracks() void
        -ordenarVideojuegos() void
        -ordenarSoundtracks() void
        -anadirVideojuego() void
        -anadirSoundtrack() void
        -destruirVideojuegos() void
        -destruirSoundtracks() void
        -leerEntero(prompt : String, min : int, max : int) int
        -leerDouble(prompt : String, min : double, max : double) double
        -leerTexto(prompt : String) String
        -leerBoolean(prompt : String) boolean
        -truncar(texto : String, maxLen : int) String
    }

    %% ==================== GUI ====================
    class GameVaultGUI {
        -colVideojuegos : Colecciones~Videojuego~
        -colSoundtracks : Colecciones~Soundtrack~
        -cardLayout : CardLayout
        -panelPrincipal : JPanel
        -modeloTablaVj : DefaultTableModel
        -tablaVj : JTable
        -buscadorVj : JTextField
        -modeloTablaSt : DefaultTableModel
        -tablaSt : JTable
        -buscadorSt : JTextField
        -accionGuardar : Runnable
        +GameVaultGUI(colVideojuegos, colSoundtracks, accionGuardar)
        -crearPanelInicio() JPanel
        -crearPanelMenu() JPanel
        -crearPanelVideojuegos() JPanel
        -crearPanelSoundtracks() JPanel
        -crearBotonMenu(texto : String, color : Color) JButton
        -aplicarFiltroVideojuegos(indiceFiltro : int) List~Videojuego~
        -aplicarOrdenVideojuegos(lista : List~Videojuego~, indiceOrden : int) List~Videojuego~
        -aplicarFiltroSoundtracks(indiceFiltro : int) List~Soundtrack~
        -aplicarOrdenSoundtracks(lista : List~Soundtrack~, indiceOrden : int) List~Soundtrack~
        +cargarTablaVideojuegos(lista : List~Videojuego~) void
        +cargarTablaSoundtracks(lista : List~Soundtrack~) void
        -configurarCierre() void
    }

    class DialogoVideojuego {
        -coleccion : Colecciones~Videojuego~
        -txtNombre : JTextField
        -txtDesarrolladora : JTextField
        -txtGeneros : JTextField
        -chkDisponible : JCheckBox
        -spnValoracion : JSpinner
        -rbFisico : JRadioButton
        -rbDigital : JRadioButton
        -panelTipoEspecifico : JPanel
        -cardTipo : CardLayout
        -txtEstado : JTextField
        -chkCaja : JCheckBox
        -chkConexion : JCheckBox
        +DialogoVideojuego(padre : JFrame, coleccion : Colecciones~Videojuego~)
        -initComponentes() void
        -validarCampos() boolean
        -limpiarCampos() void
    }

    class DialogoSoundtrack {
        -coleccion : Colecciones~Soundtrack~
        -txtNombre : JTextField
        -txtCompositor : JTextField
        -txtVideojuego : JTextField
        -chkDisponible : JCheckBox
        -spnValoracion : JSpinner
        -txtDuracion : JTextField
        -lblDuracionFormato : JLabel
        +DialogoSoundtrack(padre : JFrame, coleccion : Colecciones~Soundtrack~)
        -initComponentes() void
        -actualizarVistaPrevia() void
        -validarCampos() boolean
        -limpiarCampos() void
    }

    %% ==================== MAIN ====================
    class Main {
        -RUTA_CSV : String$
        -RUTA_JSON : String$
        +main(args : String[])$ void
    }

    %% ==================== RELACIONES ====================

    %% Herencia
    Fisico --|> Videojuego : extends
    Digital --|> Videojuego : extends
    GameVaultGUI --|> JFrame : extends
    DialogoVideojuego --|> JDialog : extends
    DialogoSoundtrack --|> JDialog : extends

    %% Implementacion de interfaces
    Videojuego ..|> Valorable : implements
    Videojuego ..|> Exportable : implements
    Soundtrack ..|> Valorable : implements
    Soundtrack ..|> Exportable : implements

    %% Composicion y uso
    Colecciones o-- "0..*" Valorable : lista (ArrayList)
    Colecciones o-- "0..*" Valorable : indiceSecundario (HashMap)

    GestorCSV ..> Videojuego : crea
    GestorCSV ..> Fisico : crea
    GestorCSV ..> Digital : crea
    GestorJSON ..> Soundtrack : crea

    GameVaultGUI --> Colecciones : usa
    GameVaultGUI ..> DialogoVideojuego : crea
    GameVaultGUI ..> DialogoSoundtrack : crea
    DialogoVideojuego --> Colecciones : usa
    DialogoSoundtrack --> Colecciones : usa

    Consola --> Colecciones : usa

    Main ..> GestorCSV : crea
    Main ..> GestorJSON : crea
    Main ..> Colecciones : crea
    Main ..> GameVaultGUI : crea
    Main ..> Consola : crea

'''
