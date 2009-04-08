package com.pingfit.exercisechoosers;

import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.dao.User;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.Exerciselist;
import com.pingfit.util.Num;
import com.pingfit.util.DateDiff;
import com.pingfit.util.Time;
import com.pingfit.util.GeneralException;
import com.pingfit.api.CoreMethods;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:27:15 AM
 */
public class ExerciseChooserList implements ExerciseChooser {

    public int getId(){
        return 1;
    }

    public String getName() {
        return "Lists";
    }


    public ArrayList<ExerciseExtended> getNextExercises(User user, int numbertoget){
        return getNextExercises(user.getExerciselistid(), user.getLastexerciseplaceinlist(), numbertoget);
    }


    public ArrayList<ExerciseExtended> getNextExercises(int exerciselistid, String lastexerciseplaceinlist, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<ExerciseExtended> out = new ArrayList<ExerciseExtended>();
        logger.debug("getNextExercises()");
        //For this particular chooser I use user.lastexerciseplaceinlist to store the exerciselistitemid
        int lastexerciselistitemid = 0;
        if (Num.isinteger(lastexerciseplaceinlist)){
            lastexerciselistitemid = Integer.parseInt(lastexerciseplaceinlist);
        }
        logger.debug("lastexerciselistitemid="+lastexerciselistitemid);
        //Iterate all items to see if lastexerciseid is in the list
        List<Exerciselistitem> exerciselistitems = HibernateUtil.getSession().createCriteria(Exerciselistitem.class)
                                               .add(Restrictions.eq("exerciselistid", exerciselistid))
                                               .addOrder(Order.asc("num"))
                                               .setCacheable(true)
                                               .list();
        logger.debug("exerciselistitems.size()="+exerciselistitems.size());
        boolean isLastexerciseidInList = false;
        for (Iterator<Exerciselistitem> exerciselistitemIterator=exerciselistitems.iterator(); exerciselistitemIterator.hasNext();) {
            Exerciselistitem exerciselistitem=exerciselistitemIterator.next();
            if (exerciselistitem.getExerciselistitemid()==lastexerciselistitemid){
                isLastexerciseidInList = true;
                break;
            }
        }
        logger.debug("isLastexerciseidInList="+isLastexerciseidInList);
        //Basically, we have a list here.  And we have a user property of the last exerciseid they did.
        //But that exerciseid could be from another list.  Or it could be 0.
        //So we need some trigger to tell us when to start adding.
        boolean addNow = false;
        if (!isLastexerciseidInList){ addNow = true; }
        if (lastexerciselistitemid==0){ addNow = true; }
        logger.debug("addNow="+addNow);
        //Now iterate and when I see the lastexerciseid, start adding
        logger.debug("out.size()="+out.size());
        int i = 0;
        int attempts = 0;
        while(out.size()<=numbertoget && attempts<=(exerciselistitems.size()+numbertoget+10)){
            //logger.debug("====");
            //logger.debug("i="+i);
            //logger.debug("attempts="+attempts);
            //logger.debug("out.size()="+out.size());
            try{
                if (exerciselistitems.size()>=(i+1) && exerciselistitems.get(i)!=null){
                    logger.debug("exerciseid="+exerciselistitems.get(i).getExerciseid());
                    if (addNow){
                        logger.debug("addNow=true");
                        ExerciseExtended exExt = new ExerciseExtended();
                        exExt.setExercise(Exercise.get(exerciselistitems.get(i).getExerciseid()));
                        exExt.setReps(exerciselistitems.get(i).getReps());
                        exExt.setExerciseplaceinlist(String.valueOf(exerciselistitems.get(i).getExerciselistitemid()));
                        exExt.setTimeinseconds(exerciselistitems.get(i).getTimeinseconds());
                        exExt.setExerciselistid(exerciselistitems.get(i).getExerciselistid());
                        exExt.setExerciselistitemid(exerciselistitems.get(i).getExerciselistitemid());
                        out.add(exExt);
                        logger.debug("adding exerciseid="+exerciselistitems.get(i).getExerciseid()+" exerciseplaceinlist="+exExt.getExerciseplaceinlist()+" out.size()="+out.size());
                    } else {
                        logger.debug("addNow=false");
                        //If i'm iterating the list of all exercises and suddenly i see that the user recently did this one, start adding
                        if (exerciselistitems.get(i).getExerciselistitemid()==lastexerciselistitemid){
                            addNow = true;
                        }
                    }
                }
            } catch (Exception ex){logger.error("", ex);}
            attempts++;
            i++;
            //If we hit the end of the list, may have to wrap around to the start
            if (i>=exerciselistitems.size()){ i = 0; }
            if (out.size()>=numbertoget){ break; }
        }
        //If there isn't one, find the default system ex just to make sure things don't crash and get ugly on the client side
        if (out.size()==0){
            try{out.add(CoreMethods.getDefaultSystemExerciseExtended());}catch(Exception ex){logger.error("", ex);}
        }
        //Return
        return out;
    }

