/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Daniel
 */
public class SignYmlDb {

    Spawner plugin;
    File configsignfile;
    YamlConfiguration SpawnerYML;

    public SignYmlDb(Spawner plugin,String name,Location location) {
        this.plugin = plugin;
        LoadSigns(name,location);
    }
    
    public SignYmlDb(Spawner plugin) {
        this.plugin = plugin;
    }
    
    public Location LoadSign(String Filename) {
        
        configsignfile = new File(Spawner.SignDir + File.separator + Filename);
        SpawnerYML = new YamlConfiguration();
        initLoadYML();  
        Double x,y,z,pitch,yaw;
        String world;

        x = SpawnerYML.getDouble("Location.x");
        y = SpawnerYML.getDouble("Location.y");
        z = SpawnerYML.getDouble("Location.z");
        pitch = SpawnerYML.getDouble("Location.pitch");
        yaw = SpawnerYML.getDouble("Location.yaw");
        world = SpawnerYML.getString("Location.world");
            
        return new Location(Bukkit.getWorld(world), x, y, z, yaw.floatValue(), pitch.floatValue());
    }
    
    public boolean RemoveSpawnerControl(String name) {
        configsignfile = new File(Spawner.SignDir + File.separator + name + ".yml");
        return configsignfile.delete();
    }
    
    private void initLoadYML() {
        LoadYML();
    }

    public void LoadYML() {
        try {
            SpawnerYML.load(configsignfile);
        } catch (FileNotFoundException ex) {
            Spawner.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (InvalidConfigurationException ex) {
            Spawner.logger.log(Level.SEVERE, "{0} Invalid Configuration {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }
    }

    private void LoadSigns(String name,Location location) {

         configsignfile = new File(Spawner.SignDir + File.separator + name + ".yml");
         SpawnerYML = new YamlConfiguration();
         
         boolean havetocreate = false;

         try {
             havetocreate = configsignfile.createNewFile();
         } catch (IOException ex) {
             Spawner.logger.log(Level.WARNING, "{0} Can't create the sign file {1}", new Object[]{plugin.prefix, ex.getMessage()});
         }

         initLoadYML();

         if (havetocreate) {
               Spawner.logger.log(Level.INFO, "{0} Creating sign for {1}!", new Object[]{plugin.prefix, name });
             
                SpawnerYML.set("Location.x",location.getX());		
                SpawnerYML.set("Location.y",location.getY());
                SpawnerYML.set("Location.z",location.getZ());
                SpawnerYML.set("Location.pitch",location.getPitch());
                SpawnerYML.set("Location.yaw",location.getYaw());
                SpawnerYML.set("Location.world",location.getWorld().getName());
         } else {
             CheckConfig(location);
         }
         
         SaveYML();
    }

    public void CheckConfig(Location location) {
        if (!SpawnerYML.isSet("Location.x"))        SpawnerYML.set("Location.x",location.getX());
        if (!SpawnerYML.isSet("Location.y"))        SpawnerYML.set("Location.y",location.getY());
        if (!SpawnerYML.isSet("Location.z"))        SpawnerYML.set("Location.z",location.getZ());
        if (!SpawnerYML.isSet("Location.pitch"))    SpawnerYML.set("Location.pitch",location.getPitch());
        if (!SpawnerYML.isSet("Location.yaw"))      SpawnerYML.set("Location.yaw",location.getYaw());
        if (!SpawnerYML.isSet("Location.world"))    SpawnerYML.set("Location.world",location.getWorld().getName());
    }
    
    public void SaveYML() {
        try {
            SpawnerYML.save(configsignfile);
        } catch (FileNotFoundException ex) {
            Spawner.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }
    }
}
