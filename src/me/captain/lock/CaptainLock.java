package me.captain.lock;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;

/**
 * Main class of CaptainLock. Overrides required methods and loads configuration
 * 
 * @author andrewbulkeley
 * @version 1.0
 */
public class CaptainLock extends JavaPlugin {
    
    public FileConfiguration config;
    
    public Integer totalchests;
    
    public ChestHandler ch;
    
    public ZoneHandler zh;
    
    public PermissionManager pm;
    
    public ArrayList<String> lockingPlayers;
    public ArrayList<String> unlockingPlayers;
    public HashMap<String, String> addingPlayers;
    public HashMap<String, String>  removingPlayers;
    public ArrayList<String> isSpecial;
    public ArrayList<String> checking;
    
    public LockListener listener;
    
    @Override
    public void onEnable() {
        lockingPlayers = new ArrayList();
        unlockingPlayers = new ArrayList();
        addingPlayers = new HashMap();
        removingPlayers = new HashMap();
        isSpecial = new ArrayList();
        checking = new ArrayList();
        PluginManager pluginm = getServer().getPluginManager();
        this.getCommand("lock").setExecutor(new LockCmd(this));
        this.getCommand("unlock").setExecutor(new UnlockCmd(this));
        this.getCommand("zone").setExecutor(new ZoneCmd(this));
        this.getCommand("password").setExecutor(new PasswordCmd(this));
        loadConfig();
        ch = new ChestHandler(this);
        zh = new ZoneHandler(this);
        listener = new LockListener(this);
        pluginm.registerEvents(listener, this);
        ch.loadChests();
        zh.loadZones();
    }
    
    public void loadConfig() {
        config = getConfig();
        //set defaults
        config.addDefault("totalChestsAllowed", 100);
        config.options().copyDefaults(true);
        this.saveConfig();
        totalchests = config.getInt("totalChestsAllowed");
    }
    
    public final ZoneHandler getZoneHandler() {
        return zh;
    }
    
    @Override
    public void onDisable() {
        ch.saveChests();
        zh.saveZones();
    }
}
