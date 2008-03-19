package com.pingfit.api;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Mar 15, 2008
 * Time: 2:03:29 PM
 */
public class CompletedExercise {

    private int exerciseid;
    private int reps;
    private Calendar date;


    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid = exerciseid;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
