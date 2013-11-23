/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Daniel
 */
public class SpawnerProfile {

    public SpawnerControl spawner;
    Spawner plugin;
    File configplayerfile;
    YamlConfiguration SpawnerYML;

    public SpawnerProfile(Spawner plugin,SpawnerControl spawner) {
        this.spawner = spawner;
        LoadSpawnerProfile(spawner, plugin);
    }
    
    public SpawnerProfile(Spawner plugin) {
        this.plugin = plugin;
    }
    
    public SpawnerControl LoadSpawnerControl(String Filename) {
        configplayerfile = new File(Spawner.PluginPlayerDir + File.separator + Filename);
        SpawnerYML = new YamlConfiguration();
        initLoadYML();
        Double x = SpawnerYML.getDouble("Location.x");
        Double y = SpawnerYML.getDouble("Location.y");
        Double z = SpawnerYML.getDouble("Location.z");
        Double pitch = SpawnerYML.getDouble("Location.pitch");
        Double yaw = SpawnerYML.getDouble("Location.yaw");
        String world = SpawnerYML.getString("Location.world");
        Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw.floatValue(), pitch.floatValue());
        EntityType type = EntityType.valueOf(SpawnerYML.getString("Type"));
        Integer qtd = SpawnerYML.getInt("Qtd");
        Integer time = SpawnerYML.getInt("Time");
        String name = Filename.replace(".yml","");
        return new SpawnerControl(name, location ,type ,qtd ,time );
    }
    
    private void initLoadYML() {
        LoadYML();
    }

    public void LoadYML() {
        try {
            SpawnerYML.load(configplayerfile);
        } catch (FileNotFoundException ex) {
            Spawner.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (InvalidConfigurationException ex) {
            Spawner.logger.log(Level.SEVERE, "{0} Invalid Configuration {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }
    }

    private void LoadSpawnerProfile(SpawnerControl spawner, Spawner plugin) {

         configplayerfile = new File(Spawner.PluginPlayerDir + File.separator + spawner.getName() + ".yml");
         SpawnerYML = new YamlConfiguration();
         
         boolean havetocreate = false;

         try {
             havetocreate = configplayerfile.createNewFile();
         } catch (IOException ex) {
             Spawner.logger.log(Level.WARNING, "{0} Can't create the rankup user file {1}", new Object[]{plugin.prefix, ex.getMessage()});
         }

         initLoadYML();

         if (havetocreate) {
             Spawner.logger.log(Level.INFO, "{0} Creating profile for {1}!", new Object[]{plugin.prefix, spawner.getName() });
             SpawnerYML.set("Location.x",spawner.getLocation().getX());		
             SpawnerYML.set("Location.y",spawner.getLocation().getY());
             SpawnerYML.set("Location.z",spawner.getLocation().getZ());
             SpawnerYML.set("Location.pitch",spawner.getLocation().getPitch());
             SpawnerYML.set("Location.yaw",spawner.getLocation().getYaw());
             SpawnerYML.set("Location.world",spawner.getLocation().getWorld().getName());
             SpawnerYML.set("Type",spawner.getType().name());
             SpawnerYML.set("Qtd",spawner.getQuantd());
             SpawnerYML.set("Time",spawner.getTime());
         } else {
             CheckConfig(spawner);
         }
         
         SaveYML();
         
         this.plugin = plugin;
    }

    public void CheckConfig(SpawnerControl spawner) {
        if (!SpawnerYML.isSet("Location.x"))        SpawnerYML.set("Location.x",spawner.getLocation().getX());
        if (!SpawnerYML.isSet("Location.y"))        SpawnerYML.set("Location.y",spawner.getLocation().getY());
        if (!SpawnerYML.isSet("Location.z"))        SpawnerYML.set("Location.z",spawner.getLocation().getZ());
        if (!SpawnerYML.isSet("Location.pitch"))    SpawnerYML.set("Location.pitch",spawner.getLocation().getPitch());
        if (!SpawnerYML.isSet("Location.yaw"))      SpawnerYML.set("Location.yaw",spawner.getLocation().getYaw());
        if (!SpawnerYML.isSet("Location.world"))    SpawnerYML.set("Location.world",spawner.getLocation().getWorld().getName());
        if (!SpawnerYML.isSet("Type"))              SpawnerYML.set("Type",spawner.getType().name());
        if (!SpawnerYML.isSet("Qtd"))               SpawnerYML.set("Qtd",spawner.getQuantd());
        if (!SpawnerYML.isSet("Time"))              SpawnerYML.set("Time",spawner.getTime());
    }
    
    public void SaveYML() {
        try {
            SpawnerYML.save(configplayerfile);
        } catch (FileNotFoundException ex) {
            Spawner.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{plugin.prefix, ex.getMessage()});
        } catch (IOException ex) {
            Spawner.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{plugin.prefix, ex.getMessage()});
        }
    }
}
