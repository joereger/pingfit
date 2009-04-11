package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Exercisemusclegroup extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int exercisemusclegroupid;
    private int exerciseid;
    private int musclegroupid;



    public static Exercisemusclegroup get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Exercisemusclegroup");
        try {
            logger.debug("Exercisemusclegroup.get(" + id + ") called.");
            Exercisemusclegroup obj = (Exercisemusclegroup) HibernateUtil.getSession().get(Exercisemusclegroup.class, id);
            if (obj == null) {
                logger.debug("Exercisemusclegroup.get(" + id + ") returning new instance because hibernate returned null.");
                return new Exercisemusclegroup();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Exercisemusclegroup", ex);
            return new Exercisemusclegroup();
        }
    }

    // Constructors

    /** default constructor */
    public Exercisemusclegroup() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getExercisemusclegroupid() {
        return exercisemusclegroupid;
    }

    public void setExercisemusclegroupid(int exercisemusclegroupid) {
        this.exercisemusclegroupid=exercisemusclegroupid;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid=exerciseid;
    }

    public int getMusclegroupid() {
        return musclegroupid;
    }

    public void setMusclegroupid(int musclegroupid) {
        this.musclegroupid=musclegroupid;
    }
}