package datasoul.render;

import java.awt.image.BufferedImage;

/**
 *
 * @author samuellucas
 */
public interface ContentDisplay {
    public void flip();
    public void registerOutputImage(BufferedImage img);
}
