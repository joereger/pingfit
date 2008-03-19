package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Usereula extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int usereulaid;
     private int userid;
     private int eulaid;
     private Date date;
     private String ip;



    public static Usereula get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Usereula");
        try {
            logger.debug("Usereula.get(" + id + ") called.");
            Usereula obj = (Usereula) HibernateUtil.getSession().get(Usereula.class, id);
            if (obj == null) {
                logger.debug("Usereula.get(" + id + ") returning new instance because hibernate returned null.");
                return new Usereula();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Usereula", ex);
            return new Usereula();
        }
    }

    // Constructors

    /** default constructor */
    public Usereula() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public int getUsereulaid() {
        return usereulaid;
    }

    public void setUsereulaid(int usereulaid) {
        this.usereulaid = usereulaid;
    }

    public int getEulaid() {
        return eulaid;
    }

    public void setEulaid(int eulaid) {
        this.eulaid = eulaid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
