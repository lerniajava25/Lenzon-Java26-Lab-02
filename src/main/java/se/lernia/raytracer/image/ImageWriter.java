package se.lernia.raytracer.image;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageWriter {
    String extension();

    void write(Image image, Path path) throws IOException;
}
