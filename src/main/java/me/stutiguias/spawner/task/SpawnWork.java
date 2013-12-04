/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.task;

import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 *
 * @author Daniel
 */
public class SpawnWork implements Runnable {

    private final SpawnerControl spawnerControl;
    private final Spawner plugin;
    
    public SpawnWork(Spawner plugin,SpawnerControl spawner) {
        this.spawnerControl = spawner;
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        try {
            Spawner.SpawnerList.remove(spawnerControl);
            
            for (int i = 1; i <= spawnerControl.getQuantd().intValue(); i++) {
                MakeEntity();
            }
            
            if(plugin.ShowDebug) {
                Spawner.logger.log(Level.INFO, "Spawning {0}", spawnerControl.getName());
            }
            
            Spawner.SpawnerList.add(spawnerControl);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    public void MakeEntity() {
        Entity ent;
        String worldname;
        if(spawnerControl.getLocationZ() == null || spawnerControl.getLocationX() == null) {
            worldname = spawnerControl.getLocation().getWorld().getName();
            
            if(worldname == null) {
                Spawner.logger.log(Level.WARNING, "{0} World Not found", plugin.prefix);
                return;
            }   
            
            ent = Bukkit.getWorld(worldname).spawnEntity(spawnerControl.getLocation(), spawnerControl.getType());
        }else{
            
            double xx = spawnerControl.getLocationX().getX();
            double yx = spawnerControl.getLocationZ().getX();
            double xz = spawnerControl.getLocationX().getZ();
            double yz = spawnerControl.getLocationZ().getZ();
      
            double x,z;
            
            if(xx > yx)
                 x = Random(xx,yx);
            else
                 x = Random(yx,xx);    
            
            if(yz > xz)
               z = Random(yz,xz);
            else
               z = Random(xz,yz);    
             
            Location location = new Location(spawnerControl.getLocationX().getWorld(), x, spawnerControl.getLocationX().getY(), z);
            
            worldname = location.getWorld().getName();
            
            if(worldname == null) {
                Spawner.logger.log(Level.WARNING, "{0} World Not found", plugin.prefix);
                return;
            }                
            
            ent = Bukkit.getWorld(worldname).spawnEntity(location, spawnerControl.getType());
        }
        spawnerControl.addMob(ent.getUniqueId());
    }
    
        
    public double Random(double start,double end){
        long range = (long)end - (long)start + 1;
        long fraction = (long)(range * Spawner.r.nextDouble());
        return  (int)(fraction + start);    
    }
}
