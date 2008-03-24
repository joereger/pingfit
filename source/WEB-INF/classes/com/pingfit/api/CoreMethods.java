package com.pingfit.api;

import com.pingfit.util.GeneralException;
import com.pingfit.util.Time;
import com.pingfit.util.Util;
import com.pingfit.dao.Pingback;
import com.pingfit.dao.User;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.exercisechoosers.ExerciseChooserFactory;
import com.pingfit.exercisechoosers.ExerciseChooser;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Mar 15, 2008
 * Time: 11:38:26 AM
 */
public class CoreMethods {


    public static void doExercise(Exerciser exerciser, int exerciseid, int reps) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            //Record to database
            if (exerciser.getUserid()>0){
                Pingback pingback = new Pingback();
                pingback.setDate(new Date());
                pingback.setExerciseid(exerciseid);
                pingback.setReps(reps);
                pingback.setUserid(exerciser.getUserid());
                pingback.save();
            }
            //Clean out memory a bit, if necessary
            int maxinmemory = 100;
            if (exerciser.getCompletedexercises()!=null && exerciser.getCompletedexercises().size()>maxinmemory){
                exerciser.getCompletedexercises().remove(0);
            }
            //Save completed exercise to memory
            CompletedExercise ce = new CompletedExercise();
            ce.setDate(Calendar.getInstance());
            ce.setExerciseid(exerciseid);
            ce.setReps(reps);
            exerciser.getCompletedexercises().add(ce);
            //Advance exercises
            advanceOneExercise(exerciser);
            //Calculate next exercisetime
            resetNextExerciseTime(exerciser);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static void resetNextExerciseTime(Exerciser exerciser) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            //Add the correct number of minutes
            Calendar nexttime = Time.xMinutesAgo(Calendar.getInstance(), (-1)*exerciser.getExerciseeveryxminutes());
            exerciser.setNextexercisetime(nexttime);
            //Store in db if there's a user
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                user.setNextexercisetime(nexttime.getTime());
                user.save();
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void skipCurrentOrNextExercise(Exerciser exerciser) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            //Advance one exercise without recording anythign to database
            advanceOneExercise(exerciser);
            //Reset next time
            resetNextExerciseTime(exerciser);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void flushUpcomingExercises(Exerciser exerciser) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            exerciser.setUpcomingexercises(new ArrayList<Integer>());
            exerciser.setCurrentexercisenum(1);
            //Store in db if there's a user
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                user.setUpcomingexercises("");
                user.setCurrentexercisenum(1);
                user.save();
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void advanceOneExercise(Exerciser exerciser) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            //Get the current exercisechooser
            int exercisechooserid = exerciser.getExercisechooserid();
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                exercisechooserid = user.getExercisechooserid();
            }
            //Increment currentexercisenum
            exerciser.setCurrentexercisenum(exerciser.getCurrentexercisenum()+1);
            int maxNumInList = NumFromUniqueResult.getInt("select max(num) from Exerciselistitem where exerciselistid='"+exerciser.getExerciselistid()+"'");
            if (exerciser.getCurrentexercisenum()>maxNumInList){
                exerciser.setCurrentexercisenum(1);
            }
            //Find the next exercise using the ExerciseChooser infrastructure
            ArrayList<Integer> upcomingexercises = ((ExerciseChooser)ExerciseChooserFactory.get(exercisechooserid)).getNextExercises(exerciser, 6);
            //Store in Exerciser
            exerciser.setUpcomingexercises(upcomingexercises);
            exerciser.setNextexerciseid(upcomingexercises.get(0));
            //Store in db if there's a user
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                user.setUpcomingexercises(Util.integerArrayListToString(upcomingexercises));
                user.save();
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void setExerciseEveryXMinutes(Exerciser exerciser, int minutes) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            exerciser.setExerciseeveryxminutes(minutes);
            //Store in db if there's a user
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                user.setExerciseeveryxminutes(minutes);
                user.save();
            }
            //Reset next time
            resetNextExerciseTime(exerciser);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void setExerciselistid(Exerciser exerciser, int exerciselistid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            exerciser.setExerciselistid(exerciselistid);
            //Store in db if there's a user
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                user.setExerciselistid(exerciselistid);
                user.save();
            }
            //Flush
            flushUpcomingExercises(exerciser);
            //Advance one exercise without recording anythign to database
            advanceOneExercise(exerciser);
            //Reset next time
            resetNextExerciseTime(exerciser);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void setExerciseChooserId(Exerciser exerciser, int exercisechooserid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            exerciser.setExercisechooserid(exercisechooserid);
            //Store in db if there's a user
            if (exerciser.getUserid()>0){
                User user = User.get(exerciser.getUserid());
                user.setExercisechooserid(exercisechooserid);
                user.save();
            }
            //Flush
            flushUpcomingExercises(exerciser);
            //Advance one exercise without recording anythign to database
            advanceOneExercise(exerciser);
            //Reset next time
            resetNextExerciseTime(exerciser);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

}
