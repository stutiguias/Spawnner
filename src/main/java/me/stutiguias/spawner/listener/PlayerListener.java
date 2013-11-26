/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.listener;

import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerAreaCreating;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Daniel
 */
public class PlayerListener implements Listener {
    
    private Spawner plugin;
    
    public PlayerListener(Spawner plugin) {
        this.plugin = plugin;
    }
        
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
 
        if(plugin.UpdaterNotify && plugin.hasPermission(player,"tsp.update") && Spawner.update)
        {
          player.sendMessage(plugin.parseColor("&6An update is available: " + Spawner.name + ", a " + Spawner.type + " for " + Spawner.version + " available at " + Spawner.link));
          player.sendMessage(plugin.parseColor("&6Type /sp update if you would like to automatically update."));
        }
    }
    
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event){
        if(!event.getItem().hasItemMeta() || !event.getItem().getItemMeta().getDisplayName().equals("TimeSpawner Wand")) return;
        
        SpawnerAreaCreating spawnerAreaCreating = new SpawnerAreaCreating();
        
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            spawnerAreaCreating.player = event.getPlayer();
            spawnerAreaCreating.location = event.getClickedBlock().getLocation();
            spawnerAreaCreating.select = "Left";
            Spawner.SpawnerCreating.add(spawnerAreaCreating);
            event.getPlayer().sendMessage(plugin.parseColor("&6First Spot Set!"));
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            spawnerAreaCreating.player = event.getPlayer();
            spawnerAreaCreating.location = event.getClickedBlock().getLocation();
            spawnerAreaCreating.select = "Right";
            Spawner.SpawnerCreating.add(spawnerAreaCreating);
            event.getPlayer().sendMessage(plugin.parseColor("&6Second Spot Set!"));
            event.setCancelled(true);            
        }
        
    }
}
