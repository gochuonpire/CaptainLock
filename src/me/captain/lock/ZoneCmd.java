/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

/**
 *
 * @author andre
 */
public class ZoneCmd implements CommandExecutor {

    public CaptainLock plugin;
    
    public ZoneCmd(CaptainLock aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if ((cs instanceof Player)) {
            Player player = (Player) cs;
            if (player.hasPermission("captainlock.create") || player.hasPermission("captainlock.*")) {
                switch (args.length) {
                    case 0:

                        break;
                    case 1:
                        if (args[0].equals("info")) {
                            boolean inside = false;
                            for (Zone z : plugin.zh.getZones()) {
                                if (plugin.zh.isInZone(z, player.getLocation())) {
                                    inside = true;
                                    player.sendMessage(ChatColor.GRAY + "Owner: " + z.getOwner());
                                    player.sendMessage(ChatColor.GRAY + "Node:" + z.getNode());
                                    player.sendMessage(ChatColor.GRAY + "Id:" + z.getId());
                                }
                            }
                            if(!inside) {
                                ArrayList<String> zs = new ArrayList<>();
                                for(Zone z : plugin.zh.getZones()) {
                                    String s = ChatColor.GRAY + z.getNode() + "(" + ChatColor.GREEN + z.getId() + ChatColor.GRAY + ")";
                                    zs.add(s);
                                }
                                String msg = ChatColor.GRAY + "Zones: " + zs.toString();
                                player.sendMessage(msg);
                            }
                        }
                        break;
                    case 2:
                        if (args[0].equals("remove")) {
                            String zone = args[1];
                            ArrayList<Integer> removed = plugin.zh.removeZone(zone);
                            if(removed.isEmpty()) {
                                player.sendMessage(ChatColor.GRAY + "Enter a zone id or node to remove a zone.");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.GRAY + "Removed zones: " + removed.toString());
                                return true;
                            }
                        } else if(args[0].equals("info")) {
                            ArrayList<Zone> zones = new ArrayList();
                            for(Player p : plugin.getServer().getOnlinePlayers()) {
                                if(args[1].equals(p.getName())) {
                                    for(Zone z : plugin.zh.getZones()) {
                                        if(z.getOwner().equals(p.getName())) {
                                            zones.add(z);
                                        }
                                    }
                                }
                            }
                            for(Zone z : plugin.zh.getZones()) {
                                if(z.getNode().equals(args[1])) {
                                    zones.add(z);
                                }
                            }
                            if(zones.size()>0) {
                                for(Zone z : zones) {
                                    player.sendMessage(ChatColor.GRAY + "Owner: " + z.getOwner());
                                    player.sendMessage(ChatColor.GRAY + "Node: " + z.getNode());
                                    player.sendMessage(ChatColor.GRAY + "Id:" + z.getId());
                                }
                            } else {
                                player.sendMessage(ChatColor.GRAY + "No zones found with owner/node " + args[1]);
                            }
                        }
                        break;
                    case 3:
                        if (args[0].equals("add")) {
                            String node = args[1];
                            String offset = args[2];
                            plugin.zh.nodes.put(player.getName(), node);
                            plugin.zh.offsets.put(player.getName(), Integer.parseInt(offset));
                            plugin.zh.setting1.add(player.getName());
                            player.sendMessage(ChatColor.GRAY + "Creating zone with node " + node + " and offsetY " + offset + ". Set location one now.");
                        }
                        break;
                }
            }

        }
        return false;
    }
}
