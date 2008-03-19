package com.pingfit.api;

import com.pingfit.cachedstuff.CachedStuff;
import com.pingfit.cache.providers.CacheFactory;
import com.pingfit.util.DateDiff;
import com.pingfit.exercisechoosers.ExerciseChooserRandom;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Mar 15, 2008
 * Time: 2:09:18 PM
 */
public class ExerciserCache {

    

    public static Exerciser get(String key){
        Logger logger = Logger.getLogger(ExerciserCache.class);
        String group = "ExerciserCache";
        try{
            Object obj = CacheFactory.getCacheProvider().get(key, group);
            if (obj!=null && (obj instanceof Exerciser)){
                Exerciser cachedExerciser = (Exerciser)obj;
                return cachedExerciser;
            } else {
                Exerciser newEx = getNewEmptyExerciser();
                CoreMethods.skipCurrentOrNextExercise(newEx);
                CacheFactory.getCacheProvider().put(key, group, newEx);
                return newEx;
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return getNewEmptyExerciser();
    }

    public static Exerciser getNewEmptyExerciser(){
        Exerciser newEx = new Exerciser();
        newEx.setCompletedexercises(new ArrayList<CompletedExercise>());
        newEx.setExerciseeveryxminutes(20);
        newEx.setExercisesessionstarted(Calendar.getInstance());
        newEx.setNextexerciseid(1);
        newEx.setExercisechooserid((new ExerciseChooserRandom()).getId());
        return newEx;
    }



}
