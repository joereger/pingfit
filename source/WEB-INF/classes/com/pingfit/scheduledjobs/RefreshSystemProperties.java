package com.pingfit.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.pingfit.systemprops.InstanceProperties;
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.util.Time;
import com.pingfit.util.GeneralException;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class RefreshSystemProperties implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() RefreshSystemProperties called");
            SystemProperty.refreshAllProps();
        //} else {
            //logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        //}
    }

}
