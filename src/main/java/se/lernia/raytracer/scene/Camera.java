package se.lernia.raytracer.scene;

import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;

public class Camera {
    private final Vector3D position;
    private final Vector3D lowerLeftCorner;
    private final Vector3D horizontal;
    private final Vector3D vertical;

    public Camera(Vector3D position, Vector3D lookAt, Vector3D up,
                  double verticalFovDegrees, double aspectRatio) {
        if (verticalFovDegrees <= 0 || verticalFovDegrees >= 180) {
            throw new IllegalArgumentException("Synfältet måste ligga mellan 0 och 180 grader, fick " + verticalFovDegrees);
        }
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException("Bildförhållandet måste vara positivt, fick " + aspectRatio);
        }

        double viewportHeight = 2 * Math.tan(Math.toRadians(verticalFovDegrees) / 2);
        double viewportWidth = aspectRatio * viewportHeight;

        Vector3D w = position.subtract(lookAt).normalize();
        Vector3D u = up.cross(w).normalize();
        Vector3D v = w.cross(u);

        this.position = position;
        this.horizontal = u.scale(viewportWidth);
        this.vertical = v.scale(viewportHeight);
        this.lowerLeftCorner = position
                .subtract(horizontal.scale(0.5))
                .subtract(vertical.scale(0.5))
                .subtract(w);
    }

    public Ray rayThrough(double s, double t) {
        Vector3D target = lowerLeftCorner
                .add(horizontal.scale(s))
                .add(vertical.scale(t));
        return new Ray(position, target.subtract(position));
    }

    public Vector3D position() {
        return position;
    }
}
