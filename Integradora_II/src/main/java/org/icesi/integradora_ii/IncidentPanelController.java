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
        // Une el Label a la propiedad observable del manager
        counterLabel.textProperty().bind(
                Bindings.concat("Incidentes: ", manager.totalIncidentsProperty())
        );

        // Redibuja cada vez que incrementa el total (simple pero funcional)
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


    //----------------------------------------------------------------------------------------------
    // Dibujo en Canvas
    //----------------------------------------------------------------------------------------------

    private void drawIncidents() {
        // Canvas solo puede dibujarse en el hilo de JavaFX
        Platform.runLater(() -> {
            GraphicsContext gc = incidentCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, incidentCanvas.getWidth(), incidentCanvas.getHeight());

            BST<Incident> tree = manager.getTree();
            // Asegúrate que tu BST tiene un getRoot()
            drawNode(tree.getRoot(), gc, 20); // Ajusta la coordenada Y inicial según tu diseño
        });
    }

    /**
     * Recorre el BST y dibuja cada incidente como un texto simple.
     * @param node nodo actual (asumo que es structures.Node<Incident>)
     * @param gc   context2D
     * @param y    coordenada vertical acumulada
     * @return     y final (para que las llamadas recursivas sepan dónde continuar)
     */
    private double drawNode(Node<Incident> node, GraphicsContext gc, double y) {
        if (node == null) return y;
        y = drawNode(node.getLeft(), gc, y);

        Incident inc = node.getData(); // Asumo que tu Node para BST usa getValue()
        gc.setFill(Color.BLACK);
        gc.fillText(inc.toString(), 20, y); // Ajusta la coordenada X si es necesario
        y += 16; // Espacio entre líneas

        y = drawNode(node.getRight(), gc, y);
        return y;
    }
}