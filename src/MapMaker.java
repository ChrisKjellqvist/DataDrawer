import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chris on 9/21/15.
 * Uses data created by gatherData to construct an overlay for a map image.
 *
 * NOT COMPLETED YET.
 */
public class MapMaker {
    public static void main(String[] args) throws IOException {
        makeMap();
    }

    public static void makeMap() throws IOException {
        HSBColor red = new HSBColor(0, 255, 255);
        final int dataSize = 50;
        //INPUT DATA
        BufferedReader read = new BufferedReader(new FileReader("output.txt"));
        String[] coordsSplit = read.readLine().split(" ");
        try {
            double[] nw = {Double.parseDouble(coordsSplit[0]), Double.parseDouble(coordsSplit[1])};
            double[] se = {Double.parseDouble(coordsSplit[2]), Double.parseDouble(coordsSplit[3])};
        } catch (Exception e) {
            System.out.println(coordsSplit[0] + " " + coordsSplit[1] + " " + coordsSplit[2] + " " + coordsSplit[3]);
            throw e;
        }
        int[][] data = new int[dataSize][dataSize];
        String str = "";
        while ((str = read.readLine()) != null) {
            String[] spl = str.split(" ");
            int x = Integer.parseInt(spl[1]);
            int y = Integer.parseInt(spl[0]);
            int address = Integer.parseInt(spl[2]);
            data[x][y] = address;
        }


        //PUT DATA IN A LIST
        ArrayList<Integer> vals = new ArrayList<>();
        for (int[] a : data) {
            for (int b : a) {
                if (b < 8000) {
                    vals.add(b);
                }
            }
        }
        //FIND MAX
        double max = 0;
        for (int a : vals) {
            if (a > max) {
                max = a;
            }
        }
        //FINDING PERCENTAGES
        double[][] overlayVals = new double[400][400];
        for (int i = 0; i < dataSize; i++) {
            for (int j = 0; j < dataSize; j++) {
                double percentage = 0.0;
                if (data[j][i] != 0) {
                    percentage = (double) data[j][i] / max;
                }
                for (int k = 0; k < 400 / dataSize; k++) {
                    for (int l = 0; l < 400 / dataSize; l++) {
                        overlayVals[j * (400 / dataSize) + k][i * (400 / dataSize) + l] = percentage;
                    }
                }
            }
        }
        //GET MAP
        BufferedImage map = ImageIO.read(new File("maps3.png"));
        BufferedImage finalMap = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < 400; i++) {
            for (int j = 0; j < 400; j++) {
                int finalRGB;
                int mapInt = map.getRGB(j, i);
                Color mapC = new Color(mapInt);
                finalRGB = combine(new HSBColor(mapC.getRed(), mapC.getGreen(), mapC.getBlue()),
                        red, (1 - overlayVals[j][i])).RGBV;
                finalMap.setRGB(j, i, finalRGB);
            }
        }
        ImageIO.write(finalMap, "png", new File("map.png"));
    }

    public static HSBColor combine(HSBColor a, HSBColor b, double ratio) {
        double[] finalVals = new double[3];
        for (int i = 0; i < 3; i++) {
            finalVals[i] = a.HSV[i] * ratio + b.HSV[i] * (1 - ratio);
        }
        return new HSBColor(finalVals[0], finalVals[1], a.HSV[2]);
    }


}