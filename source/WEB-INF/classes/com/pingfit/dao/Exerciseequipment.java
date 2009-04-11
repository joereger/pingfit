package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Exerciseequipment extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int exerciseequipmentid;
    private int exerciseid;
    private int equipmentid;



    public static Exerciseequipment get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Exerciseequipment");
        try {
            logger.debug("Exerciseequipment.get(" + id + ") called.");
            Exerciseequipment obj = (Exerciseequipment) HibernateUtil.getSession().get(Exerciseequipment.class, id);
            if (obj == null) {
                logger.debug("Exerciseequipment.get(" + id + ") returning new instance because hibernate returned null.");
                return new Exerciseequipment();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Exerciseequipment", ex);
            return new Exerciseequipment();
        }
    }

    // Constructors

    /** default constructor */
    public Exerciseequipment() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getExerciseequipmentid() {
        return exerciseequipmentid;
    }

    public void setExerciseequipmentid(int exerciseequipmentid) {
        this.exerciseequipmentid=exerciseequipmentid;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid=exerciseid;
    }

    public int getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(int equipmentid) {
        this.equipmentid=equipmentid;
    }
}