package com.pingfit.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.pingfit.systemprops.InstanceProperties;
import com.pingfit.dao.Userpersistentlogin;
import com.pingfit.dao.Room;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.util.Time;
import com.pingfit.util.DateDiff;
import com.pingfit.exercisechoosers.ExerciseChooserList;
import com.pingfit.exercisechoosers.ExerciseChooserGroup;
import com.pingfit.exercisechoosers.ExerciseExtended;


import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class RoomExerciseProgress implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() RoomExerciseProgress called");

            List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                                   .add(Restrictions.eq("isenabled", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Room> iterator=rooms.iterator(); iterator.hasNext();) {
                    Room room=iterator.next();
                    processRoom(room);
                }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void processRoom(Room room){
        Logger logger = Logger.getLogger(RoomExerciseProgress.class);
        try{
            logger.debug("---");
            logger.debug("---");
            logger.debug("processing roomid="+room.getRoomid()+" "+room.getName());
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
            if (secondsuntilnext<60){
                logger.debug("need to move the group forward");
                //Move the exercise forward by recording lastexerciseplaceinlist
                String lastexerciseplaceinlist = "";
                ExerciseChooserGroup ecg = new ExerciseChooserGroup();
                ArrayList<ExerciseExtended> currentExercises = ecg.getNextExercises(room.getExerciselistid(), room.getLastexerciseplaceinlist(), 1);
                if (currentExercises!=null && currentExercises.size()>0){
                    ExerciseExtended currentExercise = currentExercises.get(0);
                    lastexerciseplaceinlist = currentExercise.getExerciseplaceinlist();
                }
                room.setLastexerciseplaceinlist(lastexerciseplaceinlist);
                logger.debug("lastexerciseplaceinlist="+lastexerciseplaceinlist);
                //@todo correct for the fact that this won't run at the exact moment... there's an offset of secondsuntilnext seconds
                if (secondsuntilnext>0 && secondsuntilnext<60){
                    logger.debug("adding offset of "+secondsuntilnext+" seconds");
                    Date nowDate = new Date();
                    Calendar nowCal = Time.getCalFromDate(nowDate);
                    Calendar nowPlusOffsetCal = Time.xSecondsAgo(nowCal, (-1)*secondsuntilnext);
                    logger.debug("new Date()="+Time.dateformatcompactwithtime(Time.getCalFromDate(new Date()))+" nowPlusOffsetCal="+Time.dateformatcompactwithtime(nowPlusOffsetCal));
                    room.setLastexercisetime(nowPlusOffsetCal.getTime());
                } else {
                    room.setLastexercisetime(new Date());
                }
                room.save();
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }







}