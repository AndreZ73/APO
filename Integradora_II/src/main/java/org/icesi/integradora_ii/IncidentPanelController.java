package org.icesi.integradora_ii;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import model.Incident;
import model.IncidentManager;
import structures.BST;
import structures.Node; // Asumo que este es tu Node para el BST

public class IncidentPanelController {

    @FXML private Canvas incidentCanvas;
    @FXML private Label counterLabel;

    private final IncidentManager manager = IncidentManager.getInstance();

    @FXML
    private void initialize() {
        counterLabel.textProperty().bind(
                Bindings.concat("Incidentes: ", manager.totalIncidentsProperty())
        );

        manager.totalIncidentsProperty().addListener((obs, oldV, newV) -> drawIncidents());
        drawIncidents();

        incidentCanvas.setFocusTraversable(true);

        incidentCanvas.requestFocus();

        incidentCanvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q) {
                SceneController.getInstance().showMap();
            }
        });
    }

    @FXML
    private void handleBack() {
        SceneController.getInstance().showMap();
    }

    private void drawIncidents() {
        Platform.runLater(() -> {
            GraphicsContext gc = incidentCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, incidentCanvas.getWidth(), incidentCanvas.getHeight());

            BST<Incident> tree = manager.getTree();
            drawNode(tree.getRoot(), gc, 20);
        });
    }

    private double drawNode(Node<Incident> node, GraphicsContext gc, double y) {
        if (node == null) return y;
        y = drawNode(node.getLeft(), gc, y);

        Incident inc = node.getData();
        gc.setFill(Color.BLACK);
        gc.fillText(inc.toString(), 20, y);
        y += 16;

        y = drawNode(node.getRight(), gc, y);
        return y;
    }
}