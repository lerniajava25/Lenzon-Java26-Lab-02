package se.lernia.raytracer.material;

import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.light.Lighting;

public class SolidColor extends AbstractMaterial {
    public SolidColor(Color albedo) {
        super(albedo);
    }

    @Override
    public Color shade(SurfacePoint surface, Lighting lighting) {
        return albedo();
    }
}
