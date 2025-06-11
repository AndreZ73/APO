package org.icesi.integradora_ii;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import model.Car1;
import model.Incident; // Importar la clase Incident
import model.IncidentManager; // Importar IncidentManager
import model.IncidentSprite; // Importar IncidentSprite

import java.net.URL;
import java.util.HashMap; // Usaremos un HashMap para los sprites de incidentes
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private Image wallpaper;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    private double cameraX = 0;
    private double cameraY = 0;
    private double cameraSpeed = 5;

    private double wallpaperWidth;
    private double wallpaperHeight;

    private boolean W_PRESSED = false;
    private boolean A_PRESSED = false;
    private boolean S_PRESSED = false;
    private boolean D_PRESSED = false;


    private double scaleFactor = 1.0;
    private final double SCALE_STEP = 0.1;
    private double MIN_SCALE = 0.5;

    private Car1 car1;

    @FXML
    private Button muteButton;

    private ImageView gameMuteIcon;

    @FXML
    private Button gameManualButton;

    private final String UNMUTE_ICON_PATH = "/Icons/unmute_icon.png";
    private final String MUTE_ICON_PATH = "/Icons/mute_icon.png";

    // COLECCIÓN PARA LOS SPRITES DE INCIDENTES
    private HashMap<String, IncidentSprite> activeIncidentSprites; // Map<Incident ID, IncidentSprite>

    @FXML
    private void toggleMute() {
        boolean isMuted = !MenuController.isMuted();
        if (HelloApplication.getMediaPlayer() != null) {
            HelloApplication.getMediaPlayer().setMute(isMuted);
        }
        MenuController.setMuted(isMuted);
        updateGameMuteIcon();
        canvas.requestFocus();
    }

    @FXML
    private void showGameManual() {
        ManualViewer.toggleManualVisibility();
        canvas.requestFocus();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();

        wallpaper = new Image(getClass().getResourceAsStream("/Background/map.png"));
        if (wallpaper.isError()) {
            System.err.println("Error loading map.png: " + wallpaper.getException());
        }

        wallpaperWidth = wallpaper.getWidth();
        wallpaperHeight = wallpaper.getHeight();

        initEvents();

        // Inicializar el HashMap de sprites de incidentes
        activeIncidentSprites = new HashMap<>();

        // Registrar este GameController como listener del IncidentManager
        // Esto asume que IncidentManager tiene un mecanismo para notificar.
        // Si no lo tiene, lo añadiremos en IncidentManager.
        IncidentManager.getInstance().setGameController(this); // Necesitaremos un setter en IncidentManager

        Platform.runLater(() -> {
            if (canvas.getScene() != null) {
                Stage stage = (Stage) canvas.getScene().getWindow();
                if (!stage.isFullScreen()) {
                    stage.setFullScreen(true);
                }

                canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateMinScale());
                canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateMinScale());

                canvas.widthProperty().bind(canvas.getScene().widthProperty());
                canvas.heightProperty().bind(canvas.getScene().heightProperty());

                updateMinScale();
            }

            gameMuteIcon = new ImageView();
            gameMuteIcon.setFitHeight(26.0);
            gameMuteIcon.setFitWidth(33.0);
            gameMuteIcon.setPickOnBounds(true);
            gameMuteIcon.setPreserveRatio(true);

            if (muteButton != null) {
                muteButton.setGraphic(gameMuteIcon);
                muteButton.setStyle("-fx-background-color: transparent;");
            } else {
                System.err.println("Error: muteButton no fue inyectado. Asegúrate de que tu GameController-view.fxml tiene un Button con fx:id=\"muteButton\".");
            }
            updateGameMuteIcon();


            new Thread(() -> {
                while (true) {
                    try {
                        Platform.runLater(() -> {
                            updateCamera();
                            paintGame();
                        });
                        Thread.sleep(17);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Game loop interrupted: " + e.getMessage());
                        // Si tienes un SimulationManager, también lo detendrías aquí.
                        break;
                    }
                }
            }).start();
        });

        car1 = new Car1(canvas, 176, 580, 50, 50); // Puedes usar una coordenada de un nodo del grafo
        car1.start();
    }

    private void updateGameMuteIcon() {
        if (gameMuteIcon != null) {
            String iconPath = MenuController.isMuted() ? MUTE_ICON_PATH : UNMUTE_ICON_PATH;
            URL iconUrl = getClass().getResource(iconPath);

            if (iconUrl != null) {
                Image iconImage = new Image(iconUrl.toExternalForm());
                gameMuteIcon.setImage(iconImage);
            } else {
                System.err.println("Error: Icono de mute del juego no encontrado en " + iconPath + ". Revisa la ruta y la extensión.");
            }
        }
    }

    public void initEvents() {
        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> W_PRESSED = true;
                case A -> A_PRESSED = true;
                case S -> S_PRESSED = true;
                case D -> D_PRESSED = true;
                case F -> {
                    Stage stage = (Stage) canvas.getScene().getWindow();
                    if (!stage.isFullScreen()) {
                        stage.setFullScreen(true);
                    }
                }
                case Q -> {
                    // Si tienes un SceneController, así se cambiaría de escena
                    SceneController.getInstance().showIncidents();
                }

                default -> {}
            }
            canvas.requestFocus();
            event.consume();
        });

        canvas.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W -> W_PRESSED = false;
                case A -> A_PRESSED = false;
                case S -> S_PRESSED = false;
                case D -> D_PRESSED = false;
                default -> {}
            }
            event.consume();
        });

        canvas.setOnScroll((ScrollEvent event) -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            double mouseMapX = cameraX + mouseX / scaleFactor;
            double mouseMapY = cameraY + mouseY / scaleFactor;

            if (event.getDeltaY() > 0) {
                scaleFactor += SCALE_STEP;
            } else {
                scaleFactor -= SCALE_STEP;
            }

            scaleFactor = Math.max(MIN_SCALE, scaleFactor);

            double maxScaleX = wallpaperWidth / canvas.getWidth();
            double maxScaleY = wallpaperHeight / canvas.getHeight();
            double maxScale = Math.min(maxScaleX, maxScaleY);
            scaleFactor = Math.min(scaleFactor, maxScale * 2);

            cameraX = mouseMapX - (mouseX / scaleFactor);
            cameraY = mouseMapY - (mouseY / scaleFactor);

            constrainCamera();
            event.consume();
        });
    }

    private void updateMinScale() {
        if (canvas.getWidth() == 0 || canvas.getHeight() == 0) return;

        double scaleX = canvas.getWidth() / wallpaperWidth;
        double scaleY = canvas.getHeight() / wallpaperHeight;
        MIN_SCALE = Math.max(scaleX, scaleY);

        if (scaleFactor < MIN_SCALE) {
            scaleFactor = MIN_SCALE;
        }

        constrainCamera();
    }

    private void updateCamera() {
        double effectiveSpeed = cameraSpeed / scaleFactor;

        if (W_PRESSED) cameraY -= effectiveSpeed;
        if (S_PRESSED) cameraY += effectiveSpeed;
        if (A_PRESSED) cameraX -= effectiveSpeed;
        if (D_PRESSED) cameraX += effectiveSpeed;

        constrainCamera();
    }

    private void constrainCamera() {
        double visibleWidth = canvas.getWidth() / scaleFactor;
        double visibleHeight = canvas.getHeight() / scaleFactor;

        if (wallpaperWidth <= visibleWidth) {
            cameraX = (wallpaperWidth - visibleWidth) / 2;
        } else {
            cameraX = Math.max(0, Math.min(cameraX, wallpaperWidth - visibleWidth));
        }

        if (wallpaperHeight <= visibleHeight) {
            cameraY = (wallpaperHeight - visibleHeight) / 2;
        } else {
            cameraY = Math.max(0, Math.min(cameraY, wallpaperHeight - visibleHeight));
        }
    }

    private void paintGame() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double sourceX = cameraX;
        double sourceY = cameraY;
        double sourceWidth = canvas.getWidth() / scaleFactor;
        double sourceHeight = canvas.getHeight() / scaleFactor;

        gc.drawImage(wallpaper,
                sourceX, sourceY, sourceWidth, sourceHeight,
                0, 0, canvas.getWidth(), canvas.getHeight());

        gc.save();
        gc.scale(scaleFactor, scaleFactor);
        gc.translate(-cameraX, -cameraY);

        // Pinta el carro
        car1.paint();

        // Pinta todos los sprites de incidentes activos
        for (IncidentSprite sprite : activeIncidentSprites.values()) {
            sprite.paint(gc);
        }

        gc.restore();
    }

    // Métodos para que IncidentManager agregue/elimine incidentes
    public void addIncidentToDisplay(Incident incident) {
        IncidentSprite sprite = new IncidentSprite(incident);
        activeIncidentSprites.put(incident.getId(), sprite);
    }

    public void removeIncidentFromDisplay(String incidentId) {
        activeIncidentSprites.remove(incidentId);
    }

    // Puedes agregar un método para actualizar un incidente si cambia su estado
    public void updateIncidentDisplay(Incident incident) {
        IncidentSprite sprite = activeIncidentSprites.get(incident.getId());
        if (sprite != null) {
            sprite.setResolved(incident.isResolved());
            // Si el sprite cambia su apariencia al resolverse, se actualizará en el siguiente paint.
            // Si quieres que desaparezca inmediatamente, puedes llamar a removeIncidentFromDisplay aquí
            // si incident.isResolved() es true. Pero el loop de remoción en IncidentManager es mejor para eso.
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }
}