package com.pingfit.exercisechoosers;


import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.dao.User;
import com.pingfit.dao.Room;
import com.pingfit.dao.Exerciselist;
import com.pingfit.util.Time;
import com.pingfit.util.DateDiff;
import com.pingfit.util.GeneralException;
import com.pingfit.api.CoreMethods;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:27:15 AM
 */
public class ExerciseChooserGroup implements ExerciseChooser {

    public int getId(){
        return 2;
    }

    public String getName() {
        return "GroupLists";
    }

    public ArrayList<ExerciseExtended> getNextExercises(User user, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getNextExercise()");
        //Figure out which room this user is in
        Room room = null;
        try{ room = CoreMethods.getCurrentRoom(user); } catch (Exception ex){room = Room.get(1);}
        //Now I coopt the ExerciseChooserList to simply grab the next item in the list
        ExerciseChooserList ecl = new ExerciseChooserList();
        return ecl.getNextExercises(room.getExerciselistid(), room.getLastexerciseplaceinlist(), numbertoget);
    }

    public ArrayList<ExerciseExtended> getNextExercises(int exerciselistid, String lastexerciseplaceinlist, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<ExerciseExtended> out = new ArrayList<ExerciseExtended>();
        //Again, coopt the list chooser
        ExerciseChooserList ecl = new ExerciseChooserList();
        out = ecl.getNextExercises(exerciselistid, lastexerciseplaceinlist, numbertoget);
        return out;
    }

    public int getSecondsUntilNextExercise(User user){
        //Figure out which room this user is in
        Room room = null;
        try{ room = CoreMethods.getCurrentRoom(user); } catch (Exception ex){room = Room.get(1);}
        //Coopt the list chooser
        return getSecondsUntilNextExercise(room.getExerciselistid(), room.getLastexerciseplaceinlist(), Time.getCalFromDate(room.getLastexercisetime()));
    }

    public int getSecondsUntilNextExercise(int exerciselistid, String lastexerciseplaceinlist, Calendar lastexercisetime) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Again, coopt the list chooser
        ExerciseChooserList ecl = new ExerciseChooserList();
        return ecl.getSecondsUntilNextExercise(exerciselistid, lastexerciseplaceinlist, lastexercisetime);
    }



  
}