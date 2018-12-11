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
import org.bukkit.block.Chest;

/**
 *
 * @author andre
 */
public class PasswordCmd implements CommandExecutor {

    public CaptainLock plugin;
    
    public PasswordCmd(CaptainLock aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            switch (args.length) {
                case 0:
                    if(plugin.ch.playersTyping.containsKey(player.getName())) {
                        player.sendMessage(ChatColor.GRAY + "Cancelled password entry");
                        plugin.ch.playersTyping.remove(player.getName());
                    }
                    if(plugin.ch.playersWarned.contains(player.getName())) {
                        plugin.ch.playersWarned.remove(player.getName());
                    }
                    break;
                case 1:
                    String pass = args[0];
                    if(plugin.ch.playersTyping.containsKey(player.getName())) {
                        Lock l = plugin.ch.playersTyping.get(player.getName());
                        String pword = l.getPassword();
                        if(pass.equals(pword)) {
                            Chest c = (Chest) l.getChest().getWorld().getBlockAt(l.getChest().getLocation()).getState();
                            player.openInventory(c.getBlockInventory().getHolder().getInventory());
                        } else {
                            player.sendMessage(ChatColor.GRAY + "Wrong password");
                        }
                        plugin.ch.playersTyping.remove(player.getName());
                    } else {
                        player.sendMessage(ChatColor.GRAY + "No chest chosen");
                    }
                    if(plugin.ch.playersWarned.contains(player.getName())) {
                        plugin.ch.playersWarned.remove(player.getName());
                    }
                    break;
                case 2:
                    if(!player.hasPermission("captainlock.*")) {
                        return false;
                    }
                    String arg = args[0];
                    String pword = args[1];
                    if(arg.equals("add")) {
                        plugin.ch.playersPassing.put(player.getName(), pword);
                        plugin.isSpecial.add(player.getName());
                        player.sendMessage(ChatColor.GRAY + "Choose the chest you wish to place a password on");
                    } else if(arg.equals("remove")) {
                        plugin.ch.playersRemoving.put(player.getName(), pword);
                        plugin.isSpecial.add(player.getName());
                        player.sendMessage(ChatColor.GRAY + "Choose the chest to remove the password from");
                    }
            }
        }
        return false;
    }
    
}
