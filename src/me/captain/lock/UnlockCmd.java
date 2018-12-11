/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class UnlockCmd implements CommandExecutor {

    public CaptainLock plugin;
    
    public UnlockCmd(CaptainLock aThis) {
        plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if ((cs instanceof Player)) {
            Player player = (Player) cs;
            switch (args.length) {
                case 0:
                    if(player.hasPermission("captainlock.lock") || player.hasPermission("captainlock.*")) {
                        player.sendMessage(ChatColor.GRAY + "Left click the chest you wish to unlock");
                        plugin.unlockingPlayers.add(player.getName());
                        plugin.isSpecial.add(player.getName());
                    }
            }
        }
        return true;
    }
    
}
