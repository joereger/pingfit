package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Genre extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int genreid;
     private String name;



    public static Genre get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Genre");
        try {
            logger.debug("Genre.get(" + id + ") called.");
            Genre obj = (Genre) HibernateUtil.getSession().get(Genre.class, id);
            if (obj == null) {
                logger.debug("Genre.get(" + id + ") returning new instance because hibernate returned null.");
                return new Genre();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Genre", ex);
            return new Genre();
        }
    }

    // Constructors

    /** default constructor */
    public Genre() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getGenreid() {
        return genreid;
    }

    public void setGenreid(int genreid) {
        this.genreid=genreid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}