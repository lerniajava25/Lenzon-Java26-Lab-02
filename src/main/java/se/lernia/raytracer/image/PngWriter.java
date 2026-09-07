package se.lernia.raytracer.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class PngWriter implements ImageWriter {
    @Override
    public String extension() {
        return "png";
    }

    @Override
    public void write(Image image, Path path) throws IOException {
        BufferedImage buffer = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                int[] rgb = image.getPixel(x, y).toRgb255();
                buffer.setRGB(x, y, (rgb[0] << 16) | (rgb[1] << 8) | rgb[2]);
            }
        }

        if (!ImageIO.write(buffer, "png", path.toFile())) {
            throw new IOException("Hittade ingen PNG-skrivare i den här JDK:n");
        }
    }
}
