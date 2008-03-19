package com.pingfit.htmluibeans;

import org.apache.log4j.Logger;
import com.pingfit.dao.*;
import com.pingfit.dao.hibernate.HibernateUtil;

import com.pingfit.util.Str;
import com.pingfit.util.Time;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:11 AM
 */
public class PublicProfile implements Serializable {

    private User user;
    private String msg;
    private int socialinfluenceratingpercentile;
    private int socialinfluenceratingpercentile90days;
    private String charityamtdonatedForscreen;

    public PublicProfile(){

    }
    


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());




    }









    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }




    public int getSocialinfluenceratingpercentile() {
        return socialinfluenceratingpercentile;
    }

    public void setSocialinfluenceratingpercentile(int socialinfluenceratingpercentile) {
        this.socialinfluenceratingpercentile = socialinfluenceratingpercentile;
    }

    public int getSocialinfluenceratingpercentile90days() {
        return socialinfluenceratingpercentile90days;
    }

    public void setSocialinfluenceratingpercentile90days(int socialinfluenceratingpercentile90days) {
        this.socialinfluenceratingpercentile90days = socialinfluenceratingpercentile90days;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCharityamtdonatedForscreen() {
        return charityamtdonatedForscreen;
    }

    public void setCharityamtdonatedForscreen(String charityamtdonatedForscreen) {
        this.charityamtdonatedForscreen = charityamtdonatedForscreen;
    }
}
