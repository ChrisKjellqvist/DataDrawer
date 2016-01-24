/**
 * Created by chris on 12/5/15.
 * HSB stands for Hue, Saturation, and Brightness. I use the formula
 * I found online to compute between RGB and HSB.
 */
public class HSBColor {
    double[] HSV = new double[3];
    int[] RGB = new int[3];
    int RGBV;

    public HSBColor(int R, int G, int B) {
        RGB[0] = R;
        RGB[1] = G;
        RGB[2] = B;
        RGBV = (256 * 256) * R + 256 * G + B;
        double r = (double) R / 255.0;
        double g = (double) G / 255.0;
        double b = (double) B / 255.0;
        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));
        double delta = max - min;
        if (r == max) {
            HSV[0] = 60 * (((g - b) / delta) % 6);
        } else if (g == max) {
            HSV[0] = 60 * (((b - r) / delta) + 2);
        } else if (b == max) {
            HSV[0] = 60 * (((r - g) / delta) + 4);
        } else {
            HSV[0] = 0;
        }
        if (max == 0) {
            HSV[1] = 0;
        } else {
            HSV[1] = delta / max;
        }
        HSV[2] = max;
    }

    public HSBColor(double H, double S, double V) {
        HSV[0] = H;
        HSV[1] = S;
        HSV[2] = V;
        double C = V * S;
        double X = C * (1 - Math.abs(((H / 60) % 2) - 1));
        double m = V - C;
        double r;
        double g;
        double b;
        if (H < 60) {
            r = C;
            g = X;
            b = 0;
        } else if (H < 120) {
            r = X;
            g = C;
            b = 0;
        } else if (H < 180) {
            r = 0;
            g = C;
            b = X;
        } else if (H < 240) {
            r = 0;
            g = X;
            b = C;
        } else if (H < 300) {
            r = X;
            g = 0;
            b = C;
        } else {
            r = C;
            g = 0;
            b = X;
        }
        RGB[0] = (int) ((r + m) * 255);
        RGB[1] = (int) ((g + m) * 255);
        RGB[2] = (int) ((b + m) * 255);
        RGBV = (256 * 256) * RGB[0] + 256 * RGB[1] + RGB[2];
    }

    @Override
    public String toString() {
        return "HSV: " + this.HSV[0] + " " + this.HSV[1] + " " + this.HSV[2] + "\n" +
                "RGB: " + this.RGB[0] + " " + this.RGB[1] + " " + this.RGB[2];
    }
}