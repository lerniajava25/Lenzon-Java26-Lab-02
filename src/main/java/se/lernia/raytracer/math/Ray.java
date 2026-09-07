package se.lernia.raytracer.math;

public record Ray(Vector3D origin, Vector3D direction) {
    // Riktningen normaliseras alltid. Då blir t i pointAt samma sak som avståndet.
    public Ray {
        direction = direction.normalize();
    }

    public Vector3D pointAt(double t) {
        return origin.add(direction.scale(t));
    }

    @Override
    public String toString() {
        return "Ray från " + origin + " mot " + direction;
    }
}
