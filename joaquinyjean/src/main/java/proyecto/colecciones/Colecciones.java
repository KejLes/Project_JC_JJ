package proyecto.colecciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import proyecto.interfaces.Valorable;

/**
 * Clase generica que gestiona una coleccion de elementos.
 * Mantiene dos estructuras sincronizadas:
 * <ul>
 *   <li>{@code ArrayList} como coleccion principal (orden de insercion).</li>
 *   <li>{@code HashMap} como indice secundario para busquedas rapidas
 *       por una clave configurable.</li>
 * </ul>
 *
 * El tipo parametrizado {@code T} debe implementar {@link Valorable}
 * para que los metodos de filtrado por valoracion funcionen.
 *
 * Ejemplo de uso:
 * <pre>
 *   // Para videojuegos, el indice secundario es la desarrolladora
 *   Colecciones&lt;Videojuego&gt; colVj = new Colecciones&lt;&gt;(Videojuego::getDesarrolladora);
 *
 *   // Para soundtracks, el indice secundario es el compositor
 *   Colecciones&lt;Soundtrack&gt; colSt = new Colecciones&lt;&gt;(Soundtrack::getCompositor);
 * </pre>
 *
 * @param <T> tipo de elemento que extiende Valorable
 */
public class Colecciones<T extends Valorable> {

    // -- Coleccion principal: guarda todos los elementos en orden de insercion --
    private List<T> lista;

    // -- Indice secundario: agrupa elementos por una clave para busquedas rapidas --
    private Map<String, List<T>> indiceSecundario;

    // -- Funcion que extrae la clave para el indice secundario de cada elemento --
    private Function<T, String> extractorClave;

    /**
     * Crea una coleccion vacia con el extractor de clave indicado.
     * El extractor define que campo se usara como clave del HashMap.
     *
     * @param extractorClave funcion que, dado un elemento, devuelve
     *                       la clave para el indice secundario
     *                       (por ejemplo, {@code Videojuego::getDesarrolladora})
     */
    public Colecciones(Function<T, String> extractorClave) {
        this.lista = new ArrayList<>();
        this.indiceSecundario = new HashMap<>();
        this.extractorClave = extractorClave;
    }

    // =========================================================================
    //  OPERACIONES BASICAS
    // =========================================================================

    /**
     * Anade un elemento a ambas colecciones (ArrayList y HashMap).
     * Si la clave del indice secundario no existe, se crea una nueva entrada.
     *
     * @param elemento el objeto a anadir
     */
    public void anadir(T elemento) {
        lista.add(elemento);

        // computeIfAbsent: si la clave no existe, crea un ArrayList nuevo;
        // si ya existe, simplemente anade el elemento a la lista existente
        String clave = extractorClave.apply(elemento).toLowerCase();
        indiceSecundario
                .computeIfAbsent(clave, k -> new ArrayList<>())
                .add(elemento);
    }

    /**
     * Carga una lista completa de elementos, reconstruyendo ambas colecciones.
     * Se usa al iniciar el programa para cargar los datos desde fichero.
     *
     * @param elementos lista de elementos a cargar
     */
    public void cargarTodos(List<T> elementos) {
        lista.clear();
        indiceSecundario.clear();
        for (T elem : elementos) {
            anadir(elem);
        }
    }

