package me.captain.lock;

import org.bukkit.Location;

/**
 *
 * @author andre
 */
public class Zone {

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the offsetY
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * @param offsetY the offsetY to set
     */
    public void setOffsetY(Integer offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the node
     */
    public String getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(String node) {
        this.node = node;
    }
    
    private Integer id;
    private Location loc1;
    private Location loc2;
    private Integer offsetY;
    private String owner;
    private String node;
    
    public Zone(int idnumber, String nOwner, String nNode, Location nLoc1, Location nLoc2, int nOffsetY) {
        id = idnumber;
        owner = nOwner;
        node = nNode;
        loc1 = nLoc1;
        loc2 = nLoc2;
        offsetY = nOffsetY;
    }

    /**
     * @return the loc1
     */
    public Location getLoc1() {
        return loc1;
    }

    /**
     * @param loc1 the loc1 to set
     */
    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    /**
     * @return the loc2
     */
    public Location getLoc2() {
        return loc2;
    }

    /**
     * @param loc2 the loc2 to set
     */
    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }
}
