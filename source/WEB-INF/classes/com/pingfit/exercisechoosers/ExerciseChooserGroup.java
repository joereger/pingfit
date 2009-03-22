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
        //The way this works is essentially to stop the effect of exerciseeveryxminutes and lastexerciseid and instead
        //use a coordinated group value so that thousands of people can do the same exercise routine at
        //the same time.
        Room room = getRoomUserIsIn(user);
        //Now I coopt the ExerciseChooserList to simply grab the next item in the list
        int exerciselistid = room.getExerciselistid();
        String lastexerciseplaceinlist = room.getLastexerciseplaceinlist();
        ArrayList<ExerciseExtended> out = new ArrayList<ExerciseExtended>();
        ExerciseChooserList ecl = new ExerciseChooserList();
        ArrayList<ExerciseExtended> exercises = ecl.getNextExercises(exerciselistid, lastexerciseplaceinlist, numbertoget);
        for (Iterator it = exercises.iterator(); it.hasNext(); ) {
            ExerciseExtended exExt = (ExerciseExtended)it.next();
            exExt.setSecondsuntilnextexercise(getSecondsUntilNextExercise(user));
            out.add(exExt);
        }
        return out;
    }

    public ArrayList<ExerciseExtended> getNextExercises(int exerciselistid, String lastexerciseplaceinlist, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<ExerciseExtended> out = new ArrayList<ExerciseExtended>();
        //Again, coopt the list chooser
        ExerciseChooserList ecl = new ExerciseChooserList();
        out = ecl.getNextExercises(exerciselistid, lastexerciseplaceinlist, numbertoget);
        return out;
    }

    public int getSecondsUntilNextExercise(User user) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Room room = getRoomUserIsIn(user);
        int secondsuntilnext = 0;
        Calendar last = Time.getCalFromDate(room.getLastexercisetime());
        Calendar next = Time.xSecondsAgo(last, (-1)*room.getExerciseeveryxminutes()*60);
        logger.debug("last="+Time.dateformatcompactwithtime(last));
        logger.debug("now ="+Time.dateformatcompactwithtime(Calendar.getInstance()));
        logger.debug("next="+Time.dateformatcompactwithtime(next));
        if (next.after(Calendar.getInstance())){
            logger.debug("next.after(Calendar.getInstance())");
            secondsuntilnext = DateDiff.dateDiff("second", next, Calendar.getInstance());
        } else {
            logger.debug("!next.after(Calendar.getInstance())");
        }
        logger.debug("secondsuntilnext="+secondsuntilnext);
        if (secondsuntilnext<0){
            logger.debug("setting secondsuntilnext to zero");
            secondsuntilnext = 0;
        }
        logger.debug("returning secondsuntilnext="+secondsuntilnext);
        return secondsuntilnext;
    }

    private Room getRoomUserIsIn(User user){
        Room room = null;
        try{
            room = CoreMethods.getCurrentRoom(user);
        } catch (GeneralException gex){
            room = Room.get(1);
        }
        return room;
    }

  
}