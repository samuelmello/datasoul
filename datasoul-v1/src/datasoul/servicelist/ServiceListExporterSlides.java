/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.servicelist;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import datasoul.DatasoulMainForm;
import datasoul.render.ContentRender;
import datasoul.templates.DisplayTemplate;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 *
 * @author samuel
 */
public class ServiceListExporterSlides {

    private Document document;
    private LinkedList<String> deleteOnDispose;
    private BufferedImage background;
    private BufferedImage buffer;
    private ExporterContentRender render;
    private int width = DisplayTemplate.TEMPLATE_WIDTH;
    private int height = DisplayTemplate.TEMPLATE_HEIGHT;
    private int slideCount;
    
    public ServiceListExporterSlides(String filename) throws FileNotFoundException, DocumentException{
        document = new Document();
        deleteOnDispose = new LinkedList<String>();
        slideCount = 0;
        
        // ensure file do not exist to avoid garbage at the end of the file
        File f = new File(filename);
        if (f.exists()){
            f.delete();
        }
        PdfWriter.getInstance(document, new FileOutputStream(filename));

        document.setMargins(0, 0, 0, 0);
        background = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        document.setPageSize(new Rectangle (width, height));
        document.open();
        document.addCreator("Datasoul "+DatasoulMainForm.getVersion());
        document.addCreationDate();

        render = new ExporterContentRender();

    }
    
    public void write() {
        document.close();
    }

    public void cleanup(){
        /* Delete temporary files */
        for (String s : deleteOnDispose){
            File f = new File(s);
            f.delete();
        }
        /* Stop render thread */
        render.shutdown();
    }

    public void addEmptySlide() throws DocumentException{
        try{
            File tmp = File.createTempFile("datasoul-img-bg", ".png");
            tmp.deleteOnExit();

            ImageIO.write(background, "png", tmp);
            deleteOnDispose.add(tmp.getAbsolutePath());

            document.add(Image.getInstance(tmp.getAbsolutePath()));
            slideCount++;
        }catch(IOException e){
            e.printStackTrace();
            slideCount = -1;
        }

    }

    public ContentRender getRender() {
        return render;
    }

    public int getSlideCount(){
        return slideCount;
    }


    public class ExporterContentRender extends ContentRender {

        public ExporterContentRender(){
            super(DisplayTemplate.TEMPLATE_WIDTH, DisplayTemplate.TEMPLATE_HEIGHT);
        }

        public void paint(BufferedImage img, AlphaComposite rule) {
            try{
                buffer.setData(background.getData());
                Graphics gd = buffer.getGraphics();

                synchronized(img){
                    gd.drawImage(img, 0, 0, width, height, null);
                }
                File tmp = File.createTempFile("datasoul-img", ".png");
                tmp.deleteOnExit();

                ImageIO.write(buffer, "png", tmp);
                deleteOnDispose.add(tmp.getAbsolutePath());

                document.add(Image.getInstance(tmp.getAbsolutePath()));
                slideCount++;
            }catch(Exception e){
                e.printStackTrace();
                slideCount = -1;
            }
        }

        @Override
        public void paintBackground(BufferedImage bufferedImage) {
            Graphics gd = background.getGraphics();
            gd.drawImage(bufferedImage, 0, 0, width, height, null);
        }

    }

}
