package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
// Generated Apr 17, 2006 3:45:25 PM by Hibernate Tools 3.1.0.beta4



/**
 * User generated by hbm2java
 */

public class User extends BasePersistentClass implements java.io.Serializable, AuthControlled {


     private int userid;
     private boolean isenabled;
     private String email;
     private String password;
     private String firstname;
     private String lastname;
     private String nickname;
     private String facebookuid;
     private boolean isactivatedbyemail;
     private String emailactivationkey;
     private Date emailactivationlastsent;
     private Date createdate;
     private int chargemethod;
     private int chargemethodcreditcardid;
     private int exerciseeveryxminutes;
     private int exercisechooserid;
     private int exerciselistid;
     private Date lastexercisetime;
     private String lastexerciseplaceinlist;
     private int roomid;
     private int plid;

    //Association
    private Set<Userrole> userroles = new HashSet<Userrole>();
    private Set<Usereula> usereulas = new HashSet<Usereula>();




    public static User get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.User");
        try {
            logger.debug("User.get(" + id + ") called.");
            User obj = (User) HibernateUtil.getSession().get(User.class, id);
            if (obj == null) {
                logger.debug("User.get(" + id + ") returning new instance because hibernate returned null.");
                return new User();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.User", ex);
            return new User();
        }
    }

    // Constructors

    /** default constructor */
    public User() {
    }

    public boolean canRead(User user){
        if (user.getUserid()==userid){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }

    
    /** full constructor */
    public User(int userid, String email, String password, String firstname, String lastname) {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    

   
    // Property accessors


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean getIsactivatedbyemail() {
        return isactivatedbyemail;
    }

    public void setIsactivatedbyemail(boolean isactivatedbyemail) {
        this.isactivatedbyemail = isactivatedbyemail;
    }

    public String getEmailactivationkey() {
        return emailactivationkey;
    }

    public void setEmailactivationkey(String emailactivationkey) {
        this.emailactivationkey = emailactivationkey;
    }

    public Date getEmailactivationlastsent() {
        return emailactivationlastsent;
    }

    public void setEmailactivationlastsent(Date emailactivationlastsent) {
        this.emailactivationlastsent = emailactivationlastsent;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public int getChargemethod() {
        return chargemethod;
    }

    public void setChargemethod(int chargemethod) {
        this.chargemethod = chargemethod;
    }

    public int getChargemethodcreditcardid() {
        return chargemethodcreditcardid;
    }

    public void setChargemethodcreditcardid(int chargemethodcreditcardid) {
        this.chargemethodcreditcardid = chargemethodcreditcardid;
    }

    public Set<Userrole> getUserroles() {
        return userroles;
    }

    public void setUserroles(Set<Userrole> userroles) {
        this.userroles = userroles;
    }

    public Set<Usereula> getUsereulas() {
        return usereulas;
    }

    public void setUsereulas(Set<Usereula> usereulas) {
        this.usereulas = usereulas;
    }

    public int getExerciseeveryxminutes() {
        return exerciseeveryxminutes;
    }

    public void setExerciseeveryxminutes(int exerciseeveryxminutes) {
        this.exerciseeveryxminutes = exerciseeveryxminutes;
    }

    public int getExercisechooserid() {
        return exercisechooserid;
    }

    public void setExercisechooserid(int exercisechooserid) {
        this.exercisechooserid = exercisechooserid;
    }

    public int getExerciselistid() {
        return exerciselistid;
    }

    public void setExerciselistid(int exerciselistid) {
        this.exerciselistid = exerciselistid;
    }

    public Date getLastexercisetime() {
        return lastexercisetime;
    }

    public void setLastexercisetime(Date lastexercisetime) {
        this.lastexercisetime=lastexercisetime;
    }

    public String getLastexerciseplaceinlist() {
        return lastexerciseplaceinlist;
    }

    public void setLastexerciseplaceinlist(String lastexerciseplaceinlist) {
        this.lastexerciseplaceinlist=lastexerciseplaceinlist;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid=roomid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid=plid;
    }

    public String getFacebookuid() {
        return facebookuid;
    }

    public void setFacebookuid(String facebookuid) {
        this.facebookuid=facebookuid;
    }
}
