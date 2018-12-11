/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.lock;

import java.util.ArrayList;
import org.bukkit.block.Chest;

/**
 *
 * @author andre
 */
public class Lock {

    //Values
    private String owner;
    private Chest chest;
    private ArrayList<String> allowedPlayers;
    private String password;

    public Lock(String playername, Chest sChest, ArrayList<String> alPlayers) {
        owner = playername;
        chest = sChest;
        allowedPlayers = alPlayers;
        password = null;
    }
    
    public Lock(String playername, Chest sChest, String pword, ArrayList<String> alPlayers) {
        owner = playername;
        chest = sChest;
        password = pword;
        allowedPlayers = alPlayers;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the chest
     */
    public Chest getChest() {
        return chest;
    }

    /**
     * @return the allowedPlayers
     */
    public ArrayList<String> getAllowedPlayers() {
        return allowedPlayers;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @param chest the chest to set
     */
    public void setChest(Chest chest) {
        this.chest = chest;
    }

    /**
     * @param allowedPlayers the allowedPlayers to set
     */
    public void setAllowedPlayers(ArrayList<String> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public void addPlayer(String playername) {
        this.allowedPlayers.add(playername);
    }

    public void removePlayer(String playername) {
        if (this.allowedPlayers.contains(playername)) {
            this.allowedPlayers.remove(playername);
        }
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
