/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.commands;

import java.util.Iterator;
import java.util.UUID;
import me.stutiguias.spawner.init.Spawner;
import me.stutiguias.spawner.model.SpawnerControl;
import me.stutiguias.spawner.db.YAML.SpawnerYmlDb;
import me.stutiguias.spawner.task.SpawnLocation;
import net.milkbowl.vault.economy.EconomyResponse;
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
            case "w":
            case "wand":
                if(!plugin.hasPermission(sender.getName(),"tsp.wand")) return false;
                return Wand();
            case "ss":
            case "setspawn" :
                if(!plugin.hasPermission(sender.getName(),"tsp.setspawn")) return false;
                return SetSpawn(false);
            case "bs":
            case "buyspawn":
                if(!plugin.hasPermission(sender.getName(),"tsp.buyspawn")) return false;
                return SetSpawn(true);
            case "spawnconf":
                // TODO : Working on Spawconfig
                //return SpawnConfig();
                SendFormatMessage("&6Not working");
                return true;
            case "ds":
            case "delspawn":
                return DelSpawn();
            case "sp":
            case "spawners":
                return Spawners();
            case "tp":
            case "teleport":
                return teleportToSpawn();
            case "rl":
            case "reloc":
                return Reloc();
            case "rs":
            case "reset":
                return Reset();                
            case "?":
            case "help":
            default:
                return Help();
        }       
    }
       
    public boolean Reset() {
        if (args.length == 2) {
             String resetName = args[1];
             SpawnerControl target = plugin.FindSpawn(resetName);
             if(!CanManageSpawner(target, "tsp.reset")) return true;

             plugin.getServer().getScheduler().cancelTasks(plugin);
             ResetName(resetName);

             for(SpawnerControl spawnerControl:Spawner.SpawnerList) {
                 if(spawnerControl.getName().equalsIgnoreCase(resetName) || spawnerControl.hasMobs()) continue;
                 plugin.Spawn(spawnerControl);
             }
            
        }else{
            if(!HasPermission("tsp.reset")) return false;
            plugin.getServer().getScheduler().cancelTasks(plugin);
            
            for(SpawnerControl spawnerControl:Spawner.SpawnerList) {

                RemoveAllEntitysFromSpawn(spawnerControl.getWorld(), spawnerControl);
                spawnerControl.cleanMobs();
                plugin.Spawn(spawnerControl);
            }
            
        }

        plugin.ScheduleConfiguredTasks();
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
             SpawnerControl target = plugin.FindSpawn(args[1]);
             if(!CanManageSpawner(target, "tsp.reloc")) return true;
             RelocName(args[1]);
            
        }else{
            if(!HasPermission("tsp.reloc")) return false;
            
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

    
    public boolean Wand() {
        Player player = (Player)sender;
        ItemStack itemStack = new ItemStack(Material.STICK,1);
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("TimeSpawner Wand");
        itemStack.setItemMeta(itemMeta);
        
        player.getInventory().setItemInMainHand(itemStack);
        return true;
    }
        
    public boolean Reload() {
        SendFormatMessage("&6Reloading!");
        plugin.OnReload();
        SendFormatMessage("&6Reload Done!");    
        return true;
    }
    
    public boolean Help() {
        
        SendFormatMessage(MsgHr);
        SendFormatMessage(" &7TimeSpawner ");
        
        SendFormatMessage(MsgHr);
        
        if(plugin.hasPermission(sender.getName(),"tsp.setspawn")){
            SendFormatMessage("&6/sp <setspawn|ss> <spawnerName> <typeMob> <quantity> <time>");
        }

        if(plugin.hasPermission(sender.getName(),"tsp.buyspawn")){
            SendFormatMessage("&6/sp <buyspawn|bs> <spawnerName> <typeMob> <quantity> <time>");
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
        boolean found = false;
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if(!CanViewSpawner(spawnerControl)) continue;
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
            found = true;
        }
        if(!found) {
            SendFormatMessage("&6Has no set spawn.");
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
            if(!CanViewSpawner(spawnerControl)) {
                FormatMsgRed("You can only view spawners that you bought.");
                return true;
            }
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
            if(spawnerControl.hasOwner()) {
                SendFormatMessage(String.format("&4owner:&6 %s", spawnerControl.getOwnerName()));
            }
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
               
        if (args.length < 2) {
            FormatMsgRed("Wrong arguments on command tp");
            return true;
        }
        
        String name = args[1];
        
        for (SpawnerControl spawnerControl : Spawner.SpawnerList) {
            if (spawnerControl.getName().equals(name)) {
                if(!CanManageSpawner(spawnerControl, "tsp.tp")) return true;
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
    
    public boolean SetSpawn(boolean chargePlayer) {

        if (args.length < 5) {
            FormatMsgRed("Wrong arguments on command " + args[0]);
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
            type = SpawnerControl.parseEntityType(args[2]);
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

        if(quantd <= 0) {
            FormatMsgRed("The quantity must be greater than zero");
            return true;
        }

        if(tempo <= 0) {
            FormatMsgRed("The time must be greater than zero");
            return true;
        }

        Player player = (Player)sender;
        SpawnerControl newSpawner;
        String BroadcastType;
        
        if(Spawner.SpawnerCreating.containsKey(player)){
            if(Spawner.SpawnerCreating.get(player).locationLeft == null
            || Spawner.SpawnerCreating.get(player).locationRight == null) {
                SendFormatMessage("Need to set all points");
                return false;
            }
            
            Location locationx = Spawner.SpawnerCreating.get(player).locationLeft;
            Location locationz = Spawner.SpawnerCreating.get(player).locationRight;

            newSpawner = NewSpawnerControl(name.toLowerCase(), locationx, locationz, type, quantd, tempo, chargePlayer);
            BroadcastType = "Area ";
        }else{
            newSpawner = NewSpawnerControl(name.toLowerCase(), player.getLocation(), type, quantd, tempo, chargePlayer);
            BroadcastType = "Fixed ";
        }

        if(chargePlayer && !ChargeSpawnerPurchase(player, type, quantd, tempo)) {
            return true;
        }

        if(Spawner.SpawnerCreating.containsKey(player)) {
            Spawner.SpawnerCreating.remove(player);
        }

        SpawnerYmlDb spawnerProfile = new SpawnerYmlDb(plugin, newSpawner);
        
        Spawner.SpawnerList.add(spawnerProfile.spawner);

        plugin.Spawn(spawnerProfile.spawner);
        
        SendFormatMessage("&6"+ BroadcastType +"mob spawner successfully added.");
        return true;
    }

    private SpawnerControl NewSpawnerControl(String name, Location locationx, Location locationz, EntityType type, Integer quantd, Integer tempo, boolean withOwner) {
        if(withOwner) {
            Player player = (Player)sender;
            return new SpawnerControl(name, locationx, locationz, type, quantd, tempo, player.getUniqueId(), player.getName());
        }
        return new SpawnerControl(name, locationx, locationz, type, quantd, tempo);
    }

    private SpawnerControl NewSpawnerControl(String name, Location location, EntityType type, Integer quantd, Integer tempo, boolean withOwner) {
        if(withOwner) {
            Player player = (Player)sender;
            return new SpawnerControl(name, location, type, quantd, tempo, player.getUniqueId(), player.getName());
        }
        return new SpawnerControl(name, location, type, quantd, tempo);
    }

    private boolean ChargeSpawnerPurchase(Player player, EntityType type, int quantity, int time) {
        if(!plugin.config.EnableBuySpawners) {
            FormatMsgRed("Buying spawners is disabled.");
            return false;
        }

        if(plugin.economy == null) {
            FormatMsgRed("Economy is not available.");
            return false;
        }

        double price = GetSpawnerPrice(type, quantity, time);

        if(price <= 0) {
            SendFormatMessage("&6This spawner is free.");
            return true;
        }

        if(!plugin.economy.has(player, price)) {
            FormatMsgRed("You need " + plugin.economy.format(price) + " to buy this spawner.");
            return false;
        }

        EconomyResponse response = plugin.economy.withdrawPlayer(player, price);
        if(!response.transactionSuccess()) {
            FormatMsgRed("Could not charge you: " + response.errorMessage);
            return false;
        }

        SendFormatMessage("&6Paid &e" + plugin.economy.format(price) + "&6 for this spawner.");
        return true;
    }

    private double GetSpawnerPrice(EntityType type, int quantity, int time) {
        double basePrice = plugin.config.BuySpawnerDefaultPrice;
        if(plugin.config.BuySpawnerPrices.containsKey(type.name())) {
            basePrice = plugin.config.BuySpawnerPrices.get(type.name());
        }
        return basePrice
                + (quantity * plugin.config.BuySpawnerQuantityMultiplier)
                + (time * plugin.config.BuySpawnerTimeMultiplier);
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
             type = SpawnerControl.parseEntityType(args[1]);
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
            if(!CanManageSpawner(spawnerControl, "tsp.delspawn")) return true;
            
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

    private boolean CanViewSpawner(SpawnerControl spawnerControl) {
        return HasPermission("tsp.spawners") || IsOwner(spawnerControl);
    }

    private boolean CanManageSpawner(SpawnerControl spawnerControl, String permission) {
        if(spawnerControl == null) {
            SendFormatMessage("&4Spawner not found");
            return false;
        }

        if(HasPermission(permission) || IsOwner(spawnerControl)) {
            return true;
        }

        FormatMsgRed("You can only manage spawners that you bought.");
        return false;
    }

    private boolean HasPermission(String permission) {
        return plugin.hasPermission((Player)sender, permission);
    }

    private boolean IsOwner(SpawnerControl spawnerControl) {
        return spawnerControl != null && spawnerControl.isOwner(((Player)sender).getUniqueId());
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
