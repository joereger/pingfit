package com.pingfit.htmlui;

import com.pingfit.dao.Pingback;
import com.pingfit.dao.Exercise;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 3:37:30 PM
 */
public class PingbackListItem {

    private Pingback pingback;
    private Exercise exercise;

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
}
