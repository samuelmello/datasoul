/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datasoul.config;

/**
 *
 * @author samuel
 */
public class UsageStatsConfig extends AbstractConfig {

    private static UsageStatsConfig instance;

    private String id;
    private String lastSent;

    private UsageStatsConfig(){
        load("usageData.config");
    }

    public static UsageStatsConfig getInstance(){
        if (instance == null){
            instance = new UsageStatsConfig();
        }
        return instance;
    }

    private void save(){
        save("usageData.config");
    }

    @Override
    public void registerProperties(){
        super.registerProperties();
        properties.add("ID");
        properties.add("LastSent");
    }

    public String getID(){
        return id;
    }

    public void setID(String id){
        this.id = id;
        save();
    }

    public String getLastSent(){
        return lastSent;
    }

    public void setLastSent(String s){
        this.lastSent = s;
        save();
    }
}
