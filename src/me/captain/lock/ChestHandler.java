/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;



/**
 *
 * @author andre
 */
public class ChestHandler {
    
    public CaptainLock plugin;
    
    public ArrayList<Lock> locks;
    
    public HashMap<String, Lock> playersTyping;
    
    public HashMap<String, String> playersPassing;
    public HashMap<String, String> playersRemoving;
    public ArrayList<String> playersWarned;

    ChestHandler(CaptainLock aThis) {
        plugin = aThis;
        locks = new ArrayList();
        playersTyping = new HashMap();
        playersPassing = new HashMap();
        playersRemoving = new HashMap();
        playersWarned = new ArrayList();
    }
    
    public boolean addLock(String playername, Chest chest, ArrayList<String> alPlayers) {
        Lock l = getLock(chest.getWorld().getBlockAt(chest.getLocation()));
        if (l != null) {
            return false;
        } else {
            Lock lock = new Lock(playername, chest, alPlayers);
            locks.add(lock);
            return true;
        }
    }
    
    public boolean removeLock(String playername, Chest chest) {
        Lock l = getLock(chest.getWorld().getBlockAt(chest.getLocation()));
        if(l!=null) {
            locks.remove(l);
            return true;
        } else {
            return false;
        }
    }
    
    public void addPlayer(String player, Lock l) {
        l.addPlayer(player);
    }
    
    public void removePlayer(String player, Lock l) {
        l.removePlayer(player);
    }
    
    public void loadChests() {
        try {
            File f = new File(plugin.getDataFolder(), "locks.yml");
            YamlConfiguration lockf = new YamlConfiguration();
            lockf.load(f);
            HashMap<String, ArrayList<Lock>> newLocks = new HashMap();
            ArrayList<Lock> temp = new ArrayList();
            for (String s : lockf.getKeys(false)) {
                String owner = lockf.getString(s + ".owner");
                String wn = lockf.getString(s + ".world");
                World world = this.plugin.getServer().getWorld(wn);
                Double x = lockf.getDouble(s + ".x");
                Double y = lockf.getDouble(s + ".y");
                Double z = lockf.getDouble(s + ".z");
                String pass = lockf.getString(s + ".password");
                Location loc = new Location(world, x, y, z);
                Block block = loc.getBlock();
                Chest chest = (Chest) block.getState();
                ArrayList<String> pls = (ArrayList<String>) lockf.getStringList(s + ".players");
                Lock lock;
                if (pass == null || pass.equals("")) {
                    lock = new Lock(owner, chest, pls);
                    locks.add(lock);
                } else {
                    lock = new Lock(owner, chest, pass, pls);
                    locks.add(lock);
                }
            }
            System.out.println("[CaptainLock] Locks loaded.");
        } catch (Exception e) {
            System.out.println("[CaptainLock] Error while loading locks.yml");
        }
    }
    
    public void reloadChests() {
        locks.clear();
        loadChests();
    }
    
    public void saveChests() {
        try {
            File f = new File(plugin.getDataFolder(), "locks.yml");
            YamlConfiguration lockf = new YamlConfiguration();
            for (Lock lock : locks) {
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".owner", lock.getOwner());
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".world", lock.getChest().getLocation().getWorld().getName());
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".x", lock.getChest().getLocation().getX());
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".y", lock.getChest().getLocation().getY());
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".z", lock.getChest().getLocation().getZ());
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".players", (List<String>) lock.getAllowedPlayers());
                lockf.set(lock.getOwner() + locks.indexOf(lock) + ".password", lock.getPassword());
            }

            lockf.save(f);
            System.out.println("[CaptainLock] Locks saved.");
        } catch (Exception e) {
            System.out.println("[CaptainLock] Error while saving locks.yml");
        }
    }

    public Lock isLocked(Block chest, Player player) {
        if (player.hasPermission("captainlock.*")) {
            return null;
        }
        for (Lock l : locks) {
            if (l.getAllowedPlayers().contains(player.getName()) || l.getOwner().equals(player.getName())) {
            } else {
                if (isSame(chest, l.getChest(), chest.getWorld())) {
                    return l;
                }
            }
        }
        return null;
    }

    public Lock getLock(Block chest) {
        for (Lock l : locks) {
            if (isSame(chest, l.getChest(), chest.getWorld())) {
                return l;
            }
        }
        return null;
    }

    
    public boolean isSame(Block chest1, Chest chest2, World w) {
        Block chb1 = w.getBlockAt(chest1.getLocation());
        Block chb2 = w.getBlockAt(chest2.getLocation());
        if(chb1.getType() == Material.CHEST && chb2.getType() == Material.CHEST) {
            Chest ch1 = (Chest) chb1.getState();
            Chest ch2 = (Chest) chb2.getState();
            if(ch1.equals(ch2)) {
                return true;
            }
        }
        return false;
    }
}
