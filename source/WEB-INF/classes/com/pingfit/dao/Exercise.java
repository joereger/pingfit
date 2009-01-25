package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;

public class Exercise extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int exerciseid;
     private String title;
     private String description;
     private int reps;
     private String image;
     private boolean issystem;
     private int useridofcreator;
     private boolean ispublic;
     private String imagecredit;


    public static Exercise get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Exercise");
        try {
            logger.debug("Exercise.get(" + id + ") called.");
            Exercise obj = (Exercise) HibernateUtil.getSession().get(Exercise.class, id);
            if (obj == null) {
                logger.debug("Exercise.get(" + id + ") returning new instance because hibernate returned null.");
                return new Exercise();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Exercise", ex);
            return new Exercise();
        }
    }

    // Constructors

    /** default constructor */
    public Exercise() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid = exerciseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getIssystem() {
        return issystem;
    }

    public void setIssystem(boolean issystem) {
        this.issystem = issystem;
    }

    public int getUseridofcreator() {
        return useridofcreator;
    }

    public void setUseridofcreator(int useridofcreator) {
        this.useridofcreator = useridofcreator;
    }

    public boolean getIspublic() {
        return ispublic;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public String getImagecredit() {
        return imagecredit;
    }

    public void setImagecredit(String imagecredit) {
        this.imagecredit=imagecredit;
    }
}
