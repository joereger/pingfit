package com.pingfit.exercisechoosers;

import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.dao.User;
import com.pingfit.dao.Exercise;
import com.pingfit.util.Num;
import com.pingfit.util.DateDiff;
import com.pingfit.util.Time;
import com.pingfit.util.GeneralException;
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
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getNextExercise()");
        int exerciselistid = user.getExerciselistid();
        int secondsuntilnextexercise = getSecondsUntilNextExercise(user);
        String lastexerciseplaceinlist = user.getLastexerciseplaceinlist();
        ArrayList<ExerciseExtended> out = new ArrayList<ExerciseExtended>();
        ArrayList<ExerciseExtended> exercises = getNextExercises(exerciselistid, lastexerciseplaceinlist, numbertoget);
        for (Iterator it = exercises.iterator(); it.hasNext(); ) {
            ExerciseExtended exExt = (ExerciseExtended)it.next();
            exExt.setSecondsuntilnextexercise(secondsuntilnextexercise);
            out.add(exExt);
        }
        return out;
    }

    protected ArrayList<ExerciseExtended> getNextExercises(int exerciselistid, String lastexerciseplaceinlist, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<ExerciseExtended> out = new ArrayList<ExerciseExtended>();
        logger.debug("getNextExercise()");
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
        if (!isLastexerciseidInList){
            addNow = true;
        }
        if (lastexerciselistitemid==0){
            addNow = true;
        }
        logger.debug("addNow="+addNow);
        //Now iterate and when I see the lastexerciseid, start adding
        logger.debug("out.size()="+out.size());
        int i = 0;
        int attempts = 0;
        while(out.size()<=numbertoget && attempts<=(exerciselistitems.size()+numbertoget+10)){
            logger.debug("====");
            logger.debug("i="+i);
            logger.debug("attempts="+attempts);
            logger.debug("out.size()="+out.size());
            try{
                if (exerciselistitems.get(i)!=null){
                    logger.debug("exerciseid="+exerciselistitems.get(i).getExerciseid());
                    if (addNow){
                        logger.debug("addNow=true");
                        ExerciseExtended exExt = new ExerciseExtended();
                        exExt.setExercise(Exercise.get(exerciselistitems.get(i).getExerciseid()));
                        exExt.setRepsfromlist(exerciselistitems.get(i).getReps());
                        exExt.setExerciseplaceinlist(String.valueOf(exerciselistitems.get(i).getExerciselistitemid()));
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
            if (i>=exerciselistitems.size()){
                i = 0;
            }
            if (out.size()>=numbertoget){
                break;
            }
        }
        return out;
    }


    public int getSecondsUntilNextExercise(User user) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        int secondsuntilnext = 0;
        Calendar last = Time.getCalFromDate(user.getLastexercisetime());
        Calendar next = Time.xSecondsAgo(last, user.getExerciseeveryxminutes()*60);
        if (!next.before(Calendar.getInstance())){
            secondsuntilnext = DateDiff.dateDiff("second", Calendar.getInstance(), next);
            if (secondsuntilnext<0){
                logger.debug("secondsuntilnext="+secondsuntilnext+" so making it 0");
                secondsuntilnext = 0;
            }
        }
        return secondsuntilnext;
    }




}
