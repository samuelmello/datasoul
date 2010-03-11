/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class GstManagerBgPipeline {

    protected Pipeline pipe;
    protected Element tee;
    protected Element queue;
    protected Element queue2;

    public GstManagerBgPipeline(){
        pipe = new Pipeline("main pipeline");
        pipe.setAutoFlushBus(true);
    }

    public void start(){

        Bus bus = pipe.getBus();

        bus.connect(new Bus.ERROR() {
            @Override
            public void errorMessage(GstObject source, int code, String message) {
                System.out.println("Error: code=" + code + " message=" + message);
            }
        });
        bus.connect(new Bus.EOS() {
            @Override
            public void endOfStream(GstObject source) {
                /* Play in loop */
                pipe.setState(State.NULL);
                pipe.setState(State.PLAYING);
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

        pipe.play();

    }

    public void stop(){
        pipe.setState(State.NULL);
        Element.unlinkMany(tee, queue, GstManagerClient.getInstance().getMainVideoSink());
        pipe.removeMany(queue, tee, GstManagerClient.getInstance().getMainVideoSink());
        if (GstManagerClient.getInstance().isMonitorEnabled()){
            Element.unlinkMany(tee, queue2, GstManagerClient.getInstance().getMonitorVideoSink());
            pipe.removeMany(queue2, GstManagerClient.getInstance().getMonitorVideoSink());
        }
    }

    public void pause(){
        pipe.setState(State.PAUSED);
    }

    public void resume(){
        pipe.setState(State.PLAYING);
    }

    public void dispose(){
        if (pipe != null) pipe.dispose();
        if (tee != null)  tee.dispose();
        if (queue != null) queue.dispose();
        if (GstManagerClient.getInstance().isMonitorEnabled()){
            if (queue2 != null) queue2.dispose();
        }
    }

}
