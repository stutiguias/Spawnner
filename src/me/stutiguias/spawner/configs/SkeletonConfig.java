/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.configs;

import java.io.IOException;
import me.stutiguias.spawner.init.Spawner;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Daniel
 */
public class SkeletonConfig {
    
    private ConfigAccessor config;
    
    public boolean diebysun;
    
    public SkeletonConfig (Spawner plugin) {
        
        try{
                    
            config = new ConfigAccessor(plugin, "skeleton.yml");
            config.setupConfig();
            FileConfiguration c = config.getConfig();   
                        
            if(!c.isSet("configversion") || c.getInt("configversion") != 1){ 
                config.MakeOld();
                config.setupConfig();
                c = config.getConfig();
            }
            
            diebysun = c.getBoolean("DiebySun");
            
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void Reload() {
        config.reloadConfig();
    }
}
