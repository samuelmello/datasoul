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
public class GstManagerVideoGenericPipeline extends GstManagerPipeline {

    protected String filename;

    protected Element src;
    protected DecodeBin decodeBin;
    protected Element decodeQueue;
    protected Element colorSpace;

    public GstManagerVideoGenericPipeline(String filename){
        super();
        this.filename = filename;
    }

    @Override
    public void prepareForStart(){

        super.prepareForStart();

        src = ElementFactory.make("filesrc", "Input File");
        src.set("location", this.filename);
        colorSpace = ElementFactory.make("ffmpegcolorspace", "Color Space");
        decodeBin = new DecodeBin("Decode Bin");
        decodeQueue = ElementFactory.make("queue", "Decode Queue");
        pipe.addMany(src, colorSpace, decodeQueue, decodeBin);
        Element.linkMany(src, colorSpace, decodeQueue,  decodeBin);

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
                    pad.link(tee.getStaticPad("sink"));
                }
            }
        });

    }

    @Override
    public void stop(){
        super.stop();
        if (pipe != null){
            pipe.removeMany(src, colorSpace, decodeQueue, decodeBin);
            Element.unlinkMany(src, colorSpace, decodeQueue,  decodeBin);
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        if (src != null) src.dispose();
        if (decodeBin != null) decodeBin.dispose();
        if (decodeQueue != null) decodeQueue.dispose();
        if (colorSpace != null) colorSpace.dispose();
    }

}

