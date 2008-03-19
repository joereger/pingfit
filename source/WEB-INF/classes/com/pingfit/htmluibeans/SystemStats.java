package com.pingfit.htmluibeans;



import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Feb 21, 2007
 * Time: 11:26:03 AM
 */
public class SystemStats implements Serializable {

    //BE SURE TO SYNC CODE HERE WITH MAIN SystemStats in scheduledjobs
    //These need to be here because of direct POJO injection by the framework
    private int totalusers=com.pingfit.scheduledjobs.SystemStats.getTotalusers();
    private double systembalance=com.pingfit.scheduledjobs.SystemStats.getSystembalance();
    private double systembalancerealworld=com.pingfit.scheduledjobs.SystemStats.getSystembalancerealworld();
    private double systembalancetotal=com.pingfit.scheduledjobs.SystemStats.getSystembalancetotal();

    public SystemStats(){}

    public void initBean(){
        
    }

    public int getTotalusers() {
        return com.pingfit.scheduledjobs.SystemStats.getTotalusers();
    }

    public void setTotalusers(int totalusers) {

    }



    public double getSystembalance() {
        return com.pingfit.scheduledjobs.SystemStats.getSystembalance();
    }

    public void setSystembalance(double systembalance) {

    }

    public double getSystembalancerealworld() {
        return com.pingfit.scheduledjobs.SystemStats.getSystembalancerealworld();
    }

    public void setSystembalancerealworld(double systembalancerealworld) {

    }

    public double getSystembalancetotal() {
        return com.pingfit.scheduledjobs.SystemStats.getSystembalancetotal();
    }

    public void setSystembalancetotal(double systembalancetotal) {

    }




}
