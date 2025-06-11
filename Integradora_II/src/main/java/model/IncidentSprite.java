package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.Objects;

public class IncidentSprite {
    private String incidentId;
    private double x;
    private double y;
    private Image spriteImage;
    private double width;
    private double height;
    private boolean isResolved;
    private IncidentType incidentType; // Store the incident type as a field

    public IncidentSprite(Incident incident) {
        this.incidentId = incident.getId();
        this.x = incident.getX();
        this.y = incident.getY();
        this.isResolved = incident.isResolved();
        this.incidentType = incident.getType(); // Store the type

        // 1. Cargar la imagen según el tipo de incidente
        String imagePath = getImagePathForIncidentType(incident.getType());
        try {
            // Only try to load the image if imagePath is not null
            if (imagePath != null) {
                this.spriteImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                this.width = 30; // Puedes ajustar el tamaño base si quieres
                this.height = 30;
            } else {
                this.spriteImage = null; // No image path provided, so no image
                this.width = 20; // Default size if no image
                this.height = 20;
            }
        } catch (Exception e) {
            System.err.println("Error loading incident sprite image for type " + incident.getType() + " at " + imagePath + ": " + e.getMessage() + ". Using default shape.");
            this.spriteImage = null; // En caso de error, no usar imagen
            this.width = 20; // Tamaño por defecto si no hay imagen
            this.height = 20;
        }
    }

    // Método auxiliar para obtener la ruta de la imagen basada en el tipo de incidente
    private String getImagePathForIncidentType(IncidentType type) {
        return switch (type) {
            case FIRE -> "/Fire/Fire/Fire1.png";
            // For ACCIDENT and ROBBERY, return null or specific paths if you have them
            case ACCIDENT, ROBBERY -> null; // Explicitly returning null if no specific image
            default -> null; // Handle any other future types explicitly or return null
        };
    }

    public void paint(GraphicsContext gc) {
        if (isResolved) {
            return; // No dibujar incidentes resueltos
        }

        if (spriteImage != null) {
            gc.drawImage(spriteImage, x - width / 2, y - height / 2, width, height);
        } else {
            // Dibujar una forma por defecto si la imagen no carga o no existe
            Color fillColor = Color.RED; // Por defecto
            switch (this.incidentType) { // Use the stored incidentType field
                case ACCIDENT:
                    fillColor = Color.ORANGE;
                    break;
                case FIRE:
                    fillColor = Color.DARKRED;
                    break;
                case ROBBERY: // Add a case for ROBBERY if you want a specific color
                    fillColor = Color.BLUE; // Example color
                    break;
                default:
                    // Keep default RED if no specific type is matched
                    break;
            }
            gc.setFill(fillColor);
            gc.fillOval(x - width / 2, y - height / 2, width, height);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeOval(x - width / 2, y - height / 2, width, height);
        }
    }

    // Getters
    public String getIncidentId() {
        return incidentId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isResolved() {
        return isResolved;
    }

    // Setter para actualizar el estado
    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}