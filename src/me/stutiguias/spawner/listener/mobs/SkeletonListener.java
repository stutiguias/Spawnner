/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.listener.mobs;

import me.stutiguias.spawner.init.Spawner;
import org.bukkit.event.entity.EntityCombustEvent;

/**
 *
 * @author Daniel
 */
public class SkeletonListener {
        
    private final Spawner plugin;
    
    public SkeletonListener(Spawner plugin) {
        this.plugin = plugin;
    }
    
    public void onCombust(EntityCombustEvent event) {
        if(!plugin.skeletonConfig.diebysun) event.setCancelled(true);
    }
}
