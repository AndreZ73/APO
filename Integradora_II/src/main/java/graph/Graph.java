package graph;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    private HashMap<String, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public ArrayList<Node> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }

    public void addEdge(String sourceId, String targetId, double weight) {
        Node source = nodes.get(sourceId);
        Node target = nodes.get(targetId);
        if (source != null && target != null) {
            source.addEdge(new Edge(target, weight));
            target.addEdge(new Edge(source, weight));
        }
    }
    private double calculateDistance(Node n1, Node n2) {
        return Math.sqrt(Math.pow(n1.getX() - n2.getX(), 2) + Math.pow(n1.getY() - n2.getY(), 2));
    }
    public void loadMapStructure() {
        Node n1 = new Node("N1", 176, 580);
        Node n2 = new Node("N2", 350, 580);
        Node n3 = new Node("N3", 350, 450);
        Node n4 = new Node("N4", 176, 450);

        addNode(n1);
        addNode(n2);
        addNode(n3);
        addNode(n4);

        addEdge("N1", "N2", calculateDistance(n1, n2));
        addEdge("N2", "N3", calculateDistance(n2, n3));
        addEdge("N3", "N4", calculateDistance(n3, n4));
        addEdge("N4", "N1", calculateDistance(n4, n1));

        Node n_1_1 = getNode("N1"); // Already defined as N1
        Node n_1_2 = getNode("N2"); // Already defined as N2
        Node n_1_3 = new Node("N1_3", 520, 580);
        Node n_1_4 = new Node("N1_4", 690, 580);
        Node n_1_5 = new Node("N1_5", 860, 580);

        addNode(n_1_3); addNode(n_1_4); addNode(n_1_5);
        addEdge("N1_2", "N1_3", calculateDistance(n_1_2, n_1_3)); addEdge("N1_3", "N1_2", calculateDistance(n_1_3, n_1_2));
        addEdge("N1_3", "N1_4", calculateDistance(n_1_3, n_1_4)); addEdge("N1_4", "N1_3", calculateDistance(n_1_4, n_1_3));
        addEdge("N1_4", "N1_5", calculateDistance(n_1_4, n_1_5)); addEdge("N1_5", "N1_4", calculateDistance(n_1_5, n_1_4));

        Node n_2_1 = getNode("N4");
        Node n_2_2 = getNode("N3");
        Node n_2_3 = new Node("N2_3", 520, 450);
        Node n_2_4 = new Node("N2_4", 690, 450);
        Node n_2_5 = new Node("N2_5", 860, 450);

        addNode(n_2_3); addNode(n_2_4); addNode(n_2_5);
        addEdge("N2_2", "N2_3", calculateDistance(n_2_2, n_2_3)); addEdge("N2_3", "N2_2", calculateDistance(n_2_3, n_2_2));
        addEdge("N2_3", "N2_4", calculateDistance(n_2_3, n_2_4)); addEdge("N2_4", "N2_3", calculateDistance(n_2_4, n_2_3));
        addEdge("N2_4", "N2_5", calculateDistance(n_2_4, n_2_5)); addEdge("N2_5", "N2_4", calculateDistance(n_2_5, n_2_4));

        Node n_3_1 = new Node("N3_1", 176, 320);
        Node n_3_2 = new Node("N3_2", 350, 320);
        Node n_3_3 = new Node("N3_3", 520, 320);
        Node n_3_4 = new Node("N3_4", 690, 320);
        Node n_3_5 = new Node("N3_5", 860, 320);

        addNode(n_3_1); addNode(n_3_2); addNode(n_3_3); addNode(n_3_4); addNode(n_3_5);
        addEdge("N3_1", "N3_2", calculateDistance(n_3_1, n_3_2)); addEdge("N3_2", "N3_1", calculateDistance(n_3_2, n_3_1));
        addEdge("N3_2", "N3_3", calculateDistance(n_3_2, n_3_3)); addEdge("N3_3", "N3_2", calculateDistance(n_3_3, n_3_2));
        addEdge("N3_3", "N3_4", calculateDistance(n_3_3, n_3_4)); addEdge("N3_4", "N3_3", calculateDistance(n_3_4, n_3_3));
        addEdge("N3_4", "N3_5", calculateDistance(n_3_4, n_3_5)); addEdge("N3_5", "N3_4", calculateDistance(n_3_5, n_3_4));

        Node n_4_1 = new Node("N4_1", 176, 190);
        Node n_4_2 = new Node("N4_2", 350, 190);
        Node n_4_3 = new Node("N4_3", 520, 190);
        Node n_4_4 = new Node("N4_4", 690, 190);
        Node n_4_5 = new Node("N4_5", 860, 190);

        addNode(n_4_1); addNode(n_4_2); addNode(n_4_3); addNode(n_4_4); addNode(n_4_5);
        addEdge("N4_1", "N4_2", calculateDistance(n_4_1, n_4_2)); addEdge("N4_2", "N4_1", calculateDistance(n_4_2, n_4_1));
        addEdge("N4_2", "N4_3", calculateDistance(n_4_2, n_4_3)); addEdge("N4_3", "N4_2", calculateDistance(n_4_3, n_4_2));
        addEdge("N4_3", "N4_4", calculateDistance(n_4_3, n_4_4)); addEdge("N4_4", "N4_3", calculateDistance(n_4_4, n_4_3));
        addEdge("N4_4", "N4_5", calculateDistance(n_4_4, n_4_5)); addEdge("N4_5", "N4_4", calculateDistance(n_4_5, n_4_4));


        // Vertical connections
        addEdge("N1", "N4", calculateDistance(getNode("N1"), getNode("N4"))); addEdge("N4", "N1", calculateDistance(getNode("N4"), getNode("N1")));
        addEdge("N2", "N3", calculateDistance(getNode("N2"), getNode("N3"))); addEdge("N3", "N2", calculateDistance(getNode("N3"), getNode("N2")));

        addEdge("N1", "N3_1", calculateDistance(getNode("N1"), n_3_1)); addEdge("N3_1", "N1", calculateDistance(n_3_1, getNode("N1")));
        addEdge("N2", "N3_2", calculateDistance(getNode("N2"), n_3_2)); addEdge("N3_2", "N2", calculateDistance(n_3_2, getNode("N2")));
        addEdge("N1_3", "N3_3", calculateDistance(n_1_3, n_3_3)); addEdge("N3_3", "N1_3", calculateDistance(n_3_3, n_1_3));
        addEdge("N1_4", "N3_4", calculateDistance(n_1_4, n_3_4)); addEdge("N3_4", "N1_4", calculateDistance(n_3_4, n_1_4));
        addEdge("N1_5", "N3_5", calculateDistance(n_1_5, n_3_5)); addEdge("N3_5", "N1_5", calculateDistance(n_3_5, n_1_5));

        addEdge("N4", "N3_1", calculateDistance(getNode("N4"), n_3_1)); addEdge("N3_1", "N4", calculateDistance(n_3_1, getNode("N4")));
        addEdge("N3", "N3_2", calculateDistance(getNode("N3"), n_3_2)); addEdge("N3_2", "N3", calculateDistance(n_3_2, getNode("N3")));
        addEdge("N2_3", "N3_3", calculateDistance(n_2_3, n_3_3)); addEdge("N3_3", "N2_3", calculateDistance(n_3_3, n_2_3));
        addEdge("N2_4", "N3_4", calculateDistance(n_2_4, n_3_4)); addEdge("N3_4", "N2_4", calculateDistance(n_3_4, n_2_4));
        addEdge("N2_5", "N3_5", calculateDistance(n_2_5, n_3_5)); addEdge("N3_5", "N2_5", calculateDistance(n_3_5, n_2_5));

        addEdge("N3_1", "N4_1", calculateDistance(n_3_1, n_4_1)); addEdge("N4_1", "N3_1", calculateDistance(n_4_1, n_3_1));
        addEdge("N3_2", "N4_2", calculateDistance(n_3_2, n_4_2)); addEdge("N4_2", "N3_2", calculateDistance(n_4_2, n_3_2));
        addEdge("N3_3", "N4_3", calculateDistance(n_3_3, n_4_3)); addEdge("N4_3", "N3_3", calculateDistance(n_4_3, n_3_3));
        addEdge("N3_4", "N4_4", calculateDistance(n_3_4, n_4_4)); addEdge("N4_4", "N3_4", calculateDistance(n_4_4, n_3_4));
        addEdge("N3_5", "N4_5", calculateDistance(n_3_5, n_4_5)); addEdge("N4_5", "N3_5", calculateDistance(n_4_5, n_3_5));
    }
}