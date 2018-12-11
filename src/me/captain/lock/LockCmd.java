/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;


/**
 *
 * @author andre
 */
public class LockCmd implements CommandExecutor {

    public CaptainLock plugin;
    
    public LockCmd(CaptainLock aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if ((cs instanceof Player)) {
            Player player = (Player) cs;
            switch (args.length) {
                case 0:
                    if(player.hasPermission("captainlock.lock") || player.hasPermission("captainlock.*")) {
                        player.sendMessage(ChatColor.GRAY + "Left click the chest you wish to lock");
                        plugin.lockingPlayers.add(player.getName());
                        plugin.isSpecial.add(player.getName());
                    } else {
                        player.sendMessage(ChatColor.GRAY + "You don't have permission to lock chests");
                    }
                    break;
                case 1:
                    String arg1 = args[0];
                    if(arg1.equals("reload") && player.hasPermission("captainlock.*")) {
                        plugin.ch.reloadChests();
                    } else if (arg1.equals("info") && player.hasPermission("captainlock.*")) {
                        if(player.hasPermission("captainlock.*")) {
                            player.sendMessage(ChatColor.GRAY + "Left click the chest you want info for");
                            plugin.checking.add(player.getName());
                            plugin.isSpecial.add(player.getName());
                        }
                    } else if (player.hasPermission("captainlock.*") && arg1.equals("save")) {
                        plugin.ch.saveChests();
                    }
                    break;
                case 2:
                    String arg = args[0];
                    String newPlayer = args[1];
                    if(arg.equals("add")) {
                        if(player.hasPermission("captainlock.lock") || player.hasPermission("captainlock.*")) {
                            player.sendMessage(ChatColor.GRAY + "Left click the chest you wish to let " + newPlayer + " access");
                            plugin.addingPlayers.put(player.getName(), newPlayer);
                            plugin.isSpecial.add(player.getName());
                        }
                    } else if (arg.equals("remove")) {
                        if(player.hasPermission("captainlock.lock") || player.hasPermission("captainlock.*")) {
                            player.sendMessage(ChatColor.GRAY + "Left click the chest you wish to lock " + newPlayer + " out of");
                            plugin.removingPlayers.put(player.getName(), newPlayer);
                            plugin.isSpecial.add(player.getName());
                        }
                    }
                    break;
            }
        }
        return true;
    }
    
}
