# GameVault

Sistema de gestion de una biblioteca de videojuegos y soundtracks desarrollado en Java con interfaz grafica Swing y menu de consola.

Proyecto academico de Programacion DAW 1.o que cubre POO avanzada (RA7), tipos avanzados (RA6) y E/S con GUI (RA5).

---

## Descripcion del dominio

GameVault gestiona dos colecciones independientes:

- **Videojuegos**: pueden ser de tipo **Fisico** (con estado del disco y si incluye caja) o **Digital** (con indicador de conexion a internet requerida). La diferencia entre ambos justifica la herencia mediante subclases.
- **Soundtracks**: bandas sonoras asociadas a videojuegos, con compositor, duracion (almacenada en segundos, mostrada en formato mm:ss) y valoracion.

Ambas colecciones permiten anadir, buscar, filtrar, ordenar y destruir elementos. Los videojuegos se persisten en CSV y los soundtracks en JSON.

---

## Estructura del proyecto

```
proyecto/
├── Main.java                             Punto de entrada (GUI por defecto, consola con argumento)
│
├── interfaces/
│   ├── Exportable.java                   Interface con toJson() y toCsv()
│   └── Valorable.java                   Interface con valorar() y getValoracion()
│
├── coleccion_videojuego/
│   ├── Videojuego.java                   Clase abstracta - superclase con herencia
│   ├── Fisico.java                       Subclase - videojuego en formato fisico
│   └── Digital.java                      Subclase - videojuego en formato digital
│
├── coleccion_soundtrack/
│   └── Soundtrack.java                   Clase concreta con Valorable y Exportable
│
├── colecciones/
│   └── Colecciones.java                  Generica <T extends Valorable> con ArrayList + HashMap
│
├── persistencia/
│   ├── GestorCSV.java                    BufferedReader + FileWriter + RandomAccessFile para CSV
│   └── GestorJSON.java                   Los mismos tres mecanismos para JSON (parseo manual)
│
├── consola/
│   └── Consola.java                      Menu interactivo con printf, regex y validacion
│
├── gui/
│   ├── GameVaultGUI.java                 Ventana principal con CardLayout (4 pantallas)
│   ├── DialogoVideojuego.java            Dialogo modal para anadir videojuegos (Fisico/Digital)
│   └── DialogoSoundtrack.java            Dialogo modal para anadir soundtracks (vista previa mm:ss)
│
└── resources/
    ├── videojuegos.csv                   20 videojuegos de ejemplo
    └── soundtracks.json                  20 soundtracks de ejemplo
```

---

## Conceptos aplicados

### Hito 1 - Modelo de clases (RA7)

- **Clase abstracta**: `Videojuego` no se puede instanciar directamente; obliga a usar `Fisico` o `Digital`.
- **Herencia**: `Fisico` y `Digital` extienden `Videojuego`, heredando atributos comunes y anadiendo los propios.
- **Interfaces**: `Valorable` y `Exportable` definen capacidades transversales. Cualquier clase futura podria implementarlas sin heredar de `Videojuego`.
- **Metodos abstractos**: `mostrarInformacion()`, `obtenerFormatoDescripcion()` y `getTipo()` se implementan de forma distinta en cada subclase.
- **Encapsulamiento**: todos los campos son `private` con getters publicos.
- **Validacion en constructores**: `IllegalArgumentException` si los datos son invalidos.

### Hito 2 - Colecciones y streams (RA6)

- **ArrayList**: coleccion principal que mantiene el orden de insercion.
- **HashMap**: indice secundario para busquedas rapidas por clave (desarrolladora en videojuegos, compositor en soundtracks).
- **Clase generica**: `Colecciones<T extends Valorable>` funciona con cualquier tipo valorable. Recibe un `Function<T, String>` como extractor de clave para el HashMap.
- **Metodo generico con bounded type**: `filtrarPorTipo(Class<S> tipo)` filtra por clase concreta (Fisico o Digital).
- **Streams**: ordenacion, filtrado y busqueda con `stream()`, `filter()`, `sorted()`, `map()` y `collect()`.
- **Iterator explicito**: el metodo `destruir()` recorre el ArrayList con un Iterator y elimina elemento a elemento, luego limpia el HashMap.
- **Predicate y Comparator**: busquedas y ordenaciones parametrizables.

### Hito 2 - Ficheros (RA5)

| Clase             | Metodo         | Mecanismo de E/S     | Justificacion                                      |
|-------------------|----------------|----------------------|-----------------------------------------------------|
| GestorCSV         | cargar()       | BufferedReader       | Lectura secuencial eficiente de todas las lineas    |
| GestorCSV         | guardar()      | FileWriter           | Escritura completa del fichero al guardar           |
| GestorCSV         | buscarPorId()  | RandomAccessFile     | Salto directo a la posicion del registro por byte   |
| GestorJSON        | cargar()       | BufferedReader       | Lectura secuencial del JSON completo                |
| GestorJSON        | guardar()      | FileWriter           | Escritura completa del fichero JSON                 |
| GestorJSON        | buscarPorId()  | RandomAccessFile     | Acceso directo sin releer todo el fichero           |

