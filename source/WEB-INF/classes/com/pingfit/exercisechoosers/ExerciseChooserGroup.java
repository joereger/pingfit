package com.pingfit.exercisechoosers;


import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.dao.User;
import com.pingfit.dao.Room;
import com.pingfit.dao.Exerciselist;
import com.pingfit.util.Time;
import com.pingfit.util.DateDiff;
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

      public int getSecondsUntilNextExercise(User user) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Room room = getRoomUserIsIn(user);
        int secondsuntilnext = 30;
        Calendar last = Time.getCalFromDate(room.getLastexercisetime());
        Calendar next = Time.xSecondsAgo(last, room.getExerciseeveryxminutes()*60);
        if (!next.before(Calendar.getInstance())){
            secondsuntilnext = DateDiff.dateDiff("second", Calendar.getInstance(), next);
            if (secondsuntilnext<0){
                logger.debug("secondsuntilnext="+secondsuntilnext+" so making it 0");
                secondsuntilnext = 0;
            }
        }
        return secondsuntilnext;
    }

    private Room room = null;
    private Room getRoomUserIsIn(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (room!=null){
            return room;
        }
        //See if the user has chosen a roomid
        if (room==null){
            if (user.getRoomid()>0){
                room = Room.get(user.getRoomid());
                if (!room.getIsenabled()){
                    room = null;
                }
            }
        }
        //See if the user has chosen an exerciselistid
        if (room==null){
            if (user.getExerciselistid()>0){
                Exerciselist exerciselist = Exerciselist.get(user.getExerciselistid());
                room = CoreMethods.getRoomForExerciselist(exerciselist);
                if (!room.getIsenabled()){
                    room = null;
                }
            }
        }
        //Go to the default room
        if (room==null){
            try{
                room = CoreMethods.getDefaultSystemRoom(user);
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        //Wing it
        if (room==null){
            room = Room.get(1);
        }
        //Return it
        return room;
    }

  
}