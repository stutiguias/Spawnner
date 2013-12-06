/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.db;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.PlayerProfile;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Daniel
 */
public class PlayerYmlDb {
    
    Spawner plugin;
    File configFile;
    YamlConfiguration SpawnerYML;

    public PlayerYmlDb(Spawner plugin,PlayerProfile playerProfile) {
        this.plugin = plugin;
        Create(playerProfile);
    }
    
    public PlayerYmlDb(Spawner plugin) {
        this.plugin = plugin;
    }
    
    public boolean Exist(String playerName) {
        configFile = new File(Spawner.PlayerDir + File.separator + playerName + ".yml");
        return configFile.exists();
    }
    
    public PlayerProfile LoadPlayer(String playerName) {
            
        configFile = new File(Spawner.PlayerDir + File.separator + playerName + ".yml");
        SpawnerYML = new YamlConfiguration();
        initLoadYML();  

        String name = SpawnerYML.getString("name");
        Boolean ban = SpawnerYML.getBoolean("ban");
        long expTime = SpawnerYML.getLong("expTime");

        return new PlayerProfile(name,ban,expTime);
    }
    
    public boolean RemoveSpawnerControl(String name) {
        configFile = new File(Spawner.PlayerDir + File.separator + name + ".yml");
        return configFile.delete();
    }
    
    private void initLoadYML() {
        LoadYML();
    }

    public void LoadYML() {
        try {
            SpawnerYML.load(configFile);
        } catch (FileNotFoundException ex) {
            Spawner.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (InvalidConfigurationException ex) {
            Spawner.logger.log(Level.SEVERE, "{0} Invalid Configuration {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }
    }

    public final void Create(PlayerProfile playerProfile) {

         configFile = new File(Spawner.PlayerDir + File.separator + playerProfile.getName() + ".yml");
         SpawnerYML = new YamlConfiguration();
         
         boolean havetocreate = false;

         try {
             havetocreate = configFile.createNewFile();
         } catch (IOException ex) {
             Spawner.logger.log(Level.WARNING, "{0} Can't create file {1}", new Object[]{plugin.prefix, ex.getMessage()});
         }

         initLoadYML();

         if (havetocreate) {
               Spawner.logger.log(Level.INFO, "{0} Creating profile for {1}!", new Object[]{plugin.prefix, playerProfile.getName()});
             
                SpawnerYML.set("name", playerProfile.getName());		
                SpawnerYML.set("ban", playerProfile.getBan());
                SpawnerYML.set("expTime", playerProfile.getExpTime());
                
         } else {
             CheckConfig(playerProfile);
         }
         
         SaveYML();
    }

    public void CheckConfig(PlayerProfile playerProfile) {
        if (!SpawnerYML.isSet("name"))        SpawnerYML.set("name", playerProfile.getName());
        if (!SpawnerYML.isSet("ban"))         SpawnerYML.set("ban", playerProfile.getBan());
        if (!SpawnerYML.isSet("expTime"))     SpawnerYML.set("expTime", playerProfile.getExpTime());
    }
    
    public void SaveYML() {
        try {
            SpawnerYML.save(configFile);
        } catch (FileNotFoundException ex) {
            Spawner.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }
    }
}
