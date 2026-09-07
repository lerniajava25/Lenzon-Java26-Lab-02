package se.lernia.raytracer.scene;

import java.util.Optional;
import java.util.Random;
import se.lernia.raytracer.geometry.Hit;
import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.image.Image;
import se.lernia.raytracer.material.SurfacePoint;
import se.lernia.raytracer.math.Ray;

public class Renderer {
    private static final double MIN_DISTANCE = 1e-4;

    private final int width;
    private final int height;
    private final int samplesPerPixel;
    private final Random random;

    public Renderer(int width, int height, int samplesPerPixel) {
        this(width, height, samplesPerPixel, 42L);
    }

    public Renderer(int width, int height, int samplesPerPixel, long seed) {
        if (samplesPerPixel < 1) {
            throw new IllegalArgumentException("Antal strålar per pixel måste vara minst 1, fick " + samplesPerPixel);
        }
        this.width = width;
        this.height = height;
        this.samplesPerPixel = samplesPerPixel;
        this.random = new Random(seed);
    }

    public int samplesPerPixel() {
        return samplesPerPixel;
    }

    public Image render(Scene scene, Camera camera) {
        Image image = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color sum = Color.BLACK;

                // Antialiasing: flera strålar per pixel med slumpat läge inom pixeln,
                // medelvärdet blir pixelns färg. Slumpen är seedad, så samma
                // inställningar ger alltid exakt samma bild.
                for (int sample = 0; sample < samplesPerPixel; sample++) {
                    double offsetX = samplesPerPixel == 1 ? 0.5 : random.nextDouble();
                    double offsetY = samplesPerPixel == 1 ? 0.5 : random.nextDouble();

                    double s = (x + offsetX) / width;
                    double t = (height - 1 - y + offsetY) / height;

                    sum = sum.add(trace(camera.rayThrough(s, t), scene));
                }

                image.setPixel(x, y, sum.scale(1.0 / samplesPerPixel).clamped());
            }
            reportProgress(y);
        }

        return image;
    }

    private Color trace(Ray ray, Scene scene) {
        Optional<Hit> hit = scene.closestHit(ray, MIN_DISTANCE, Double.POSITIVE_INFINITY);
        if (hit.isEmpty()) {
            return scene.background(ray);
        }

        Hit h = hit.get();
        SurfacePoint surface = new SurfacePoint(h.point(), h.normal(), ray.direction().negate());
        return h.shape().material().shade(surface, scene);
    }

    private void reportProgress(int completedRow) {
        int step = Math.max(height / 10, 1);
        if ((completedRow + 1) % step == 0 || completedRow == height - 1) {
            System.out.println("  " + Math.round(100.0 * (completedRow + 1) / height) + "%");
        }
    }
}
