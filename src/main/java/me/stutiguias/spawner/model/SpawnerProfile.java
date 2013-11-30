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
        Location location = null;
        Location locationx = null;
        Location locationy = null;
        Double x,y,z,pitch,yaw;
        String world;
            
        if(SpawnerYML.isSet("Location.x")) {
            x = SpawnerYML.getDouble("Location.x");
            y = SpawnerYML.getDouble("Location.y");
            z = SpawnerYML.getDouble("Location.z");
            pitch = SpawnerYML.getDouble("Location.pitch");
            yaw = SpawnerYML.getDouble("Location.yaw");
            world = SpawnerYML.getString("Location.world");
            location = new Location(Bukkit.getWorld(world), x, y, z, yaw.floatValue(), pitch.floatValue());
        }else{
            
            x = SpawnerYML.getDouble("LocationX.x");		
            y = SpawnerYML.getDouble("LocationX.y");
            z = SpawnerYML.getDouble("LocationX.z");
            pitch = SpawnerYML.getDouble("LocationX.pitch");
            yaw = SpawnerYML.getDouble("LocationX.yaw");
            world = SpawnerYML.getString("LocationX.world");
            locationx = new Location(Bukkit.getWorld(world), x, y, z, yaw.floatValue(), pitch.floatValue());

            x = SpawnerYML.getDouble("LocationY.x");		
            y = SpawnerYML.getDouble("LocationY.y");
            z = SpawnerYML.getDouble("LocationY.z");
            pitch = SpawnerYML.getDouble("LocationY.pitch");
            yaw = SpawnerYML.getDouble("LocationY.yaw");
            world = SpawnerYML.getString("LocationY.world");
            locationy = new Location(Bukkit.getWorld(world), x, y, z, yaw.floatValue(), pitch.floatValue());
        }
        
        EntityType type = EntityType.valueOf(SpawnerYML.getString("Type"));
        Integer qtd = SpawnerYML.getInt("Qtd");
        Integer time = SpawnerYML.getInt("Time");
        String name = Filename.replace(".yml","");
        
        if(location != null)
            return new SpawnerControl(name, location ,type ,qtd ,time );
        else
            return new SpawnerControl(name, locationx,locationy ,type ,qtd ,time );
    }
    
    public boolean RemoveSpawnerControl(String name) {
        configplayerfile = new File(Spawner.PluginPlayerDir + File.separator + name + ".yml");
        return configplayerfile.delete();
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
             
             if(spawner.getLocationX() == null) {
                SpawnerYML.set("Location.x",spawner.getLocation().getX());		
                SpawnerYML.set("Location.y",spawner.getLocation().getY());
                SpawnerYML.set("Location.z",spawner.getLocation().getZ());
                SpawnerYML.set("Location.pitch",spawner.getLocation().getPitch());
                SpawnerYML.set("Location.yaw",spawner.getLocation().getYaw());
                SpawnerYML.set("Location.world",spawner.getLocation().getWorld().getName());
             }else{
                SpawnerYML.set("LocationX.x",spawner.getLocationX().getX());		
                SpawnerYML.set("LocationX.y",spawner.getLocationX().getY());
                SpawnerYML.set("LocationX.z",spawner.getLocationX().getZ());
                SpawnerYML.set("LocationX.pitch",spawner.getLocationX().getPitch());
                SpawnerYML.set("LocationX.yaw",spawner.getLocationX().getYaw());
                SpawnerYML.set("LocationX.world",spawner.getLocationX().getWorld().getName());
                
                SpawnerYML.set("LocationY.x",spawner.getLocationZ().getX());		
                SpawnerYML.set("LocationY.y",spawner.getLocationZ().getY());
                SpawnerYML.set("LocationY.z",spawner.getLocationZ().getZ());
                SpawnerYML.set("LocationY.pitch",spawner.getLocationZ().getPitch());
                SpawnerYML.set("LocationY.yaw",spawner.getLocationZ().getYaw());
                SpawnerYML.set("LocationY.world",spawner.getLocationZ().getWorld().getName());
             }
             
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
          if(spawner.getLocationX() == null) {
            if (!SpawnerYML.isSet("Location.x"))        SpawnerYML.set("Location.x",spawner.getLocation().getX());
            if (!SpawnerYML.isSet("Location.y"))        SpawnerYML.set("Location.y",spawner.getLocation().getY());
            if (!SpawnerYML.isSet("Location.z"))        SpawnerYML.set("Location.z",spawner.getLocation().getZ());
            if (!SpawnerYML.isSet("Location.pitch"))    SpawnerYML.set("Location.pitch",spawner.getLocation().getPitch());
            if (!SpawnerYML.isSet("Location.yaw"))      SpawnerYML.set("Location.yaw",spawner.getLocation().getYaw());
            if (!SpawnerYML.isSet("Location.world"))    SpawnerYML.set("Location.world",spawner.getLocation().getWorld().getName());
            if (!SpawnerYML.isSet("Type"))              SpawnerYML.set("Type",spawner.getType().name());
            if (!SpawnerYML.isSet("Qtd"))               SpawnerYML.set("Qtd",spawner.getQuantd());
            if (!SpawnerYML.isSet("Time"))              SpawnerYML.set("Time",spawner.getTime());
          }else{
            if (!SpawnerYML.isSet("LocationX.x"))       SpawnerYML.set("LocationX.x",spawner.getLocationX().getX());		
            if (!SpawnerYML.isSet("LocationX.y"))       SpawnerYML.set("LocationX.y",spawner.getLocationX().getY());
            if (!SpawnerYML.isSet("LocationX.z"))       SpawnerYML.set("LocationX.z",spawner.getLocationX().getZ());
            if (!SpawnerYML.isSet("LocationX.pitch"))   SpawnerYML.set("LocationX.pitch",spawner.getLocationX().getPitch());
            if (!SpawnerYML.isSet("LocationX.yaw"))     SpawnerYML.set("LocationX.yaw",spawner.getLocationX().getYaw());
            if (!SpawnerYML.isSet("LocationX.world"))   SpawnerYML.set("LocationX.world",spawner.getLocationX().getWorld().getName());

            if (!SpawnerYML.isSet("LocationY.x"))       SpawnerYML.set("LocationY.x",spawner.getLocationZ().getX());		
            if (!SpawnerYML.isSet("LocationY.y"))       SpawnerYML.set("LocationY.y",spawner.getLocationZ().getY());
            if (!SpawnerYML.isSet("LocationY.z"))       SpawnerYML.set("LocationY.z",spawner.getLocationZ().getZ());
            if (!SpawnerYML.isSet("LocationY.pitch"))   SpawnerYML.set("LocationY.pitch",spawner.getLocationZ().getPitch());
            if (!SpawnerYML.isSet("LocationY.yaw"))     SpawnerYML.set("LocationY.yaw",spawner.getLocationZ().getYaw());
            if (!SpawnerYML.isSet("LocationY.world"))   SpawnerYML.set("LocationY.world",spawner.getLocationZ().getWorld().getName());
          }
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
