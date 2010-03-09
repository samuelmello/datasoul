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

    protected Element tee;
    protected Pipeline pipe;

    public GstManagerBgPipeline(){
        pipe = new Pipeline("main pipeline");
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
        Element queue = ElementFactory.make("queue", "q1");
        pipe.addMany(tee, queue, GstManagerClient.getInstance().getMainVideoSink());
        Element.linkMany(tee, queue, GstManagerClient.getInstance().getMainVideoSink());

        if (GstManagerClient.getInstance().isMonitorEnabled()){
            Element queue2 = ElementFactory.make("queue", "q2");
            pipe.addMany(queue2, GstManagerClient.getInstance().getMonitorVideoSink());
            Element.linkMany(tee, queue2, GstManagerClient.getInstance().getMonitorVideoSink());
        }

        pipe.play();

    }

    public void stop(){
        pipe.stop();
    }

}
