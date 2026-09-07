package se.lernia.raytracer.light;

import java.util.List;
import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.math.Vector3D;

public interface Lighting {
    List<PointLight> lights();

    Color ambient();

    boolean isInShadow(Vector3D point, PointLight light);
}
