package se.lernia.raytracer.material;

import se.lernia.raytracer.math.Vector3D;

public record SurfacePoint(Vector3D point, Vector3D normal, Vector3D viewDirection) {
}
