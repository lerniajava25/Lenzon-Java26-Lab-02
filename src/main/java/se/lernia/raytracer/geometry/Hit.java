package se.lernia.raytracer.geometry;

import se.lernia.raytracer.math.Vector3D;

public record Hit(double t, Vector3D point, Vector3D normal, Shape shape) {
}
