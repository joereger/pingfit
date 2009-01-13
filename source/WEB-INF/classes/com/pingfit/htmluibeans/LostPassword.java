package com.pingfit.htmluibeans;

import org.apache.log4j.Logger;

import com.pingfit.util.Str;
import com.pingfit.dao.User;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.email.EmailActivationSend;
import com.pingfit.email.LostPasswordSend;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;


import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPassword implements Serializable {

    //Form props
    private String email;


    public LostPassword(){
        
    }

    public void initBean(){

    }

    public void recoverPassword() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());


        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){
            for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
                User user = iterator.next();
                LostPasswordSend.sendLostPasswordEmail(user);
            }
        } else {
            vex.addValidationError("Email address not found.");
            throw vex;
        }

    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    
}
