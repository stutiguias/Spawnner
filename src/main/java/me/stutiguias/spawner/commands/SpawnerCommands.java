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
        
        this.sender = sender;
        this.args = args;
        
        if (args.length == 0) return Help();
        
        switch(args[0].toLowerCase())
        {
            case "reload":
                if(!plugin.hasPermission(sender.getName(),"tsp.reload")) return false;
                return Reload();
            case "update":
                if(!plugin.hasPermission(sender.getName(),"tsp.update")) return false;
                return Update();
            case "wand":
                if(!plugin.hasPermission(sender.getName(),"tsp.wand")) return false;
                return Wand();
            case "setspawn" :
                if(!plugin.hasPermission(sender.getName(),"tsp.setspawn")) return false;
                return SetSpawn();
            case "spawnconf":
                // TODO : Working on Spawconfig
                //return SpawnConfig();
                SendFormatMessage("&6Not working on 0.1");
                return true;
            case "delspawn":
                if(!plugin.hasPermission(sender.getName(),"tsp.delspawn")) return false;
                return DelSpawn();
            case "spawners":
                if(!plugin.hasPermission(sender.getName(),"tsp.spawners")) return false;
                return Spawners();
            case "?":
            case "help":
            default:
                return Help();
        }       
    }
       
    public boolean Update() {
        plugin.Update();
        return true;
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
        
        if(plugin.hasPermission(sender.getName(),"tsp.setspawn")){
            SendFormatMessage("&6/sp setspawn <spawnerName> <typeMob> <quantity> <time>");
        }
        
        if(plugin.hasPermission(sender.getName(),"tsp.wand")){
            SendFormatMessage("&6/sp wand");
        }
        
        //SendFormatMessage("&6/sp spawnconf <spawnerName> <typeMob> <quantity> <time>");

        if(plugin.hasPermission(sender.getName(),"tsp.delspawn")){
            SendFormatMessage("&6/sp delspawn <spawnerName>");
        }
        
        if(plugin.hasPermission(sender.getName(),"tsp.spawners")){
            SendFormatMessage("&6/sp spawners");
        }
        
        if(plugin.hasPermission(sender.getName(),"tsp.update")){
            SendFormatMessage("&6/sp update");
        }
        
        if(plugin.hasPermission(sender.getName(),"tsp.reload")){
            SendFormatMessage("&6/sp reload");
        }
        
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
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            double x,y,z;
            if(spawnerControl.getLocationX() == null) {
                x = spawnerControl.getLocation().getX();
                y = spawnerControl.getLocation().getY();
                z = spawnerControl.getLocation().getZ();
            }else{
                x = spawnerControl.getLocationX().getX();
                y = spawnerControl.getLocationX().getY();
                z = spawnerControl.getLocationX().getZ();
            }
            SendFormatMessage("&6" + spawnerControl.getName() + " &4x:&6"+ x +" &4y:&6"+y+" &4z:&6"+z);
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
        String BroadcastType;
        if(locationx != null && locationz != null) {
            spawnerProfile = new SpawnerProfile(plugin, new SpawnerControl(name.toLowerCase(),locationx,locationz, type, quantd, tempo));
            
            for(SpawnerAreaCreating spawnerAreaCreating:Spawner.SpawnerCreating) {
              if(spawnerAreaCreating.player.equals((Player)sender))
                  Spawner.SpawnerCreating.remove(spawnerAreaCreating);
            }
            BroadcastType = "Area ";
        }else{
            spawnerProfile = new SpawnerProfile(plugin, new SpawnerControl(name.toLowerCase(), ((Player) sender).getLocation(), type, quantd, tempo));
            BroadcastType = "Fixed ";
        }
        
        Spawner.SpawnerList.add(spawnerProfile.spawner);

        plugin.Spawn(spawnerProfile.spawner);
        
        SendFormatMessage("&6"+ BroadcastType +"mob spawner successfully added.");
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
