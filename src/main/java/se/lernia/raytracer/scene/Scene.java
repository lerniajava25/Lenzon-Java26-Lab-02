package se.lernia.raytracer.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import se.lernia.raytracer.geometry.Hit;
import se.lernia.raytracer.geometry.Shape;
import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.light.Lighting;
import se.lernia.raytracer.light.PointLight;
import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;

public class Scene implements Lighting {
    // Skuggstrålen startar en bit från ytan, annars träffar den ytan själv
    // och bilden blir prickig.
    private static final double SHADOW_BIAS = 1e-3;

    private final List<Shape> shapes = new ArrayList<>();
    private final List<PointLight> lights = new ArrayList<>();

    private Color ambient = new Color(0.05, 0.05, 0.06);
    private Color horizonColor = Color.WHITE;
    private Color zenithColor = new Color(0.35, 0.55, 0.95);

    public Scene add(Shape shape) {
        shapes.add(Objects.requireNonNull(shape));
        return this;
    }

    public Scene add(PointLight light) {
        lights.add(Objects.requireNonNull(light));
        return this;
    }

    public Scene ambient(Color color) {
        this.ambient = Objects.requireNonNull(color);
        return this;
    }

    public Scene background(Color horizon, Color zenith) {
        this.horizonColor = Objects.requireNonNull(horizon);
        this.zenithColor = Objects.requireNonNull(zenith);
        return this;
    }

    public List<Shape> shapes() {
        return Collections.unmodifiableList(shapes);
    }

    @Override
    public List<PointLight> lights() {
        return Collections.unmodifiableList(lights);
    }

    @Override
    public Color ambient() {
        return ambient;
    }

    public Optional<Hit> closestHit(Ray ray, double tMin, double tMax) {
        Hit closest = null;
        double nearest = tMax;

        for (Shape shape : shapes) {
            Optional<Hit> hit = shape.hit(ray, tMin, nearest);
            if (hit.isPresent()) {
                closest = hit.get();
                nearest = closest.t();
            }
        }

        return Optional.ofNullable(closest);
    }

    @Override
    public boolean isInShadow(Vector3D point, PointLight light) {
        Vector3D toLight = light.position().subtract(point);
        double distance = toLight.length();
        if (distance < SHADOW_BIAS) {
            return false;
        }

        // tMax = avståndet till lampan, så att objekt bakom lampan inte skuggar.
        Ray shadowRay = new Ray(point, toLight);
        return closestHit(shadowRay, SHADOW_BIAS, distance - SHADOW_BIAS).isPresent();
    }

    public Color background(Ray ray) {
        double t = 0.5 * (ray.direction().y() + 1.0);
        return horizonColor.lerp(zenithColor, t);
    }

    @Override
    public String toString() {
        return "Scene med " + shapes.size() + " objekt och " + lights.size() + " ljuskällor";
    }
}
