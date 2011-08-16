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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pad;
import org.gstreamer.Structure;
import org.gstreamer.elements.DecodeBin2;
import org.gstreamer.io.InputStreamSrc;

/**
 *
 * @author samuel
 */
public class GstManagerVideoGenericPipeline extends GstManagerPipeline {

    protected String filename;

    protected Element src;
    protected DecodeBin2 decodeBin;
    protected Element decodeQueue;
    protected FileInputStream fis;
    private boolean isPipelineSet;

    public GstManagerVideoGenericPipeline(String filename){
        super();
        this.filename = filename;
        isPipelineSet = false;
    }

    @Override
    public void prepareForStart(){

        super.prepareForStart();
        try {
            fis = new FileInputStream(this.filename);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.out);
        }
        src = new InputStreamSrc(fis, "Input File");
        decodeBin = new DecodeBin2("Decode Bin");
        decodeQueue = ElementFactory.make("queue", "Decode Queue");
        pipe.addMany(src, decodeQueue, decodeBin);
        Element.linkMany(src, decodeQueue,  decodeBin);

        decodeBin.connect(new DecodeBin2.NEW_DECODED_PAD() {
            @Override
            public void newDecodedPad(DecodeBin2 db, Pad pad, boolean last) {
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

        isPipelineSet = true;
    }

    @Override
    public void stop(){
        super.stop();
        if (isPipelineSet){
            Element.unlinkMany(src, decodeQueue,  decodeBin);
            pipe.removeMany(src, decodeQueue, decodeBin);
            isPipelineSet = false;
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        if (src != null) { src.dispose(); src = null; }
        if (decodeBin != null) { decodeBin.dispose(); decodeBin = null; }
        if (decodeQueue != null) { decodeQueue.dispose(); decodeQueue = null; }
        if (fis != null) try {
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

