/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.commands;

import java.util.Iterator;
import java.util.UUID;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerClass;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class SpawnerCommands implements CommandExecutor {
     
    private Spawner plugin;
    private CommandSender sender;
    private String[] args;
    
    public SpawnerCommands(Spawner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        this.sender = sender;
        this.args = args;
        
        switch(args[0].toLowerCase())
        {
            case "setmob" :
                return SetMob();
            case "spawnconf":
                return SpawnConfig();
            case "delspawn":
                return DelSpawn();
            case "spawners":
                return Spawners();
        }
        return false;
        
    }
    
    public boolean Spawners() {
        if (!sender.isOp()) {
            sender.sendMessage(FormatRed("You do not have permission"));
            return true;
        }
        if (Spawner.mobList.isEmpty()) {
            sender.sendMessage(FormatAqua("Has no set spawn."));
            return true;
        }
        sender.sendMessage(FormatAqua("List of Spawners:"));
        for (SpawnerClass mobs : Spawner.mobList) {
            sender.sendMessage("  " + ChatColor.AQUA + "- " + mobs.getName());
        }
        return true;
    }
    
    public boolean SetMob() {
        
        if (!sender.isOp()) {
            sender.sendMessage(FormatRed("You do not have permission"));
            return true;
        }
        
        if (args.length < 4) {
            return false;
        }
        
        String name = args[0];
        for (SpawnerClass mbs : Spawner.mobList) {
            if (mbs.getName().equals(name)) {
                sender.sendMessage(FormatRed("this name is already in use."));
                plugin.getServer().dispatchCommand(sender, "spawners");
                return true;
            }
        }
        Integer quantd;
        Integer tempo;
        EntityType type;
        try {
            type = EntityType.valueOf(args[1].toUpperCase());
        } catch (Exception ex) {
            sender.sendMessage(FormatRed("Type of mob not found."));
            return true;
        }
        try {
            tempo = Integer.valueOf(Integer.parseInt(args[3]));
        } catch (NumberFormatException ex) {
            sender.sendMessage(FormatRed("The time not is number."));
            return true;
        }
        try {
            quantd = Integer.valueOf(Integer.parseInt(args[2]));
        } catch (NumberFormatException ex) {
            sender.sendMessage(FormatRed("The quantity not is number"));
            return true;
        }

        SpawnerClass mobs = new SpawnerClass(name.toLowerCase(), ((Player) sender).getLocation(), type, quantd, tempo);

        Spawner.mobList.add(mobs);

        plugin.Spawn(mobs);
        sender.sendMessage(FormatAqua("Mob Spawner successfully added."));
        return true;
    }
    
    public boolean SpawnConfig() {
        if (!sender.isOp()) {
             sender.sendMessage(FormatRed("You do not have permission"));
             return true;
         }
         if (args.length < 4) {
             return false;
         }
         String name = args[0];
         Integer quantd;
         Integer tempo;
         SpawnerClass mobs = null;
         EntityType type;
         try {
             tempo = Integer.valueOf(Integer.parseInt(args[3]));
         } catch (NumberFormatException ex) {
             sender.sendMessage(FormatRed("The time not is number."));
             return true;
         }
         try {
             type = EntityType.valueOf(args[1].toUpperCase());
         } catch (Exception ex) {
             sender.sendMessage(FormatRed("Type of mob not found."));
             return true;
         }
         try {
             quantd = Integer.valueOf(Integer.parseInt(args[2]));
         } catch (NumberFormatException ex) {
             sender.sendMessage(FormatRed("The quantity not is number."));
             return true;
         }
         for (SpawnerClass mbs : Spawner.mobList) {
             if (mbs.getName().startsWith(name)) {
                 mobs = mbs;
                 Spawner.mobList.remove(mbs);
                 break;
             }
         }
         if (mobs == null) {
             sender.sendMessage(FormatRed("Mob Spawner not found."));
             return true;
         }
         UUID id;
         Iterator it;
         
         mobs.cleanMobs();
         mobs.setLocation(((Player) sender).getLocation());
         mobs.setType(type);
         mobs.setQuantd(quantd);
         mobs.setTime(tempo);
         
         Spawner.mobList.add(mobs);
         
         plugin.Spawn(mobs);
         
         for (Iterator i = mobs.getMobs().iterator(); i.hasNext();) {
             id = (UUID) i.next();
             for (it = ((Player) sender).getLocation().getWorld().getLivingEntities().iterator(); it.hasNext();) {
                 LivingEntity ent = (LivingEntity) it.next();
                 if (ent.getUniqueId().equals(id)) {
                     ent.remove();
                 }
             }
         }

         sender.sendMessage(FormatAqua("Mob Spawner modified Successfully."));
         return true;
    }
    
    public boolean DelSpawn() {
        if (!sender.isOp()) {
            sender.sendMessage(FormatRed("You do not have permission"));
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        String name = args[0];
        SpawnerClass mobs = null;
        for (SpawnerClass mbs : Spawner.mobList) {
            if (mbs.getName().startsWith(name)) {
                mobs = mbs;
                Spawner.mobList.remove(mbs);
                break;
            }
        }
        if (mobs == null) {
            sender.sendMessage(FormatRed("Mob Spawner not found."));
            return true;
        }
        UUID id;
        Iterator it;
        mobs.cleanMobs();
        for (Iterator i$ = mobs.getMobs().iterator(); i$.hasNext();) {
            id = (UUID) i$.next();
            for (it = ((Player) sender).getLocation().getWorld().getLivingEntities().iterator(); it.hasNext();) {
                LivingEntity ent = (LivingEntity) it.next();
                if (ent.getUniqueId().equals(id)) {
                    ent.remove();
                }
            }
        }
        sender.sendMessage(FormatRed("Mob Spawner removed successfully."));
        return true;
    }
    
    public String FormatRed(String msg) {
        return plugin.prefix + ChatColor.RED + msg;
    }
    
    public String FormatAqua(String msg) {
        return plugin.prefix + ChatColor.AQUA + msg;
    }
}
