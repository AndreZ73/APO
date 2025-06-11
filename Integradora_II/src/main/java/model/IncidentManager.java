package model;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.icesi.integradora_ii.GameController; // Importar GameController
import structures.BST;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID; // Para generar IDs únicos
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Gestiona la colección de incidentes y genera nuevos periódicamente.
 * Es singleton porque debe existir una única “base de datos” de incidentes.
 */
public class IncidentManager {

    private static final IncidentManager INSTANCE = new IncidentManager();

    /** Árbol con los incidentes. */
    private final BST<Incident> tree = new BST<>();

    /** Propiedad observable con el conteo total. */
    private final IntegerProperty totalIncidents = new SimpleIntegerProperty(0);

    /** Generador de números aleatorios seguro. */
    private final Random rnd = new SecureRandom();

    /** Executor que dispara el generador. */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Referencia al GameController para notificar cambios visuales
    private GameController gameController;

    private IncidentManager() {
        // Arranca generación automática cada 5 segundos
        scheduler.scheduleAtFixedRate(this::generateRandomIncident,
                0, 5, TimeUnit.SECONDS);

        // Agregamos un scheduler para limpiar incidentes resueltos
        scheduler.scheduleAtFixedRate(this::cleanResolvedIncidents,
                10, 5, TimeUnit.SECONDS); // Limpia cada 5 segundos, empezando a los 10
    }

    public static IncidentManager getInstance() {
        return INSTANCE;
    }

    // Setter para el GameController (llamado desde GameController.initialize())
    public void setGameController(GameController gc) {
        this.gameController = gc;
    }

    //----------------------------------------------------------------------------------------------
    // API pública
    //----------------------------------------------------------------------------------------------

    /**
     * Inserta un incidente manualmente (por ejemplo, desde la IU).
     * @return true si el incidente fue agregado (era nuevo), false si ya existía.
     */
    public boolean addIncident(Incident inc) {
        // El método insert de tu BST debe devolver true si el elemento fue insertado, false si ya existía
        if (tree.insert(inc)) { // Asumiendo que BST.insert devuelve boolean
            Platform.runLater(() -> {
                totalIncidents.set(totalIncidents.get() + 1);
                if (gameController != null) {
                    gameController.addIncidentToDisplay(inc); // Notifica al GameController
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Marca un incidente como resuelto y lo notifica al GameController.
     * @param incident El incidente a resolver.
     */
    public void resolveIncident(Incident incident) {
        if (incident != null && !incident.isResolved()) {
            incident.setResolved(true);
            // Si el incidente ya está resuelto, notifica al GameController para actualizar su display
            Platform.runLater(() -> {
                if (gameController != null) {
                    gameController.updateIncidentDisplay(incident);
                }
            });
            // La remoción del árbol se hace en cleanResolvedIncidents
        }
    }


    public BST<Incident> getTree() {
        return tree;
    }

    public IntegerProperty totalIncidentsProperty() {
        return totalIncidents;
    }

    //----------------------------------------------------------------------------------------------
    // Generador aleatorio
    //----------------------------------------------------------------------------------------------

    private void generateRandomIncident() {
        // Para que se muestre en el mapa, las coordenadas deben estar dentro de los límites del mapa.
        // Las coordenadas de tu map.png son aproximadamente 960x640 (ancho x alto).
        double mapWidth = 960;
        double mapHeight = 640;

        String id = UUID.randomUUID().toString(); // Genera un ID único
        double x = rnd.nextDouble() * mapWidth;
        double y = rnd.nextDouble() * mapHeight;
        int severity = rnd.nextInt(10) + 1; // 1-10
        IncidentType type = IncidentType.values()[rnd.nextInt(IncidentType.values().length)];

        addIncident(new Incident(id, x, y, severity, type));
    }

    /**
     * Recorre el árbol y elimina los incidentes que están marcados como resueltos.
     */
    private void cleanResolvedIncidents() {
        // Obtenemos una lista de los incidentes para evitar ConcurrentModificationException
        // mientras iteramos y modificamos el árbol.
        // Asumiendo que tu BST tiene un método inOrderTraversal que devuelve un ArrayList.
        for (Incident inc : tree.inOrderTraversal()) { // Necesitas que inOrderTraversal funcione bien.
            if (inc.isResolved()) {
                // Eliminar del árbol. Asegúrate de que el método delete de tu BST funciona.
                if (tree.delete(inc)) { // Asumiendo que BST.delete devuelve boolean
                    Platform.runLater(() -> {
                        totalIncidents.set(totalIncidents.get() - 1);
                        if (gameController != null) {
                            gameController.removeIncidentFromDisplay(inc.getId()); // Notifica al GameController
                        }
                    });
                }
            }
        }
    }
}