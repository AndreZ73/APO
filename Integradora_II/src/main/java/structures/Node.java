package structures;

/**
 * Nodo genérico para un árbol binario de búsqueda.
 *
 * @param <T> Tipo de dato almacenado; debe ser comparable.
 */
public class Node<T extends Comparable<T>> {

    /** Información almacenada en el nodo. */
    private T data;

    /** Referencia al hijo izquierdo. */
    private Node<T> left;

    /** Referencia al hijo derecho. */
    private Node<T> right;

    /**
     * Construye un nodo con el elemento dado.
     *
     * @param data Elemento a almacenar; no debe ser {@code null}.
     */
    public Node(T data) {
        if (data == null) throw new IllegalArgumentException("data no puede ser null");
        this.data = data;
    }

    // --- Getters/Setters (añadidos al final como solicita el usuario) -----------------------------

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if (data == null) throw new IllegalArgumentException("data no puede ser null");
        this.data = data;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }
}
