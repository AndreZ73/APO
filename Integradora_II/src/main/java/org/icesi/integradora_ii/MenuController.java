package org.icesi.integradora_ii;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button muteButton;

    @FXML
    private ImageView muteIcon;

    @FXML
    private BorderPane mainBorderPane;

    private static boolean isMuted = false;

    private final String UNMUTE_ICON_PATH = "/Icons/unmute_icon.png";
    private final String MUTE_ICON_PATH = "/Icons/mute_icon.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String imageUrlPath = "/Background/menu_background.png";
        URL imageUrl = getClass().getResource(imageUrlPath);

        if (imageUrl != null) {
            mainBorderPane.setStyle(
                    "-fx-background-image: url('" + imageUrl.toExternalForm() + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-position: center center;"
            );
        } else {
            System.err.println("Error: Imagen de fondo no encontrada en " + imageUrlPath + ". Revisa la ruta y la extensión.");
        }

        // Cargar ícono del botón de volumen al inicio
        updateMuteIcon();

        // Activar detección de la tecla F para pantalla completa
        Platform.runLater(() -> {
            if (mainBorderPane.getScene() != null) {
                mainBorderPane.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    switch (event.getCode()) {
                        case F -> {
                            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
                            if (!stage.isFullScreen()) {
                                stage.setFullScreen(true);
                            }
                        }
                        default -> {}
                    }
                });

                mainBorderPane.requestFocus();
            }
        });
    }

    @FXML
    public void EnterGame(ActionEvent event) {

        try{
            SceneController.getInstance().init(new Stage());
            SceneController.getInstance().showMap();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameController-view.fxml"));
            Scene gameScene = new Scene(fxmlLoader.<Parent>load());

            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.setScene(gameScene);
            currentStage.setTitle("SGMMS - Game");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading GameController-view.fxml: " + e.getMessage());
        }*/
    }

    @FXML
    public void showManual(ActionEvent event) {
        ManualViewer.toggleManualVisibility();
    }

    @FXML
    public void toggleMute(ActionEvent event) {
        if (HelloApplication.getMediaPlayer() != null) {
            isMuted = !isMuted;
            HelloApplication.getMediaPlayer().setMute(isMuted);
            updateMuteIcon();
        }
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void updateMuteIcon() {
        String iconPath = isMuted ? MUTE_ICON_PATH : UNMUTE_ICON_PATH;
        URL iconUrl = getClass().getResource(iconPath);

        if (iconUrl != null) {
            Image iconImage = new Image(iconUrl.toExternalForm());
            muteIcon.setImage(iconImage);
        } else {
            System.err.println("Error: Icono de mute no encontrado en " + iconPath + ". Revisa la ruta y la extensión.");
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }

    public static void setMuted(boolean muted) {
        isMuted = muted;
    }
}
