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
            int secondsuntilnext = 0;
            Calendar last = Time.getCalFromDate(room.getLastexercisetime());
            Calendar next = Time.xSecondsAgo(last, room.getExerciseeveryxminutes()*60);
            if (!next.before(Calendar.getInstance())){
                secondsuntilnext = DateDiff.dateDiff("second", Calendar.getInstance(), next);
            }
            if (secondsuntilnext<60){
                //Time to move the exercise forward
                //@todo correct for the fact that this won't run at the exact moment... there's an offset
                room.setLastexercisetime(new Date());
                
                room.save();
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }







}