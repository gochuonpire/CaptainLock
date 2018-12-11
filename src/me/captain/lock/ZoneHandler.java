/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author andre
 */
public class ZoneHandler {

    public CaptainLock plugin;

    public ArrayList<String> setting1;
    public ArrayList<String> setting2;

    public HashMap<String, ArrayList<Location>> locs;
    public HashMap<String, Integer> offsets;
    public HashMap<String, String> nodes;

    private ArrayList<Zone> zones;

    public ZoneHandler(CaptainLock aThis) {
        plugin = aThis;
        zones = new ArrayList();
        setting1 = new ArrayList();
        setting2 = new ArrayList();
        offsets = new HashMap();
        nodes = new HashMap();
        locs = new HashMap();
    }

    public void loadZones() {
        try {
            File f = new File(plugin.getDataFolder(), "zones.yml");
            YamlConfiguration zonef = new YamlConfiguration();
            zonef.load(f);
            for (String s : zonef.getKeys(false)) {
                int id = zonef.getInt(s + ".id");
                String owner = zonef.getString(s + ".owner");
                String wn = zonef.getString(s + ".world");
                World world = this.plugin.getServer().getWorld(wn);
                String node = zonef.getString(s + ".node");
                Double x = zonef.getDouble(s + ".x1");
                Double y = zonef.getDouble(s + ".y1");
                Double z = zonef.getDouble(s + ".z1");
                Double x2 = zonef.getDouble(s + ".x2");
                Double y2 = zonef.getDouble(s + ".y2");
                Double z2 = zonef.getDouble(s + ".z2");
                Location loc = new Location(world, x, y, z);
                Location loc2 = new Location(world, x2, y2, z2);
                int off = zonef.getInt(s + ".offset");
                Zone zone = new Zone(id, owner, node, loc, loc2, off);
                addZone(zone);
            }
            System.out.println("[CaptainLock] Zones loaded.");
        } catch (Exception e) {
            System.out.println("[CaptainLock] Error while loading zones.yml");
        }
    }

    public void saveZones() {
        try {
            File f = new File(plugin.getDataFolder(), "zones.yml");
            YamlConfiguration zonef = new YamlConfiguration();
            for (Zone z : zones) {
                zonef.set(z.getOwner() + zones.indexOf(z) + ".id", z.getId());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".owner", z.getOwner());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".world", z.getLoc1().getWorld().getName());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".node", z.getNode());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".x1", z.getLoc1().getX());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".y1", z.getLoc1().getY());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".z1", z.getLoc1().getZ());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".x2", z.getLoc2().getX());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".y2", z.getLoc2().getY());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".z2", z.getLoc2().getZ());
                zonef.set(z.getOwner() + zones.indexOf(z) + ".offset", z.getOffsetY());
            }
            zonef.save(f);
            System.out.println("[CaptainLock] Zones saved.");
        } catch (Exception e) {
            System.out.println("[CaptainLock] Error while saving zones.yml");
        }
    }

    public void addZone(Zone z) {
        zones.add(z);
    }
    
    public ArrayList<Integer> removeZone(String s) {
        ArrayList<Zone> todelete = new ArrayList();
        for(Zone z : zones) {
            if(z.getNode().equals(s)) {
                todelete.add(z);
            }
        }
        ArrayList<Integer> removed = new ArrayList<>();
        if (todelete.isEmpty()) {
            try {
                Integer id = Integer.parseInt(s);
                for (Zone z : zones) {
                    if(Objects.equals(z.getId(), id)) {
                        zones.remove(z);
                        removed.add(z.getId());
                    }
                }
            } catch (Exception e) {
                return removed;
            }
            return removed;
        } else {
            for (Zone z : todelete) {
                zones.remove(z);
                removed.add(z.getId());
            }
            return removed;
        }
    }

    public void removeZoneByNode(String node) {
        ArrayList<Zone> todelete = new ArrayList();
        for (Zone z : zones) {
            if (z.getNode().equals(node)) {
                todelete.add(z);
            }
        }
        for (Zone z : todelete) {
            zones.remove(z);
        }
    }

    public Zone isInside(Location loc) {
        for (Zone z : zones) {
            if (isInZone(z, loc)) {
                return z;
            }
        }
        return null;
    }

    public boolean isInZone(Zone z, Location loc) {
        return inArea(loc, z.getLoc1(), z.getLoc2(), true, z);
    }

    public boolean inArea(Location targetLocation, Location inAreaLocation1, Location inAreaLocation2, boolean checkY, Zone z) {
        if (inAreaLocation1.getWorld().getName().equals(inAreaLocation2.getWorld().getName())) { // Check for worldName location1, location2
            if (targetLocation.getWorld().getName().equals(inAreaLocation1.getWorld().getName())) { // Check for worldName targetLocation, location1
                if ((targetLocation.getBlockX() >= inAreaLocation1.getBlockX() && targetLocation.getBlockX() <= inAreaLocation2.getBlockX()) || (targetLocation.getBlockX() <= inAreaLocation1.getBlockX() && targetLocation.getBlockX() >= inAreaLocation2.getBlockX())) { // Check X value
                    if ((targetLocation.getBlockZ() >= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() <= inAreaLocation2.getBlockZ()) || (targetLocation.getBlockZ() <= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() >= inAreaLocation2.getBlockZ())) { // Check Z value
                        if (checkY == true) { // If should check for Y value
                            if ((targetLocation.getBlockY() >= inAreaLocation1.getBlockY() - z.getOffsetY() && targetLocation.getBlockY() <= inAreaLocation2.getBlockY() + z.getOffsetY()) || (targetLocation.getBlockY() <= inAreaLocation1.getBlockY() + z.getOffsetY() && targetLocation.getBlockY() >= inAreaLocation2.getBlockY() - z.getOffsetY())) { // Check Y value
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public ArrayList<Zone> getZones() {
        return zones;
    }
    
    public int nextId() {
        int max = 0;
        if(zones.isEmpty()) {
            return 1;
        }
        for(Zone z : zones) {
            int id = z.getId();
            if(id>max) {
                max = id;
            }
        }
        max++;
        return max;
    }
    
    public Zone getZone(int id) {
        for(Zone z : zones) {
            if(z.getId() == id) {
                return z;
            }
        }
        return null;
    }
}
