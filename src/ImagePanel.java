import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 12/5/15.
 * Panel that is capable of displaying an image.
 */
public class ImagePanel extends JPanel {
    Image pic;

    public void setPic(Image pic) {
        this.pic = pic;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        //Paint background first
        super.paintComponent(g);
        g.drawImage(pic, 0, 0, getWidth(), getHeight(), this);
    }
}