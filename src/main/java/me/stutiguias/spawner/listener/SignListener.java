/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.listener;

import javax.jws.HandlerChain;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author Daniel
 */
public class SignListener implements Listener {
        
    private final Spawner plugin;
    
    public SignListener(Spawner plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        
        String[] lines = event.getLines();
        Player player = event.getPlayer();
        Block sign = event.getBlock();

        if (player != null) {
            if(lines[0].equalsIgnoreCase("[TimeSpawner]")) TimeSpawner(lines, player, sign, event);
        }

    }
                
    public void TimeSpawner(String[] lines,Player player,Block sign,SignChangeEvent event)    {
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if(!spawnerControl.getName().equalsIgnoreCase(lines[1])) continue;
            event.setLine(0, ChatColor.GREEN + "[TimeSpawner]");
            event.setLine(2,"Q: " + spawnerControl.getQuantd() + " " + spawnerControl.getType().name());
            event.setLine(3,spawnerControl.getTime().toString());
            Spawner.SpawnerSignLocation.put(spawnerControl,event.getBlock().getLocation());
            return;
        }
        CancelEvent(event, player, sign);
    }
    
    private void CancelEvent(SignChangeEvent event,Player player,Block thisSign) {
            event.setCancelled(true);
            thisSign.setTypeId(0);
            player.sendMessage(plugin.prefix + " You do not have permission");
    }
}
