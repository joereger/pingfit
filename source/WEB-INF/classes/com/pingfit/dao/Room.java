package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
// Generated Apr 17, 2006 3:45:25 PM by Hibernate Tools 3.1.0.beta4


/**
 * User generated by hbm2java
 */

public class Room extends BasePersistentClass implements java.io.Serializable, AuthControlled {

     private int roomid;
     private boolean isenabled;
     private boolean issystem;
     private boolean isprivate;
     private boolean isfriendautopermit;
     private int useridofcreator;
     private String name;
     private String description;
     private Date createdate;
     private int exerciselistid;
     private Date lastexercisetime;
     private String lastexerciseplaceinlist;

    public static Room get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Room");
        try {
            logger.debug("Room.get(" + id + ") called.");
            Room obj = (Room) HibernateUtil.getSession().get(Room.class, id);
            if (obj == null) {
                logger.debug("Room.get(" + id + ") returning new instance because hibernate returned null.");
                return new Room();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Room", ex);
            return new Room();
        }
    }

    // Constructors

    /** default constructor */
    public Room() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }





    // Property accessors

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid=roomid;
    }

    public boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled=isenabled;
    }

    public boolean getIssystem() {
        return issystem;
    }

    public void setIssystem(boolean issystem) {
        this.issystem=issystem;
    }

    public int getUseridofcreator() {
        return useridofcreator;
    }

    public void setUseridofcreator(int useridofcreator) {
        this.useridofcreator=useridofcreator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate=createdate;
    }

    public int getExerciselistid() {
        return exerciselistid;
    }

    public void setExerciselistid(int exerciselistid) {
        this.exerciselistid=exerciselistid;
    }

    public Date getLastexercisetime() {
        return lastexercisetime;
    }

    public void setLastexercisetime(Date lastexercisetime) {
        this.lastexercisetime=lastexercisetime;
    }

    public String getLastexerciseplaceinlist() {
        return lastexerciseplaceinlist;
    }

    public void setLastexerciseplaceinlist(String lastexerciseplaceinlist) {
        this.lastexerciseplaceinlist=lastexerciseplaceinlist;
    }

    public boolean getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(boolean isprivate) {
        this.isprivate=isprivate;
    }

    public boolean getIsfriendautopermit() {
        return isfriendautopermit;
    }

    public void setIsfriendautopermit(boolean isfriendautopermit) {
        this.isfriendautopermit=isfriendautopermit;
    }
}