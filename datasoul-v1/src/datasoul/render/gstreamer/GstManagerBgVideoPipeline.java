/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pad;
import org.gstreamer.Structure;
import org.gstreamer.elements.DecodeBin;

/**
 *
 * @author samuel
 */
public class GstManagerBgVideoPipeline extends GstManagerBgPipeline {

    protected String filename;

    public GstManagerBgVideoPipeline(String filename){
        super();
        this.filename = filename;
    }

    public void start(){

        Element src = ElementFactory.make("filesrc", "Input File");
        src.set("location", this.filename);

        DecodeBin decodeBin = new DecodeBin("Decode Bin");
        Element decodeQueue = ElementFactory.make("queue", "Decode Queue");
        pipe.addMany(src, decodeQueue, decodeBin);
        Element.linkMany(src, decodeQueue,  decodeBin);

        decodeBin.connect(new DecodeBin.NEW_DECODED_PAD() {
            @Override
            public void newDecodedPad(Element elem, Pad pad, boolean last) {

                /* only link once */
                if (pad.isLinked()) {
                    return;
                }
                /* check media type */
                Caps caps = pad.getCaps();
                Structure struct = caps.getStructure(0);
                if (struct.getName().startsWith("video/")) {
                    System.out.println("Linking video pad: " + struct.getName());
                    pad.link(tee.getStaticPad("sink"));
                }
            }
        });

        super.start();
    }

}
