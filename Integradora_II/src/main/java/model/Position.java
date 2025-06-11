package model;

/**
 * Representa una posición 2-D
 * (más adelante podremos extenderla a un sistema de coordenadas de mapa).
 */
public class Position {

    private final double x;
    private final double y;

    /**
     * @param x Coordenada horizontal.
     * @param y Coordenada vertical.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Distancia euclídea a otra posición.
     */
    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.hypot(dx, dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // Getters al final (regla de orden del usuario)

    public double getX() { return x; }
    public double getY() { return y; }
}
