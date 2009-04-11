package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Exercisegenre extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int exercisegenreid;
    private int exerciseid;
    private int genreid;



    public static Exercisegenre get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Exercisegenre");
        try {
            logger.debug("Exercisegenre.get(" + id + ") called.");
            Exercisegenre obj = (Exercisegenre) HibernateUtil.getSession().get(Exercisegenre.class, id);
            if (obj == null) {
                logger.debug("Exercisegenre.get(" + id + ") returning new instance because hibernate returned null.");
                return new Exercisegenre();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Exercisegenre", ex);
            return new Exercisegenre();
        }
    }

    // Constructors

    /** default constructor */
    public Exercisegenre() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getExercisegenreid() {
        return exercisegenreid;
    }

    public void setExercisegenreid(int exercisegenreid) {
        this.exercisegenreid=exercisegenreid;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid=exerciseid;
    }

    public int getGenreid() {
        return genreid;
    }

    public void setGenreid(int genreid) {
        this.genreid=genreid;
    }
}