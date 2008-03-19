package com.pingfit.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.pingfit.dao.User;
import com.pingfit.dao.Userpersistentlogin;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.util.GeneralException;
import com.pingfit.util.Time;
import com.pingfit.session.PersistentLogin;
import com.pingfit.systemprops.InstanceProperties;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class DeleteOldPersistentlogins implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() DeleteOldPersistentlogins called");


            List<Userpersistentlogin> userpersistentlogins = HibernateUtil.getSession().createQuery("from Userpersistentlogin").list();
            for (Iterator iterator = userpersistentlogins.iterator(); iterator.hasNext();) {
                Userpersistentlogin userpersistentlogin = (Userpersistentlogin) iterator.next();
                Date longtimeago = Time.xDaysAgoEnd(Calendar.getInstance(), PersistentLogin.daysToKeepPersistentRecordWithoutLogin).getTime();
                if (userpersistentlogin.getLastusedtologin().before(longtimeago)){
                   try{userpersistentlogin.delete();}catch(Exception ex){logger.error("",ex);};
                }
            }

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }


    }

}
