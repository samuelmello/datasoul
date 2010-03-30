/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.gstreamer.notifications.GstNotificationBackgroundVideoError;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pad;
import org.gstreamer.State;
import org.gstreamer.Structure;
import org.gstreamer.elements.DecodeBin;

/**
 *
 * @author samuel
 */
public class GstManagerVideoLiveBgPipeline extends GstManagerPipeline {

    protected Element src;
    protected DecodeBin decodeBin;
    protected Element decodeQueue;
    protected Element colorSpace;

    public GstManagerVideoLiveBgPipeline(){
        super();
    }

    @Override
    public void prepareForStart(){

        super.prepareForStart();

        src = ElementFactory.make("autovideosrc", "Input Source");
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

    @Override
    public void error(int code, String msg){
        GstManagerClient.getInstance().sendNotification(new GstNotificationBackgroundVideoError("("+code+") "+msg));
        pipe.setState(State.NULL);
    }


    @Override
    public void eos(){
        super.eos();
        /* play in loop */
        pipe.setState(State.PLAYING);
    }


}
