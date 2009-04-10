package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Invitebyemail extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int invitebyemailid;
     private int userid;
     private Date date;
     private String email;
    private String custommessage;
    private boolean isaccepted;
    private Date acceptdate;


    public static Invitebyemail get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Invitebyemail");
        try {
            logger.debug("Invitebyemail.get(" + id + ") called.");
            Invitebyemail obj = (Invitebyemail) HibernateUtil.getSession().get(Invitebyemail.class, id);
            if (obj == null) {
                logger.debug("Invitebyemail.get(" + id + ") returning new instance because hibernate returned null.");
                return new Invitebyemail();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Invitebyemail", ex);
            return new Invitebyemail();
        }
    }

    // Constructors

    /** default constructor */
    public Invitebyemail() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getInvitebyemailid() {
        return invitebyemailid;
    }

    public void setInvitebyemailid(int invitebyemailid) {
        this.invitebyemailid=invitebyemailid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public boolean getIsaccepted() {
        return isaccepted;
    }

    public void setIsaccepted(boolean isaccepted) {
        this.isaccepted=isaccepted;
    }

    public Date getAcceptdate() {
        return acceptdate;
    }

    public void setAcceptdate(Date acceptdate) {
        this.acceptdate=acceptdate;
    }

    public String getCustommessage() {
        return custommessage;
    }

    public void setCustommessage(String custommessage) {
        this.custommessage=custommessage;
    }
}