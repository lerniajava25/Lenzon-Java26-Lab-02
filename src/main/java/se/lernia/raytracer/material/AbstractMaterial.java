package se.lernia.raytracer.material;

import java.util.Objects;
import se.lernia.raytracer.image.Color;

public abstract class AbstractMaterial implements Material {
    private final Color albedo;

    protected AbstractMaterial(Color albedo) {
        this.albedo = Objects.requireNonNull(albedo, "albedo får inte vara null");
    }

    public Color albedo() {
        return albedo;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + albedo;
    }
}
