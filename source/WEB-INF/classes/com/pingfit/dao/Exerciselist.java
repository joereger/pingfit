package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;

public class Exerciselist extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int exerciselistid;
     private String title;
     private String description;
     private boolean issystem;
     private int useridofcreator;
     private boolean ispublic;

    private Set<Exerciselistitem> exerciselistitems = new HashSet<Exerciselistitem>();


    public static Exerciselist get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Exerciselist");
        try {
            logger.debug("Exerciselist.get(" + id + ") called.");
            Exerciselist obj = (Exerciselist) HibernateUtil.getSession().get(Exerciselist.class, id);
            if (obj == null) {
                logger.debug("Exerciselist.get(" + id + ") returning new instance because hibernate returned null.");
                return new Exerciselist();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Exerciselist", ex);
            return new Exerciselist();
        }
    }

    // Constructors

    /** default constructor */
    public Exerciselist() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getExerciselistid() {
        return exerciselistid;
    }

    public void setExerciselistid(int exerciselistid) {
        this.exerciselistid = exerciselistid;
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


    public Set<Exerciselistitem> getExerciselistitems() {
        return exerciselistitems;
    }

    public void setExerciselistitems(Set<Exerciselistitem> exerciselistitems) {
        this.exerciselistitems = exerciselistitems;
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
}
