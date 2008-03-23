package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;
import org.apache.log4j.Logger;

public class Exerciselistitem extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int exerciselistitemid;
     private int exerciselistid;
     private int exerciseid;
     private int reps;
     private int num;



    public static Exerciselistitem get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Exerciselistitem");
        try {
            logger.debug("Exerciselistitem.get(" + id + ") called.");
            Exerciselistitem obj = (Exerciselistitem) HibernateUtil.getSession().get(Exerciselistitem.class, id);
            if (obj == null) {
                logger.debug("Exerciselistitem.get(" + id + ") returning new instance because hibernate returned null.");
                return new Exerciselistitem();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Exerciselistitem", ex);
            return new Exerciselistitem();
        }
    }

    // Constructors

    /** default constructor */
    public Exerciselistitem() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getExerciselistitemid() {
        return exerciselistitemid;
    }

    public void setExerciselistitemid(int exerciselistitemid) {
        this.exerciselistitemid = exerciselistitemid;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid = exerciseid;
    }

    public int getExerciselistid() {
        return exerciselistid;
    }

    public void setExerciselistid(int exerciselistid) {
        this.exerciselistid = exerciselistid;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
