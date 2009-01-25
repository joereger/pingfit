package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;

public class Pingback extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int pingbackid;
     private int userid;
     private Date date;
     private int exerciseid;
     private int reps;
     private int userinterface;


    public static Pingback get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Pingback");
        try {
            logger.debug("Pingback.get(" + id + ") called.");
            Pingback obj = (Pingback) HibernateUtil.getSession().get(Pingback.class, id);
            if (obj == null) {
                logger.debug("Pingback.get(" + id + ") returning new instance because hibernate returned null.");
                return new Pingback();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Pingback", ex);
            return new Pingback();
        }
    }

    // Constructors

    /** default constructor */
    public Pingback() {
    }

    public boolean canRead(User user){
        if (user.getUserid()==userid){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getPingbackid() {
        return pingbackid;
    }

    public void setPingbackid(int pingbackid) {
        this.pingbackid = pingbackid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid = exerciseid;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getUserinterface() {
        return userinterface;
    }

    public void setUserinterface(int userinterface) {
        this.userinterface=userinterface;
    }
}
