package se.lernia.raytracer.material;

import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.light.Lighting;

public interface Material {
    Color shade(SurfacePoint surface, Lighting lighting);
}
