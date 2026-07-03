/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.spawner.configs;

/**
 *
 * @author Daniel
 */
import java.io.*;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigAccessor {

    private final String fileName;
    private final JavaPlugin plugin;
    
    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigAccessor(JavaPlugin plugin, String fileName) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("plugin must be initialized");
        }
        this.plugin = plugin;
        this.fileName = fileName;
    }

    public void setupConfig() throws IOException {
        configFile = new File(plugin.getDataFolder(), fileName);
        
        if(!configFile.exists()) {
            configFile.createNewFile();
            copy(plugin.getResource(fileName), configFile);
        }
    }
    
    private void copy(java.io.InputStream input, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=input.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reloadConfig() {
        if (configFile == null) {
            File dataFolder = plugin.getDataFolder();
            if (dataFolder == null) {
                throw new IllegalStateException();
            }
            configFile = new File(dataFolder, fileName);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);

        if (defConfigStream != null) {
            Reader reader = new InputStreamReader(defConfigStream);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || configFile == null) {
        } else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }
    
    public void saveDefaultConfig() {
        if (!configFile.exists()) {            
            this.plugin.saveResource(fileName, false);
        }
    }
    
    public boolean MakeOld() {
        File file = new File(plugin.getDataFolder(),fileName + "_old");
        file.delete();
        boolean renamed = configFile.renameTo(file);
        fileConfiguration = null;
        return renamed;
    }

    public void UpgradeFromOld() throws IOException {
        File oldFile = new File(plugin.getDataFolder(),fileName + "_old");
        oldFile.delete();

        if(configFile.exists() && !configFile.renameTo(oldFile)) {
            throw new IOException("Could not rename old config file");
        }

        configFile = new File(plugin.getDataFolder(), fileName);
        configFile.createNewFile();
        copy(plugin.getResource(fileName), configFile);

        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldFile);
        YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(configFile);

        copyOldValues(oldConfig, newConfig, "");
        newConfig.save(configFile);
        fileConfiguration = null;
        reloadConfig();
    }

    private void copyOldValues(ConfigurationSection oldConfig, ConfigurationSection newConfig, String parentPath) {
        for(String key:oldConfig.getKeys(false)) {
            String path = parentPath.isEmpty() ? key : parentPath + "." + key;

            if(path.equalsIgnoreCase("configversion")) continue;

            if(oldConfig.isConfigurationSection(key)) {
                if(!newConfig.isConfigurationSection(key)) {
                    newConfig.createSection(key);
                }
                copyOldValues(oldConfig.getConfigurationSection(key), newConfig.getConfigurationSection(key), path);
            } else {
                newConfig.set(key, oldConfig.get(key));
            }
        }
    }
}
