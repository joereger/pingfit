package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Userfriend extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int userfriendid;
     private int userid;
     private int useridoffriend;
     private boolean ispendingapproval;
     private boolean isfulltwoway;


    public static Userfriend get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Userfriend");
        try {
            logger.debug("Userfriend.get(" + id + ") called.");
            Userfriend obj = (Userfriend) HibernateUtil.getSession().get(Userfriend.class, id);
            if (obj == null) {
                logger.debug("Userfriend.get(" + id + ") returning new instance because hibernate returned null.");
                return new Userfriend();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Userfriend", ex);
            return new Userfriend();
        }
    }

    // Constructors

    /** default constructor */
    public Userfriend() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public int getUseridoffriend() {
        return useridoffriend;
    }

    public void setUseridoffriend(int useridoffriend) {
        this.useridoffriend=useridoffriend;
    }

    public int getUserfriendid() {
        return userfriendid;
    }

    public void setUserfriendid(int userfriendid) {
        this.userfriendid=userfriendid;
    }

    public boolean getIspendingapproval() {
        return ispendingapproval;
    }

    public void setIspendingapproval(boolean ispendingapproval) {
        this.ispendingapproval=ispendingapproval;
    }

    public boolean isIsfulltwoway() {
        return isfulltwoway;
    }

    public void setIsfulltwoway(boolean isfulltwoway) {
        this.isfulltwoway=isfulltwoway;
    }
}