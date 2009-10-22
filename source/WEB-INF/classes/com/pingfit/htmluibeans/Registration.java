package com.pingfit.htmluibeans;

import org.apache.log4j.Logger;
import org.apache.commons.validator.EmailValidator;
import com.pingfit.dao.*;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.util.*;
import com.pingfit.htmlui.UserSession;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;
import com.pingfit.session.PersistentLogin;
import com.pingfit.email.EmailActivationSend;
import com.pingfit.money.PaymentMethod;
import com.pingfit.xmpp.SendXMPPMessage;
import com.pingfit.eula.EulaHelper;
import com.pingfit.exercisechoosers.ExerciseChooserList;
import com.pingfit.exercisechoosers.ExerciseChooserGroup;
import com.pingfit.api.CoreMethods;


import javax.servlet.http.Cookie;
import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class Registration implements Serializable {

    //Form props
    private String email;
    private String password;
    private String passwordverify;
    private String firstname;
    private String lastname;
    private String nickname;
    private String eula;
    private boolean displaytempresponsesavedmessage;
    private boolean isflashsignup = true;
    private int plid = 1;
    private String facebookuid = "";


    //private String temp;

    //Other props
    private int userid;

    public Registration(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        displaytempresponsesavedmessage = false;
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getPl()!=null){
            plid = Pagez.getUserSession().getPl().getPlid();
        } else {
            plid = 1;
        }
        eula = EulaHelper.getMostRecentEula(plid).getEula();

        
    }

    public void registerAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("registerAction called:  email="+email+" password="+password+" firstname="+firstname+" lastname="+lastname);

        //Validation
        boolean haveErrors = false;

        if (firstname==null || firstname.equals("")){
            vex.addValidationError("First Name can't be blank.");
            haveErrors = true;
        }

        if (lastname==null || lastname.equals("")){
            vex.addValidationError("Last Name can't be blank.");
            haveErrors = true;
        }

        if (nickname==null || nickname.equals("")){
            vex.addValidationError("Nickname can't be blank.");
            haveErrors = true;
        }
        

        if (nickname!=null && nickname.length()>15){
            vex.addValidationError("Nickname must be 15 characters or less.");
            haveErrors = true;
        }

        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)){
            vex.addValidationError("Not a valid email address.");
            haveErrors = true;
        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){
            vex.addValidationError("That email address is already in use.");
            haveErrors = true;
        }

        List<User> usersByNickname = HibernateUtil.getSession().createQuery("from User where nickname='"+ Str.cleanForSQL(nickname)+"'").list();
        if (usersByNickname.size()>0){
            vex.addValidationError("That Nickname is already in use.");
            haveErrors = true;
        }

        if (password==null || password.equals("") || password.length()<5){
            vex.addValidationError("Password must be at least five characters long.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            vex.addValidationError("Password and Verify Password must match.");
            haveErrors = true;
        }

        //@todo need to check for lcase(firstname), lcase(lastname), email in the database... people are changing caps on name and creating another account.

        if (eula==null || !eula.trim().equals(EulaHelper.getMostRecentEula(plid).getEula().trim())){
            //@todo Registration EULA validation
            //logger.debug("eula="+eula);
            //logger.debug("EulaHelper.getMostRecentEula().getEula()="+EulaHelper.getMostRecentEula().getEula());
            //vex.addValidationError("The end user license can't be edited.");
            //eula = EulaHelper.getMostRecentEula().getEula();
            //haveErrors = true;
        }

        if (haveErrors){
            throw vex;
        }

        //Create the user
        //@todo Use http://www.jasypt.org/ to encrypt password
        User user = new User();
        user.setEmail(email);
        user.setPlid(plid);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setNickname(nickname);
        if (isflashsignup){
            user.setIsactivatedbyemail(true);
        } else {
            user.setIsactivatedbyemail(false);
        }
        try{
            user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
            user.setEmailactivationlastsent(new Date());
            user.setCreatedate(new Date());
            user.setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
            user.setChargemethodcreditcardid(0);
            user.setIsenabled(true);
            user.setFacebookuid(facebookuid);
            user.setExerciseeveryxminutes(20);
            ExerciseChooserGroup lst = new ExerciseChooserGroup();
            user.setExercisechooserid(lst.getId());
            int exerciselistid = CoreMethods.getDefaultSystemExerciselist().getExerciselistid();
            user.setExerciselistid(exerciselistid);
            user.setLastexercisetime(Calendar.getInstance().getTime());
            user.setLastexerciseplaceinlist("");
            int roomid = CoreMethods.getDefaultSystemRoom().getRoomid();
            user.setRoomid(roomid);
            user.save();
            userid = user.getUserid();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            throw new ValidationException("An internal server error occurred.  Please try again.");
        }

        //Eula version check
        Usereula usereula = new Usereula();
        usereula.setDate(new Date());
        if (isflashsignup){
            usereula.setEulaid(0);
        } else {
            usereula.setEulaid(EulaHelper.getMostRecentEula(plid).getEulaid());
        }
        usereula.setUserid(user.getUserid());
        usereula.setIp(Pagez.getRequest().getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            throw new ValidationException("An internal server error occurred.  Please try again.");
        }
        user.getUsereulas().add(usereula);

        //Notify customer care group
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New pingFit User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+")");
        xmpp.send();

        if (!isflashsignup){
            //Send the activation email
            EmailActivationSend.sendActivationEmail(user);
            //Log user in
            UserSession userSession = new UserSession();
            userSession.setUser(user);
            userSession.setIsloggedin(true);
            userSession.setIsLoggedInToBeta(Pagez.getUserSession().getIsLoggedInToBeta());
            userSession.setIseulaok(true);
            userSession.setIsfacebookui(Pagez.getUserSession().getIsfacebookui());
            userSession.setFacebookSessionKey(Pagez.getUserSession().getFacebookSessionKey());
            //Set persistent login cookie
            Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
            //Add a cookies to the response
            for (int j = 0; j < cookies.length; j++) {
                Pagez.getResponse().addCookie(cookies[j]);
            }
            //Put userSession object into cache
            Pagez.setUserSessionAndUpdateCache(userSession);
        }

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

    public String getPasswordverify() {
        return passwordverify;
    }

    public void setPasswordverify(String passwordverify) {
        this.passwordverify = passwordverify;
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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }



    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }


    public boolean getDisplaytempresponsesavedmessage() {
        return displaytempresponsesavedmessage;
    }

    public void setDisplaytempresponsesavedmessage(boolean displaytempresponsesavedmessage) {
        this.displaytempresponsesavedmessage = displaytempresponsesavedmessage;
    }

    public boolean getIsflashsignup() {
        return isflashsignup;
    }

    public void setIsflashsignup(boolean isflashsignup) {
        this.isflashsignup=isflashsignup;
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
