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
import com.pingfit.friends.Friend;
import com.pingfit.friends.RoomPermissionRequest;

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

    public static ArrayList<Friend> getFriends(User user){
        ArrayList<Friend> out = new ArrayList<Friend>();
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("ispendingapproval", false))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
            Userfriend userfriend=userfriendIterator.next();
            User u = User.get(userfriend.getUseridoffriend());
            Friend friend = new Friend();
            friend.setUserid(u.getUserid());
            friend.setNickname(u.getNickname());
            out.add(friend);
        }
        return out;
    }

    public static ArrayList<Friend> getFriendRequests(User user){
        ArrayList<Friend> out = new ArrayList<Friend>();
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                           .add(Restrictions.eq("useridoffriend", user.getUserid()))
                                           .add(Restrictions.eq("ispendingapproval", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
            Userfriend userfriend=userfriendIterator.next();
            User u = User.get(userfriend.getUseridoffriend());
            Friend friend = new Friend();
            friend.setUserid(u.getUserid());
            friend.setNickname(u.getNickname());
            out.add(friend);
        }
        return out;
    }

    public static void approveFriendRequest(User user, int useridoffriend){
        Logger logger = Logger.getLogger(CoreMethods.class);
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                            .add(Restrictions.eq("userid", useridoffriend))
                                            .add(Restrictions.eq("useridoffriend", user.getUserid()))
                                            .add(Restrictions.eq("ispendingapproval", true))
                                            .setCacheable(true)
                                            .list();
        for (Iterator<Userfriend> userfriendIterator=userfriends.iterator(); userfriendIterator.hasNext();) {
            try{
                Userfriend userfriend=userfriendIterator.next();
                userfriend.setIspendingapproval(false);
                userfriend.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
    }

    public static void rejectFriendRequest(User user, int useridoffriend){
        Logger logger = Logger.getLogger(CoreMethods.class);
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                            .add(Restrictions.eq("userid", useridoffriend))
                                            .add(Restrictions.eq("useridoffriend", user.getUserid()))
                                            .add(Restrictions.eq("ispendingapproval", true))
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

    public static void friendRequestByUserid(User user, int useridoffriend){
        Logger logger = Logger.getLogger(CoreMethods.class);
        List<Userfriend> userfriends = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                            .add(Restrictions.eq("userid", user.getUserid()))
                                            .add(Restrictions.eq("useridoffriend", useridoffriend))
                                            .setCacheable(true)
                                            .list();
        if (userfriends==null || userfriends.size()==0){
            //Only if there isn't already an existing friendship or friend request
            try{
                Userfriend userfriend = new Userfriend();
                userfriend.setUserid(user.getUserid());
                userfriend.setUseridoffriend(useridoffriend);
                userfriend.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
    }

    public static void breakFriendship(User user, int useridoffriend){
        Logger logger = Logger.getLogger(CoreMethods.class);
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

    public static void createRoom(User user, String name, String description, int exerciseeveryxminutes, int exerciselistid, boolean isprivate, boolean isfriendautopermit) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethods.class);
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
                room.setExerciseeveryxminutes(exerciseeveryxminutes);
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
            throw new GeneralException("A room by that name already exists.");
        }
    }

    public static void editRoom(User user, int roomid, String name, String description, int exerciseeveryxminutes, int exerciselistid) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Room room = Room.get(roomid);
            if (room.getUseridofcreator()!=user.getUserid()){
                throw new GeneralException("You did not create that room so you can not edit it.");
            }
            room.setName(name);
            room.setDescription(description);
            room.setExerciseeveryxminutes(exerciseeveryxminutes);
            room.setExerciselistid(exerciselistid);
            room.save();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static void deleteRoom(User user, int roomid) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Room room = Room.get(roomid);
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

    public static ArrayList<Room> getRoomsIModerate(User user){
        ArrayList<Room> out = new ArrayList<Room>();
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
        List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                           .add(Restrictions.in("roomid", roomidsUserMods))
                                           .add(Restrictions.eq("isenabled", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Room> it=rooms.iterator(); it.hasNext();) {
            Room room=it.next();
            out.add(room);
        }
        return out;
    }

    public static ArrayList<Room> getMyRooms(User user){
        ArrayList<Room> out = new ArrayList<Room>();
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
        List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                           .add(Restrictions.in("roomid", roomidsUserMods))
                                           .add(Restrictions.eq("isenabled", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Room> it=rooms.iterator(); it.hasNext();) {
            Room room=it.next();
            out.add(room);
        }
        return out;
    }

    public static void addToMyRooms(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        //See if room is valid
        Room room = Room.get(roomid);
        if (room!=null && room.getRoomid()>0){
            if (!room.getIsenabled()){
                throw new GeneralException("Room is disabled.");
            }
        }
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
                                if (room.getIsfriendautopermit() && isFriend(user, room.getUseridofcreator())){
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
                        if (room.getIsfriendautopermit() && isFriend(user, room.getUseridofcreator())){
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
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
    }

    public static void removeFromMyRooms(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
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

    public static ArrayList<Room> getRoomsMyFriendsAreIn(User user){
        ArrayList<Room> out = new ArrayList<Room>();
        //Get a list of friends' userids
        ArrayList<Friend> friends = getFriends(user);
        ArrayList<Integer> friendUserids = new ArrayList<Integer>();
        for (Iterator<Friend> iterator=friends.iterator(); iterator.hasNext();) {
            Friend friend=iterator.next();
            friendUserids.add(friend.getUserid());
        }
        //Find roomids that friends are in
        ArrayList<Integer> roomsFriendsInRoomids = new ArrayList<Integer>();
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
        //Go get the room objects themselves
        List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                            .add(Restrictions.in("roomid", roomsFriendsInRoomids))
                                           .add(Restrictions.eq("isenabled", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Room> roomIterator=rooms.iterator(); roomIterator.hasNext();) {
            Room room=roomIterator.next();
            out.add(room);
        }
        return out;
    }

    public static ArrayList<Room> getRoomsMyFriendsModerate(User user){
        ArrayList<Room> out = new ArrayList<Room>();
        //Get a list of friends' userids
        ArrayList<Friend> friends = getFriends(user);
        ArrayList<Integer> friendUserids = new ArrayList<Integer>();
        for (Iterator<Friend> iterator=friends.iterator(); iterator.hasNext();) {
            Friend friend=iterator.next();
            friendUserids.add(friend.getUserid());
        }
        //Find roomids that friends are moderating
        ArrayList<Integer> roomidsFriendsModerate = new ArrayList<Integer>();
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
        //Go get the room objects themselves
        List<Room> rooms = HibernateUtil.getSession().createCriteria(Room.class)
                                            .add(Restrictions.in("roomid", roomidsFriendsModerate))
                                           .add(Restrictions.eq("isenabled", true))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Room> roomIterator=rooms.iterator(); roomIterator.hasNext();) {
            Room room=roomIterator.next();
            out.add(room);
        }
        return out;
    }

    public static void requestRoomPermission(User user, int roomid){
        Logger logger = Logger.getLogger(CoreMethods.class);
        List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Userfriend.class)
                                            .add(Restrictions.eq("userid", user.getUserid()))
                                            .add(Restrictions.eq("roomid", roomid))
                                            .setCacheable(true)
                                            .list();
        if (roompermissions==null || roompermissions.size()==0){
            //Only if there isn't already an existing request
            try{
                Roompermission roompermission = new Roompermission();
                roompermission.setUserid(user.getUserid());
                roompermission.setRoomid(roomid);
                roompermission.setIspendingapproval(true);
                roompermission.setIsmoderator(false);
                roompermission.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
    }

    public static boolean isFriend(User user, int useridofotheruser){
        ArrayList<Friend> friends = getFriends(user);
        for (Iterator<Friend> iterator=friends.iterator(); iterator.hasNext();) {
            Friend friend=iterator.next();
            if (friend.getUserid()==useridofotheruser){
                return true;
            }
        }
        return false;
    }

    public static boolean isAllowedInRoom(User user, int roomid) {
        Room room = Room.get(roomid);
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
                if (isFriend(user, room.getUseridofcreator())){
                    return true;
                }
            }
            //If you have explicit permission to that room
            List<Roompermission> roompermissions = HibernateUtil.getSession().createCriteria(Roompermission.class)
                                               .add(Restrictions.eq("userid", user.getUserid()))
                                                .add(Restrictions.eq("roomid", roomid))
                                                .add(Restrictions.eq("ispendingapproval", false))
                                                .setCacheable(true)
                                               .list();
            if (roompermissions!=null && roompermissions.size()>0){
                return true;
            }
        }
        return false;
    }

    public static boolean isModeratorOfRoom(int userid, int roomid) {
        Room room = Room.get(roomid);
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

    public static void revokeRoomPermission(User user, int useridtogivepermissionto, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
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
                    roompermission.delete();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static void revokeRoomMod(User user, int useridtogivepermissionto, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
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
                    roompermission.setIsmoderator(false);
                    roompermission.save();
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }

    public static ArrayList<RoomPermissionRequest> getRoomPermissionRequests(User user){
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
