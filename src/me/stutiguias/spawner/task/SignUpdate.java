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
        if(Spawner.SignLocation.isEmpty()) return;
        
        Location location = Spawner.SignLocation.get(spawnerControl.getName());
        
        if(location == null) return;
        
        String worldname = location.getWorld().getName();
        
        BlockState blockState = plugin.getServer().getWorld(worldname).getBlockAt(location).getState();

        if(blockState instanceof Sign){
            
            Sign sign = (Sign)plugin.getServer().getWorld(worldname).getBlockAt(location).getState();
            
            String line3 = sign.getLine(3);
            
            if(line3.contains("-")) {
                line3 = spawnerControl.getTime().toString();
            }
            
            int i = Integer.parseInt(line3);
            i--;
            
            if(i == 0){
                line3 = "-";
            }else{
                line3 = String.valueOf(i);
            }
            
            sign.setLine(3,line3);
            sign.update();
            
            if(!line3.contains("-")){
               Bukkit.getScheduler().runTaskLater(plugin, new SignUpdate(plugin,spawnerControl),20L);
            }
            
        }
            
    }
    
}
