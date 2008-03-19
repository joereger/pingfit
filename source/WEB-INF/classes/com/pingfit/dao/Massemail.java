package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;
import com.pingfit.htmlui.Authorization;


import java.util.Date;

import org.apache.log4j.Logger;



public class Massemail extends BasePersistentClass implements java.io.Serializable, AuthControlled {

    public static int STATUS_NEW = 0;
    public static int STATUS_PROCESSING = 1;
    public static int STATUS_COMPLETE = 2;

    // Fields
     private int massemailid;
     private int status;
     private int lastuseridprocessed;
     private Date date;
     private String subject;
     private String txtmessage;
     private String htmlmessage;






    public static Massemail get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Massemail");
        try {
            logger.debug("Massemail.get(" + id + ") called.");
            Massemail obj = (Massemail) HibernateUtil.getSession().get(Massemail.class, id);
            if (obj == null) {
                logger.debug("Massemail.get(" + id + ") returning new instance because hibernate returned null.");
                return new Massemail();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Massemail", ex);
            return new Massemail();
        }
    }

    // Constructors

    /** default constructor */
    public Massemail() {
    }

    public boolean canRead(User user){
        if (Authorization.isUserSysadmin(user)){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors




    public int getMassemailid() {
        return massemailid;
    }

    public void setMassemailid(int massemailid) {
        this.massemailid = massemailid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLastuseridprocessed() {
        return lastuseridprocessed;
    }

    public void setLastuseridprocessed(int lastuseridprocessed) {
        this.lastuseridprocessed = lastuseridprocessed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTxtmessage() {
        return txtmessage;
    }

    public void setTxtmessage(String txtmessage) {
        this.txtmessage = txtmessage;
    }

    public String getHtmlmessage() {
        return htmlmessage;
    }

    public void setHtmlmessage(String htmlmessage) {
        this.htmlmessage = htmlmessage;
    }


}
