package me.stutiguias.spawner.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawner extends JavaPlugin implements Listener {
    
    public String prefix = "[Spawner] ";
    public static final Logger logger = Logger.getLogger("Minecraft");
    
    File dat = new File("./plugins/TimeSpawner/spawns.db");
    File CfgFile;
    FileConfiguration Cfg;
    
    public static List<SpawnerClass> mobList;

    @Override
    public void onEnable() {
        if (!getName().equals("TimeSpawner")) {
            setEnabled(false);
        }
        mobList = new ArrayList();
        checkCfg();
        try {
            Load();
        } catch (IOException ex) {
            Logger.getLogger(Spawner.class.getName()).log(Level.SEVERE, null, ex);
        }
        ReloadMobs();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        if (!getName().equals("TimeSpawner")) {
            return;
        }
        Save();
    }

    private void Load() throws IOException {
        if (this.dat.length() == 0L) {
            getLogger().log(Level.INFO, "Nothing to load.");
            return;
        }
        try {
            getLogger().log(Level.INFO, "Loading Spawns...");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.dat));
            Throwable localThrowable2 = null;
            try {
                this.mobList = ((List) ois.readObject());
            } catch (IOException | ClassNotFoundException localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (ois != null) {
                    if (localThrowable2 != null) {
                        try {
                            ois.close();
                        } catch (Throwable x2) {
                            //localThrowable2.addSuppressed(x2);
                        }
                    } else {
                        ois.close();
                    }
                }
            }
            getLogger().log(Level.INFO, "Spawns loaded with sucess.");
        } catch (FileNotFoundException | java.lang.ClassNotFoundException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }
    }

    private void Save() {
        if (this.mobList.isEmpty()) {
            getLogger().log(Level.INFO, "Nothing to save.");
            return;
        }
        try {
            getLogger().log(Level.INFO, "Salving Spawns...");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.dat));
            Throwable localThrowable2 = null;
            try {
                oos.writeObject(this.mobList);
                oos.flush();
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (oos != null) {
                    if (localThrowable2 != null) {
                        try {
                            oos.close();
                        } catch (Throwable x2) {
                            //localThrowable2.addSuppressed(x2);
                        }
                    } else {
                        oos.close();
                    }
                }
            }
            getLogger().log(Level.INFO, "Spawns Save with sucess.");
        } catch (FileNotFoundException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }
    }

    @EventHandler
    public void MobDeath(EntityDeathEvent event) {
        if (this.mobList.isEmpty()) {
            return;
        }
        for (SpawnerClass mobs : this.mobList) {
            if (mobs.containsMob(event.getEntity().getUniqueId())) {
                mobs.removeMob(event.getEntity().getUniqueId());
                if (!mobs.hasMobs()) {
                    Spawn(mobs);
                }
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Join(PlayerJoinEvent event) {
        if (getConfig().getBoolean("NoJoinMsgs")) {
            event.setJoinMessage(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Leave(PlayerQuitEvent event) {
        if (getConfig().getBoolean("NoLeaveMsgs")) {
            event.setQuitMessage(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Kick(PlayerKickEvent event) {
        if (getConfig().getBoolean("NoKickMsgs")) {
            event.setLeaveMessage(null);
        }
    }

    public void Spawn(final SpawnerClass mbs) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                SpawnerClass mobs = mbs;
                for (int i = 1; i <= mobs.getQuantd().intValue(); i++) {
                    LivingEntity ent = mobs.getLocation().getWorld().spawnCreature(mobs.getLocation(), mobs.getType());
                    mobs.addMob(ent.getUniqueId());
                }
                Spawner.this.mobList.remove(mbs);
                Spawner.this.mobList.add(mobs);
            }
        }, mbs.getTime().intValue() * 20L);
    }

    private void checkCfg() {
        this.CfgFile = new File(getDataFolder(), "config.yml");
        if (!this.CfgFile.exists()) {
            this.CfgFile.getParentFile().mkdirs();
            InputStream in = getResource("config.yml");
            try {
                OutputStream out = new FileOutputStream(this.CfgFile);
                Throwable localThrowable2 = null;
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (Throwable localThrowable1) {
                    localThrowable2 = localThrowable1;
                    throw localThrowable1;
                } finally {
                    if (out != null) {
                        if (localThrowable2 != null) {
                            try {
                                out.close();
                            } catch (Throwable x2) {
                                //localThrowable2.addSuppressed(x2);
                            }
                        } else {
                            out.close();
                        }
                    }
                }
                in.close();
            } catch (Exception e) {
            }
        }
        this.Cfg = new YamlConfiguration();
        try {
            this.dat.createNewFile();
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }
    }

    private void ReloadMobs() {
        SpawnerClass mobs;
        Iterator it;
        for (Iterator i = this.mobList.iterator(); i.hasNext();) {
            mobs = (SpawnerClass) i.next();
            for (it = mobs.getLocation().getWorld().getLivingEntities().iterator(); it.hasNext();) {
                LivingEntity ent = (LivingEntity) it.next();
                if (mobs.containsMob(ent.getUniqueId())) {
                    mobs.removeMob(ent.getUniqueId());
                    ent.remove();
                    if (!mobs.hasMobs()) {
                        Spawn(mobs);
                        break;
                    }
                }
            }
        }
    }
}