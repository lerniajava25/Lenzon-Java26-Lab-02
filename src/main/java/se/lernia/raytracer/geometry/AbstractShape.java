package se.lernia.raytracer.geometry;

import java.util.Objects;
import se.lernia.raytracer.material.Material;
import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;

public abstract class AbstractShape implements Shape {
    private final String name;
    private final Material material;

    protected AbstractShape(String name, Material material) {
        this.name = Objects.requireNonNull(name, "namn får inte vara null");
        this.material = Objects.requireNonNull(material, "material får inte vara null");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Material material() {
        return material;
    }

    // Vänder normalen så den alltid vetter mot strålen. Utan det blir baksidor
    // av plan och trianglar svarta, för då pekar normalen bort från ljuset.
    protected Hit buildHit(Ray ray, double t, Vector3D outwardNormal) {
        Vector3D normal = outwardNormal.dot(ray.direction()) > 0 ? outwardNormal.negate() : outwardNormal;
        return new Hit(t, ray.pointAt(t), normal, this);
    }

    @Override
    public String toString() {
        return name + " [" + material + "]";
    }
}
