/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.spawner.model;

/**
 *
 * @author Daniel
 */
public class PlayerProfile {
    
    public PlayerProfile() {
    }
    
    public PlayerProfile(String name,Boolean ban,long expTime){ 
        
        this.name = name;
        this.ban = ban;
        this.expTime = expTime;
        
    }
    
    private String name;
    private Boolean ban;
    private long expTime;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ban
     */
    public Boolean getBan() {
        return ban;
    }

    /**
     * @param ban the ban to set
     */
    public void setBan(Boolean ban) {
        this.ban = ban;
    }

    /**
     * @return the expTime
     */
    public long getExpTime() {
        return expTime;
    }

    /**
     * @param expTime the expTime to set
     */
    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }
}
