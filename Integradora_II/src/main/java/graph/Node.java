package graph;

import java.util.ArrayList;

public class Node {
    private String id;
    private double x;
    private double y;
    private ArrayList<Edge> adjacencies;

    private double distance = Double.MAX_VALUE;
    private Node predecessor = null;
    private boolean visited = false;

    public Node(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.adjacencies = new ArrayList<>();
    }

    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public ArrayList<Edge> getAdjacencies() { return adjacencies; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public Node getPredecessor() { return predecessor; }
    public void setPredecessor(Node predecessor) { this.predecessor = predecessor; }
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

    public void addEdge(Edge edge) {
        this.adjacencies.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}