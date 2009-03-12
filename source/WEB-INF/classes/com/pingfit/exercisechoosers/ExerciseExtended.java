package com.pingfit.exercisechoosers;

import com.pingfit.dao.Exercise;

/**
 * User: Joe Reger Jr
 * Date: Mar 10, 2009
 * Time: 11:55:20 AM
 */
public class ExerciseExtended {

    private Exercise exercise;
    private int repsfromlist;
    private String exerciseplaceinlist;
    private int secondsuntilnextexercise=0;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise=exercise;
    }

    public int getRepsfromlist() {
        return repsfromlist;
    }

    public void setRepsfromlist(int repsfromlist) {
        this.repsfromlist=repsfromlist;
    }

    public String getExerciseplaceinlist() {
        return exerciseplaceinlist;
    }

    public void setExerciseplaceinlist(String exerciseplaceinlist) {
        this.exerciseplaceinlist=exerciseplaceinlist;
    }

    public int getSecondsuntilnextexercise() {
        return secondsuntilnextexercise;
    }

    public void setSecondsuntilnextexercise(int secondsuntilnextexercise) {
        this.secondsuntilnextexercise=secondsuntilnextexercise;
    }
}
