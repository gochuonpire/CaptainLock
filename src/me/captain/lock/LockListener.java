/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import java.util.ArrayList;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.ChatColor;


/**
 *
 * @author andre
 */
public class LockListener implements Listener {
    
    public CaptainLock plugin;

    LockListener(CaptainLock instance) {
        plugin = instance;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.isSpecial.contains(player.getName()) && event.getClickedBlock().getType() == Material.CHEST) {
            if(plugin.addingPlayers.containsKey(player.getName())) {
                String newP = plugin.addingPlayers.get(player.getName());
                Block ch = event.getClickedBlock();
                Lock l = plugin.ch.getLock(ch);
                if (l != null) {
                    plugin.ch.addPlayer(newP, l);
                    player.sendMessage(ChatColor.GRAY + newP + " can now access your chest");
                } else {
                    player.sendMessage(ChatColor.GRAY + "Unable to add player to chest");
                }
                plugin.addingPlayers.remove(player.getName());
                plugin.isSpecial.remove(player.getName());
            } else if (plugin.removingPlayers.containsKey(player.getName())) {
                String rP = plugin.removingPlayers.get(player.getName());
                Block ch = event.getClickedBlock();
                Lock l = plugin.ch.getLock(ch);
                if (l != null) {
                    plugin.ch.removePlayer(rP, l);
                    player.sendMessage(ChatColor.GRAY + rP + " can no longer access your chest");
                } else {
                    player.sendMessage(ChatColor.GRAY + "Unable to remove player from chest");
                }
                plugin.removingPlayers.remove(player.getName());
                plugin.isSpecial.remove(player.getName());
            } else if (plugin.lockingPlayers.contains(player.getName())) {
                ArrayList<String> blank = new ArrayList();
                Block ch = event.getClickedBlock();
                Lock l = plugin.ch.getLock(ch);
                if(l!=null) {
                    player.sendMessage(ChatColor.GRAY + "Chest already locked");
                } else {
                    plugin.ch.addLock(player.getName(), (Chest) event.getClickedBlock().getState(), blank);
                    player.sendMessage(ChatColor.GRAY + "Chest locked");
                }
                plugin.lockingPlayers.remove(player.getName());
                plugin.isSpecial.remove(player.getName());
            } else if (plugin.unlockingPlayers.contains(player.getName())) {
                if (plugin.ch.removeLock(player.getName(), (Chest) event.getClickedBlock().getState())) {
                    player.sendMessage(ChatColor.GRAY + "Chest unlocked");
                } else {
                    player.sendMessage(ChatColor.GRAY + "Unalbe to unlock chest");
                }
                plugin.unlockingPlayers.remove(player.getName());
                plugin.isSpecial.remove(player.getName());
            } else if(plugin.ch.playersPassing.containsKey(player.getName())) {
                String pass = plugin.ch.playersPassing.get(player.getName());
                Block ch = event.getClickedBlock();
                Lock l = plugin.ch.getLock(ch);
                if (l != null) {
                    if (l.getOwner().equals(player.getName())) {
                        l.setPassword(pass);
                        player.sendMessage(ChatColor.GRAY + "Password set to " + pass);
                    }
                } else {
                    player.sendMessage(ChatColor.GRAY + "That chest isn't locked.");
                }
                plugin.ch.playersPassing.remove(player.getName());
                plugin.isSpecial.remove(player.getName());
            } else if (plugin.ch.playersRemoving.containsKey(player.getName())) {
                String pass = plugin.ch.playersRemoving.get(player.getName());
                Block ch = event.getClickedBlock();
                Lock l = plugin.ch.getLock(ch);
                if (l != null) {
                    if (l.getOwner().equals(player.getName()) && l.getPassword().equals(pass)) {
                        l.setPassword("");
                        player.sendMessage(ChatColor.GRAY + "Password removed");
                    }
                } else {
                    player.sendMessage(ChatColor.GRAY + "That chest isn't locked.");
                }
                plugin.ch.playersRemoving.remove(player.getName());
                plugin.isSpecial.remove(player.getName());
            } else if (plugin.checking.contains(player.getName())) {
                Block ch = event.getClickedBlock();
                Lock l = plugin.ch.getLock(ch);
                if (l != null) {
                    player.sendMessage(ChatColor.GRAY + "Owner: " + l.getOwner());
                    String allowedPlayers = "[";
                    for (String s : l.getAllowedPlayers()) {
                        String n = ChatColor.GREEN + s;
                        n += ChatColor.GRAY + ",";
                        allowedPlayers += n;
                    }
                    String fAllowed = allowedPlayers.substring(0, allowedPlayers.length() - 1) + "]";
                    player.sendMessage(ChatColor.GRAY + "Allowed Players: " + fAllowed);
                    if (l.getPassword() != null) {
                        player.sendMessage(ChatColor.GRAY + "Password: " + l.getPassword());
                    }
                } else {
                    player.sendMessage(ChatColor.GRAY + "That chest isn't locked.");
                }
                plugin.checking.remove(player.getName());
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
            Lock l = plugin.ch.isLocked(event.getClickedBlock(), player);
            if (l == null) {
            } else {
                if (l.getPassword() == null) {
                    if(l.getAllowedPlayers().contains(player.getName()) || l.getOwner().equals(player.getName())) {
                        
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.GRAY + "Chest locked!");
                    }
                    
                } else {
                    switch (l.getPassword()) {
                        case "":
                            if (l.getOwner().equals(player.getName()) || l.getAllowedPlayers().contains(player.getName())) {
                            } else {
                                event.setCancelled(true);
                                player.sendMessage(ChatColor.GRAY + "Locked.");
                            }
                            break;
                        default:
                            if(plugin.ch.playersTyping.containsKey(player.getName())) {
                                event.setCancelled(true);
                                break;
                            }
                            event.setCancelled(true);
                            plugin.ch.playersTyping.put(player.getName(), l);
                            player.sendMessage(ChatColor.GRAY + "Chest locked! Enter password with /password *pwordhere*");
                            break;
                    }
                }
            }
        } else if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.getInventory().getItemInMainHand().getType() == Material.DIAMOND && (player.hasPermission("captainlock.create") || player.hasPermission("captainlock.*"))) {
            if(plugin.zh.setting1.contains(player.getName())) {
                ArrayList<Location> locs = new ArrayList();
                locs.add(event.getClickedBlock().getLocation());
                plugin.zh.locs.put(player.getName(), locs);
                player.sendMessage(ChatColor.GRAY + "First location set. Set next location.");
                plugin.zh.setting1.remove(player.getName());
                plugin.zh.setting2.add(player.getName());
                return;
            }
            if (plugin.zh.setting2.contains(player.getName())) {
                plugin.zh.locs.get(player.getName()).add(event.getClickedBlock().getLocation());
                plugin.zh.addZone(new Zone(plugin.zh.nextId(), player.getName(), plugin.zh.nodes.get(player.getName()), plugin.zh.locs.get(player.getName()).get(0), plugin.zh.locs.get(player.getName()).get(1), plugin.zh.offsets.get(player.getName())));
                player.sendMessage(ChatColor.GRAY + "Second location set. Your zone is now protected.");
                plugin.zh.setting2.remove(player.getName());
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission("captainlock.*")) {
            Location loc = event.getBlock().getLocation();
            Zone z = plugin.zh.isInside(loc);
            if(z!=null) {
                if(!player.hasPermission("captainlock." + z.getNode())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GRAY + "You can't break blocks here");    
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission("captainlock.*")) {
            Location loc = event.getBlock().getLocation();
            Zone z = plugin.zh.isInside(loc);
            if(z!=null) {
                if(!player.hasPermission("captainlock." + z.getNode())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GRAY + "You can't place blocks here");   
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(plugin.ch.playersTyping.containsKey(player.getName())) {
            if(!plugin.ch.playersWarned.contains(player.getName())) {
                player.sendMessage(ChatColor.GRAY + "You cannot move while entering a password.");
                player.sendMessage(ChatColor.GRAY + "Type '/password' to cancel password entry");
                plugin.ch.playersWarned.add(player.getName());
            }
            event.setCancelled(true);
        }
    }
}
