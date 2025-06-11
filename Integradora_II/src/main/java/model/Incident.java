package model;

/**
 * Modelo de un incidente en la ciudad.
 */
public class Incident implements Comparable<Incident> {

    // Nuevos atributos para el mapa y el estado
    private String id; // Para identificarlo unívocamente, útil para eliminar de HashMap
    private double x;  // Coordenada X para el mapa
    private double y;  // Coordenada Y para el mapa
    private boolean resolved; // Estado del incidente

    private final int severity;          // 1–10 (10 es más grave)
    private final IncidentType type;
    private final Position position; // Esto ahora es redundante si usas x,y directamente, pero lo mantengo si lo necesitas para algo más.

    /**
     * Crea un nuevo incidente.
     *
     * @param id        ID único del incidente.
     * @param x         Coordenada X en el mapa.
     * @param y         Coordenada Y en el mapa.
     * @param severity  Gravedad 1–10.
     * @param type      Tipo de incidente.
     */
    public Incident(String id, double x, double y, int severity, IncidentType type) {
        if (severity < 1 || severity > 10)
            throw new IllegalArgumentException("severity fuera de rango [1,10]");
        this.id = id;
        this.x = x;
        this.y = y;
        this.severity = severity;
        this.type = type;
        this.resolved = false; // Por defecto, un nuevo incidente no está resuelto
        this.position = new Position(x, y); // Mantengo Position si lo usas en otros sitios, sino lo puedes quitar.
    }

    //----------------------------------------------------------------------------------------------
    // Comparable
    //----------------------------------------------------------------------------------------------

    /**
     * Orden natural: primero mayor gravedad,
     * luego por tipo (orden enum) y finalmente por hash de posición
     * para garantizar unicidad en el árbol.
     */
    @Override
    public int compareTo(Incident o) {
        int diff = Integer.compare(o.severity, this.severity); // mayor primero (orden descendente por gravedad)
        if (diff != 0) return diff;

        diff = this.type.compareTo(o.type); // Luego por tipo
        if (diff != 0) return diff;

        // Si la gravedad y el tipo son iguales, usa el ID para una ordenación consistente y única en el BST.
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return type + ":" + severity + "@" + position + (resolved ? " (Resuelto)" : "");
    }

    // Getters
    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isResolved() { return resolved; }
    public int getSeverity() { return severity; }
    public IncidentType getType() { return type; }
    public Position getPosition() { return position; } // Puede que ya no necesites esto si usas x,y

    // Setter para marcar como resuelto
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return id.equals(incident.id); // La igualdad se basa en el ID
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}