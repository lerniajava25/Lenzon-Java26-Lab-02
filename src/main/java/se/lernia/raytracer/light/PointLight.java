package se.lernia.raytracer.light;

import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.math.Vector3D;

public record PointLight(Vector3D position, Color color, double intensity) {
    public PointLight {
        if (intensity <= 0) {
            throw new IllegalArgumentException("Ljusstyrkan måste vara positiv, fick " + intensity);
        }
    }

    public Color contributionAt(double distance) {
        double falloff = intensity / Math.max(distance * distance, 1e-6);
        return color.scale(falloff);
    }

    @Override
    public String toString() {
        return "PointLight " + color + " i " + position;
    }
}
