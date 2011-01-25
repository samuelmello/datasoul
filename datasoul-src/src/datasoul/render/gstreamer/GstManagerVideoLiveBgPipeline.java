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

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pad;
import org.gstreamer.State;
import org.gstreamer.Structure;
import org.gstreamer.elements.DecodeBin2;

import datasoul.render.gstreamer.notifications.GstNotificationBackgroundVideoError;

/**
 *
 * @author samuel
 */
public class GstManagerVideoLiveBgPipeline extends GstManagerPipeline {

    protected Element src;
    protected DecodeBin2 decodeBin;
    protected Element colorSpace;

    public GstManagerVideoLiveBgPipeline(){
        super();
    }

    @Override
    public void prepareForStart(){

        super.prepareForStart();

        src = ElementFactory.make("autovideosrc", "Input Source");
        colorSpace = ElementFactory.make("ffmpegcolorspace", "Color Space");
        decodeBin = new DecodeBin2("Decode Bin");
        pipe.addMany(src, colorSpace, decodeBin);
        Element.linkMany(src, colorSpace, decodeBin);

        decodeBin.connect(new DecodeBin2.NEW_DECODED_PAD() {
            @Override
            public void newDecodedPad(DecodeBin2 elem, Pad pad, boolean last) {

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
            pipe.removeMany(src, colorSpace, decodeBin);
            Element.unlinkMany(src, colorSpace, decodeBin);
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        if (src != null) src.dispose();
        if (decodeBin != null) decodeBin.dispose();
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

