package datasoul.render;

import java.awt.image.BufferedImage;

/**
 *
 * @author samuellucas
 */
public interface ContentDisplay {
    public void flip(int i);
    public void registerOutputImage0(BufferedImage img);
    public void registerOutputImage1(BufferedImage img);
}
