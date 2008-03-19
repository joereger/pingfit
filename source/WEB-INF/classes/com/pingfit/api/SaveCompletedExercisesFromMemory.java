package com.pingfit.api;

import com.pingfit.dao.User;
import com.pingfit.dao.Pingback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 10:24:18 PM
 */
public class SaveCompletedExercisesFromMemory {

    public static void saveAll(Exerciser exerciser){
        Logger logger = Logger.getLogger(SaveCompletedExercisesFromMemory.class);
        logger.debug("saveAll() called... exerciser.getUserid()="+exerciser.getUserid());
        if (exerciser.getUserid()>0){
            User user = User.get(exerciser.getUserid());
            if (user!=null){
                ArrayList<CompletedExercise> cexs = new ArrayList<CompletedExercise>();
                cexs = exerciser.getCompletedexercises();
                logger.debug("cexs.size()="+cexs.size());
                if (cexs!=null && cexs.size()>0){
                    for (Iterator<CompletedExercise> iterator = cexs.iterator(); iterator.hasNext();) {
                        CompletedExercise completedExercise = iterator.next();
                        Pingback pingback = new Pingback();
                        pingback.setDate(completedExercise.getDate().getTime());
                        pingback.setExerciseid(completedExercise.getExerciseid());
                        pingback.setReps(completedExercise.getReps());
                        pingback.setUserid(exerciser.getUserid());
                        try{pingback.save();}catch(Exception ex){logger.error("", ex);}
                    }
                }
            }
        }
    }



}
