/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.task;

import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author Daniel
 */
public class SpawnLocation implements Runnable {

    private final Spawner plugin;
    
    public SpawnLocation(Spawner plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        if(Spawner.SpawnerList.isEmpty()) return;
        
        for(SpawnerControl spawner:Spawner.SpawnerList) {
            
            int x,y,z;
            String worldname;
            
            if(spawner.getLocation() == null) {
                x = (int) spawner.getLocationX().getX();
                y = (int) spawner.getLocationX().getY();
                z = (int) spawner.getLocationX().getZ();
                worldname = spawner.getLocationX().getWorld().getName();
            }else{
                x = (int) spawner.getLocation().getX();
                y = (int) spawner.getLocation().getY();
                z = (int) spawner.getLocation().getZ();
                worldname = spawner.getLocation().getWorld().getName();
            }
            
            World world = Bukkit.getWorld(worldname);
            Location location = new Location(world, x, y, z);

            for(LivingEntity livingEntity:world.getLivingEntities()) {
                if(!spawner.getMobs().contains(livingEntity.getUniqueId())) continue;

                Location moblocation = livingEntity.getLocation();

                int mx = moblocation.getBlockX();
                int mz = moblocation.getBlockZ();

                Location relativeLocation = new Location(world, mx, y, mz);

                if (relativeLocation.distance(location) > plugin.PulliFFarAwayLimit) {
                      livingEntity.teleport(location);
                }
            }
            
        }
    }
    
}
