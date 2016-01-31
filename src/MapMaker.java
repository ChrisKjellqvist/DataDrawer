import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by chris on 9/21/15.
 * Uses data created by gatherData to construct an overlay for a map image.
 */
public class MapMaker {
    static final int dataSize = 50;
    static int[][] data;
    static HSBColor red = new HSBColor(0, 255, 255);

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("settings.txt"));
        String coords1 = sc.nextLine();
        String coords2 = sc.nextLine();
        String[] xyDivs = sc.nextLine().split(" ");
        int x = Integer.parseInt(xyDivs[0]);
        int y = Integer.parseInt(xyDivs[1]);
        data = new int[dataSize][dataSize];
        makeMap(x, y);
    }

    public static void makeMap(int divsX, int divsY) throws IOException {
        BufferedReader read;
        for (int i = 0; i < divsX; i++) {
            for (int j = 0; j < divsY; j++) {
                try {
                    read = new BufferedReader(new FileReader("input_" + i * divsY + j + "txt"));
                    read.readLine();
                    String a;
                    int startingPointX = i * dataSize;
                    int startingPointY = j * dataSize;
                    while ((a = read.readLine()) != null) {
                        String[] str = a.split(" ");
                        int[] coords = {Integer.parseInt(str[0]), Integer.parseInt(str[1])};
                        int value = Integer.parseInt(str[2]);
                        data[startingPointX + coords[0]][startingPointY + coords[1]] = value;
                    }
                } catch (FileNotFoundException ex) {
                }
            }
        }
        //PUT DATA IN A LIST
        ArrayList<Integer> vals = new ArrayList<>();
        for (int[] a : data) {
            for (int b : a) {
                vals.add(b);
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
        double[][] overlayVals = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                double percentage;
                try {
                    percentage = data[i][j] / max;
                } catch (ArithmeticException ex) {
                    percentage = 0;
                }
                overlayVals[i][j] = percentage;
            }
        }
        BufferedImage map = ImageIO.read(new File("template.png"));
        BufferedImage finalMap = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int mapInt = map.getRGB(i, j);
                Color mapRGB = new Color(mapInt);
                HSBColor mapHSB = new HSBColor(mapRGB.getRed(), mapRGB.getGreen(), mapRGB.getBlue());
                HSBColor finalHSB = combine(mapHSB, red, overlayVals[i][j]);
                finalMap.setRGB(i, j, finalHSB.RGBV);

            }
        }
        ImageIO.write(finalMap, "png", new File("map.png"));
    }

    public static HSBColor combine(HSBColor a, HSBColor b, double ratio) {
        double[] finalVals = new double[3];
        for (int i = 0; i < 3; i++) {
            finalVals[i] = a.HSV[i] * ratio + b.HSV[i] * ratio;
        }
        return new HSBColor(finalVals[0], finalVals[1], a.HSV[2]);
    }


}