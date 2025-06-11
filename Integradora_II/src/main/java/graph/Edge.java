package graph;

public class Edge {
    private Node target; // El nodo al que esta arista se dirige
    private double weight;

    public Edge(Node target, double weight) {
        this.target = target;
        this.weight = weight;
    }

    public Node getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "target=" + target.getId() + // Mostrar solo el ID del target para evitar recursión infinita
                ", weight=" + String.format("%.2f", weight) +
            '}';
    }
}