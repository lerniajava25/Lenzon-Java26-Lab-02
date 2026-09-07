package se.lernia.raytracer.image;

public record Color(double r, double g, double b) {
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    // Skärmar visar inte ljus linjärt. Utan gammakorrigering blir bilden för mörk.
    private static final double GAMMA = 2.2;

    public static Color of(int red, int green, int blue) {
        return new Color(red / 255.0, green / 255.0, blue / 255.0);
    }

    public Color add(Color other) {
        return new Color(r + other.r, g + other.g, b + other.b);
    }

    public Color scale(double factor) {
        return new Color(r * factor, g * factor, b * factor);
    }

    public Color multiply(Color other) {
        return new Color(r * other.r, g * other.g, b * other.b);
    }

    public Color lerp(Color other, double t) {
        return scale(1 - t).add(other.scale(t));
    }

    public Color clamped() {
        return new Color(clamp(r), clamp(g), clamp(b));
    }

    public int[] toRgb255() {
        return new int[] {channelToByte(r), channelToByte(g), channelToByte(b)};
    }

    private static int channelToByte(double channel) {
        double corrected = Math.pow(clamp(channel), 1.0 / GAMMA);
        return (int) Math.round(corrected * 255);
    }

    private static double clamp(double value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, 1);
    }

    @Override
    public String toString() {
        int[] rgb = toRgb255();
        return String.format("Color(%d, %d, %d)", rgb[0], rgb[1], rgb[2]);
    }
}
