package com.pingfit.htmluibeans;

import com.pingfit.eula.EulaHelper;
import com.pingfit.dao.User;
import com.pingfit.dao.Usereula;

import com.pingfit.util.GeneralException;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;

import java.util.Date;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:48:46 PM
 */
public class LoginAgreeNewEula implements Serializable {

    private String eula;
    private String init;

    public LoginAgreeNewEula(){

    }

    public void initBean(){
        eula = EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl().getPlid()).getEula();
    }

    public void agree() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = Pagez.getUserSession().getUser();

        if (!eula.equals(EulaHelper.getMostRecentEula(user.getPlid()).getEula())){
            //@todo LoginAgreeNewEula validate eula
            //vex.addValidationError("The end user license can't be edited.");
            //eula = EulaHelper.getMostRecentEula().getEula();
            //throw vex;
        }

        Usereula usereula = new Usereula();
        usereula.setDate(new Date());
        usereula.setEulaid(EulaHelper.getMostRecentEula(user.getPlid()).getEulaid());
        usereula.setUserid(user.getUserid());
        usereula.setIp(Pagez.getRequest().getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.error(gex);
            logger.debug("agree failed: " + gex.getErrorsAsSingleString());
            vex.addValidationError("Error... please try again.");
            throw vex;
        }
        Pagez.getUserSession().setIseulaok(true);




    }


    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (init!=null && init.equals("doinit")){
            logger.debug("init = doinit so calling load()");
            initBean();
        } else {
            logger.debug("init null or not doinit");
        }
    }

    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }
}
