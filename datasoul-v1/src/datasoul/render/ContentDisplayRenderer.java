/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 *
 * @author samuellucas
 */
public abstract class ContentDisplayRenderer implements ContentDisplay {
    
    private int activeImage;
    private BufferedImage transitionImage;
    private BufferedImage outputImage0;
    private BufferedImage backgroundImage;
    private BufferedImage alertImage;
    private BufferedImage templateImage;
    private BufferedImage outputImage1;

    public abstract void repaint();

    public BufferedImage getActiveImage(){
        if (activeImage == 0){
            return outputImage0;
        }else{
            return outputImage1;
        }
    }

    private BufferedImage getNextOutputImage(){
        if (activeImage == 0){
            return outputImage1;
        }else{
            return outputImage0;
        }
    }
    public void initDisplay(int width, int height){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        transitionImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        templateImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        alertImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        backgroundImage = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        outputImage0 = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        outputImage1 = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        activeImage = 0;
    }

    private void paintImage(BufferedImage dst, Image src){
        clearImage(dst);
        Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, dst.getWidth(), dst.getHeight(), null);
        g.dispose();
    }

    @Override
    public void paintBackground(Image im) {
        paintImage(backgroundImage, im);
    }

    @Override
    public void paintTransition(Image im) {
        paintImage(transitionImage, im);
    }

    @Override
    public void paintTemplate(Image im) {
        paintImage(templateImage, im);
    }

    @Override
    public void paintAlert(Image im) {
        paintImage(alertImage, im);
    }

    @Override
    public synchronized void updateScreen(boolean isBlack, boolean keepbg, float background, float transition, float template, float alert) {
        if (isBlack){
            paintOutputBlack();
        }else{
            synchronized(getNextOutputImage()){
                clearImage(getNextOutputImage());
                if (background > 0.0f)
                    paintOutput(backgroundImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, background));

                if (keepbg){
                    paintOutput(transitionImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                    paintOutput(templateImage, AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, template));
                }else{
                    if (transition > 0.0f)
                        paintOutput(transitionImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transition));
                    if (template > 0.0f)
                        paintOutput(templateImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, template));
                }
                if (alert > 0.0f)
                    paintOutput(alertImage, AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alert));
            }
        }

        if (activeImage == 0)
            activeImage = 1;
        else
            activeImage = 0;

        repaint();
    }

    private void paintOutput(BufferedImage img, AlphaComposite rule){
        Graphics2D g = getNextOutputImage().createGraphics();
        g.setComposite( rule );
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
        g.dispose();
    }

    private void paintOutputBlack(){
        Graphics2D g = getNextOutputImage().createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getNextOutputImage().getWidth(), getNextOutputImage().getHeight());
        g.dispose();
    }

    private void clearImage(BufferedImage img){
        Graphics2D g = img.createGraphics();
        g.setComposite( AlphaComposite.getInstance(AlphaComposite.CLEAR, 1) );
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();
    }

}
