/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.render.gstreamer;

import org.gstreamer.Bin;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pad;
import org.gstreamer.Structure;
import org.gstreamer.elements.DecodeBin2;

import datasoul.render.gstreamer.notifications.GstNotificationVideoItemEnd;
import datasoul.render.gstreamer.notifications.GstNotificationVideoItemError;
import datasoul.render.gstreamer.notifications.GstNotificationVideoItemStart;

/**
 *
 * @author samuel
 */
public class GstManagerVideoItemPipeline extends GstManagerVideoGenericPipeline {

    protected GstManagerPipeline oldpipeline;
    protected Element conv;
    protected Element sink;
    protected Bin audioBin;
    private boolean isPipelineSet;

    public GstManagerVideoItemPipeline(String filename, GstManagerPipeline oldpipeline){
        super(filename);
        this.oldpipeline = oldpipeline;
        isPipelineSet = false;
    }

    @Override
    public void error(int code, String msg){
        System.out.println("GstManagerVideoItemPipeline.Error Called: "+code+" msg="+msg);
        GstManagerClient.getInstance().sendNotification(new GstNotificationVideoItemError("("+code+") "+msg));
        eos();
    }

    @Override
    public void eos(){
        super.eos();
        //System.out.println("GstManagerVideoItemPipeline.EOS Called. filename was "+filename);
        GstManagerClient.getInstance().setBgPipeline(oldpipeline);
        GstManagerClient.getInstance().sendNotification(new GstNotificationVideoItemEnd());
    }

    @Override
    public void prepareForStart(){

        //System.out.println("GstManagerVideoItemPipeline.prepareForStart Begin"); 
        super.prepareForStart();

        audioBin = new Bin("Audio Bin");

        conv = ElementFactory.make("audioconvert", "Audio Convert");
        sink = ElementFactory.make("autoaudiosink", "sink");
        audioBin.addMany(conv, sink);
        Element.linkMany(conv, sink);
        audioBin.addPad(new GhostPad("sink", conv.getStaticPad("sink")));

        pipe.add(audioBin);

        decodeBin.connect(new DecodeBin2.NEW_DECODED_PAD() {
            @Override
            public void newDecodedPad(DecodeBin2 elem, Pad pad, boolean last) {
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
        
        GstManagerClient.getInstance().sendNotification(new GstNotificationVideoItemStart());
        
        //System.out.println("GstManagerVideoItemPipeline.prepareForStart End"); 
        isPipelineSet = true;
    }

    @Override
    public void stop(){
        //System.out.println("GstManagerVideoItemPipeline.Stop Begin"); 
        super.stop();
        if (isPipelineSet){
            audioBin.removeMany(conv, sink);
            Element.unlinkMany(conv, sink);
            pipe.remove(audioBin);
            isPipelineSet = false;
        }
        //System.out.println("GstManagerVideoItemPipeline.Stop End"); 
    }

    @Override
    public void dispose(){
        //System.out.println("GstManagerVideoItemPipeline.Dispose Begin"); 
        super.dispose();
        if (audioBin != null) {audioBin.dispose(); audioBin = null; }
        if (conv != null) { conv.dispose(); conv = null; }
        if (sink != null) { sink.dispose(); sink = null; }
        //System.out.println("GstManagerVideoItemPipeline.Dispose End"); 
    }


}

