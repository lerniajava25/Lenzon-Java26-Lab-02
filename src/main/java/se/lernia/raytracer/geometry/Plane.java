package se.lernia.raytracer.geometry;

import java.util.Optional;
import se.lernia.raytracer.material.Material;
import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;

public class Plane extends AbstractShape {
    private final Vector3D pointOnPlane;
    private final Vector3D normal;

    public Plane(String name, Vector3D pointOnPlane, Vector3D normal, Material material) {
        super(name, material);
        this.pointOnPlane = pointOnPlane;
        this.normal = normal.normalize();
    }

    @Override
    public Optional<Hit> hit(Ray ray, double tMin, double tMax) {
        double denominator = normal.dot(ray.direction());
        if (Math.abs(denominator) < Vector3D.EPSILON) {
            return Optional.empty();
        }

        double t = pointOnPlane.subtract(ray.origin()).dot(normal) / denominator;
        if (t < tMin || t > tMax) {
            return Optional.empty();
        }

        return Optional.of(buildHit(ray, t, normal));
    }
}
