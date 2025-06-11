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
        // 1. Validar que los nodos existen
        Node startNode = graph.getNode(startNodeId);
        Node endNode = graph.getNode(endNodeId);

        if (startNode == null || endNode == null) {
            System.err.println("Error: Nodo de inicio o fin no encontrado.");
            return Collections.emptyList();
        }

        // 2. Inicializar todos los nodos para una nueva búsqueda de Dijkstra
        for (Node node : graph.getAllNodes()) {
            node.resetForDijkstra(); // Resetea distance a INF, previous a null, visited a false
        }

        // 3. Establecer la distancia del nodo inicial a 0
        startNode.setDistance(0);

        // 4. Crear una cola de prioridad para los nodos no visitados
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(startNode);

        // 5. Conjunto para llevar el registro de nodos visitados
        Set<Node> visitedNodes = new HashSet<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll(); // Extrae el nodo con la menor distancia actual

            if (current.isVisited()) {
                continue; // Ya hemos procesado este nodo, saltar
            }

            current.setVisited(true); // Marcar como visitado

            // Si llegamos al nodo final, podemos detenernos
            if (current.getId().equals(endNode.getId())) {
                break;
            }

            // Iterar sobre las aristas salientes del nodo actual
            for (Edge edge : current.getEdges()) {
                Node neighbor = edge.getTarget();
                double edgeWeight = edge.getWeight();

                // Calcular la nueva distancia potencial a través del nodo actual
                double newDistance = current.getDistance() + edgeWeight;

                // Si se encuentra un camino más corto al vecino
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setDistance(newDistance);
                    // ************************************************
                    // ¡CORRECCIÓN AQUÍ! Cambiar setPredecessor a setPrevious
                    neighbor.setPrevious(current); // Establecer el predecesor
                    // ************************************************
                    pq.add(neighbor); // Añadir o actualizar el vecino en la cola de prioridad
                }
            }
        }

        // 6. Reconstruir el camino desde el nodo final al nodo inicial
        List<Node> shortestPath = new ArrayList<>();
        Node step = endNode;

        // Comprobar si se puede llegar al nodo final
        if (endNode.getPrevious() == null && !endNode.getId().equals(startNode.getId())) {
            // No se encontró un camino si el nodo final no tiene predecesor y no es el nodo de inicio
            System.err.println("No se encontró un camino del nodo " + startNodeId + " al nodo " + endNodeId);
            return Collections.emptyList();
        }

        while (step != null) {
            shortestPath.add(0, step); // Añadir al principio para construir el camino en orden
            step = step.getPrevious(); // Moverse al nodo predecesor
        }

        return shortestPath;
    }
}