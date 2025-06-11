package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {

    public static List<Node> findShortestPath(Graph graph, String startNodeId, String endNodeId) {
        Node startNode = graph.getNode(startNodeId);
        Node endNode = graph.getNode(endNodeId);

        if (startNode == null || endNode == null) {
            System.err.println("Error: Nodo de inicio o fin no encontrado.");
            return Collections.emptyList();
        }

        for (Node node : graph.getAllNodes()) {
            node.resetForDijkstra();
        }

        startNode.setDistance(0);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(startNode);

        Set<Node> visitedNodes = new HashSet<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.isVisited()) {
                continue;
            }

            current.setVisited(true);

            if (current.getId().equals(endNode.getId())) {
                break;
            }

            for (Edge edge : current.getEdges()) {
                Node neighbor = edge.getTarget();
                double edgeWeight = edge.getWeight();
                double newDistance = current.getDistance() + edgeWeight;
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setDistance(newDistance);
                    neighbor.setPrevious(current);

                    pq.add(neighbor);
                }
            }
        }

        List<Node> shortestPath = new ArrayList<>();
        Node step = endNode;

        if (endNode.getPrevious() == null && !endNode.getId().equals(startNode.getId())) {
            System.err.println("No se encontró un camino del nodo " + startNodeId + " al nodo " + endNodeId);
            return Collections.emptyList();
        }

        while (step != null) {
            shortestPath.add(0, step);
            step = step.getPrevious();
        }

        return shortestPath;
    }
}