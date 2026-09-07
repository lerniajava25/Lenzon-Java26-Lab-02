package se.lernia.raytracer.image;

import java.util.Arrays;

public class Image {
    private final int width;
    private final int height;
    private final Color[] pixels;

    public Image(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Bilden måste vara minst 1x1, fick " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.pixels = new Color[width * height];
        Arrays.fill(this.pixels, Color.BLACK);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Color getPixel(int x, int y) {
        checkBounds(x, y);
        return pixels[y * width + x];
    }

    public void setPixel(int x, int y, Color color) {
        checkBounds(x, y);
        pixels[y * width + x] = color;
    }

    private void checkBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException(
                    "Pixel (" + x + ", " + y + ") ligger utanför bilden " + width + "x" + height);
        }
    }

    @Override
    public String toString() {
        return "Image " + width + "x" + height;
    }
}
