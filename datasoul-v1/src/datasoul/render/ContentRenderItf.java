/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render;

import java.awt.image.BufferedImage;

/**
 *
 * @author samuel
 */
public interface ContentRenderItf {

    void alertHide(int transictionTime);

    void alertShow(int transictionTime);

    void paintBackground(BufferedImage img);

    void saveTransitionImage();

    void setActiveImage(BufferedImage img);

    void setAlert(String alert);

    void setAlertActive(boolean active);

    void setAlertTemplate(String template);

    void setBlack(boolean b);

    void setClock(String clock);

    void setCopyright(String copyright);

    void setNextImage(BufferedImage img);

    void setNextSlide(String nextSlide);

    void setShowBackground(boolean b);

    void setShowTemplate(boolean b);

    void setShowTimer(boolean show);

    void setSlide(String slide);

    void setSongAuthor(String songAuthor);

    void setSongSource(String songSource);

    void setTemplate(String template);

    void setTimer(String timer);

    void setTimerProgress(float f);

    void setTitle(String title);

    void slideChange(int transictionTime);

    void slideChangeFromTimerManager();

    void slideHide(int transictionTime);

    void slideShow(int transictionTime);

}