    /**
     * Elimina un elemento concreto de ambas colecciones.
     * Usa Iterator explicito sobre el ArrayList para eliminar de forma segura
     * y luego sincroniza el HashMap.
     *
     * @param elemento el objeto a eliminar
     * @return true si se encontro y elimino, false si no existia
     */
    public boolean eliminar(T elemento) {
        Iterator<T> it = lista.iterator();
        boolean encontrado = false;
        while (it.hasNext()) {
            T actual = it.next();
            if (actual.equals(elemento)) {
                it.remove();
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            // Sincronizar el HashMap: eliminar el elemento de su grupo
            String clave = extractorClave.apply(elemento).toLowerCase();
            List<T> grupo = indiceSecundario.get(clave);
            if (grupo != null) {
                grupo.remove(elemento);
                if (grupo.isEmpty()) {
                    indiceSecundario.remove(clave);
                }
            }
        }
        return encontrado;
    }

    /**
     * Destruye toda la coleccion usando un Iterator explicito.
     * Recorre el ArrayList elemento a elemento y los va eliminando.
     * Finalmente limpia el HashMap.
     *
     * Este metodo es el que se invoca desde el boton "Destruir" de la GUI.
     * Se usa un iterator explicito como requisito del proyecto.
     */
    public void destruir() {
        // Iterator explicito: recorre y elimina uno a uno
        Iterator<T> it = lista.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        // Limpiar el indice secundario
        indiceSecundario.clear();
    }

    // =========================================================================
    //  BUSQUEDAS
    // =========================================================================

    /**
     * Busca elementos por la clave del indice secundario.
     * Usa directamente el HashMap para acceso O(1), sin recorrer la lista.
     *
     * @param clave valor a buscar (por ejemplo, nombre de desarrolladora)
     * @return lista de elementos que coinciden, o lista vacia si no hay resultados
     */
    public List<T> buscarPorIndice(String clave) {
        return indiceSecundario.getOrDefault(
                clave.toLowerCase(), Collections.emptyList());
    }

    /**
     * Busca elementos que cumplan un predicado arbitrario.
     * Se usa para busquedas genericas por texto en multiples campos.
     *
     * @param filtro predicado que define la condicion de busqueda
     * @return lista de elementos que cumplen el filtro
     */
    public List<T> buscar(Predicate<T> filtro) {
        return lista.stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    // =========================================================================
    //  FILTROS (usan streams)
    // =========================================================================

    /**
     * Filtra los elementos que cumplen un predicado.
     *
     * @param filtro condicion de filtrado
     * @return lista filtrada
     */
    public List<T> filtrar(Predicate<T> filtro) {
        return lista.stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    /**
     * Filtra por mejor valorado: devuelve elementos con valoracion igual a 5.0.
     *
     * @return lista de elementos con valoracion maxima
     */
    public List<T> filtrarMejorValorado() {
        return lista.stream()
                .filter(e -> e.getValoracion() == 5.0)
                .collect(Collectors.toList());
    }

    /**
     * Filtra por peor valorado: devuelve elementos con valoracion igual a 0.0.
     *
     * @return lista de elementos con valoracion minima
     */
    public List<T> filtrarPeorValorado() {
        return lista.stream()
                .filter(e -> e.getValoracion() == 0.0)
                .collect(Collectors.toList());
    }

    /**
     * Metodo generico con bounded type: filtra elementos por su clase concreta.
     * Util para separar Fisico de Digital dentro de una coleccion de Videojuego.
     *
     * Ejemplo de uso:
     * <pre>
     *   List&lt;Fisico&gt; fisicos = coleccion.filtrarPorTipo(Fisico.class);
     * </pre>
     *
     * @param tipo clase por la que filtrar
     * @param <S>  subtipo de T
     * @return lista de elementos que son instancia de la clase indicada
     */
    public <S extends T> List<S> filtrarPorTipo(Class<S> tipo) {
        return lista.stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .collect(Collectors.toList());
    }

    // =========================================================================
    //  ORDENACION (usan streams)
    // =========================================================================

    /**
     * Ordena la coleccion usando el comparador proporcionado.
     * No modifica la lista original; devuelve una nueva lista ordenada.
     *
     * @param comparador criterio de ordenacion
     * @return nueva lista ordenada
     */
    public List<T> ordenar(Comparator<T> comparador) {
        return lista.stream()
                .sorted(comparador)
                .collect(Collectors.toList());
    }

    /**
     * Ordena por valoracion de forma ascendente (de menor a mayor).
     *
     * @return lista ordenada por valoracion ascendente
     */
    public List<T> ordenarPorValoracionAsc() {
        return lista.stream()
                .sorted(Comparator.comparingDouble(Valorable::getValoracion))
                .collect(Collectors.toList());
    }

    /**
     * Ordena por valoracion de forma descendente (de mayor a menor).
     *
     * @return lista ordenada por valoracion descendente
     */
    public List<T> ordenarPorValoracionDesc() {
        return lista.stream()
                .sorted(Comparator.comparingDouble(Valorable::getValoracion).reversed())
                .collect(Collectors.toList());
    }

    // =========================================================================
    //  EXPORTACION MANUAL (JSON y CSV con StringBuilder, sin librerias)
    // =========================================================================

    /**
     * Genera la representacion JSON de toda la coleccion.
     * Construye el array JSON manualmente con StringBuilder.
     *
     * @param conversor funcion que convierte cada elemento a su cadena JSON
     * @return cadena con el array JSON completo
     */
    public String toJson(Function<T, String> conversor) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < lista.size(); i++) {
            sb.append("  ").append(conversor.apply(lista.get(i)));
            if (i < lista.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Genera la representacion CSV de toda la coleccion.
     * Cada elemento ocupa una linea.
     *
     * @param conversor funcion que convierte cada elemento a su cadena CSV
     * @return cadena con todas las lineas CSV
     */
    public String toCsv(Function<T, String> conversor) {
        StringBuilder sb = new StringBuilder();
        for (T elem : lista) {
            sb.append(conversor.apply(elem)).append("\n");
        }
        return sb.toString();
    }

    // =========================================================================
    //  UTILIDADES
    // =========================================================================

    /**
     * @return lista principal (referencia directa, no copia)
     */
    public List<T> getLista() {
        return lista;
    }

    /**
     * @return mapa del indice secundario
     */
    public Map<String, List<T>> getIndiceSecundario() {
        return indiceSecundario;
    }

    /**
     * @return numero total de elementos en la coleccion
     */
    public int tamano() {
        return lista.size();
    }

    /**
     * @return true si la coleccion esta vacia
     */
    public boolean estaVacia() {
        return lista.isEmpty();
    }
}
