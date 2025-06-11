package org.icesi.integradora_ii;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneController {

    private static final SceneController INSTANCE = new SceneController();
    private Stage stage;
    private Scene monitoringScene, mapScene, incidentScene;

    private SceneController() {}

    public static SceneController getInstance() { return INSTANCE; }

    public void init(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            //monitoringScene = loadScene("/org/example/sgmms/view/monitoring_center.fxml");
            mapScene        = loadScene("GameController-view.fxml");
            incidentScene   = loadScene("Incident_Panel.fxml");
        } catch (Exception e) {
            throw new RuntimeException("No se pudieron cargar las vistas", e);
        }

        stage.setTitle("SGMMS – Simulador");
        stage.setScene(mapScene);
        stage.show();
    }

    private Scene loadScene(String resource) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        return new Scene(root);
    }

    public void showMonitoring() { stage.setScene(monitoringScene); }

    public void showMap()        {
        stage.setScene(mapScene);
        stage.setTitle("SGMMS - Game");
    }

    public void showIncidents()  { stage.setScene(incidentScene); }
}
