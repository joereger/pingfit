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
import com.pingfit.exercisechoosers.ExerciseChooserGroup;
import com.pingfit.htmluibeans.Registration;
import com.pingfit.eula.EulaHelper;
import com.pingfit.htmlui.ValidationException;
import com.pingfit.friends.RoomPermissionRequest;
import com.pingfit.email.EmailTemplateProcessor;
import com.pingfit.helpers.PlExerciseListHelper;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

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

    public static Eula getCurrentEula(int plid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return EulaHelper.getMostRecentEula(plid);
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
                //Get exercise lists that this pl has permission to access
                List<Exerciselist> systemlists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                                   .add(Restrictions.eq("issystem", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Exerciselist> exerciselistIterator=systemlists.iterator(); exerciselistIterator.hasNext();) {
                    Exerciselist exerciselist=exerciselistIterator.next();
                    if (PlExerciseListHelper.canPlUseExerciseList(user.getPlid(), exerciselist.getExerciselistid())){
                        out.add(exerciselist);
                    }
                }
            }
            if (1==1){
                //Get this user's exercise lists
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

    public static Exercise getDefaultSystemExercise() throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            //@todo better way to define default system exercise
            Exercise exercise = null;
            List<Exercise> exercises = HibernateUtil.getSession().createCriteria(Exercise.class)
                                               .add(Restrictions.eq("issystem", true))
                                               .add(Restrictions.eq("issystem", true))
                                               .setMaxResults(1)
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Exercise> exerciseIterator=exercises.iterator(); exerciseIterator.hasNext();) {
                Exercise ex=exerciseIterator.next();
                exercise = ex;
            }
            if (exercise==null){
                throw new GeneralException("Sorry, no default exercise found.");
            }
            return exercise;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static ExerciseExtended getDefaultSystemExerciseExtended() throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Exerciselist exerciselist = CoreMethods.getDefaultSystemExerciselist();
            Exerciselistitem exerciselistitem = null;
            for (Iterator<Exerciselistitem> exerciselistitemIterator=exerciselist.getExerciselistitems().iterator(); exerciselistitemIterator.hasNext();) {
                Exerciselistitem exli=exerciselistitemIterator.next();
                exerciselistitem = exli;
            }
            Exercise exercise = Exercise.get(exerciselistitem.getExerciseid());
            ExerciseExtended exExt = new ExerciseExtended();
            exExt.setExercise(exercise);
            exExt.setReps(exerciselistitem.getReps());
            exExt.setExerciseplaceinlist(String.valueOf(exerciselistitem.getExerciselistitemid()));
            exExt.setTimeinseconds(exerciselistitem.getTimeinseconds());
            exExt.setExerciselistid(exerciselistitem.getExerciselistid());
            exExt.setExerciselistitemid(exerciselistitem.getExerciselistitemid());
            return exExt;
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

    public static ArrayList<ExerciseExtended> getNextExercises(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return null;
            }
            //Find the next exercises using the ExerciseChooser infrastructure
            ExerciseChooser exerciseChooser = ExerciseChooserFactory.get(user.getExercisechooserid());
            if(exerciseChooser!=null){
                return exerciseChooser.getNextExercises(user, 25);
            } else {
                throw new GeneralException("ExerciseChooser is null... a sysadmin is working on it.");
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
    }

    public static ArrayList<Pingback> getRecentExercises(User user, int useridToShowRecentExercisesFor, int numberToGet) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        ArrayList<Pingback> out = new ArrayList<Pingback>();
        try{
            User userToShowRecentExercisesFor = User.get(useridToShowRecentExercisesFor);
            if (!isUserOk(userToShowRecentExercisesFor)){ return null; }
            //@todo user-specific privacy controls to manage who can see what
            if (numberToGet>100){ numberToGet = 100; }
            List<Pingback> pingbacks = HibernateUtil.getSession().createCriteria(Pingback.class)
                                               .add(Restrictions.eq("userid", useridToShowRecentExercisesFor))
                                               .addOrder(Order.desc("pingbackid"))
                                               .setMaxResults(numberToGet)
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Pingback> pingbackIterator=pingbacks.iterator(); pingbackIterator.hasNext();) {
                Pingback pingback=pingbackIterator.next();
                out.add(pingback);
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Database error... sorry... please try again.");
        }
        return out;
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

    public static void inviteByEmail(User user, String emailtoinvite, String custommessage) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (!isUserOk(user)){
                return;
            }
            if (custommessage==null || custommessage.equals("null")){
                custommessage = "";
            }
            List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                               .add(Restrictions.eq("email", emailtoinvite.trim()))
                                               .setCacheable(true)
                                               .list();
            if (users!=null && users.size()>0){
                for (Iterator<User> userIterator=users.iterator(); userIterator.hasNext();) {
                    User userAlreadyInSystem=userIterator.next();
                    addFriend(user, userAlreadyInSystem.getUserid());
                }
            } else {
                //Send the friend an email
                String[] args = new String[4];
                args[0] = user.getFirstname()+" "+user.getLastname();
                args[1] = String.valueOf(user.getUserid());
                args[2] = "<br/><br/>"+user.getFirstname()+" "+user.getLastname()+" says:<br/>"+custommessage;
                args[3] = "\n\n"+user.getFirstname()+" "+user.getLastname()+" says:\n"+custommessage;
                EmailTemplateProcessor.sendMail(user.getFirstname()+" "+user.getLastname()+" wants to be your pingFit friend!", "invite", null, args, emailtoinvite, user.getEmail());
                //See if they've invited already
                List<Invitebyemail> invitebyemails = HibernateUtil.getSession().createCriteria(Invitebyemail.class)
                                                   .add(Restrictions.eq("userid", user.getUserid()))
                                                   .add(Restrictions.eq("email", emailtoinvite.trim()))
                                                   .setCacheable(true)
                                                   .list();
                if (invitebyemails==null || invitebyemails.size()==0){
                    //User hasn't invited this user before so create a record of the invite
                    Invitebyemail invitebyemail = new Invitebyemail();
                    invitebyemail.setUserid(user.getUserid());
                    invitebyemail.setDate(new Date());
                    invitebyemail.setEmail(emailtoinvite.trim());
                    invitebyemail.setCustommessage(custommessage.trim());
                    invitebyemail.setIsaccepted(false);
                    invitebyemail.setAcceptdate(new Date());
                    invitebyemail.save();
                }
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Error... sorry... please try again.");
        }
    }

    public static void signUp(String email, String password, String passwordverify, String firstname, String lastname, String nickname, int plid) throws GeneralException {
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
            registration.setEula(EulaHelper.getMostRecentEula(plid).getEula().trim());
            registration.setPlid(plid);
            registration.setDisplaytempresponsesavedmessage(false);
            registration.registerAction();
            User newUser = User.get(registration.getUserid());
            //Now see if anybody had invited this person
            List<Invitebyemail> invitebyemails = HibernateUtil.getSession().createCriteria(Invitebyemail.class)
                                               .add(Restrictions.eq("email", email.trim()))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Invitebyemail> invitebyemailIterator=invitebyemails.iterator(); invitebyemailIterator.hasNext();) {
                Invitebyemail invitebyemail=invitebyemailIterator.next();
                User userWhoInvited = User.get(invitebyemail.getUserid());
                //Create friendship both ways
                addFriend(newUser, userWhoInvited.getUserid());
                addFriend(userWhoInvited, newUser.getUserid());
                //Update the invitebyemail records
                invitebyemail.setAcceptdate(new Date());
                invitebyemail.setIsaccepted(true);
                invitebyemail.save();
                //@todo Send Email to userWhoInvited
            }
        } catch (ValidationException vex){
            throw new GeneralException(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            throw new GeneralException("Sorry, an unknown error occurred... please try again.");
        } 
    }

    public static Room getRoomForExerciselist(Exerciselist exerciselist) throws GeneralException {
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
                room.setExerciselistid(exerciselist.getExerciselistid());
                room.setIsenabled(true);
                room.setIssystem(exerciselist.getIssystem());
                room.setLastexerciseplaceinlist("");
                room.setLastexercisetime(new Date());
                room.setName(exerciselist.getTitle());
                room.setDescription(exerciselist.getDescription());
                room.setUseridofcreator(0);
                room.setIsprivate(false);
                room.setIsfriendautopermit(false);
                room.save();
                return room;
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        return null;
    }

    public static ArrayList<User> getFriends(User user) throws GeneralException {
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        ArrayList<User> out = new ArrayList<User>();
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("isfulltwoway", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
            Userfriend userfriend=userfriendIterator.next();
            User u = User.get(userfriend.getUseridoffriend());
            out.add(u);
        }
        return out;
    }

    public static ArrayList<User> getFriendRequests(User user) throws GeneralException {
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        ArrayList<User> out = new ArrayList<User>();
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                           .add(Restrictions.eq("useridoffriend", user.getUserid()))
                                           .add(Restrictions.eq("ispendingapproval", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
            Userfriend userfriend=userfriendIterator.next();
            User u = User.get(userfriend.getUserid());
            out.add(u);
        }
        return out;
    }

    public static void addFriend(User user, int useridoffriend) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //Get the userfriend
        User friendUser = User.get(useridoffriend);
        if (!isUserOk(friendUser)){
            throw new GeneralException("Friend User invalid.");
        }
        //Send email
        String[] args = new String[4];
        args[0] = user.getFirstname()+" "+user.getLastname();
        args[1] = String.valueOf(user.getUserid());
        args[2] = "";
        args[3] = "";
        EmailTemplateProcessor.sendMail(user.getFirstname()+" "+user.getLastname()+" wants to be friends on pingFit!", "friendrequest", null, args, friendUser.getEmail(), user.getEmail());
        //See if they have a pending or accepted friendship with you
        boolean theyWannaBeFriends = false;
        if (1==1){
            List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                            .add(Restrictions.eq("userid", useridoffriend))
                                            .add(Restrictions.eq("useridoffriend", user.getUserid()))
                                            .setCacheable(true)
                                            .list();
            if (userfriends!=null && userfriends.size()!=0){
                for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
                    Userfriend userfriend=userfriendIterator.next();
                    //They want to be friends
                    theyWannaBeFriends = true;
                    //If they're not approved, approve them
                    if (userfriend.getIspendingapproval()){
                        userfriend.setIspendingapproval(false);
                        userfriend.setIsfulltwoway(true);
                        userfriend.save();
                    }
                }
            }
        }
        //Now check your relationship to them
        if (1==1){
            List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                                .add(Restrictions.eq("userid", user.getUserid()))
                                                .add(Restrictions.eq("useridoffriend", useridoffriend))
                                                .setCacheable(true)
                                                .list();
            if (userfriends==null || userfriends.size()==0){
                //There isn't already an existing friendship or friend request
                try{
                    Userfriend userfriend = new Userfriend();
                    userfriend.setUserid(user.getUserid());
                    userfriend.setUseridoffriend(useridoffriend);
                    if (theyWannaBeFriends){
                        //They want to be friends with you so no need for approval
                        userfriend.setIspendingapproval(false);
                        userfriend.setIsfulltwoway(true);
                    } else {
                        //They haven't said they want to be friends with you so you need approval
                        userfriend.setIspendingapproval(true);
                        userfriend.setIsfulltwoway(false);
                    }
                    userfriend.save();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static void breakFriendship(User user, int useridoffriend) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        if (1==1){
            //Delete user's tie to friend
            List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                                .add(Restrictions.eq("userid", user.getUserid()))
                                                .add(Restrictions.eq("useridoffriend", useridoffriend))
                                                .setCacheable(true)
                                                .list();
            for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
                try{
                    Userfriend userfriend=userfriendIterator.next();
                    userfriend.delete();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
        if (1==1){
            //Delete friend's tie to user
            List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                                .add(Restrictions.eq("userid", useridoffriend))
                                                .add(Restrictions.eq("useridoffriend", user.getUserid()))
                                                .setCacheable(true)
                                                .list();
            for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
                try{
                    Userfriend userfriend=userfriendIterator.next();
                    userfriend.delete();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static void createRoom(User user, String name, String description, int exerciselistid, boolean isprivate, boolean isfriendautopermit) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        if (name==null || name.equals("")){
            throw new GeneralException("Room Name is required.");
        }
        if (description==null || description.equals("")){
            throw new GeneralException("Room Description is required.");
        }
        Exerciselist exList = Exerciselist.get(exerciselistid);
        if (exList==null || exList.getExerciselistid()<0){
            throw new GeneralException("The ExerciseListId doesn't appear valid.");
        }
        List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                            .add(Restrictions.eq("useridofcreator", user.getUserid()))
                                            .add(Restrictions.eq("name", name))
                                            .setCacheable(true)
                                            .list();
        if (rooms==null || rooms.size()==0){
            //Only if there isn't already an existing room by this user
            try{
                Room room = new Room();
                room.setName(name);
                room.setDescription(description);
                room.setCreatedate(new Date());
                room.setExerciselistid(exerciselistid);
                room.setIsenabled(true);
                room.setIssystem(false);
                room.setLastexerciseplaceinlist("");
                room.setLastexercisetime(new Date());
                room.setUseridofcreator(user.getUserid());
                room.setIsprivate(isprivate);
                room.setIsfriendautopermit(isfriendautopermit);
                room.save();
                Roompermission roompermission = new Roompermission();
                roompermission.setIsmoderator(true);
                roompermission.setIspendingapproval(false);
                roompermission.setRoomid(room.getRoomid());
                roompermission.setUserid(user.getUserid());
                roompermission.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        } else {
            //@todo throw error, room by that name already exists
            throw new GeneralException("A Room by that name already exists for this user.");
        }
    }

    public static void editRoom(User user, int roomid, String name, String description, int exerciselistid, boolean isprivate, boolean isfriendautopermit) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        if (name==null || name.equals("")){
            throw new GeneralException("Room Name is required.");
        }
        if (description==null || description.equals("")){
            throw new GeneralException("Room Description is required.");
        }
        Exerciselist exList = Exerciselist.get(exerciselistid);
        if (exList==null || exList.getExerciselistid()<0){
            throw new GeneralException("The ExerciseListId doesn't appear valid.");
        }
        try{
            Room room = Room.get(roomid);
            if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
                throw new GeneralException("Room invalid.");
            }
            if (room.getUseridofcreator()!=user.getUserid()){
                throw new GeneralException("You did not create that room so you can not edit it.");
            }
            room.setName(name);
            room.setDescription(description);
            room.setExerciselistid(exerciselistid);
            room.setIsprivate(isprivate);
            room.setIsfriendautopermit(isfriendautopermit);
            room.save();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static void deleteRoom(User user, int roomid) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        try{
            Room room = Room.get(roomid);
            if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
                throw new GeneralException("Room invalid.");
            }
            if (room.getUseridofcreator()!=user.getUserid()){
                throw new GeneralException("You did not create that room so you can not delete it.");
            }
            room.delete();
            //Delete roompermissions
            String emptyStr = "";
            int rowsupdated = HibernateUtil.getSession().createQuery("DELETE from Roompermission where roomid='"+roomid+"'"+emptyStr).executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static ArrayList<Room> getRoomsIModerate(User user) throws GeneralException {
        ArrayList<Room> out = new ArrayList<Room>();
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //Get roomids of rooms this user mods
        ArrayList<Integer> roomidsUserMods = new ArrayList<Integer>();
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("ispendingapproval", false))
                                           .add(Restrictions.eq("ismoderator", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Roompermission> usrIt=roompermissions.iterator(); usrIt.hasNext();) {
            Roompermission roompermission = usrIt.next();
            roomidsUserMods.add(roompermission.getRoomid());
        }
        //Go get the rooms themselves
        if (roomidsUserMods!=null && roomidsUserMods.size()>0){
            List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                               .add(Restrictions.in("roomid", roomidsUserMods))
                                               .add(Restrictions.eq("isenabled", true))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Room> it=rooms.iterator(); it.hasNext();) {
                Room room=it.next();
                out.add(room);
            }
        }
        return out;
    }

    public static ArrayList<Room> getMyRooms(User user) throws GeneralException {
        ArrayList<Room> out = new ArrayList<Room>();
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //Get roomids of rooms this user has permission to be in
        ArrayList<Integer> roomidsUserMods = new ArrayList<Integer>();
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("ispendingapproval", false))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Roompermission> usrIt=roompermissions.iterator(); usrIt.hasNext();) {
            Roompermission roompermission = usrIt.next();
            roomidsUserMods.add(roompermission.getRoomid());
        }
        //Go get the rooms themselves
        if (roomidsUserMods!=null && roomidsUserMods.size()>0){
            List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                               .add(Restrictions.in("roomid", roomidsUserMods))
                                               .add(Restrictions.eq("isenabled", true))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Room> it=rooms.iterator(); it.hasNext();) {
                Room room=it.next();
                out.add(room);
            }
        }
        return out;
    }

    public static void joinRoom(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //See if room is valid
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        //Boolean to track whether or not user can join room
        boolean userCanJoinRoom = false;
        //See if there's already a permission for this user and this roomid
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                            .add(Restrictions.eq("userid", user.getUserid()))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions!=null && roompermissions.size()>0){
            //There are already permissions assigned... not really much to do here
            for (Iterator<Roompermission> rmpIt=roompermissions.iterator(); rmpIt.hasNext();) {
                try{
                    Roompermission roompermission=rmpIt.next();
                    //Only need to consider changing pending status if it is actually pending
                    if (roompermission.getIspendingapproval()){
                        if (room.getIsprivate()){
                            if (isModeratorOfRoom(user.getUserid(), roomid)){
                                //Mod of room, so grant permission
                                roompermission.setIspendingapproval(false);
                            } else {
                                //Not mod of room
                                if (room.getIsfriendautopermit() && areFriends(user.getUserid(), room.getUseridofcreator())){
                                    //User is a friend of room creator, so grant permission
                                    roompermission.setIspendingapproval(false);
                                }
                            }
                        } else {
                            //Room's public so grant permission
                            roompermission.setIspendingapproval(false);
                        }
                        roompermission.save();
                    }
                    //Update userCanJoinRoom
                    if (!roompermission.getIspendingapproval()){
                        userCanJoinRoom = true;
                    }
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        } else {
            //Create a new room permission
            try{
                Roompermission roompermission = new Roompermission();
                roompermission.setRoomid(roomid);
                roompermission.setUserid(user.getUserid());
                roompermission.setIsmoderator(false);
                if (room.getIsprivate()){
                    if (isModeratorOfRoom(user.getUserid(), roomid)){
                        //Mod of room, so grant permission
                        roompermission.setIspendingapproval(false);
                    } else {
                        //Not mod of room
                        if (room.getIsfriendautopermit() && areFriends(user.getUserid(), room.getUseridofcreator())){
                            //User is a friend of room creator, so grant permission
                            roompermission.setIspendingapproval(false);
                        } else {
                            //User not a friend of room creator, so deny permission, making it a permission request
                            roompermission.setIspendingapproval(true);
                        }
                    }
                } else {
                    //Room's public so grant permission
                    roompermission.setIspendingapproval(false);
                }
                roompermission.save();
                //Update userCanJoinRoom
                if (!roompermission.getIspendingapproval()){
                    userCanJoinRoom = true;
                }
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        //Add user to room
        if (userCanJoinRoom){
            ExerciseChooserGroup ecg = new ExerciseChooserGroup();
            user.setRoomid(room.getRoomid());
            user.setExerciselistid(room.getExerciselistid());
            user.setExercisechooserid(ecg.getId());
            user.save();
        } else {
            throw new GeneralException("This room is private... a request for access has been made to the room's owner.");
        }
    }

    public static void removeFromMyRooms(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //See if there's already a permission for this user and this roomid
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                            .add(Restrictions.eq("userid", user.getUserid()))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions!=null && roompermissions.size()>0){
            //There are already permissions assigned, make sure they're allowed/approved
            for (Iterator<Roompermission> rmpIt=roompermissions.iterator(); rmpIt.hasNext();) {
                try{
                    Roompermission roompermission=rmpIt.next();
                    roompermission.delete();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static ArrayList<Room> getRoomsMyFriendsAreIn(User user) throws GeneralException {
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        ArrayList<Room> out = new ArrayList<Room>();
        try{
            //Get a list of friends' userids
            ArrayList<User> friends = getFriends(user);
            ArrayList<Integer> friendUserids = new ArrayList<Integer>();
            for (Iterator<User> iterator=friends.iterator(); iterator.hasNext();) {
                User friend=iterator.next();
                friendUserids.add(friend.getUserid());
            }
            //Find roomids that friends are in
            ArrayList<Integer> roomsFriendsInRoomids = new ArrayList<Integer>();
            if (friendUserids!=null && friendUserids.size()>0){
                List<User> friendUsers = HibernateUtil.getSession().createCriteria(User.class)
                                                   .add(Restrictions.in("userid", friendUserids))
                                                   .add(Restrictions.eq("isenabled", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<User> usrIt=friendUsers.iterator(); usrIt.hasNext();) {
                    User u=usrIt.next();
                    if (u.getRoomid()>0){
                        roomsFriendsInRoomids.add(u.getRoomid());
                    }
                }
            }
            //Go get the room objects themselves
            if (roomsFriendsInRoomids!=null && roomsFriendsInRoomids.size()>0){
                List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                                    .add(Restrictions.in("roomid", roomsFriendsInRoomids))
                                                   .add(Restrictions.eq("isenabled", true))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Room> roomIterator=rooms.iterator(); roomIterator.hasNext();) {
                    Room room=roomIterator.next();
                    out.add(room);
                }
            }
        } catch (GeneralException gex){
            throw gex;
        }
        return out;
    }

    public static ArrayList<Room> getRoomsMyFriendsModerate(User user) throws GeneralException {
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        ArrayList<Room> out = new ArrayList<Room>();
        //Get a list of friends' userids
        ArrayList<User> friends = getFriends(user);
        ArrayList<Integer> friendUserids = new ArrayList<Integer>();
        for (Iterator<User> iterator=friends.iterator(); iterator.hasNext();) {
            User friend=iterator.next();
            friendUserids.add(friend.getUserid());
        }
        //Find roomids that friends are moderating
        ArrayList<Integer> roomidsFriendsModerate = new ArrayList<Integer>();
        if (friendUserids!=null && friendUserids.size()>0){
            List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                               .add(Restrictions.in("userid", friendUserids))
                                               .add(Restrictions.eq("ispendingapproval", false))
                                               .add(Restrictions.eq("ismoderator", true))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Roompermission> usrIt=roompermissions.iterator(); usrIt.hasNext();) {
                Roompermission roompermission = usrIt.next();
                roomidsFriendsModerate.add(roompermission.getRoomid());
            }
        }
        //Go get the room objects themselves
        if (roomidsFriendsModerate!=null && roomidsFriendsModerate.size()>0){
            List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                                .add(Restrictions.in("roomid", roomidsFriendsModerate))
                                               .add(Restrictions.eq("isenabled", true))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Room> roomIterator=rooms.iterator(); roomIterator.hasNext();) {
                Room room=roomIterator.next();
                out.add(room);
            }
        }
        return out;
    }

    public static boolean areFriends(int userid1, int userid2) throws GeneralException {
        if (userid1==userid2){
            return true;
        }
        if (userid1==0 || userid2==0){
            return false;
        }
        ArrayList<User> friends = getFriends(User.get(userid1));
        for (Iterator<User> iterator=friends.iterator(); iterator.hasNext();) {
            User friend=iterator.next();
            if (friend.getUserid()==userid2){
                return true;
            }
        }
        return false;
    }

    public static boolean isAllowedInRoom(int userid, int roomid) throws GeneralException {
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        if (room!=null && room.getRoomid()>0){
            //If it's not enabled, sod off
            if (!room.getIsenabled()){
                return false;
            }
            //If it's public, rock on
            if (!room.getIsprivate()){
                return true;
            }
            //If it's friendautopermit and you're a friend, rock on
            if (room.getIsfriendautopermit() && room.getUseridofcreator()>0){
                if (areFriends(userid, room.getUseridofcreator())){
                    return true;
                }
            }
            //If you have explicit permission to that room
            List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                               .add(Restrictions.eq("userid", userid))
                                                .add(Restrictions.eq("roomid", roomid))
                                                .add(Restrictions.eq("ispendingapproval", false))
                                                .setCacheable(true)
                                               .list();
            if (roompermissions!=null && roompermissions.size()>0){
                return true;
            }
        } else {
            throw new GeneralException("Room invalid.");
        }
        return false;
    }

    public static boolean isModeratorOfRoom(int userid, int roomid) throws GeneralException {
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        if (room!=null && room.getRoomid()>0){
            //If it's not enabled, sod off
            if (!room.getIsenabled()){
                return false;
            }
            //If this user created it
            if (room.getUseridofcreator()==userid){
                return true;
            }
            //If you have explicit permission to that room
            List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                               .add(Restrictions.eq("userid", userid))
                                                .add(Restrictions.eq("roomid", roomid))
                                                .add(Restrictions.eq("ispendingapproval", false))
                                                .add(Restrictions.eq("ismoderator", true))
                                                .setCacheable(true)
                                               .list();
            if (roompermissions!=null && roompermissions.size()>0){
                return true;
            }
        }
        return false;
    }

    public static void grantRoomPermission(User user, int useridtogivepermissionto, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //Make sure we have a user to grant to
        if (useridtogivepermissionto==0){
            throw new GeneralException("Sorry, useridtogivepermissionto must be >0");
        } else {
            User usertograntto = User.get(useridtogivepermissionto);
            if (!isUserOk(usertograntto)){
                throw new GeneralException("User to grant permission to is invalid.");
            }
        }
        //See if room is valid
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        //See if this user has permission to mod this room
        if (!isModeratorOfRoom(user.getUserid(), roomid)){
            throw new GeneralException("You don't have permission to do this.");
        }
        //See if there's already a permission for this user and this roomid
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                            .add(Restrictions.eq("userid", useridtogivepermissionto))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions!=null && roompermissions.size()>0){
            //There are already permissions assigned, make sure they're allowed/approved
            for (Iterator<Roompermission> rmpIt=roompermissions.iterator(); rmpIt.hasNext();) {
                try{
                    Roompermission roompermission=rmpIt.next();
                    roompermission.setIspendingapproval(false);
                    roompermission.save();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        } else {
            //Create a new room permission
            try{
                Roompermission roompermission = new Roompermission();
                roompermission.setRoomid(roomid);
                roompermission.setUserid(useridtogivepermissionto);
                roompermission.setIsmoderator(false);
                roompermission.setIspendingapproval(false);
                roompermission.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
    }

    public static void grantRoomMod(User user, int useridtogivepermissionto, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //Make sure we have a user to grant to
        if (useridtogivepermissionto==0){
            throw new GeneralException("Sorry, useridtogivepermissionto must be >0");
        } else {
            User usertograntto = User.get(useridtogivepermissionto);
            if (!isUserOk(usertograntto)){
                throw new GeneralException("User to grant permission to is invalid.");
            }
        }
        //See if room is valid
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        //See if this user has permission to mod this room
        if (!isModeratorOfRoom(user.getUserid(), roomid)){
            throw new GeneralException("You don't have permission to do this.");
        }
        //See if there's already a permission for this user and this roomid
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                            .add(Restrictions.eq("userid", useridtogivepermissionto))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions!=null && roompermissions.size()>0){
            //There are already permissions assigned, make sure they're allowed/approved
            for (Iterator<Roompermission> rmpIt=roompermissions.iterator(); rmpIt.hasNext();) {
                try{
                    Roompermission roompermission=rmpIt.next();
                    roompermission.setIspendingapproval(false);
                    roompermission.setIsmoderator(true);
                    roompermission.save();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        } else {
            //Create a new room permission
            try{
                Roompermission roompermission = new Roompermission();
                roompermission.setRoomid(roomid);
                roompermission.setUserid(useridtogivepermissionto);
                roompermission.setIsmoderator(true);
                roompermission.setIspendingapproval(false);
                roompermission.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
    }

    public static void revokeRoomPermission(User user, int useridtorevokefrom, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //See if room is valid
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        //See if this user has permission to mod this room
        if (!isModeratorOfRoom(user.getUserid(), roomid)){
            throw new GeneralException("You don't have permission to do this.");
        }
        //See if there's already a permission for this user and this roomid
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                            .add(Restrictions.eq("userid", useridtorevokefrom))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions!=null && roompermissions.size()>0){
            //There are already permissions assigned, make sure they're allowed/approved
            for (Iterator<Roompermission> rmpIt=roompermissions.iterator(); rmpIt.hasNext();) {
                try{
                    Roompermission roompermission=rmpIt.next();
                    roompermission.delete();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static void revokeRoomMod(User user, int useridtorevokefrom, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        //See if room is valid
        Room room = Room.get(roomid);
        if (room==null || room.getRoomid()==0 || !room.getIsenabled()){
            throw new GeneralException("Room invalid.");
        }
        //See if this user has permission to mod this room
        if (!isModeratorOfRoom(user.getUserid(), roomid)){
            throw new GeneralException("You don't have permission to do this.");
        }
        //See if there's already a permission for this user and this roomid
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                            .add(Restrictions.eq("userid", useridtorevokefrom))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions!=null && roompermissions.size()>0){
            //There are already permissions assigned, make sure they're allowed/approved
            for (Iterator<Roompermission> rmpIt=roompermissions.iterator(); rmpIt.hasNext();) {
                try{
                    Roompermission roompermission=rmpIt.next();
                    roompermission.setIsmoderator(false);
                    roompermission.save();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static ArrayList<RoomPermissionRequest> getRoomPermissionRequests(User user) throws GeneralException {
        if (!isUserOk(user)){
            throw new GeneralException("User invalid.");
        }
        ArrayList<RoomPermissionRequest> out = new ArrayList<RoomPermissionRequest>();
        ArrayList<Room> roomsUserModerates = getRoomsIModerate(user);
        //Iterate rooms this user moderates
        for (Iterator<Room> roomIterator=roomsUserModerates.iterator(); roomIterator.hasNext();) {
            Room room=roomIterator.next();
            //Find permissions for this room
            List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                           .add(Restrictions.eq("ispendingapproval", true))
                                            .add(Restrictions.eq("roomid", room.getRoomid()))
                                           .setCacheable(true)
                                           .list();
            for (Iterator<Roompermission> rpIt=roompermissions.iterator(); rpIt.hasNext();) {
                Roompermission roompermission=rpIt.next();
                User u = User.get(roompermission.getUserid());
                RoomPermissionRequest rpr = new RoomPermissionRequest();
                rpr.setNicknameofrequestor(u.getNickname());
                rpr.setUseridofrequestor(u.getUserid());
                rpr.setRoomid(room.getRoomid());
                rpr.setRoomname(room.getName());
                out.add(rpr);
            }
        }
        return out;
    }

}
