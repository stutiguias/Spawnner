/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.commands;

import java.util.Iterator;
import java.util.UUID;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerAreaCreating;
import me.stutiguias.spawner.model.SpawnerControl;
import me.stutiguias.spawner.model.SpawnerProfile;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Daniel
 */
public class SpawnerCommands implements CommandExecutor {
     
    private Spawner plugin;
    private CommandSender sender;
    private String[] args;
    private String MsgHr = "&e-----------------------------------------------------";
    
    public SpawnerCommands(Spawner plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (sender.getName().equalsIgnoreCase("CONSOLE")) return true;
        if (!(sender instanceof Player)) return false;
        
        // TODO : Implement Permission
        if (!sender.isOp()) {
            FormatMsgRed("You do not have permission");
            return true;
        }
        
        this.sender = sender;
        this.args = args;
        
        if (args.length == 0) return Help();
        
        switch(args[0].toLowerCase())
        {
            case "reload":
                return Reload();
            case "wand":
                return Wand();
            case "setspawn" :
                return SetSpawn();
            case "spawnconf":
                // TODO : Working on Spawconfig
                //return SpawnConfig();
                SendFormatMessage("&6Not working on 0.1");
                return true;
            case "delspawn":
                return DelSpawn();
            case "spawners":
                return Spawners();
            case "?":
            case "help":
            default:
                return Help();
        }       
    }
    
    public boolean Wand() {
        Player player = (Player)sender;
        ItemStack itemStack = new ItemStack(Material.STICK,1);
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("TimeSpawner Wand");
        itemStack.setItemMeta(itemMeta);
        
        player.setItemInHand(itemStack);
        return true;
    }
        
    public boolean Reload() {
        sender.sendMessage("&6Reloading!");
        plugin.OnReload();
        sender.sendMessage("&6Reload Done!");    
        return true;
    }
    
    public boolean Help() {
        
        SendFormatMessage(MsgHr);
        SendFormatMessage(" &7TimeSpawner ");
        
        SendFormatMessage(MsgHr);
        SendFormatMessage(" &7Admin ");
        SendFormatMessage("&6/sp setmob <spawnerName> <typeMob> <quantity> <time>");
        //SendFormatMessage("&6/sp spawnconf <spawnerName> <typeMob> <quantity> <time>");
        SendFormatMessage("&6/sp delspawn <spawnerName>");
        SendFormatMessage("&6/sp spawners");
        SendFormatMessage("&6/sp reload");
        SendFormatMessage(MsgHr);
        
        return true;
    }
    
    public boolean Spawners() {

        if (Spawner.SpawnerList.isEmpty()) {
            SendFormatMessage("&6Has no set spawn.");
            return true;
        }
        SendFormatMessage(MsgHr);
        SendFormatMessage("&7List of Spawners");
        SendFormatMessage(MsgHr);
        for (SpawnerControl mobs : Spawner.SpawnerList) {
            SendFormatMessage("&6" + mobs.getName());
        }
        SendFormatMessage(MsgHr);
        return true;
    }
    
