import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by chris on 2/11/16.
 */
public class StaticMapCreator {
    static final String API_KEY = "AIzaSyAG3MBBOC0LVqRE3ZOYiRqOvZ7DyTzoRzU";
    static final String BASE_URL_STR = "https://maps.googleapis.com/maps/api/staticmap?";

    public static void main(String[] args) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("center", "Houston");
        map.put("zoom", "11");
        map.put("size", "2500x2500");
        map.put("scale", "4");
        map.put("format", "png");
        map.put("maptype", "satellite");
        ImageIO.write(getImage(formatURL(map)), "png", new File("template.png"));
    }

    public static URL formatURL(HashMap<String, String> params) throws MalformedURLException {
        String url = BASE_URL_STR;
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }
        url += API_KEY;
        return new URL(url);
    }

    public static BufferedImage getImage(URL u) throws IOException {
        return ImageIO.read(u.openStream());
    }
}
