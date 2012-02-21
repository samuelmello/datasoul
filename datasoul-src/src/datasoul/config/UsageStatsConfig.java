/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 or later of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
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

