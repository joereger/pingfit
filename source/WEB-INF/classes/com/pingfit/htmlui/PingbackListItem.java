package com.pingfit.htmlui;

import com.pingfit.dao.Pingback;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 3:37:30 PM
 */
public class PingbackListItem {

    private Pingback pingback;
    private Exercise exercise;
    private User user;
    private String userinterfaceStr = "";

    public Pingback getPingback() {
        return pingback;
    }

    public void setPingback(Pingback pingback) {
        this.pingback = pingback;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public String getUserinterfaceStr() {
        return userinterfaceStr;
    }

    public void setUserinterfaceStr(String userinterfaceStr) {
        this.userinterfaceStr=userinterfaceStr;
    }
}
