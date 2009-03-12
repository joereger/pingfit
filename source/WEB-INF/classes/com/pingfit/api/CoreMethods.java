package com.pingfit.api;

import com.pingfit.util.GeneralException;
import com.pingfit.util.Time;
import com.pingfit.util.Util;
import com.pingfit.dao.Pingback;
import com.pingfit.dao.User;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.Exerciselist;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.exercisechoosers.ExerciseChooserFactory;
import com.pingfit.exercisechoosers.ExerciseChooser;
import com.pingfit.exercisechoosers.ExerciseExtended;
import com.pingfit.htmluibeans.Registration;
import com.pingfit.eula.EulaHelper;
import com.pingfit.htmlui.ValidationException;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Mar 15, 2008
 * Time: 11:38:26 AM
 */
public class CoreMethods {

    private static boolean isUserOk(User user){
        if (user==null || user.getUserid()<0 || !user.getIsenabled()){
            return false;
        }
        return true;
    }

    public static boolean testApi(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return false;
            }
            return true;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }


    public static void doExercise(User user, int exerciseid, int reps, String exerciseplaceinlist) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            //Record to database
            Pingback pingback = new Pingback();
            pingback.setUserid(user.getUserid());
            pingback.setDate(new Date());
            pingback.setExerciseid(exerciseid);
            pingback.setReps(reps);
            pingback.setUserinterface(0);
            pingback.save();
            //Advance user by one exercise
            user.setLastexerciseplaceinlist(exerciseplaceinlist);
            user.setLastexercisetime(new Date());
            user.save();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static ArrayList<Exerciselist> getExerciseLists(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                throw new GeneralException("User invalid.");
            }
            ArrayList<Exerciselist> out = new ArrayList<Exerciselist>();
            if (1==1){
                List<Exerciselist> systemlists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                                   .add(Restrictions.eq("issystem", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Exerciselist> exerciselistIterator=systemlists.iterator(); exerciselistIterator.hasNext();) {
                    Exerciselist exerciselist=exerciselistIterator.next();
                    out.add(exerciselist);
                }
            }
            if (1==1){
                List<Exerciselist> userlists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                                   .add(Restrictions.eq("issystem", false))
                                                   .add(Restrictions.eq("useridofcreator", user.getUserid()))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Exerciselist> exerciselistIterator=userlists.iterator(); exerciselistIterator.hasNext();) {
                    Exerciselist exerciselist=exerciselistIterator.next();
                    out.add(exerciselist);
                }
            }
            return out;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static ExerciseExtended getCurrentExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return null;
            }
            //Find the next exercise using the ExerciseChooser infrastructure
            logger.debug("user.getExercisechooserid()="+user.getExercisechooserid());
            ExerciseChooser exerciseChooser = ExerciseChooserFactory.get(user.getExercisechooserid());
            if(exerciseChooser!=null){
                ArrayList<ExerciseExtended> upcomingexercise = exerciseChooser.getNextExercises(user, 1);
                if (upcomingexercise!=null && upcomingexercise.size()>0){
                    return upcomingexercise.get(0);
                }
            } else {
                throw new GeneralException("ExerciseChooser is null... a sysadmin is working on it.");    
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
        return null;
    }

    public static Exercise getExercise(int exerciseid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return Exercise.get(exerciseid);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static Exerciselist getExerciselist(int exerciselistid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return Exerciselist.get(exerciselistid);
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static int getSecondsUntilNextExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return 0;
            }
            //Find the next exercise using the ExerciseChooser infrastructure
            int secondsuntilnextexercise = ((ExerciseChooser)ExerciseChooserFactory.get(user.getExercisechooserid())).getSecondsUntilNextExercise(user);
            return secondsuntilnextexercise;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }



    public static void skipExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            ExerciseExtended exExt = getCurrentExercise(user);
            //Advance user by one exercise
            user.setLastexerciseplaceinlist(exExt.getExerciseplaceinlist());
            user.setLastexercisetime(new Date());
            user.save();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void setExerciseEveryXMinutes(User user, int minutes) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            user.setExerciseeveryxminutes(minutes);
            user.save();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void setExerciselist(User user, int exerciselistid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            user.setExerciselistid(exerciselistid);
            user.save();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void setExerciseChooser(User user, int exercisechooserid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            user.setExercisechooserid(exercisechooserid);
            user.save();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void signUp(String email, String password, String passwordverify, String firstname, String lastname) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Registration registration = new Registration();
            registration.setIsflashsignup(true);
            registration.setEmail(email);
            registration.setPassword(password);
            registration.setPasswordverify(passwordverify);
            registration.setFirstname(firstname);
            registration.setLastname(lastname);
            registration.setEula(EulaHelper.getMostRecentEula().getEula().trim());
            registration.setDisplaytempresponsesavedmessage(false);
            registration.registerAction();
        } catch (ValidationException vex){
            throw new GeneralException(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            throw new GeneralException("Sorry, an unknown error occurred... please try again.");
        } 
    }

}
