/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.db.YAML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Daniel
 */
public class TmpYmlDb {
        
    Spawner plugin;
    File configFile;
    YamlConfiguration SpawnerYML;

    public TmpYmlDb(Spawner plugin,SpawnerControl spawner) {
        this.plugin = plugin;
        Create(spawner);
    }
    
    public TmpYmlDb(Spawner plugin) {
        this.plugin = plugin;
    }
    
    public boolean Exist(SpawnerControl spawner) {
        configFile = new File(Spawner.TmpDir + File.separator + spawner.getName() + ".yml");
        return configFile.exists();
    }
    
    public List<UUID> LoadPlayer(SpawnerControl spawner) {
            
        configFile = new File(Spawner.TmpDir + File.separator + spawner.getName() + ".yml");
        SpawnerYML = new YamlConfiguration();
        initLoadYML();  
        
        List<UUID> monsters = new ArrayList<>();
        
        for(String key:SpawnerYML.getConfigurationSection("UUID").getKeys(true)) {
          String uuid = SpawnerYML.getConfigurationSection("UUID").getString(key);
          monsters.add(UUID.fromString(uuid));
        }

        return monsters;
    }
    
    public boolean RemoveSpawnerControl(String name) {
        configFile = new File(Spawner.TmpDir + File.separator + name + ".yml");
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

    public final void Create(SpawnerControl spawner) {

        configFile = new File(Spawner.TmpDir + File.separator + spawner.getName() + ".yml");
        SpawnerYML = new YamlConfiguration();

        try {
            if(configFile.exists()) configFile.delete();
            configFile.createNewFile();
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} Can't create file {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }

        initLoadYML();

        Spawner.logger.log(Level.INFO, "{0} Creating profile for {1}!", new Object[]{plugin.prefix, spawner.getName()});
        int i = 0;
        for (UUID monster:spawner.getMobs()){ 
           SpawnerYML.set("UUID." + i, monster.toString());
           i++;
        }
        SaveYML();
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
