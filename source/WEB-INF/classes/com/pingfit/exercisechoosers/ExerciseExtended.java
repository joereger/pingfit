package com.pingfit.exercisechoosers;

import com.pingfit.dao.Exercise;

/**
 * User: Joe Reger Jr
 * Date: Mar 10, 2009
 * Time: 11:55:20 AM
 */
public class ExerciseExtended {

    private Exercise exercise;
    private int reps;
    private String exerciseplaceinlist;
    private int timeinseconds=0;
    private int exerciselistitemid;
    private int exerciselistid;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise=exercise;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps=reps;
    }

    public String getExerciseplaceinlist() {
        return exerciseplaceinlist;
    }

    public void setExerciseplaceinlist(String exerciseplaceinlist) {
        this.exerciseplaceinlist=exerciseplaceinlist;
    }

    public int getTimeinseconds() {
        return timeinseconds;
    }

    public void setTimeinseconds(int timeinseconds) {
        this.timeinseconds=timeinseconds;
    }

    public int getExerciselistitemid() {
        return exerciselistitemid;
    }

    public void setExerciselistitemid(int exerciselistitemid) {
        this.exerciselistitemid=exerciselistitemid;
    }

    public int getExerciselistid() {
        return exerciselistid;
    }

    public void setExerciselistid(int exerciselistid) {
        this.exerciselistid=exerciselistid;
    }
}
