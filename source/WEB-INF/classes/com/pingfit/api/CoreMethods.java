package com.pingfit.api;

import com.pingfit.util.GeneralException;
import com.pingfit.util.Time;
import com.pingfit.util.Util;
import com.pingfit.util.Str;
import com.pingfit.dao.*;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.exercisechoosers.ExerciseChooserFactory;
import com.pingfit.exercisechoosers.ExerciseChooser;
import com.pingfit.exercisechoosers.ExerciseExtended;
import com.pingfit.htmluibeans.Registration;
import com.pingfit.eula.EulaHelper;
import com.pingfit.htmlui.ValidationException;
import com.pingfit.htmlui.Pagez;

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

    public static Eula getCurrentEula() throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return EulaHelper.getMostRecentEula();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static boolean isUserEulaUpToDate(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return false;
            }
            if (!EulaHelper.isUserUsingMostRecentEula(user)){
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static void agreeToEula(User user, int eulaid, String ip) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            if (ip==null || ip.equals("")){
                ip = "0.0.0.0";
            }
            Usereula usereula = new Usereula();
            usereula.setDate(new Date());
            usereula.setEulaid(eulaid);
            usereula.setUserid(user.getUserid());
            usereula.setIp(ip);
            try{
                usereula.save();
            } catch (GeneralException gex){
                logger.error(gex);
                logger.debug("agree failed: " + gex.getErrorsAsSingleString());
                throw new GeneralException("Sorry, there was an error with the agreeToEula() method.");
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static void joinRoom(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                throw new GeneralException("User invalid.");
            }
            //@todo permissions on rooms??
            boolean userCanJoinRoom = true;
            //Is the room valid
            Room room = Room.get(roomid);
            if (room==null || room.getRoomid()<=0 || !room.getIsenabled()){
                userCanJoinRoom = false;
            }
            //Do the join
            if (userCanJoinRoom){
                user.setRoomid(room.getRoomid());
                user.setExerciselistid(room.getExerciselistid());
                user.save();
            } else {
                throw new GeneralException("Permission to join this room not granted.");
            }
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

    public static Exerciselist getDefaultSystemExerciselist() throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            List<Exerciselist> systemlists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                               .add(Restrictions.eq("issystemdefault", true))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Exerciselist> exerciselistIterator=systemlists.iterator(); exerciselistIterator.hasNext();) {
                Exerciselist exerciselist=exerciselistIterator.next();
                return exerciselist;
            }
            return null;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static Room getDefaultSystemRoom() throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Exerciselist exerciselist = getDefaultSystemExerciselist();
            Room room = getRoomForExerciselist(exerciselist);
            if (room==null){
                throw new GeneralException("Sorry, no default room found.");
            }
            return room;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static Room getCurrentRoom(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                throw new GeneralException("User invalid.");
            }
            Room room = null;
            if (user.getRoomid()>0){
                room = Room.get(user.getRoomid());
            } else {
                room = getDefaultSystemRoom();
            }
            if (room!=null && !room.getIsenabled() || room.getRoomid()<=0){
                room = getDefaultSystemRoom();
            }
            if (room==null){
                throw new GeneralException("Sorry, no current room found.");
            }
            return room;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static ArrayList<Room> getRooms(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                throw new GeneralException("User invalid.");
            }
            ArrayList<Room> out = new ArrayList<Room>();
            if (1==1){
                //Make sure a room exists for every exerciselist
                List<Exerciselist> systemlists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                                   .add(Restrictions.eq("issystem", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Exerciselist> exerciselistIterator=systemlists.iterator(); exerciselistIterator.hasNext();) {
                    Exerciselist exerciselist=exerciselistIterator.next();
                    Room room = getRoomForExerciselist(exerciselist);
                }
            }
            if (1==1){
                //Get the system rooms
                List<Room> systemrooms = HibernateUtil.getSession().createCriteria(Room.class)
                                                   .add(Restrictions.eq("issystem", true))
                                                   .add(Restrictions.eq("isenabled", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Room> iterator=systemrooms.iterator(); iterator.hasNext();) {
                    Room room=iterator.next();
                    out.add(room);
                }
            }
            if (1==1){
                //Get the user rooms
                List<Room> userrooms = HibernateUtil.getSession().createCriteria(Room.class)
                                                   .add(Restrictions.eq("issystem", false))
                                                   .add(Restrictions.eq("isenabled", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Room> iterator=userrooms.iterator(); iterator.hasNext();) {
                    Room room=iterator.next();
                    out.add(room);
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

    public static void setNickname(User user, String nickname) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            if (nickname!=null){
                nickname = nickname.trim();
            }
            GeneralException gex = new GeneralException();
            boolean haveErrors = false;
            if (nickname==null || nickname.equals("")){
                gex.addValidationError("Nickname can't be blank.");
                haveErrors = true;
            }
            if (nickname!=null && nickname.length()>15){
                gex.addValidationError("Nickname must be 15 characters or less.");
                haveErrors = true;
            }
            List<User> usersByNickname = HibernateUtil.getSession().createQuery("from User where nickname='"+ Str.cleanForSQL(nickname)+"' and userid<>'"+user.getUserid()+"'").list();
            if (usersByNickname.size()>0){
                gex.addValidationError("That Nickname is already in use.");
                haveErrors = true;
            }
            if (haveErrors){
                logger.debug("throwing gex: "+gex.getErrorsAsSingleStringNoHtml());
                throw gex;    
            } else {
                user.setNickname(nickname);
                user.save();
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void signUp(String email, String password, String passwordverify, String firstname, String lastname, String nickname) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Registration registration = new Registration();
            registration.setIsflashsignup(true);
            registration.setEmail(email);
            registration.setPassword(password);
            registration.setPasswordverify(passwordverify);
            registration.setFirstname(firstname);
            registration.setLastname(lastname);
            registration.setNickname(nickname);
            registration.setEula(EulaHelper.getMostRecentEula().getEula().trim());
            registration.setDisplaytempresponsesavedmessage(false);
            registration.registerAction();
        } catch (ValidationException vex){
            throw new GeneralException(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            throw new GeneralException("Sorry, an unknown error occurred... please try again.");
        } 
    }

    public static Room getRoomForExerciselist(Exerciselist exerciselist){
        Logger logger = Logger.getLogger(CoreMethods.class);
        List<Room> roomsalreadyexisting = HibernateUtil.getSession().createCriteria(Room.class)
                                                   .add(Restrictions.eq("issystem", exerciselist.getIssystem()))
                                                   .add(Restrictions.eq("isenabled", true))
                                                   .add(Restrictions.eq("exerciselistid", exerciselist.getExerciselistid()))
                                                   .setCacheable(true)
                                                   .list();
        if (roomsalreadyexisting!=null && roomsalreadyexisting.size()>0){
            if (roomsalreadyexisting.size()>1){
                //For some reason there's more than one room marked as system and enabled for this exerciselist
                logger.error("ERROR: More than one room marked as system and enabled for this exerciselistid="+exerciselist.getExerciselistid());
            }
            //Return the room as it already exists
            return (Room)roomsalreadyexisting.get(0);
        } else {
            //Room doesn't exist, create it
            try{
                Room room = new Room();
                room.setCreatedate(new Date());
                room.setDescription(exerciselist.getDescription());
                room.setExerciseeveryxminutes(exerciselist.getExerciseeveryxminutes());
                room.setExerciselistid(exerciselist.getExerciselistid());
                room.setIsenabled(true);
                room.setIssystem(exerciselist.getIssystem());
                room.setLastexerciseplaceinlist("");
                room.setLastexercisetime(new Date());
                room.setName(exerciselist.getTitle());
                room.setDescription(exerciselist.getDescription());
                room.setUseridofcreator(0);
                room.save();
                return room;
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        return null;
    }

}