    public boolean SetSpawn() {

        if (args.length < 4) {
            FormatMsgRed("Wrong arguments on command setmob");
            return true;
        }
        
        String name = args[1];
        
        for (SpawnerControl mbs : Spawner.SpawnerList) {
            if (mbs.getName().equals(name)) {
                FormatMsgRed("this name is already in use.");
                plugin.getServer().dispatchCommand(sender, "spawners");
                return true;
            }
        }
        
        Integer quantd;
        Integer tempo;
        EntityType type;
        
        try {
            type = EntityType.valueOf(args[2].toUpperCase());
        } catch (Exception ex) {
            FormatMsgRed("Type of mob not found.");
            return true;
        }
        
        try {
            tempo = Integer.valueOf(Integer.parseInt(args[4]));
        } catch (NumberFormatException ex) {
            FormatMsgRed("The time not is number.");
            return true;
        }
        
        try {
            quantd = Integer.valueOf(Integer.parseInt(args[3]));
        } catch (NumberFormatException ex) {
            FormatMsgRed("The quantity not is number");
            return true;
        }

        Location locationx = null;
        Location locationz = null;
        
        for(SpawnerAreaCreating spawnerAreaCreating:Spawner.SpawnerCreating) {
            if(!spawnerAreaCreating.player.equals((Player)sender)) continue;
            
            if(spawnerAreaCreating.select.equals("Left"))
                locationx = spawnerAreaCreating.location;
            
            if(spawnerAreaCreating.select.equals("Right"))
                locationz = spawnerAreaCreating.location;            
        }
        
        SpawnerProfile spawnerProfile;
        if(locationx != null && locationz != null) {
            spawnerProfile = new SpawnerProfile(plugin, new SpawnerControl(name.toLowerCase(),locationx,locationz, type, quantd, tempo));
            
            for(SpawnerAreaCreating spawnerAreaCreating:Spawner.SpawnerCreating) {
              if(spawnerAreaCreating.player.equals((Player)sender))
                  Spawner.SpawnerCreating.remove(spawnerAreaCreating);
            }
            
        }else{
            spawnerProfile = new SpawnerProfile(plugin, new SpawnerControl(name.toLowerCase(), ((Player) sender).getLocation(), type, quantd, tempo));
        }
        
        Spawner.SpawnerList.add(spawnerProfile.spawner);

        plugin.Spawn(spawnerProfile.spawner);
        
        SendFormatMessage("&6Mob Spawner successfully added.");
        return true;
    }
    
    public boolean SpawnConfig() {
        
         if (args.length < 5) {
             return false;
         }
         String name = args[0];
         Integer quantd;
         Integer tempo;
         SpawnerControl mobs = null;
         EntityType type;
         try {
             tempo = Integer.valueOf(Integer.parseInt(args[3]));
         } catch (NumberFormatException ex) {
             FormatMsgRed("The time not is number.");
             return true;
         }
         try {
             type = EntityType.valueOf(args[1].toUpperCase());
         } catch (Exception ex) {
             FormatMsgRed("Type of mob not found.");
             return true;
         }
         try {
             quantd = Integer.valueOf(Integer.parseInt(args[2]));
         } catch (NumberFormatException ex) {
             FormatMsgRed("The quantity not is number.");
             return true;
         }
         for (SpawnerControl mbs : Spawner.SpawnerList) {
             if (mbs.getName().startsWith(name)) {
                 mobs = mbs;
                 Spawner.SpawnerList.remove(mbs);
                 break;
             }
         }
         if (mobs == null) {
             FormatMsgRed("Mob Spawner not found.");
             return true;
         }
         UUID id;
         Iterator it;
         
         mobs.cleanMobs();
         mobs.setLocation(((Player) sender).getLocation());
         mobs.setType(type);
         mobs.setQuantd(quantd);
         mobs.setTime(tempo);
         
         Spawner.SpawnerList.add(mobs);
         
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

         FormatMsgAqua("Mob Spawner modified Successfully.");
         return true;
    }
    
    public boolean DelSpawn() {
        
        if (args.length < 2) {
            FormatMsgRed("Wrong arguments on command delspawn");
            return true;
        }
        
        String name = args[1];

        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if (!spawnerControl.getName().equalsIgnoreCase(name)) continue;
            
            Spawner.SpawnerList.remove(spawnerControl);
            
            Location location;
            if(spawnerControl.getLocationX() != null)
                location = spawnerControl.getLocationX();
            else 
                location = spawnerControl.getLocation();
            
            for (LivingEntity ent : location.getWorld().getLivingEntities()) {
               for (UUID id : spawnerControl.getMobs()) {
                    if (!ent.getUniqueId().equals(id))continue;
                    ent.remove();
               }
            }
            
            spawnerControl.cleanMobs();
            
            new SpawnerProfile(plugin).RemoveSpawnerControl(name);
            
            SendFormatMessage("&6Mob Spawner removed successfully.");
            return true;
        }
 
        FormatMsgRed("Mob Spawner not found.");
        return true;
    }
    
    public void FormatMsgRed(String msg) {
        sender.sendMessage(ChatColor.RED + msg);
    }
    
    public void FormatMsgAqua(String msg) {
        sender.sendMessage(ChatColor.AQUA + msg);
    }
    
    public void SendFormatMessage(String msg) {
        sender.sendMessage(plugin.parseColor(msg));
    }
    
}
