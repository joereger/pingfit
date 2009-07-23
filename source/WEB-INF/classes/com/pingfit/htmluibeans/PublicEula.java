package com.pingfit.htmluibeans;

import com.pingfit.eula.EulaHelper;
import com.pingfit.dao.User;
import com.pingfit.dao.Usereula;

import com.pingfit.util.GeneralException;
import com.pingfit.htmlui.Pagez;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:48:46 PM
 */
public class PublicEula implements Serializable {

    private String eula;

    public PublicEula(){

    }



    public void initBean(){
        eula = EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl().getPlid()).getEula();
    }


    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }
}
