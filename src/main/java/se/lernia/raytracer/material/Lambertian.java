package se.lernia.raytracer.material;

import se.lernia.raytracer.image.Color;
import se.lernia.raytracer.light.Lighting;
import se.lernia.raytracer.light.PointLight;
import se.lernia.raytracer.math.Vector3D;

// Matt yta enligt Lamberts cosinuslag: ljuset beror bara på vinkeln mellan
// normalen och riktningen mot lampan, och den vinkeln ger skalärprodukten.
public class Lambertian extends AbstractMaterial {
    public Lambertian(Color albedo) {
        super(albedo);
    }

    @Override
    public Color shade(SurfacePoint surface, Lighting lighting) {
        Vector3D point = surface.point();
        Vector3D normal = surface.normal();

        Color total = albedo().multiply(lighting.ambient());

        for (PointLight light : lighting.lights()) {
            Vector3D toLight = light.position().subtract(point);
            double distance = toLight.length();
            if (distance < Vector3D.EPSILON) {
                continue;
            }
            Vector3D lightDirection = toLight.scale(1.0 / distance);

            double cosAngle = normal.dot(lightDirection);
            if (cosAngle <= 0) {
                continue;
            }
            if (lighting.isInShadow(point, light)) {
                continue;
            }

            total = total.add(albedo().multiply(light.contributionAt(distance).scale(cosAngle)));
        }

        return total;
    }
}
