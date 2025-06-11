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

public class EmergencyVehicle extends Thread {
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

    public EmergencyVehicle(Canvas canvas, int x, int y, int w, int h, Graph mapGraph, CarType type) {
        this.canvas = canvas;
        this.random = new Random();
        this.gc = canvas.getGraphicsContext2D();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.state = 0;
        this.speed = 6;

        String basePath;
        String vehicleName;

        switch (type) {
            case POLICE:
                basePath = "/EmergencyVehicles/Police/";
                vehicleName = "POLICE";
                break;
            case AMBULANCE:
                basePath = "/EmergencyVehicles/Ambulance/";
                vehicleName = "AMBULANCE";
                break;
            case FIRETRUCK:
                basePath = "/EmergencyVehicles/FireTruck/";
                vehicleName = "Red_CAMPER";
                break;
            default:
                basePath = "/EmergencyVehicles/Police/";
                vehicleName = "POLICE";
                System.err.println("Warning: Unknown CarType, defaulting to Police images.");
                break;
        }

        north = new ArrayList<>();
        west = new ArrayList<>();
        east = new ArrayList<>();
        south = new ArrayList<>();

        // Load animated sprites for cardinal directions
        for (int i = 0; i < 11; i++) {
                loadAndAddImage(north, String.format(basePath + "North/%s_CLEAN_NORTH_%03d.png", vehicleName, i));
                loadAndAddImage(west, String.format(basePath + "West/%s_CLEAN_WEST_%03d.png", vehicleName, i));
                loadAndAddImage(east, String.format(basePath + "East/%s_CLEAN_EAST_%03d.png", vehicleName, i));
                loadAndAddImage(south, String.format(basePath + "South/%s_CLEAN_SOUTH_%03d.png", vehicleName, i));

            NE = loadImage(basePath + vehicleName + "_CLEAN_NORTHEAST_000.png");
            NW = loadImage(basePath + vehicleName + "_CLEAN_NORTHWEST_000.png");
            SE = loadImage(basePath + vehicleName + "_CLEAN_SOUTHEAST_000.png");
            SW = loadImage(basePath + vehicleName + "_CLEAN_SOUTHWEST_000.png");
        }

        this.mapGraph = mapGraph;
        this.currentNode = mapGraph.findNearestNode(x, y);
        if (this.currentNode != null) {
            this.x = (int) this.currentNode.getX();
            this.y = (int) this.currentNode.getY();
        } else {
            System.err.println("Warning: No nearest node found for initial coordinates (" + x + ", " + y + "). Attempting to set to node 'A'.");
            this.currentNode = mapGraph.getNode("A");
            if (this.currentNode != null) {
                this.x = (int) this.currentNode.getX();
                this.y = (int) this.currentNode.getY();
                System.out.println("Vehicle started at default node 'A' (" + this.x + ", " + this.y + ").");
            } else {
                System.err.println("Fatal Error: Could not set initial node for the vehicle. Ensure node 'A' exists in your graph.");
            }
        }
        this.targetNode = null;
    }

    private Image loadImage(String path) {
        InputStream is = EmergencyVehicle.class.getResourceAsStream(path);
        if (is == null) {
            System.err.println("ERROR! Could not find image resource: " + path);
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
                    chooseNextNode();
                    if (targetNode == null) {
                        System.out.println("Vehicle at node " + (currentNode != null ? currentNode.getId() : "null") + " with no target. Possibly stuck. Waiting...");
                        sleepMillis(500);
                        continue;
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
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("The EmergencyVehicle thread was interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error in EmergencyVehicle thread: " + e.getMessage());
            e.printStackTrace();
            try {
                sleepMillis(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("EmergencyVehicle thread terminated.");
    }

    private void chooseNextNode() {
        if (currentNode == null) {
            System.err.println("Error: CurrentNode is null when trying to choose the next node. Cannot choose destination.");
            return;
        }

        List<Edge> possibleEdges = currentNode.getEdges();

        if (possibleEdges.isEmpty()) {
            System.out.println("Node " + currentNode.getId() + " has no outgoing edges. Vehicle stopped.");
            targetNode = null;
            return;
        }

        if (possibleEdges.size() == 1) {
            targetNode = possibleEdges.get(0).getTarget();
        } else {
            int randomIndex = random.nextInt(possibleEdges.size());
            targetNode = possibleEdges.get(randomIndex).getTarget();
        }
    }

    private void updateCarState(double dx, double dy) {
        double threshold = 1.0;

        boolean movingX = Math.abs(dx) > threshold;
        boolean movingY = Math.abs(dy) > threshold;

        if (movingX && movingY) {
            if (dx > 0 && dy < 0) state = 4;
            else if (dx < 0 && dy < 0) state = 5;
            else if (dx > 0 && dy > 0) state = 6;
            else if (dx < 0 && dy > 0) state = 7;
        } else if (movingX) {
            if (dx > 0) state = 2;
            else state = 3;
        } else if (movingY) {
            if (dy > 0) state = 1;
            else state = 0;
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
            if (state >= 0 && state <= 3) {
                frame++;
            } else {
                frame = 0;
            }
        }
    }

    public int getX() { return x; }
    public int getY(){return y;}
}