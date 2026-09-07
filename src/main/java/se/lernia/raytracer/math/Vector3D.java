package se.lernia.raytracer.math;

public record Vector3D(double x, double y, double z) {
    public static final Vector3D ZERO = new Vector3D(0, 0, 0);

    public static final double EPSILON = 1e-9;

    public Vector3D add(Vector3D other) {
        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }

    public Vector3D scale(double factor) {
        return new Vector3D(x * factor, y * factor, z * factor);
    }

    public Vector3D negate() {
        return new Vector3D(-x, -y, -z);
    }

    public double dot(Vector3D other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3D cross(Vector3D other) {
        return new Vector3D(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector3D normalize() {
        double length = length();
        if (length < EPSILON) {
            throw new IllegalArgumentException("Nollvektorn har ingen riktning och går inte att normalisera");
        }
        return scale(1.0 / length);
    }

    public Vector3D reflect(Vector3D normal) {
        return subtract(normal.scale(2 * dot(normal)));
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f)", x, y, z);
    }
}
