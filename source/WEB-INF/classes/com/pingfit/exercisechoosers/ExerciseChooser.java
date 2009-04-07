package com.pingfit.exercisechoosers;

import com.pingfit.dao.User;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:25:35 AM
 */
public interface ExerciseChooser {

    public int getId();
    public String getName();
    public ArrayList<ExerciseExtended> getNextExercises(User user, int numbertoget);
    public ArrayList<ExerciseExtended> getNextExercises(int exerciselistid, String lastexerciseplaceinlist, int numbertoget);
    public int getSecondsUntilNextExercise(User user);
    public int getSecondsUntilNextExercise(int exerciselistid, String lastexerciseplaceinlist, Calendar lastexercisetime);
    
}
