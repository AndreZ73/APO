package graph;

import java.util.ArrayList;
import java.util.List;

// Implementa Comparable para usar con PriorityQueue en Dijkstra
public class Node implements Comparable<Node> {
    private String id;
    private double x;
    private double y;
    private List<Edge> edges; // Lista de aristas salientes de este nodo

    // --- Propiedades para Dijkstra (aunque no se usen en el movimiento aleatorio directo, son útiles para otras funciones) ---
    private double distance; // Distancia más corta conocida desde el nodo fuente
    private Node previous;   // El nodo predecesor en el camino más corto
    private boolean visited; // Para algoritmos de recorrido

    public Node(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
        // Inicializar para Dijkstra
        this.distance = Double.POSITIVE_INFINITY;
        this.previous = null;
        this.visited = false;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void resetForDijkstra() {
        this.distance = Double.POSITIVE_INFINITY;
        this.previous = null;
        this.visited = false;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", dist=" + (distance == Double.POSITIVE_INFINITY ? "INF" : String.format("%.2f", distance)) +
            '}';
    }
}