package model;

import graph.Graph;
import graph.Node;
import graph.Edge;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Car1 extends Thread {
    private Random random;

    private Canvas canvas;
    private GraphicsContext gc;

    private int frame;
    private int x;
    private int y;
    private int w;
    private int h;
    private int speed;

    private int state;

    private ArrayList<Image> north;
    private ArrayList<Image> west;
    private ArrayList<Image> east;
    private ArrayList<Image> south;
    private Image NE;
    private Image NW;
    private Image SE;
    private Image SW;

    private Graph mapGraph;
    private Node currentNode;
    private Node targetNode;

    public Car1(Canvas canvas, int x, int y, int w, int h, Graph mapGraph) {
        this.canvas = canvas;
        this.random = new Random();
        this.gc = canvas.getGraphicsContext2D();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.state = 0;
        this.speed = 6;

        north = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String filename = String.format("/Car1/North/POLICE_CLEAN_NORTH_%03d.png", i);
            loadAndAddImage(north, filename);
        }

        west = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String filename = String.format("/Car1/West/POLICE_CLEAN_WEST_%03d.png", i);
            loadAndAddImage(west, filename);
        }

        east = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String filename = String.format("/Car1/East/POLICE_CLEAN_EAST_%03d.png", i);
            loadAndAddImage(east, filename);
        }

        south = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String filename = String.format("/Car1/South/POLICE_CLEAN_SOUTH_%03d.png", i);
            loadAndAddImage(south, filename);
        }

        NE = loadImage("/Car1/POLICE_CLEAN_NORTHEAST_000.png");
        NW = loadImage("/Car1/POLICE_CLEAN_NORTHWEST_000.png");
        SE = loadImage("/Car1/POLICE_CLEAN_SOUTHEAST_000.png");
        SW = loadImage("/Car1/POLICE_CLEAN_SOUTHWEST_000.png");

        this.mapGraph = mapGraph;
        this.currentNode = mapGraph.findNearestNode(x, y);
        if (this.currentNode != null) {
            this.x = (int) this.currentNode.getX();
            this.y = (int) this.currentNode.getY();
            // System.out.println("Carro inició en el nodo: " + this.currentNode.getId() + " (" + this.x + ", " + this.y + ")"); // Kept for important startup info
        } else {
            System.err.println("Advertencia: No se encontró un nodo cercano para las coordenadas iniciales del Car1: (" + x + ", " + y + ").");
            this.currentNode = mapGraph.getNode("A");
            if (this.currentNode != null) {
                this.x = (int) this.currentNode.getX();
                this.y = (int) this.currentNode.getY();
                System.out.println("Carro inició en el nodo 'A' por defecto (" + this.x + ", " + this.y + ").");
            } else {
                System.err.println("Error fatal: No se pudo establecer un nodo inicial para el carro. Asegúrate de que el nodo 'A' existe.");
            }
        }
        this.targetNode = null;
    }

    private Image loadImage(String path) {
        InputStream is = Car1.class.getResourceAsStream(path);
        if (is == null) {
            System.err.println("¡ERROR! No se pudo encontrar el recurso de imagen: " + path);
            return null;
        } else {
            return new Image(is);
        }
    }

    private void loadAndAddImage(ArrayList<Image> list, String path) {
        Image img = loadImage(path);
        if (img != null) {
            list.add(img);
        }
    }

    private void sleepMillis(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (targetNode == null) {
                    // System.out.println("DEBUG: Carro en nodo " + (currentNode != null ? currentNode.getId() : "null") + ". Eligiendo siguiente nodo...");
                    chooseNextNode();
                    if (targetNode == null) {
                        System.out.println("Carro en nodo " + (currentNode != null ? currentNode.getId() : "null") + " sin target. Posiblemente atascado. Esperando...");
                        sleepMillis(500);
                        continue;
                    } else {
                        // System.out.println("DEBUG: Nuevo targetNode elegido: " + targetNode.getId());
                    }
                }

                double dx = targetNode.getX() - x;
                double dy = targetNode.getY() - y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                double arrivalThreshold = speed;
                while (distance > arrivalThreshold && !Thread.currentThread().isInterrupted()) {
                    double ratio = speed / distance;
                    x += dx * ratio;
                    y += dy * ratio;

                    updateCarState(dx, dy);
                    sleepMillis(10);

                    dx = targetNode.getX() - x;
                    dy = targetNode.getY() - y;
                    distance = Math.sqrt(dx * dx + dy * dy);
                }

                x = (int) targetNode.getX();
                y = (int) targetNode.getY();
                currentNode = targetNode;
                targetNode = null;
                // System.out.println("Carro llegó al nodo: " + currentNode.getId() + ". Preparando para la siguiente decisión."); // Kept for important movement info
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("El hilo Car1 fue interrumpido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en el hilo Car1: " + e.getMessage());
            e.printStackTrace();
            try {
                sleepMillis(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Hilo Car1 terminado.");
    }

    private void chooseNextNode() {
        // System.out.println("DEBUG: Entrando a chooseNextNode desde " + (currentNode != null ? currentNode.getId() : "null"));
        if (currentNode == null) {
            System.err.println("Error: CurrentNode es nulo al intentar elegir el siguiente nodo. No se puede elegir destino.");
            return;
        }

        List<Edge> possibleEdges = currentNode.getEdges();
        // System.out.println("DEBUG: Nodo " + currentNode.getId() + " tiene " + possibleEdges.size() + " aristas salientes.");

        if (possibleEdges.isEmpty()) {
            System.out.println("El nodo " + currentNode.getId() + " no tiene salidas. Carro detenido.");
            targetNode = null;
            return;
        }

        if (possibleEdges.size() == 1) {
            targetNode = possibleEdges.get(0).getTarget();
            // System.out.println("DEBUG: Eligiendo el único camino desde " + currentNode.getId() + " a " + targetNode.getId());
        } else {
            int randomIndex = random.nextInt(possibleEdges.size());
            targetNode = possibleEdges.get(randomIndex).getTarget();
            // System.out.println("DEBUG: Eligiendo al azar desde " + currentNode.getId() + " a " + targetNode.getId() + " (entre " + possibleEdges.size() + " opciones)");
        }
    }

    private void updateCarState(double dx, double dy) {
        double threshold = 1.0;

        boolean movingX = Math.abs(dx) > threshold;
        boolean movingY = Math.abs(dy) > threshold;

        if (movingX && movingY) {
            if (dx > 0 && dy < 0) state = 4; // NE
            else if (dx < 0 && dy < 0) state = 5; // NW
            else if (dx > 0 && dy > 0) state = 6; // SE
            else if (dx < 0 && dy > 0) state = 7; // SW
        } else if (movingX) {
            if (dx > 0) state = 2; // East
            else state = 3; // West
        } else if (movingY) {
            if (dy > 0) state = 1; // South
            else state = 0; // North
        }
    }

    public void paint() {
        Image currentImage = null;
        switch (state) {
            case 0: currentImage = north.get(frame % north.size()); break;
            case 1: currentImage = south.get(frame % south.size()); break;
            case 2: currentImage = east.get(frame % east.size()); break;
            case 3: currentImage = west.get(frame % west.size()); break;
            case 4: currentImage = NE; break;
            case 5: currentImage = NW; break;
            case 6: currentImage = SE; break;
            case 7: currentImage = SW; break;
            default: currentImage = north.get(0); break;
        }

        if (currentImage != null) {
            gc.drawImage(currentImage, x, y, w, h);
            if (state >=0 && state <=3) {
                frame++;
            } else {
                frame = 0;
            }
        }
    }

    public int getX() { return x; }
    public int getY(){return y;}
}