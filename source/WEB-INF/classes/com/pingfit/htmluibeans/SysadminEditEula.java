package com.pingfit.htmluibeans;

import com.pingfit.eula.EulaHelper;
import com.pingfit.dao.Eula;
import com.pingfit.util.GeneralException;

import com.pingfit.util.Time;
import com.pingfit.util.Num;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;
import com.pingfit.privatelabel.PlFinder;
import com.mysql.jdbc.TimeUtil;

import java.util.Date;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 3:02:56 PM
 */
public class SysadminEditEula implements Serializable {

    private String eula;
    private int eulaid;
    private String date;
    private int plid;

    public SysadminEditEula(){

    }



    public void initBean(){
        if (Num.isinteger(Pagez.getRequest().getParameter("plid"))){
            plid = Integer.parseInt(Pagez.getRequest().getParameter("plid"));
        } else {
            plid =  PlFinder.defaultPl().getPlid();
        }
        eula = EulaHelper.getMostRecentEula(plid).getEula();
        eulaid = EulaHelper.getMostRecentEula(plid).getEulaid();
        date = Time.dateformatcompactwithtime(Time.getCalFromDate(EulaHelper.getMostRecentEula(plid).getDate()));
    }

    public void edit() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (!eula.equals(EulaHelper.getMostRecentEula(plid).getEula())){
            Eula eulaObj = new Eula();
            eulaObj.setDate(new Date());
            eulaObj.setEula(eula);
            eulaObj.setPlid(plid);
            try{
                eulaObj.save();
            } catch (GeneralException gex){
                logger.error(gex);
                logger.debug("agree failed: " + gex.getErrorsAsSingleString());
                Pagez.getUserSession().setMessage("Error... please try again.");
                throw new ValidationException("Error with db.");
            }
        }
    }


    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }


    public int getEulaid() {
        return eulaid;
    }

    public void setEulaid(int eulaid) {
        this.eulaid = eulaid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid=plid;
    }
}
