import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by chris on 9/21/15.
 * Uses data created by gatherData to construct an overlay for a map image.
 */
public class MapMaker {
    static final int dataSize = 50;
    static int[][] data;
    static HSBColor red = new HSBColor(255, 0, 0);

    public static void main(String[] args) throws IOException {
        /*
        Scanner sc = new Scanner(new File("settings.txt"));
        String coords1 = sc.nextLine();
        String coords2 = sc.nextLine();
        String[] xyDivs = sc.nextLine().split(" ");
        int x = Integer.parseInt(xyDivs[0]);
        int y = Integer.parseInt(xyDivs[1]);
        data = new int[dataSize][dataSize];
        makeMap(x, y);
        */
        data = new int[255][255];
        makeMap(5, 5);
    }

    public static void makeMap(int divsX, int divsY) throws IOException {
        BufferedReader read;
        for (int i = 0; i < divsX; i++) {
            for (int j = 0; j < divsY; j++) {
                try {
                    read = new BufferedReader(new FileReader("input_" + (i * divsY + j + 1) + ".txt"));
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
        double max;
        long sum = 0;
        int c = 0;
        for (int a : vals) {
            if (a != 0) {
                c++;
                sum += a;
            }
        }
        max = 2.2 * sum / c;
        //FINDING PERCENTAGES
        double[][] overlayVals = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                double percentage;
                try {
                    percentage = data[i][j] / max;
                    if (percentage > 1) {
                        percentage = 1;
                    }
                } catch (ArithmeticException ex) {
                    percentage = 0;
                }
                overlayVals[i][j] = percentage;
            }
        }
        BufferedImage map = ImageIO.read(new File("template.png"));
        BufferedImage finalMap = new BufferedImage(1280, 1280, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 1280; i++) {
            int i_ = (int) ((double) i * ((double) 250 / (double) 1280));
            for (int j = 0; j < 1280; j++) {
                int j_ = (int) ((double) j * ((double) 250 / (double) 1280));
                Color templateRGB = new Color(map.getRGB(i, j));
                HSBColor templateHSB = new HSBColor(templateRGB.getRed(), templateRGB.getGreen(), templateRGB.getBlue());
                HSBColor finalHSB = combine(templateHSB, red, overlayVals[i_][j_]);
                finalMap.setRGB(i, j, finalHSB.RGBV);
            }
        }
        Graphics2D g = finalMap.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 1200, 200, 80);
        g.setColor(Color.black);
        g.drawString("Low", 5, 1210);
        g.drawString("High", 150, 1210);
        int sizeX = 180;//1220
        int sizeY = 50;
        HSBColor white = new HSBColor(0.0, 0.0, 1.0);
        for (int i = 10; i < 10 + sizeX; i++) {

            double ratio = ((double) i - 10) / sizeX;
            System.out.println(ratio);
            HSBColor finalHSB = combine(white, red, ratio);
            g.setColor(new Color(finalHSB.RGBV));
            g.fillRect(i, 1220, 1, 50);
        }

        ImageIO.write(finalMap, "png", new File("map.png")

        );
    }

    public static HSBColor combine(HSBColor a, HSBColor b, double ratio) {
        double[] finalVals = new double[3];
        double ratio_2 = ratio * ratio;
        for (int i = 0; i < 2; i++) {
            finalVals[i] = a.HSV[i] * (1 - ratio) + b.HSV[i] * ratio;
        }
        finalVals[2] = a.HSV[2] * (1 - ratio_2) + b.HSV[2] * ratio_2;
        return new HSBColor(finalVals[0], finalVals[1], finalVals[2]);
    }

}