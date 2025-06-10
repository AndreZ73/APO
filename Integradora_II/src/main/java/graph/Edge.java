package graph;

public class Edge {
    private Node target;
    private double weight; // Distance, time, etc.

    public Edge(Node target, double weight) {
        this.target = target;
        this.weight = weight;
    }

    // Getters
    public Node getTarget() { return target; }
    public double getWeight() { return weight; }
}