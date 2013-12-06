/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.commands;

import java.util.Iterator;
import java.util.UUID;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import me.stutiguias.spawner.db.SpawnerYmlDb;
import me.stutiguias.spawner.task.SpawnLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
     
    private final Spawner plugin;
    private CommandSender sender;
    private String[] args;
    private final String MsgHr = "&e-----------------------------------------------------";
    
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
            case "w":
            case "wand":
                if(!plugin.hasPermission(sender.getName(),"tsp.wand")) return false;
                return Wand();
            case "ss":
            case "setspawn" :
                if(!plugin.hasPermission(sender.getName(),"tsp.setspawn")) return false;
                return SetSpawn();
            case "spawnconf":
                // TODO : Working on Spawconfig
                //return SpawnConfig();
                SendFormatMessage("&6Not working");
                return true;
            case "ds":
            case "delspawn":
                if(!plugin.hasPermission(sender.getName(),"tsp.delspawn")) return false;
                return DelSpawn();
            case "sp":
            case "spawners":
                if(!plugin.hasPermission(sender.getName(),"tsp.spawners")) return false;
                return Spawners();
            case "tp":
            case "teleport":
                if(!plugin.hasPermission(sender.getName(),"tsp.tp")) return false;
                return teleportToSpawn();
            case "rl":
            case "reloc":
                if(!plugin.hasPermission(sender.getName(),"tsp.reloc")) return false;
                return Reloc();
            case "rs":
            case "reset":
                if(!plugin.hasPermission(sender.getName(),"tsp.reset")) return false;
                return Reset();                
            case "?":
            case "help":
            default:
                return Help();
        }       
    }
       
    public boolean Reset() {
        
        if (args.length == 2) {
                    
             ResetName(args[1]);
            
        }else{
            
            for(SpawnerControl spawnerControl:Spawner.SpawnerList) {

                RemoveAllEntitysFromSpawn(spawnerControl.getWorld(), spawnerControl);
                spawnerControl.cleanMobs();
                plugin.Spawn(spawnerControl);
            }
            
        }
        return true;
    }
    
    public boolean ResetName(String name) {
        SpawnerControl spawnerControl = plugin.FindSpawn(name);
        
        if(spawnerControl == null) {
            SendFormatMessage("&4Spawner not found");
            return true;
        }
        
        RemoveAllEntitysFromSpawn(spawnerControl.getWorld(), spawnerControl);
        spawnerControl.cleanMobs();
        plugin.Spawn(spawnerControl);
        
        return true;
    }
    
    public boolean Reloc() {
                
        if (args.length == 2) {
                    
             RelocName(args[1]);
            
        }else{
            
            plugin.getServer().getScheduler().runTask(plugin, new SpawnLocation(plugin, 0));      
            
        }
        return true;
    }
    
    public boolean RelocName(String name) {
        SpawnerControl spawnerControl = plugin.FindSpawn(name);
        
        if(spawnerControl == null) {
            SendFormatMessage("&4Spawner not found");
            return true;
        }
        
        plugin.getServer().getScheduler().runTask(plugin, new SpawnLocation(plugin,spawnerControl, 0));
        return true;
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
            SendFormatMessage("&6/sp <setspawn|ss> <spawnerName> <typeMob> <quantity> <time>");
        }
        
        if(plugin.hasPermission(sender.getName(),"tsp.wand")){
            SendFormatMessage("&6/sp <wand|w>");
        }
        
        //SendFormatMessage("&6/sp spawnconf <spawnerName> <typeMob> <quantity> <time>");

        if(plugin.hasPermission(sender.getName(),"tsp.delspawn")){
            SendFormatMessage("&6/sp <delspawn|ds> <spawnerName>");
        }
        
        if(plugin.hasPermission(sender.getName(),"tsp.spawners")){
            SendFormatMessage("&6/sp <spawners|sp> <nothing|spawnName>");
        }
                
        if(plugin.hasPermission(sender.getName(),"tsp.tp")){
            SendFormatMessage("&6/sp <teleport|tp> <spawnName>");
        }
                
        if(plugin.hasPermission(sender.getName(),"tsp.reloc")){
            SendFormatMessage("&6/sp <reloc|rl> <nothing|spawnName>");
        }
                      
        if(plugin.hasPermission(sender.getName(),"tsp.reset")){
            SendFormatMessage("&6/sp <reset|rs> <nothing|spawnName>");
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

        if (args.length == 2) {
            return SpawnerShow();
        }
        
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
            SendFormatMessage(String.format("&4name:&6 %s &4x:&6 %.2f &4y:&6 %.2f &4z:&6 %.2f",spawnerControl.getName(),x,y,z));
        }
        SendFormatMessage(MsgHr);
        return true;
    }
    
    public boolean SpawnerShow() {
                
        SendFormatMessage(MsgHr);
        SendFormatMessage("&7Spawner");
        SendFormatMessage(MsgHr);
        
        String name = args[1];
        
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if(!spawnerControl.getName().equalsIgnoreCase(name)) continue;
            double x,y,z,q = 0,w = 0,e = 0;
            String type;
            if(spawnerControl.getLocationX() == null) {
                type = "Fixed";
                x = spawnerControl.getLocation().getX();
                y = spawnerControl.getLocation().getY();
                z = spawnerControl.getLocation().getZ();
            }else{
                type = "Area";
                x = spawnerControl.getLocationX().getX();
                y = spawnerControl.getLocationX().getY();
                z = spawnerControl.getLocationX().getZ();
                
                q = spawnerControl.getLocationZ().getX();
                w = spawnerControl.getLocationZ().getY();
                e = spawnerControl.getLocationZ().getZ();
                
            }
            SendFormatMessage(String.format("&4name:&6 %s &4type:&6 %s  ",spawnerControl.getName(),type));
            SendFormatMessage(String.format("&4mobtype:&6 %s &4quantity:&6 %s &4time:&6 %s  ",spawnerControl.getType(),spawnerControl.getQuantd(),spawnerControl.getTime()));
            if(type.equalsIgnoreCase("Fixed")) { 
                SendFormatMessage(String.format("&4x:&6 %.2f &4y:&6 %.2f &4z:&6 %.2f",x,y,z));
            }else{
                SendFormatMessage(String.format("&4x:&6 %.2f &4y:&6 %.2f &4z:&6 %.2f",x,y,z));
                SendFormatMessage(String.format("&4x:&6 %.2f &4y:&6 %.2f &4z:&6 %.2f",q,w,e));
            }
            SendFormatMessage(String.format("&4How Many Alive Mobs:&6 %s ",spawnerControl.getMobs().size()));
            SendFormatMessage(MsgHr);
            return true;
        }
        SendFormatMessage("&4Spawner not found");
        SendFormatMessage(MsgHr);
        return true;
    }
    
    public boolean teleportToSpawn() {
        Player player = (Player)sender;
               
        if (args.length < 1) {
            FormatMsgRed("Wrong arguments on command tp");
            return true;
        }
        
        String name = args[1];
        
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if (spawnerControl.getName().equals(name)) {
                if(spawnerControl.getLocation() == null)
                    player.teleport(spawnerControl.getLocationX());
                else
                    player.teleport(spawnerControl.getLocation());
                return true;
            }
        }       
        FormatMsgRed("Spawn name not found");
        return true;
    }
    
    public boolean SetSpawn() {

        if (args.length < 5) {
            FormatMsgRed("Wrong arguments on command setspawn");
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
            
        SpawnerYmlDb spawnerProfile;
        String BroadcastType;
        
        if(Spawner.SpawnerCreating.containsKey((Player)sender)){
            if(Spawner.SpawnerCreating.get((Player)sender).locationLeft == null
            || Spawner.SpawnerCreating.get((Player)sender).locationRight == null) {
                SendFormatMessage("Need to set all points");
                return false;
            }
            
            Location locationx = Spawner.SpawnerCreating.get((Player)sender).locationLeft;
            Location locationz = Spawner.SpawnerCreating.get((Player)sender).locationRight;
        
            Spawner.SpawnerCreating.remove((Player)sender);
            
            spawnerProfile = new SpawnerYmlDb(plugin, new SpawnerControl(name.toLowerCase(),locationx,locationz, type, quantd, tempo));
            BroadcastType = "Area ";
        }else{
            spawnerProfile = new SpawnerYmlDb(plugin, new SpawnerControl(name.toLowerCase(), ((Player) sender).getLocation(), type, quantd, tempo));
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
            
            RemoveAllEntitysFromSpawn(spawnerControl.getWorld(), spawnerControl);
            
            spawnerControl.cleanMobs();
            
            new SpawnerYmlDb(plugin).RemoveSpawnerControl(name);
            
            SendFormatMessage("&6Mob Spawner removed successfully.");
            return true;
        }
 
        FormatMsgRed("Mob Spawner not found.");
        return true;
    }
    
    public void RemoveAllEntitysFromSpawn(World world,SpawnerControl spawnerControl) {
        for (LivingEntity ent : world.getLivingEntities()) {
            for (UUID id : spawnerControl.getMobs()) {
                 if (!ent.getUniqueId().equals(id))continue;
                 ent.remove();
            }
        }
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
