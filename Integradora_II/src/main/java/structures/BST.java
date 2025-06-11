package structures;

import java.util.ArrayList; // Necesitas importar ArrayList
import java.util.List;      // O List, si prefieres devolver la interfaz

/**
 * Árbol binario de búsqueda (BST) genérico.
 * <p>
 * Provee inserción, búsqueda, eliminación y recorrido in-order
 * sin utilizar estructuras de la biblioteca estándar.
 * </p>
 *
 * @param <T> Tipo de dato almacenado; debe implementar {@link Comparable}.
 */
public class BST<T extends Comparable<T>> {

    /** Raíz del árbol. */
    private Node<T> root;
    private int size;

    //----------------------------------------------------------------------------------------------
    // Inserción
    //----------------------------------------------------------------------------------------------

    /**
     * Inserta un elemento en el BST.
     *
     * @param value Elemento a insertar; no debe ser {@code null}.
     * @return {@code true} si el elemento se insertó,
     * {@code false} si ya existía y no se añadió.
     */
    public boolean insert(T value) {
        if (value == null) throw new IllegalArgumentException("value no puede ser null");
        if (root == null) {
            root = new Node<>(value);
            size++;
            return true;
        }

        if(insertRec(root, value)) {
            size ++;
            return true;
        }

        return false;
    }

    private boolean insertRec(Node<T> current, T value) {
        // Asumiendo que Node<T> tiene getData() y setData()
        int cmp = value.compareTo(current.getData());
        if (cmp == 0) { // duplicado
            return false;
        } else if (cmp < 0) {
            if (current.getLeft() == null) {
                current.setLeft(new Node<>(value));
                return true;
            }
            return insertRec(current.getLeft(), value);
        } else {
            if (current.getRight() == null) {
                current.setRight(new Node<>(value));
                return true;
            }
            return insertRec(current.getRight(), value);
        }
    }

    //----------------------------------------------------------------------------------------------
    // Búsqueda
    //----------------------------------------------------------------------------------------------

    /**
     * Verifica si el árbol contiene un elemento dado.
     *
     * @param value Elemento a buscar.
     * @return {@code true} si se encuentra, de lo contrario {@code false}.
     */
    public boolean contains(T value) {
        if (value == null) return false;
        return containsRec(root, value);
    }

    private boolean containsRec(Node<T> current, T value) {
        if (current == null) return false;
        int cmp = value.compareTo(current.getData());
        if (cmp == 0) return true;
        return cmp < 0 ? containsRec(current.getLeft(), value)
                : containsRec(current.getRight(), value);
    }

    //----------------------------------------------------------------------------------------------
    // Eliminación
    //----------------------------------------------------------------------------------------------

    /**
     * Elimina un elemento del árbol, si existe.
     *
     * @param value Elemento a eliminar.
     * @return {@code true} si se eliminó, {@code false} si no existía.
     */
    public boolean delete(T value) { // Cambiado a 'delete' para que coincida con lo usado en IncidentManager
        if (value == null) return false;
        // La implementación actual de removeRec maneja el caso de no encontrarlo
        // pero es más eficiente comprobar con contains primero para no decrementar size si no existe.
        if (!contains(value)) return false;

        root = removeRec(root, value);
        size--; // Decrementar el tamaño solo si la eliminación fue exitosa
        return true;
    }

    private Node<T> removeRec(Node<T> current, T value) {
        if (current == null) return null; // Elemento no encontrado en este subárbol

        int cmp = value.compareTo(current.getData());

        if (cmp < 0) {
            current.setLeft(removeRec(current.getLeft(), value));
        } else if (cmp > 0) {
            current.setRight(removeRec(current.getRight(), value));
        } else { // encontrado
            if (current.getLeft() == null) return current.getRight();
            if (current.getRight() == null) return current.getLeft();

            // Reemplazar por el menor de la rama derecha (sucesor in-order)
            Node<T> successor = findMin(current.getRight());
            current.setData(successor.getData()); // Actualizar el dato del nodo actual
            current.setRight(removeRec(current.getRight(), successor.getData())); // Eliminar el sucesor de la rama derecha
        }
        return current;
    }

    private Node<T> findMin(Node<T> node) {
        while (node.getLeft() != null) node = node.getLeft();
        return node;
    }

    //----------------------------------------------------------------------------------------------
    // Recorrido in-order (devuelve String sencillo para depurar)
    //----------------------------------------------------------------------------------------------

    /**
     * Retorna una representación texto in-order de los elementos.
     *
     * @return Cadena con elementos ordenados y separados por coma.
     */
    public String inOrder() {
        StringBuilder sb = new StringBuilder();
        inOrderRec(root, sb);
        return sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
    }

    private void inOrderRec(Node<T> node, StringBuilder sb) {
        if (node == null) return;
        inOrderRec(node.getLeft(), sb);
        sb.append(node.getData()).append(',');
        inOrderRec(node.getRight(), sb);
    }

    //----------------------------------------------------------------------------------------------
    // Nuevo: Recorrido in-order que devuelve un ArrayList
    //----------------------------------------------------------------------------------------------

    /**
     * Realiza un recorrido in-order del árbol y devuelve los elementos en un ArrayList.
     *
     * @return Un ArrayList que contiene todos los elementos del árbol en orden ascendente.
     */
    public ArrayList<T> inOrderTraversal() {
        ArrayList<T> result = new ArrayList<>();
        inOrderTraversalRec(root, result);
        return result;
    }

    private void inOrderTraversalRec(Node<T> node, ArrayList<T> list) {
        if (node == null) {
            return;
        }
        inOrderTraversalRec(node.getLeft(), list);
        list.add(node.getData()); // Asumiendo que Node<T> tiene getData()
        inOrderTraversalRec(node.getRight(), list);
    }


    public Node<T> getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }
}