package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Dijkstra {
    private PriorityQueue<Node> priorityQueue;
    private HashMap<String, Node> allGraphNodes;

    public Dijkstra(HashMap<String, Node> allGraphNodes) {
        this.priorityQueue = new PriorityQueue<>((node1, node2) -> Double.compare(node1.getDistance(), node2.getDistance()));
        this.allGraphNodes = allGraphNodes;
    }

    public ArrayList<Node> findShortestPath(Node startNode, Node endNode) {
        for (Node node : allGraphNodes.values()) {
            node.setDistance(Double.MAX_VALUE);
            node.setPredecessor(null);
            node.setVisited(false);
        }
        priorityQueue.clear();

        startNode.setDistance(0.0);
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (current.isVisited()) {
                continue;
            }
            current.setVisited(true);

            if (current.equals(endNode)) {
                return reconstructPath(startNode, endNode);
            }

            for (Edge edge : current.getAdjacencies()) {
                Node neighbor = edge.getTarget();
                if (!neighbor.isVisited()) {
                    double newDist = current.getDistance() + edge.getWeight();
                    if (newDist < neighbor.getDistance()) {
                        neighbor.setDistance(newDist);
                        neighbor.setPredecessor(current);
                        priorityQueue.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private ArrayList<Node> reconstructPath(Node startNode, Node endNode) {
        ArrayList<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != null && !current.equals(startNode)) {
            path.add(current);
            current = current.getPredecessor();
        }
        if (current != null && current.equals(startNode)) {
            path.add(startNode);
        }
        Collections.reverse(path);
        return path;
    }
}