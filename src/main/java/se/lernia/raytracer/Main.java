package se.lernia.raytracer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import se.lernia.raytracer.geometry.Plane;
import se.lernia.raytracer.geometry.Shape;
import se.lernia.raytracer.geometry.Sphere;
import se.lernia.raytracer.geometry.Triangle;
import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.image.Image;
import se.lernia.raytracer.image.ImageWriter;
import se.lernia.raytracer.image.PngWriter;
import se.lernia.raytracer.image.PpmWriter;
import se.lernia.raytracer.light.PointLight;
import se.lernia.raytracer.material.Lambertian;
import se.lernia.raytracer.material.SolidColor;
import se.lernia.raytracer.math.Vector3D;
import se.lernia.raytracer.scene.Camera;
import se.lernia.raytracer.scene.Renderer;
import se.lernia.raytracer.scene.Scene;

public final class Main {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 450;
    private static final int DEFAULT_SAMPLES_PER_PIXEL = 16;
    private static final Path OUTPUT_DIRECTORY = Path.of("output");

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        int samplesPerPixel = readSamples(args);

        Scene scene = buildScene();
        Camera camera = new Camera(
                new Vector3D(0, 1.2, 4.0),
                new Vector3D(0, 0.65, 0),
                new Vector3D(0, 1, 0),
                45,
                (double) WIDTH / HEIGHT);

        System.out.println(scene);
        for (Shape shape : scene.shapes()) {
            System.out.println("  - " + shape);
        }
        System.out.println("Renderar " + WIDTH + "x" + HEIGHT
                + " med " + samplesPerPixel + " strålar per pixel...");

        long start = System.nanoTime();
        Image image = new Renderer(WIDTH, HEIGHT, samplesPerPixel).render(scene, camera);
        long milliseconds = (System.nanoTime() - start) / 1_000_000;

        Files.createDirectories(OUTPUT_DIRECTORY);
        for (ImageWriter writer : List.of(new PpmWriter(), new PngWriter())) {
            Path file = OUTPUT_DIRECTORY.resolve("render." + writer.extension());
            writer.write(image, file);
            System.out.println("Sparade " + file.toAbsolutePath());
        }

        System.out.println("Klart på " + milliseconds + " ms.");
    }

    private static Scene buildScene() {
        Lambertian floor = new Lambertian(new Color(0.55, 0.58, 0.52));
        Lambertian red = new Lambertian(new Color(0.80, 0.25, 0.22));
        Lambertian teal = new Lambertian(new Color(0.20, 0.55, 0.60));
        Lambertian purple = new Lambertian(new Color(0.45, 0.30, 0.65));
        SolidColor flatYellow = new SolidColor(new Color(0.90, 0.75, 0.20));

        return new Scene()
                .ambient(new Color(0.14, 0.15, 0.17))
                .background(new Color(1.0, 1.0, 1.0), new Color(0.35, 0.55, 0.95))

                .add(new Plane("Golv", new Vector3D(0, 0, 0), new Vector3D(0, 1, 0), floor))

                .add(new Sphere("Sfär vänster (Lambertian)", new Vector3D(-1.30, 0.70, -0.40), 0.70, red))
                .add(new Sphere("Sfär mitten (Lambertian)", new Vector3D(0.20, 0.55, 0.60), 0.55, teal))
                .add(new Sphere("Sfär höger (SolidColor)", new Vector3D(1.50, 0.60, -0.20), 0.60, flatYellow))

                .add(new Triangle("Triangel bakom",
                        new Vector3D(-1.50, 0.01, -2.60),
                        new Vector3D(1.50, 0.01, -2.60),
                        new Vector3D(0.00, 2.60, -2.60),
                        purple))

                .add(new PointLight(new Vector3D(6.0, 8.0, 6.0), Color.WHITE, 130))
                .add(new PointLight(new Vector3D(-5.0, 4.0, 5.0), new Color(0.75, 0.82, 1.0), 35));
    }

    private static int readSamples(String[] args) {
        if (args.length == 0) {
            return DEFAULT_SAMPLES_PER_PIXEL;
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("'" + args[0] + "' är inget heltal, kör med "
                    + DEFAULT_SAMPLES_PER_PIXEL + " strålar per pixel istället.");
            return DEFAULT_SAMPLES_PER_PIXEL;
        }
    }
}
