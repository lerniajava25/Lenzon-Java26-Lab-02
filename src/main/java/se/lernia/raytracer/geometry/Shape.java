package se.lernia.raytracer.geometry;

import java.util.Optional;
import se.lernia.raytracer.material.Material;
import se.lernia.raytracer.math.Ray;

public interface Shape {
    Optional<Hit> hit(Ray ray, double tMin, double tMax);

    default Optional<Hit> hit(Ray ray) {
        return hit(ray, 1e-4, Double.POSITIVE_INFINITY);
    }

    Material material();

    String name();
}
