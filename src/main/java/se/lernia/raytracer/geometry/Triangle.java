package se.lernia.raytracer.geometry;

import java.util.Optional;
import se.lernia.raytracer.material.Material;
import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;

public class Triangle extends AbstractShape {
    private final Vector3D a;
    private final Vector3D b;
    private final Vector3D c;
    private final Vector3D normal;

    public Triangle(String name, Vector3D a, Vector3D b, Vector3D c, Material material) {
        super(name, material);
        this.a = a;
        this.b = b;
        this.c = c;
        this.normal = b.subtract(a).cross(c.subtract(a)).normalize();
    }

    // Möller-Trumbore. Ger avståndet t och de barycentriska koordinaterna u och v
    // i samma svep. Ligger u eller v utanför [0,1], eller u + v > 1, låg träffen
    // utanför triangeln.
    @Override
    public Optional<Hit> hit(Ray ray, double tMin, double tMax) {
        Vector3D edge1 = b.subtract(a);
        Vector3D edge2 = c.subtract(a);

        Vector3D pvec = ray.direction().cross(edge2);
        double determinant = edge1.dot(pvec);

        if (Math.abs(determinant) < Vector3D.EPSILON) {
            return Optional.empty();
        }

        double inverseDeterminant = 1.0 / determinant;
        Vector3D tvec = ray.origin().subtract(a);

        double u = tvec.dot(pvec) * inverseDeterminant;
        if (u < 0 || u > 1) {
            return Optional.empty();
        }

        Vector3D qvec = tvec.cross(edge1);
        double v = ray.direction().dot(qvec) * inverseDeterminant;
        if (v < 0 || u + v > 1) {
            return Optional.empty();
        }

        double t = edge2.dot(qvec) * inverseDeterminant;
        if (t < tMin || t > tMax) {
            return Optional.empty();
        }

        return Optional.of(buildHit(ray, t, normal));
    }
}
