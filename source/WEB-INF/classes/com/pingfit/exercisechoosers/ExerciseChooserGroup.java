package com.pingfit.exercisechoosers;


import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.dao.User;
import com.pingfit.util.Time;
import com.pingfit.util.DateDiff;
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
        
        //Group.groupid
        //     .groupname
        //     .lastexerciseid
        //     .lastexercisetime
        //     .exerciseeveryxminutes
        //     .useridofmoderator //or, useridsofmoderators and make it a string, comma sep?

        int exerciselistid = user.getExerciselistid();
        String lastexerciseplaceinlist = user.getLastexerciseplaceinlist();
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
        int secondsuntilnext = 30;
        //need to replace user with concept of group here.
//        Calendar last = Time.getCalFromDate(user.getLastexercisetime());
//        Calendar next = Time.xSecondsAgo(last, user.getExerciseeveryxminutes()*60);
//        if (!next.before(Calendar.getInstance())){
//            secondsuntilnext = DateDiff.dateDiff("second", Calendar.getInstance(), next);
//            if (secondsuntilnext<0){
//                logger.debug("secondsuntilnext="+secondsuntilnext+" so making it 0");
//                secondsuntilnext = 0;
//            }
//        }
        return secondsuntilnext;
    }

  
}