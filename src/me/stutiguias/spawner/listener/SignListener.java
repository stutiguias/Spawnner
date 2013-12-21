/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.listener;

import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.db.YAML.SignYmlDb;
import me.stutiguias.spawner.model.SpawnerControl;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
    public void onBlockBreak(BlockBreakEvent event) {
            Block block = event.getBlock();
            Player player = event.getPlayer();
            if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if(!sign.getLine(0).contains("[TimeSpawner]")) return;
                if(!plugin.hasPermission(player, "tsp.sign")) {
                    event.setCancelled(true);
                    return;
                }
                new SignYmlDb(plugin).Remove(sign.getLine(1));
                Spawner.SignLocation.remove(sign.getLine(1));
            }
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
        if(!plugin.hasPermission(player, "tsp.sign")) CancelEvent(event, player, sign," You do not have permission");
        
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if(!spawnerControl.getName().equalsIgnoreCase(lines[1])) continue;
            
            event.setLine(0, ChatColor.GREEN + "[TimeSpawner]");
            event.setLine(2,"Q: " + spawnerControl.getQuantd() + " " + spawnerControl.getType().name());
            event.setLine(3," - ");
            boolean signExist = Spawner.SignLocation.containsKey(spawnerControl.getName());
            
            
            if(signExist && !plugin.signYmlDb.Exist(spawnerControl.getName())) {
                Spawner.SignLocation.remove(spawnerControl.getName());
            } else {
                CancelEvent(event, player, sign," Timer already use !");
            }
            
            Spawner.SignLocation.put(spawnerControl.getName(),event.getBlock().getLocation());
            new SignYmlDb(plugin, spawnerControl.getName(), event.getBlock().getLocation()).SaveYML();
            return;
        }
        CancelEvent(event, player, sign," Spawner not found");
    }
    
    private void CancelEvent(SignChangeEvent event,Player player,Block thisSign,String msg) {
            event.setCancelled(true);
            thisSign.setTypeId(0);
            player.sendMessage(plugin.prefix + msg);
    }
}
