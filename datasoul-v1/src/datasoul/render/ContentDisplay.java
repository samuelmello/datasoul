package datasoul.render;

import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

/**
 *
 * @author samuellucas
 */
public interface ContentDisplay {
    public void paint(BufferedImage img, AlphaComposite rule);
    public void clear();
    public void flip();
    public void paintBackground(BufferedImage bufferedImage);
    public void setClear(int i);
    public void setBlack(int i);

}
