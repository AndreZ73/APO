package model;

import graph.Node; // Importa tu clase Node del paquete graph
import java.util.List;
import java.util.Objects;

public class Route implements Comparable<Route> {
    private String id;
    private Node origin;
    private Node destination;
    private double totalTime;
    private List<Node> pathNodes;

    public Route(String id, Node origin, Node destination, double totalTime, List<Node> pathNodes) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.totalTime = totalTime;
        this.pathNodes = pathNodes;
    }

    public String getId() { return id; }
    public Node getOrigin() { return origin; }
    public Node getDestination() { return destination; }
    public double getTotalTime() { return totalTime; }
    public List<Node> getPathNodes() { return pathNodes; }

    @Override
    public int compareTo(Route other) {
        int cmp = Double.compare(this.totalTime, other.totalTime);
        if (cmp == 0) {
            return this.id.compareTo(other.id);
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ruta desde " + origin.getId() + " a " + destination.getId() + " en " + String.format("%.2f", totalTime) + " segundos.";
    }
}