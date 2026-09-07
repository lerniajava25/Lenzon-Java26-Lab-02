package se.lernia.raytracer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import se.lernia.raytracer.geometry.Hit;
import se.lernia.raytracer.geometry.Sphere;
import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.light.PointLight;
import se.lernia.raytracer.material.SolidColor;
import se.lernia.raytracer.math.Ray;
import se.lernia.raytracer.math.Vector3D;
import se.lernia.raytracer.scene.Scene;

class RaytracerTest {

    private static final double DELTA = 1e-9;
    private static final SolidColor VIT = new SolidColor(Color.WHITE);

    @Test
    void sfarenTraffasPaRattAvstand() {
        Sphere sfar = new Sphere("test", new Vector3D(0, 0, -5), 1, VIT);

        Hit hit = sfar.hit(new Ray(Vector3D.ZERO, new Vector3D(0, 0, -1))).orElseThrow();

        assertEquals(4, hit.t(), DELTA);
        assertEquals(new Vector3D(0, 0, -4), hit.point());
        assertEquals(new Vector3D(0, 0, 1), hit.normal());
    }

    @Test
    void straleAtFelHallMissar() {
        Sphere sfar = new Sphere("test", new Vector3D(0, 0, -5), 1, VIT);

        assertTrue(sfar.hit(new Ray(Vector3D.ZERO, new Vector3D(0, 1, 0))).isEmpty());
    }

    @Test
    void narmasteObjektetVinner() {
        Sphere nara = new Sphere("nara", new Vector3D(0, 0, -2), 0.5, VIT);
        Sphere langtBort = new Sphere("langt bort", new Vector3D(0, 0, -5), 0.5, VIT);
        Scene scene = new Scene().add(langtBort).add(nara);

        Hit hit = scene.closestHit(new Ray(Vector3D.ZERO, new Vector3D(0, 0, -1)),
                1e-4, Double.POSITIVE_INFINITY).orElseThrow();

        assertSame(nara, hit.shape());
        assertEquals(1.5, hit.t(), DELTA);
    }

    @Test
    void objektMellanPunktenOchLampanGerSkugga() {
        Scene scene = new Scene().add(new Sphere("blockerare", new Vector3D(0, 2, 0), 0.5, VIT));

        assertTrue(scene.isInShadow(Vector3D.ZERO,
                new PointLight(new Vector3D(0, 5, 0), Color.WHITE, 10)));
    }

    @Test
    void objektBakomLampanSkuggarInte() {
        Scene scene = new Scene().add(new Sphere("bakom lampan", new Vector3D(0, 8, 0), 0.5, VIT));

        assertFalse(scene.isInShadow(Vector3D.ZERO,
                new PointLight(new Vector3D(0, 5, 0), Color.WHITE, 10)));
    }
}
