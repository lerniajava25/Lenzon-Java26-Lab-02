package se.lernia.raytracer.geometry;

import java.util.Optional;
import se.lernia.raytracer.material.Material;
import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;

public class Sphere extends AbstractShape {
    private final Vector3D center;
    private final double radius;

    public Sphere(String name, Vector3D center, double radius, Material material) {
        super(name, material);
        if (radius <= 0) {
            throw new IllegalArgumentException("Radien måste vara positiv, fick " + radius);
        }
        this.center = center;
        this.radius = radius;
    }

    public Vector3D center() {
        return center;
    }

    public double radius() {
        return radius;
    }

    // Andragradsekvation. Riktningen är normaliserad, så a = 1 och ekvationen
    // blir t^2 + 2*h*t + c = 0. Diskriminanten avgör om strålen träffar.
    @Override
    public Optional<Hit> hit(Ray ray, double tMin, double tMax) {
        Vector3D originToCenter = ray.origin().subtract(center);

        double h = originToCenter.dot(ray.direction());
        double c = originToCenter.lengthSquared() - radius * radius;
        double discriminant = h * h - c;

        if (discriminant < 0) {
            return Optional.empty();
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);

        double t = -h - sqrtDiscriminant;
        if (t < tMin || t > tMax) {
            t = -h + sqrtDiscriminant;
            if (t < tMin || t > tMax) {
                return Optional.empty();
            }
        }

        Vector3D outwardNormal = ray.pointAt(t).subtract(center).scale(1.0 / radius);
        return Optional.of(buildHit(ray, t, outwardNormal));
    }
}
