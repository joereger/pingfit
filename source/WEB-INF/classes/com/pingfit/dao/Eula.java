package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Eula extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int eulaid;
     private String eula;
     private Date date;
     private int plid;




    public static Eula get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Eula");
        try {
            logger.debug("Eula.get(" + id + ") called.");
            Eula obj = (Eula) HibernateUtil.getSession().get(Eula.class, id);
            if (obj == null) {
                logger.debug("Eula.get(" + id + ") returning new instance because hibernate returned null.");
                return new Eula();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Eula", ex);
            return new Eula();
        }
    }

    // Constructors

    /** default constructor */
    public Eula() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getEulaid() {
        return eulaid;
    }

    public void setEulaid(int eulaid) {
        this.eulaid = eulaid;
    }

    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid=plid;
    }
}
