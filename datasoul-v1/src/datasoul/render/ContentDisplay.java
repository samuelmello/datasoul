package datasoul.render;

import java.awt.image.BufferedImage;

/**
 *
 * @author samuellucas
 */
public interface ContentDisplay {

    public void paintBackground(BufferedImage im);
    public void paintTransition(BufferedImage im);
    public void paintTemplate(BufferedImage im);
    public void paintAlert(BufferedImage im);
    public void updateScreen(boolean isBlack, float background, float transition, float template, float alert);

}
