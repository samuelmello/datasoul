package datasoul.render;

import java.awt.Image;

/**
 *
 * @author samuellucas
 */
public interface ContentDisplay {

    public void paintBackground(Image im);
    public void paintTransition(Image im);
    public void paintTemplate(Image im);
    public void paintAlert(Image im);
    public void updateScreen(boolean isBlack, boolean keepBG, float background, float transition, float template, float alert);

}