    public int getSecondsUntilNextExercise(User user){
        return getSecondsUntilNextExercise(user.getExerciselistid(), user.getLastexerciseplaceinlist(), Time.getCalFromDate(user.getLastexercisetime()));
    }

    public int getSecondsUntilNextExercise(int exerciselistid, String lastexerciseplaceinlist, Calendar lastexercisetime) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getSecondsUntilNextExercise(User user) called");
        int secondsuntilnext = 0;
        logger.debug("lastexercisetime="+Time.dateformatcompactwithtime(lastexercisetime));
        //Figure out when next exercise should happen
        int lastexerciselistitemid = 0;
        if (Num.isinteger(lastexerciseplaceinlist)){
            lastexerciselistitemid = Integer.parseInt(lastexerciseplaceinlist);
        }
        //If there's no lastexerciselistitemid, exercise now
        if (lastexerciselistitemid==0){ return 0; }
        //Find the current exerciselistitem
        Exerciselistitem exerciselistitem = null;
        List<Exerciselistitem> exerciselistitems = HibernateUtil.getSession().createCriteria(Exerciselistitem.class)
                                               .add(Restrictions.eq("exerciselistid", exerciselistid))
                                               .addOrder(Order.asc("num"))
                                               .setCacheable(true)
                                               .list();
        logger.debug("exerciselistitems.size()="+exerciselistitems.size());
        for (Iterator<Exerciselistitem> exerciselistitemIterator=exerciselistitems.iterator(); exerciselistitemIterator.hasNext();) {
            Exerciselistitem eli = exerciselistitemIterator.next();
            if (eli.getExerciselistitemid()==lastexerciselistitemid){
                exerciselistitem = eli;
                break;
            }
        }
        //If the list has changed and the user's place in the list has been lost, exercise now
        if (exerciselistitem==null){ return 0; }
        //Calculate next exercise time
        Calendar next = Time.xSecondsAgo(lastexercisetime, (-1)*exerciselistitem.getTimeinseconds());
        logger.debug("next="+Time.dateformatcompactwithtime(next));
        logger.debug("Calendar.getInstance()="+Time.dateformatcompactwithtime(Calendar.getInstance()));
        if (!next.before(Calendar.getInstance())){
            secondsuntilnext = DateDiff.dateDiff("second", next, Calendar.getInstance());
            logger.debug("DateDiff.dateDiff(\"second\", Calendar.getInstance(), next)="+secondsuntilnext);
            if (secondsuntilnext<0){
                logger.debug("secondsuntilnext="+secondsuntilnext+" so making it 0");
                secondsuntilnext = 0;
            }
        }
        logger.debug("getSecondsUntilNextExercise() returning secondsuntilnext="+secondsuntilnext);
        return secondsuntilnext;
    }





}
