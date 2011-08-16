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

import org.gstreamer.Bus;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GstObject;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

/**
 *
 * @author samuel
 */
public class GstManagerPipeline {

    protected Pipeline pipe;
    protected Element tee;
    protected Element queue;
    protected Element queue2;
    private boolean isPipelineSet;

    public GstManagerPipeline(){
        isPipelineSet = false;
    }

    public void eos(){
        if (isPipelineSet){
            pipe.setState(State.NULL);
        }
    }

    public void error(int code, String msg){

    }

    public void prepareForStart(){

        pipe = new Pipeline("main pipeline");
        pipe.setAutoFlushBus(true);
        
        Bus bus = pipe.getBus();

        bus.connect(new Bus.ERROR() {
            @Override
            public void errorMessage(GstObject source, int code, String message) {
                error(code, message);
            }
        });
        bus.connect(new Bus.EOS() {
            @Override
            public void endOfStream(GstObject source) {
                eos();
            }
        });

        tee = ElementFactory.make("tee", "t1");
        queue = ElementFactory.make("queue", "q1");
        pipe.addMany(tee, queue, GstManagerClient.getInstance().getMainVideoSink());
        Element.linkMany(tee, queue, GstManagerClient.getInstance().getMainVideoSink());

        if (GstManagerClient.getInstance().isMonitorEnabled()){
            queue2 = ElementFactory.make("queue", "q2");
            pipe.addMany(queue2, GstManagerClient.getInstance().getMonitorVideoSink());
            Element.linkMany(tee, queue2, GstManagerClient.getInstance().getMonitorVideoSink());
        }

        isPipelineSet = true;
        
        //System.out.println("GstManagerPipeline.prepareForStart");
    }

    public void start(){
        prepareForStart();
        pipe.play();
    }

    public void stop(){
        if (isPipelineSet){
            pipe.setState(State.NULL);
            Element.unlinkMany(tee, queue, GstManagerClient.getInstance().getMainVideoSink());
            pipe.removeMany(queue, tee, GstManagerClient.getInstance().getMainVideoSink());
            if (GstManagerClient.getInstance().isMonitorEnabled()){
                Element.unlinkMany(tee, queue2, GstManagerClient.getInstance().getMonitorVideoSink());
                pipe.removeMany(queue2, GstManagerClient.getInstance().getMonitorVideoSink());
            }
            isPipelineSet = false;
        }
    }

    public void pause(){
        pipe.setState(State.PAUSED);
    }

    public void resume(){
        pipe.setState(State.PLAYING);
    }

    public void dispose(){
        if (pipe != null) { pipe.dispose(); pipe = null; }
        if (tee != null)  { tee.dispose(); tee = null; }
        if (queue != null) { queue.dispose(); queue = null; }
        if (GstManagerClient.getInstance().isMonitorEnabled()){
            if (queue2 != null) { queue2.dispose(); queue2 = null; }
        }
    }

}