- **Parseo manual**: tanto CSV como JSON se parsean sin librerias externas (sin Gson, sin Jackson).
- **try-with-resources**: todos los accesos a fichero garantizan el cierre del recurso.
- **UTF-8 explicito**: evita problemas de encoding en Windows.
- **Eliminacion de BOM**: la primera linea se limpia del caracter `\uFEFF` que anade el Bloc de notas de Windows.
- **Locale.US**: fuerza el punto decimal en `String.format` para evitar que el locale espanol use coma.

### Hito 2 - Menu de consola

- **printf con especificadores de anchura**: `%-30s`, `%6.1f` para tablas alineadas.
- **Validacion con regex**: `matches("[a-zA-Z0-9\\u00C0-\\u00FF].*")` para texto no vacio.
- **Bucles do-while**: repiten la peticion hasta obtener entrada valida.
- **Switch expressions** (Java 14+): sintaxis concisa para menus.

### Hito 3 - GUI Swing (RA5)

- **CardLayout**: navegacion entre 4 pantallas (Inicio, Menu, Videojuegos, Soundtracks) sin abrir multiples ventanas.
- **JTable con DefaultTableModel**: tabla no editable con `isCellEditable()` sobreescrito.
- **JScrollPane**: barra deslizadora para listas largas.
- **JDialog modal**: dialogos de anadir que bloquean la ventana principal.
- **JComboBox**: filtros y ordenacion por seleccion.
- **JSpinner**: valoracion de 0.0 a 5.0 con paso 0.1.
- **JRadioButton con ButtonGroup**: seleccion exclusiva Fisico/Digital.
- **CardLayout anidado**: campos especificos cambian segun el tipo seleccionado.
- **ActionListener con lambda**: `btnAnadir.addActionListener(e -> ...)` para codigo conciso.
- **ActionListener con clase anonima**: `btnDestruir.addActionListener(new ActionListener() {...})` para demostrar ambas formas.
- **KeyAdapter**: busqueda al presionar Enter.
- **WindowAdapter**: guardar datos al cerrar la ventana con confirmacion.
- **JOptionPane**: dialogos de advertencia antes de destruir y al cerrar.

---

## Formato de datos

### videojuegos.csv

Campos separados por punto y coma. Los generos se separan por punto dentro de su campo.

```
FISICO;1;The Legend of Zelda Breath of the Wild;Nintendo;Aventura.Mundo abierto.RPG;true;4.8;Nuevo;true
DIGITAL;2;Hollow Knight;Team Cherry;Metroidvania.Plataformas;true;4.5;false
```

Formato FISICO: `TIPO;id;nombre;desarrolladora;generos;disponible;valoracion;estado;caja`

Formato DIGITAL: `TIPO;id;nombre;desarrolladora;generos;disponible;valoracion;conexionRequerida`

### soundtracks.json

```json
[
{"id": 1, "nombre": "Hyrule Castle", "compositor": "Manaka Kataoka", "videojuegoAsociado": "The Legend of Zelda Breath of the Wild", "estadoDisponible": true, "valoracion": 4.8, "duracion": 312}
]
```

La duracion se almacena en segundos. Se muestra en formato mm:ss en la interfaz (312 seg = 05:12).

---

## Requisitos

- **JDK 17** o superior
- **Maven 3.8+** (opcional, para compilar con Maven)

---

## Compilar y ejecutar

### Con Maven

```powershell
mvn clean compile
java -cp target/classes proyecto.Main
```

Para modo consola:

```powershell
java -cp target/classes proyecto.Main consola
```

### Sin Maven (compilacion manual)

```powershell
mkdir bin
javac -d bin -encoding UTF-8 (Get-ChildItem -Recurse -Filter *.java | Where-Object { $_.FullName -notlike "*\bin\*" } | ForEach-Object { $_.FullName })
Copy-Item -Recurse -Force resources bin\
cd bin
java proyecto.Main
```

---

## Funcionalidades de la GUI

### Pantalla de inicio
Titulo "GameVault" con boton "Iniciar" centrado.

### Pantalla de menu
Dos botones lado a lado: "Videojuegos" y "Soundtracks".

### Pantalla de videojuegos / soundtracks
- **Barra de busqueda**: busca por ID, nombre, desarrolladora/compositor o genero. Se activa al presionar Enter.
- **Tabla con scroll**: muestra todos los elementos con sus atributos.
- **Filtrar por**: disponibilidad (si/no), tipo (fisico/digital), mejor valorado (5.0), peor valorado (0.0).
- **Ordenar por**: ID, nombre alfabetico, valoracion ascendente/descendente, duracion ascendente/descendente (solo soundtracks).
- **Boton Anadir**: abre un dialogo modal con todos los campos. Solo permite anadir si los campos obligatorios estan rellenos.
- **Boton Destruir**: borra toda la lista con confirmacion previa. Usa un Iterator explicito.
- **Boton Volver**: regresa al menu de seleccion.

### Persistencia automatica
Al cerrar la ventana, se pregunta si guardar los cambios. Los datos se escriben en los ficheros CSV y JSON correspondientes.

---

## Diagrama UML

El diagrama de clases completo esta disponible en formato Mermaid en el archivo `GameVault_UML.mermaid`. Se puede visualizar en [mermaid.live](https://mermaid.live).

---

## Autores

Joaquin y Jean - DAW 1.o
