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
        } else {
            System.err.println("Advertencia: No se pudo añadir arista. Nodo no encontrado: " + sourceId + " o " + targetId);
        }
    }

    public Node findNearestNode(int coordX, int coordY) {
        Node nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Node node : nodes.values()) {
            double dist = Math.sqrt(Math.pow(node.getX() - coordX, 2) + Math.pow(node.getY() - coordY, 2));
            if (dist < minDist) {
                minDist = dist;
                nearest = node;
            }
        }
        return nearest;
    }


    private double calculateDistance(Node n1, Node n2) {
        return Math.sqrt(Math.pow(n1.getX() - n2.getX(), 2) + Math.pow(n1.getY() - n2.getY(), 2));
    }

    public void loadMapStructure() {

        this.nodes.clear();


        Node nodeA = new Node("A", 55, 160);
        Node nodeB = new Node("B", 415, 160);
        Node nodeC = new Node("C", 415, 670);
        Node nodeD = new Node("D", 415, 995);
        Node nodeE = new Node("E", 55, 995);
        Node nodeF = new Node("F", 1072, 670);
        Node nodeG = new Node("G", 1072, 912);
        Node nodeH = new Node("H", 1860, 912);
        Node nodeI = new Node("I", 1860, 670);
        Node nodeJ = new Node("J", 1860, 1);
        Node nodeK = new Node("K", 1072, 1);
        Node nodeL = new Node("L", 1072, 630);
        Node nodeM = new Node("M", 455, 630);
        Node nodeN = new Node("N", 1860, 630);
        Node nodeO = new Node("O", 455, 120);
        Node nodeP = new Node("P", 10, 120);
        Node nodeQ = new Node("Q", 10, 1020);
        Node nodeR = new Node("R", 455, 1020);
        Node nodeS = new Node("S", 455, 670);
        Node nodeT = new Node("T", 1112, 670);
        Node nodeU = new Node("U", 1112, 41);
        Node nodeV = new Node("V", 1820, 41);
        Node nodeW = new Node("W", 1820,630 );
        Node nodeX = new Node("X", 1112, 630);
        Node nodeY = new Node("Y", 1820, 670);
        Node nodeZ = new Node("Z", 1820, 872);
        Node nodeAA = new Node("AA", 1112, 872);
        Node nodeAB = new Node("AB", 55, 490);
        Node nodeAC = new Node("AC", 105, 410);
        Node nodeAD = new Node("AD", 415, 630);
        //accidentes
        Node nodeAE = new Node("AE", 370, 80);
        Node nodeAF = new Node("AF", 1, 80);
        Node nodeAG = new Node("AG", 1432, 85);
        Node nodeAH = new Node("AH", 1549, 600);
        Node nodeAI = new Node("AI", 1285, 705);
        //no acciendetes
        Node nodeAK= new Node("AK", 198, 590);
        Node nodeAL= new Node("AL", 55, 590);
        Node nodeAM= new Node("AM", 10, 590);



        addNode(nodeA);
        addNode(nodeB);
        addNode(nodeC);
        addNode(nodeD);
        addNode(nodeE);
        addNode(nodeF);
        addNode(nodeG);
        addNode(nodeH);
        addNode(nodeI);
        addNode(nodeJ);
        addNode(nodeK);
        addNode(nodeL);
        addNode(nodeM);
        addNode(nodeN);
        addNode(nodeO);
        addNode(nodeP);
        addNode(nodeQ);
        addNode(nodeR);
        addNode(nodeS);
        addNode(nodeT);
        addNode(nodeU);
        addNode(nodeV);
        addNode(nodeW);
        addNode(nodeX);
        addNode(nodeY);
        addNode(nodeZ);
        addNode(nodeAA);
        addNode(nodeAB);
        addNode(nodeAC);
        addNode(nodeAD);
        addNode(nodeAE);
        addNode(nodeAF);
        addNode(nodeAG);
        addNode(nodeAH);
        addNode(nodeAI);
        addNode(nodeAK);
        addNode(nodeAL);
        addNode(nodeAM);


        // A va a B
        addEdge("A", "B", calculateDistance(nodeA, nodeB));

        // B a C
        addEdge("B", "C", calculateDistance(nodeB, nodeC));

        // C va a D o F
        addEdge("C", "D", calculateDistance(nodeC, nodeD));
        addEdge("C", "F", calculateDistance(nodeC, nodeF));

        // D va a E
        addEdge("D", "E", calculateDistance(nodeD, nodeE));



        // F va a G
        addEdge("F", "G", calculateDistance(nodeF, nodeG));

        // G va a H
        addEdge("G", "H", calculateDistance(nodeG, nodeH));

        // H va I o J
        addEdge("H", "I", calculateDistance(nodeH, nodeI));
        addEdge("H", "J", calculateDistance(nodeH, nodeJ));


        addEdge("J", "K", calculateDistance(nodeJ, nodeK));


        addEdge("K", "L", calculateDistance(nodeK, nodeL));

        addEdge("L", "F", calculateDistance(nodeK, nodeL));
        addEdge("I", "N", calculateDistance(nodeK, nodeL));
        addEdge("N", "M", calculateDistance(nodeK, nodeL));
        addEdge("N", "L", calculateDistance(nodeK, nodeL));
        addEdge("L", "M", calculateDistance(nodeK, nodeL));
        addEdge("M", "O", calculateDistance(nodeK, nodeL));
        addEdge("O", "P", calculateDistance(nodeK, nodeL));
        addEdge("P", "Q", calculateDistance(nodeK, nodeL));
        addEdge("Q", "R", calculateDistance(nodeK, nodeL));
        addEdge("R", "S", calculateDistance(nodeK, nodeL));
        addEdge("S", "F", calculateDistance(nodeK, nodeL));
        addEdge("F", "T", calculateDistance(nodeK, nodeL));
        addEdge("T", "U", calculateDistance(nodeK, nodeL));
        addEdge("T", "I", calculateDistance(nodeK, nodeL));
        addEdge("U", "V", calculateDistance(nodeK, nodeL));
        addEdge("V", "W", calculateDistance(nodeK, nodeL));

        addEdge("W", "X", calculateDistance(nodeK, nodeL));
        addEdge("X", "L", calculateDistance(nodeK, nodeL));
        addEdge("W", "Y", calculateDistance(nodeK, nodeL));
        addEdge("Y", "Z", calculateDistance(nodeK, nodeL));
        addEdge("Z", "AA", calculateDistance(nodeK, nodeL));
        addEdge("AA", "T", calculateDistance(nodeK, nodeL));
        addEdge("E", "AB", calculateDistance(nodeK, nodeL));
        addEdge("AB", "AC", calculateDistance(nodeK, nodeL));
        addEdge("AB", "A", calculateDistance(nodeK, nodeL));
        addEdge("M", "AD", calculateDistance(nodeK, nodeL));
        addEdge("AD", "C", calculateDistance(nodeK, nodeL));
        addEdge("O", "AE", calculateDistance(nodeK, nodeL));
        addEdge("E", "A", calculateDistance(nodeK, nodeL));
        addEdge("P", "AF", calculateDistance(nodeK, nodeL));
        addEdge("U", "AG", calculateDistance(nodeK, nodeL));
        addEdge("N", "AH", calculateDistance(nodeK, nodeL));
        addEdge("S", "I", calculateDistance(nodeK, nodeL));
        addEdge("S", "T", calculateDistance(nodeK, nodeL));
        addEdge("T", "AI", calculateDistance(nodeK, nodeL));
        addEdge("AK", "AL", calculateDistance(nodeK, nodeL));
        addEdge("AL", "A", calculateDistance(nodeK, nodeL));
        addEdge("AK", "AM", calculateDistance(nodeK, nodeL));
        addEdge("AM", "Q", calculateDistance(nodeK, nodeL));
    }
}