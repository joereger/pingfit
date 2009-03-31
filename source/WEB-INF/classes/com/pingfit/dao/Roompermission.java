package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Roompermission extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int roompermissionid;
     private int roomid;
     private int userid;
     private boolean ismoderator;
     private boolean ispendingapproval;



    public static Roompermission get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Roompermission");
        try {
            logger.debug("Roompermission.get(" + id + ") called.");
            Roompermission obj = (Roompermission) HibernateUtil.getSession().get(Roompermission.class, id);
            if (obj == null) {
                logger.debug("Roompermission.get(" + id + ") returning new instance because hibernate returned null.");
                return new Roompermission();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Roompermission", ex);
            return new Roompermission();
        }
    }

    // Constructors

    /** default constructor */
    public Roompermission() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getRoompermissionid() {
        return roompermissionid;
    }

    public void setRoompermissionid(int roompermissionid) {
        this.roompermissionid=roompermissionid;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid=roomid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public boolean getIspendingapproval() {
        return ispendingapproval;
    }

    public void setIspendingapproval(boolean ispendingapproval) {
        this.ispendingapproval=ispendingapproval;
    }

    public boolean getIsmoderator() {
        return ismoderator;
    }

    public void setIsmoderator(boolean ismoderator) {
        this.ismoderator=ismoderator;
    }

}