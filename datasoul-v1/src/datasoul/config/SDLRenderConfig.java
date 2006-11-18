/*
 * SDLRenderConfig.java
 *
 * Created on 18 November 2006, 11:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.config;

/**
 *
 * @author samuelm
 */
public class SDLRenderConfig extends AbstractConfig {
    
    private static SDLRenderConfig monitor;
    private static SDLRenderConfig main;
    
    private boolean isMonitor;
    
    
    private String x11Display;
    private boolean setX11Display;
    
    @Override
    protected void registerProperties() {
        properties.add("SetX11DislpayIdx");
        properties.add("X11Display");
    }

    public static SDLRenderConfig getInstance(boolean isMonitor) {
        if (isMonitor){
            if (monitor == null){
                monitor = new SDLRenderConfig(isMonitor);
            }
            return monitor;
        }else{
            if (main == null){
                main = new SDLRenderConfig(isMonitor);
            }
            return main;
        }
    }


    private SDLRenderConfig(boolean isMonitor){
        this.isMonitor = isMonitor;
        if (isMonitor){
            load("sdlrender.monitor.config");
        }else{
            load("sdlrender.main.config");
        }
    }
    
    public void save(){
        if (isMonitor){
            save("sdlrender.monitor.config");
        }else{
            save("sdlrender.main.config");
        }
    }
    
    public String getX11Display() {
        return x11Display;
    }

    public void setX11Display(String x11Display) {
        this.x11Display = x11Display;
    }
    
    public String getSetX11DislpayIdx(){
        if (setX11Display){
            return "true";
        }else{
            return "false";
        }
    }
    
    public void setSetX11DislpayIdx(String idx){
        if (idx.equals("true")){
            setSetX11Display(true);
        }else{
            setSetX11Display(false);
        }
    }

    public boolean getSetX11Display(){
        return setX11Display;

    }
    
    public void setSetX11Display(boolean value){
        this.setX11Display = value;
    }
    
}
