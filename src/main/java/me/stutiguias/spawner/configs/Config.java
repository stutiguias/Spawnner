/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.configs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.stutiguias.spawner.init.Spawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Daniel
 */
public class Config {
        
    private ConfigAccessor config;
    
    public boolean ShowDebug;
    public boolean UpdaterNotify;
    public boolean EnablePulliFFarAway;
    public int PulliFFarAwayTime;
    public int PulliFFarAwayLimit;
    public boolean DisableControlOverEnderDragon;
    public boolean DisableCustomName;
    public boolean DisableSpawnerRemoveMobs;
    public boolean UseTaskCheckMobAlive;
    public boolean EnableBuySpawners;
    public int UseTaskCheckMobAliveSeconds;
    public double BuySpawnerDefaultPrice;
    public double BuySpawnerQuantityMultiplier;
    public double BuySpawnerTimeMultiplier;
    public Map<String, Double> BuySpawnerPrices;
    
    public String DataBaseType;
    public String Host;
    public String Username;
    public String Password;
    public String Port;
    public String Database;
    
    public Config (Spawner plugin) {
        
       BuySpawnerPrices = new HashMap<>();
       try {
            config = new ConfigAccessor(plugin, "config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 5){ 
                config.UpgradeFromOld();
                fc = config.getConfig();
            }
            
            ShowDebug = fc.getBoolean("ShowDebug");
            UpdaterNotify =fc.getBoolean("UpdaterNotify");
            EnablePulliFFarAway =fc.getBoolean("EnablePulliFFarAway");
            PulliFFarAwayTime =fc.getInt("PulliFFarAwayTime");
            PulliFFarAwayLimit = fc.getInt("PulliFFarAwayLimit");
            DisableControlOverEnderDragon = fc.getBoolean("DisableControlOverEnderDragon");
            DisableCustomName = fc.getBoolean("DisableCustomName");
            DisableSpawnerRemoveMobs = !fc.isSet("DisableSpawner.RemoveMobs") || fc.getBoolean("DisableSpawner.RemoveMobs");
            UseTaskCheckMobAlive = fc.getBoolean("UseTaskCheckMobAlive");
            UseTaskCheckMobAliveSeconds = fc.getInt("UseTaskCheckMobAliveSeconds");
            EnableBuySpawners = fc.getBoolean("Economy.EnableBuySpawners");
            BuySpawnerDefaultPrice = fc.getDouble("Economy.BuySpawner.DefaultPrice");
            BuySpawnerQuantityMultiplier = fc.getDouble("Economy.BuySpawner.QuantityMultiplier");
            BuySpawnerTimeMultiplier = fc.getDouble("Economy.BuySpawner.TimeMultiplier");
            ConfigurationSection prices = fc.getConfigurationSection("Economy.BuySpawner.Prices");
            if(prices != null) {
                for(String key:prices.getKeys(false)) {
                    BuySpawnerPrices.put(key.toUpperCase(), prices.getDouble(key));
                }
            }
            
            DataBaseType = fc.getString("DataBase.Type");
            Host  = fc.getString("MySQL.Host");
            Username = fc.getString("MySQL.Username");
            Password = fc.getString("MySQL.Password");
            Port = fc.getString("MySQL.Port");
            Database = fc.getString("MySQL.Database");
            
        }catch(IOException ex){
            Spawner.logger.log(Level.WARNING, "Erro Loading Config");
        }
    }
    
    public void Reload() {
        config.reloadConfig();
    }
}
