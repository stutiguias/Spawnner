/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.listener;

import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerAreaCreating;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Daniel
 */
public class PlayerListener implements Listener {
    
    private Spawner plugin;
    
    public PlayerListener(Spawner plugin) {
        this.plugin = plugin;
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
            event.getPlayer().sendMessage("Left OK!");
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            spawnerAreaCreating.player = event.getPlayer();
            spawnerAreaCreating.location = event.getClickedBlock().getLocation();
            spawnerAreaCreating.select = "Right";
            Spawner.SpawnerCreating.add(spawnerAreaCreating);
            event.getPlayer().sendMessage("Right OK!");
            event.setCancelled(true);            
        }
        
    }
}
