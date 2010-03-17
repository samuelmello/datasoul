/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.render.gstreamer;

import datasoul.render.gstreamer.notifications.GstNotificationVideoItemEnd;
import org.gstreamer.Bin;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pad;
import org.gstreamer.Structure;
import org.gstreamer.elements.DecodeBin;

/**
 *
 * @author samuel
 */
public class GstManagerVideoItemPipeline extends GstManagerVideoGenericPipeline {

    protected GstManagerPipeline oldpipeline;
    protected Element conv;
    protected Element sink;
    protected Bin audioBin;

    public GstManagerVideoItemPipeline(String filename, GstManagerPipeline oldpipeline){
        super(filename);
        this.oldpipeline = oldpipeline;
    }

    @Override
    public void eos(){
        super.eos();
        GstManagerClient.getInstance().setBgPipeline(oldpipeline);
        GstManagerClient.getInstance().sendNotification(new GstNotificationVideoItemEnd());
    }

    @Override
    public void prepareForStart(){

        super.prepareForStart();

        audioBin = new Bin("Audio Bin");

        conv = ElementFactory.make("audioconvert", "Audio Convert");
        sink = ElementFactory.make("autoaudiosink", "sink");
        audioBin.addMany(conv, sink);
        Element.linkMany(conv, sink);
        audioBin.addPad(new GhostPad("sink", conv.getStaticPad("sink")));

        pipe.add(audioBin);

        decodeBin.connect(new DecodeBin.NEW_DECODED_PAD() {
            @Override
            public void newDecodedPad(Element elem, Pad pad, boolean last) {
                Pad audioPad = audioBin.getStaticPad("sink");
                if (pad.isLinked()) {
                    return;
                }

                Caps caps = pad.getCaps();
                Structure struct = caps.getStructure(0);
                if (struct.getName().startsWith("audio/")) {
                    pad.link(audioPad);
                }

            }
        });
    }

    @Override
    public void stop(){
        super.stop();
        audioBin.removeMany(conv, sink);
        Element.unlinkMany(conv, sink);
        pipe.remove(audioBin);
    }

    @Override
    public void dispose(){
        super.dispose();
        if (audioBin != null) audioBin.dispose();
        if (conv != null) conv.dispose();
        if (sink != null) sink.dispose();
    }


}
