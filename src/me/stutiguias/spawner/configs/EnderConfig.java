/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.configs;

import java.io.IOException;
import me.stutiguias.spawner.init.ConfigAccessor;
import me.stutiguias.spawner.init.Spawner;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Daniel
 */
public class EnderConfig {
        
    private ConfigAccessor config;
    
    public boolean destroyBlocks;
    public boolean spawnEgg;
    public boolean spawnPortal;
    public boolean teleportEgg;
    public boolean useCustomExp;
    public boolean dropExp;
    public long expResetMinutes;
    public long expMaxDistance;
    public int customExp;
    
    public EnderConfig (Spawner plugin) {
        
        try{
                    
            config = new ConfigAccessor(plugin, "dragon.yml");
            config.setupConfig();
            FileConfiguration c = config.getConfig();   
                        
            if(!c.isSet("configversion") || c.getInt("configversion") != 1){ 
                config.MakeOld();
                config.setupConfig();
                c = config.getConfig();
            }
            
            destroyBlocks = c.getBoolean("DestroyBlocks");
            spawnEgg = c.getBoolean("SpawnEgg");
            spawnPortal = c.getBoolean("SpawnPortal");
            teleportEgg = c.getBoolean("EggsCanTeleport");
            expResetMinutes = c.getLong("EXPResetMinutes");
            expMaxDistance = c.getLong("EXPMaxDistance");
            useCustomExp = c.getBoolean("UseCustomEXPTotal");
            dropExp = c.getBoolean("DropEXP");
            customExp = c.getInt("CustomEXPTotal");
            
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
