package se.lernia.raytracer.image;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PpmWriter implements ImageWriter {
    private static final int MAX_CHANNEL_VALUE = 255;

    @Override
    public String extension() {
        return "ppm";
    }

    @Override
    public void write(Image image, Path path) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.US_ASCII)) {
            out.write("P3\n");
            out.write(image.width() + " " + image.height() + "\n");
            out.write(MAX_CHANNEL_VALUE + "\n");

            for (int y = 0; y < image.height(); y++) {
                StringBuilder row = new StringBuilder();
                for (int x = 0; x < image.width(); x++) {
                    int[] rgb = image.getPixel(x, y).toRgb255();
                    row.append(rgb[0]).append(' ').append(rgb[1]).append(' ').append(rgb[2]);
                    row.append(x == image.width() - 1 ? '\n' : ' ');
                }
                out.write(row.toString());
            }
        }
    }
}
