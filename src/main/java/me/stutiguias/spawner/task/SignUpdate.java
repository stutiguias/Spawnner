/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.task;

import java.util.Map;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

/**
 *
 * @author Daniel
 */
public class SignUpdate implements Runnable {
    
    private final SpawnerControl spawnerControl;
    private final Spawner plugin;
    
    public SignUpdate(Spawner plugin,SpawnerControl spawner) {
        this.plugin = plugin;
        this.spawnerControl = spawner;
    }
    
    @Override
    public void run() {
        if(Spawner.SpawnerSignLocation.isEmpty()) return;
        
        Location location = Spawner.SpawnerSignLocation.get(spawnerControl);
        if(location == null) return;
        
        String worldname = location.getWorld().getName();
        
        BlockState blockState = plugin.getServer().getWorld(worldname).getBlockAt(location).getState();

        if(blockState instanceof Sign){
            Sign sign = (Sign)plugin.getServer().getWorld(worldname).getBlockAt(location).getState();
            int i = Integer.parseInt(sign.getLine(3));
            i--;
            
            if(i == 0){
                i = spawnerControl.getQuantd();
            }
            
            sign.setLine(3,String.valueOf(i));
            sign.update();
            
            if(i !=  spawnerControl.getQuantd()){
               Bukkit.getScheduler().runTaskLater(plugin, new SignUpdate(plugin,spawnerControl),1 * 20L);
            }
        }
            
    }
    
}
