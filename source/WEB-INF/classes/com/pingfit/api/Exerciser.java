package com.pingfit.api;

import java.util.Calendar;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Mar 15, 2008
 * Time: 2:01:57 PM
 */
public class Exerciser {

    private int userid=0;
    private int nextexerciseid=1;
    private Calendar nextexercisetime=Calendar.getInstance();
    private ArrayList<CompletedExercise> completedexercises=new ArrayList<CompletedExercise>();
    private ArrayList<Integer> upcomingexercises=new ArrayList<Integer>();
    private Calendar exercisesessionstarted=Calendar.getInstance();
    private int exerciseeveryxminutes=20;
    private int exercisechooserid=1;


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getNextexerciseid() {
        return nextexerciseid;
    }

    public void setNextexerciseid(int nextexerciseid) {
        this.nextexerciseid = nextexerciseid;
    }

    public ArrayList<CompletedExercise> getCompletedexercises() {
        return completedexercises;
    }

    public void setCompletedexercises(ArrayList<CompletedExercise> completedexercises) {
        this.completedexercises = completedexercises;
    }

    public Calendar getExercisesessionstarted() {
        return exercisesessionstarted;
    }

    public void setExercisesessionstarted(Calendar exercisesessionstarted) {
        this.exercisesessionstarted = exercisesessionstarted;
    }

    public int getExerciseeveryxminutes() {
        return exerciseeveryxminutes;
    }

    public void setExerciseeveryxminutes(int exerciseeveryxminutes) {
        this.exerciseeveryxminutes = exerciseeveryxminutes;
    }

    public ArrayList<Integer> getUpcomingexercises() {
        return upcomingexercises;
    }

    public void setUpcomingexercises(ArrayList<Integer> upcomingexercises) {
        this.upcomingexercises = upcomingexercises;
    }

    public Calendar getNextexercisetime() {
        return nextexercisetime;
    }

    public void setNextexercisetime(Calendar nextexercisetime) {
        this.nextexercisetime = nextexercisetime;
    }

    public int getExercisechooserid() {
        return exercisechooserid;
    }

    public void setExercisechooserid(int exercisechooserid) {
        this.exercisechooserid = exercisechooserid;
    }
}
