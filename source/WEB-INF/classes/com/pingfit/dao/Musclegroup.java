package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Musclegroup extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int musclegroupid;
     private String name;



    public static Musclegroup get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Musclegroup");
        try {
            logger.debug("Musclegroup.get(" + id + ") called.");
            Musclegroup obj = (Musclegroup) HibernateUtil.getSession().get(Musclegroup.class, id);
            if (obj == null) {
                logger.debug("Musclegroup.get(" + id + ") returning new instance because hibernate returned null.");
                return new Musclegroup();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Musclegroup", ex);
            return new Musclegroup();
        }
    }

    // Constructors

    /** default constructor */
    public Musclegroup() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getMusclegroupid() {
        return musclegroupid;
    }

    public void setMusclegroupid(int musclegroupid) {
        this.musclegroupid=musclegroupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}