package com.pingfit.api;

import com.pingfit.dao.*;
import com.pingfit.util.GeneralException;
import com.pingfit.exercisechoosers.ExerciseExtended;
import com.pingfit.friends.RoomPermissionRequest;
import com.pingfit.privatelabel.PlFinder;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Text;

import java.util.Iterator;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Mar 9, 2009
 * Time: 12:25:56 PM
 */
public class CoreMethodsReturningXML {

    public static Element bigRefresh(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("bigrefresh");
            element.addContent(getNextExercises(user));
            element.addContent(getLoggedInUser(user));
            element.addContent(getExerciseLists(user));
            element.addContent(getRooms(user));
            element.addContent(getCurrentEula(user.getPlid()));
            element.addContent(getFriends(user));
            element.addContent(getNotifications(user));
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getNotifications(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("notifications");
            element.addContent(getFriendRequests(user));
            element.addContent(getRoomPermissionRequests(user));
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element doExercise(User user, int exerciseid, int reps, String exerciseplaceinlist) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.doExercise(user, exerciseid, reps, exerciseplaceinlist);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element inviteByEmail(User user, String emailtoinvite, String custommessage) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.inviteByEmail(user, emailtoinvite, custommessage);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element skipExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.skipExercise(user);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element testApi(User user) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (CoreMethods.testApi(user)){
                return XMLConverters.resultXml(true, "");
            } else {
                return XMLConverters.resultXml(false, "Sorry, an error occurred and this test has failed.");
            }
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getPlPublicInfo(int plid) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Pl pl = Pl.get(plid);
            if (pl==null || pl.getPlid()==0){
                pl = PlFinder.defaultPl();
            }
            return XMLConverters.plPublicInfoAsXML(pl);
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getCurrentEula(int plid) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Eula eula = CoreMethods.getCurrentEula(plid);
            return XMLConverters.eulaAsXML(eula);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element isUserEulaUpToDate(User user) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return XMLConverters.booleanAsElement(CoreMethods.isUserEulaUpToDate(user), "isusereulauptodate");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element agreeToEula(User user, int eulaid, String ip) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.agreeToEula(user, eulaid, ip);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getCurrentExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            ExerciseExtended exExt = CoreMethods.getCurrentExercise(user);
            return XMLConverters.exerciseAsXML(exExt);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getNextExercises(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("nextexercises");
            element.setAttribute("secondsuntilnextexercise", String.valueOf(CoreMethods.getSecondsUntilNextExercise(user)));
            ArrayList<ExerciseExtended> nextExercises = CoreMethods.getNextExercises(user);
            for (Iterator it = nextExercises.iterator(); it.hasNext(); ) {
                ExerciseExtended exExt = (ExerciseExtended)it.next();
                element.addContent(XMLConverters.exerciseAsXML(exExt));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getExerciseLists(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("exerciselists");
            ArrayList<Exerciselist> exerciselists = CoreMethods.getExerciseLists(user);
            for (Iterator it = exerciselists.iterator(); it.hasNext(); ) {
                Exerciselist exerciselist = (Exerciselist)it.next();
                element.addContent(XMLConverters.exerciseListAsXML(exerciselist, false, false));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getRooms(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("rooms");
            ArrayList<Room> rooms = CoreMethods.getRooms(user);
            for (Iterator it = rooms.iterator(); it.hasNext(); ) {
                Room room = (Room)it.next();
                element.addContent(XMLConverters.roomAsXML(room));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getCurrentRoom(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return XMLConverters.roomAsXML(CoreMethods.getCurrentRoom(user));
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getExercise(int exerciseid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Exercise exExt = CoreMethods.getExercise(exerciseid);
            return XMLConverters.exerciseAsXML(exExt);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getExerciselist(int exerciselistid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Exerciselist exerciselist = CoreMethods.getExerciselist(exerciselistid);
            return XMLConverters.exerciseListAsXML(exerciselist, true, true);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getSecondsUntilNextExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            int secondsuntilnextexercise = CoreMethods.getSecondsUntilNextExercise(user);
            Element element = new Element("secondsuntilnextexercise");
            element.setContent(new Text(String.valueOf(secondsuntilnextexercise)));
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getUser(int userid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return XMLConverters.userAsXML(User.get(userid));
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getLoggedInUser(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("loggedinuser");
            element.addContent(XMLConverters.userAsXML(user));
            return element;
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element setExerciseEveryXMinutes(User user, int minutes) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.setExerciseEveryXMinutes(user, minutes);
            return bigRefresh(user);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element setNickname(User user, String nickname) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.setNickname(user, nickname);
            return bigRefresh(user);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element setExerciselist(User user, int exerciselistid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.setExerciselist(user, exerciselistid);
            return bigRefresh(user);
        } catch (GeneralException gex) {
            throw gex;
        } catch (Exception ex) {
            logger.error("", ex);
            throw new GeneralException("Unknown error... sorry... please try again.");
        }
    }

    public static Element setExerciseChooser(User user, int exercisechooserid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.setExerciseChooser(user, exercisechooserid);
            return bigRefresh(user);
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element signUp(String email, String password, String passwordverify, String firstname, String lastname, String nickname, int plid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.signUp(email, password, passwordverify, firstname, lastname, nickname, plid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getFriends(User user){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("friends");
            ArrayList<User> friends = CoreMethods.getFriends(user);
            for (Iterator it = friends.iterator(); it.hasNext(); ) {
                User friend = (User)it.next();
                element.addContent(XMLConverters.userAsXML(friend));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getFriendRequests(User user) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("friendrequests");
            ArrayList<User> friendrequests = CoreMethods.getFriendRequests(user);
            for (Iterator it = friendrequests.iterator(); it.hasNext(); ) {
                User friend = (User)it.next();
                element.addContent(XMLConverters.userAsXML(friend));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element addFriend(User user, int useridoffriend){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.addFriend(user, useridoffriend);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element breakFriendship(User user, int useridoffriend){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.breakFriendship(user, useridoffriend);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element createRoom(User user, String name, String description, int exerciselistid, boolean isprivate, boolean isfriendautopermit) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.createRoom(user, name, description, exerciselistid, isprivate, isfriendautopermit);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element editRoom(User user, int roomid, String name, String description, int exerciselistid, boolean isprivate, boolean isfriendautopermit) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.editRoom(user, roomid, name, description, exerciselistid, isprivate, isfriendautopermit);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element deleteRoom(User user, int roomid) throws GeneralException{
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.deleteRoom(user, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getRoomsIModerate(User user){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("roomsimoderate");
            ArrayList<Room> rooms = CoreMethods.getRoomsIModerate(user);
            for (Iterator it = rooms.iterator(); it.hasNext(); ) {
                Room room = (Room)it.next();
                element.addContent(XMLConverters.roomAsXML(room));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getMyRooms(User user){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("myrooms");
            ArrayList<Room> rooms = CoreMethods.getMyRooms(user);
            for (Iterator it = rooms.iterator(); it.hasNext(); ) {
                Room room = (Room)it.next();
                element.addContent(XMLConverters.roomAsXML(room));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element joinRoom(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.joinRoom(user, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element removeFromMyRooms(User user, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.removeFromMyRooms(user, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getRoomsMyFriendsAreIn(User user){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("roomsmyfriendsarein");
            ArrayList<Room> rooms = CoreMethods.getRoomsMyFriendsAreIn(user);
            for (Iterator it = rooms.iterator(); it.hasNext(); ) {
                Room room = (Room)it.next();
                element.addContent(XMLConverters.roomAsXML(room));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getRoomsMyFriendsModerate(User user){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("roomsmyfriendsmoderate");
            ArrayList<Room> rooms = CoreMethods.getRoomsMyFriendsModerate(user);
            for (Iterator it = rooms.iterator(); it.hasNext(); ) {
                Room room = (Room)it.next();
                element.addContent(XMLConverters.roomAsXML(room));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element areFriends(int userid1, int userid2){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            return XMLConverters.booleanAsElement(CoreMethods.areFriends(userid1, userid2), "arefriends");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element isAllowedInRoom(int userid, int roomid) {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            return XMLConverters.booleanAsElement(CoreMethods.isAllowedInRoom(userid, roomid), "isallowedinroom");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element isModeratorOfRoom(int userid, int roomid) {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            return XMLConverters.booleanAsElement(CoreMethods.isModeratorOfRoom(userid, roomid), "ismoderatorofroom");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element grantRoomPermission(User user, int useridtogivepermissionto, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.grantRoomPermission(user, useridtogivepermissionto, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element grantRoomMod(User user, int useridtogivepermissionto, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.grantRoomMod(user, useridtogivepermissionto, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element revokeRoomPermission(User user, int useridtorevokefrom, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.revokeRoomPermission(user, useridtorevokefrom, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element revokeRoomMod(User user, int useridtorevokefrom, int roomid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            CoreMethods.revokeRoomMod(user, useridtorevokefrom, roomid);
            return XMLConverters.resultXml(true, "");
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getRoomPermissionRequests(User user){
        Logger logger = Logger.getLogger(CoreMethodsReturningXML.class);
        try{
            Element element = new Element("roompermissionrequests");
            ArrayList<RoomPermissionRequest> roompermissionrequests = CoreMethods.getRoomPermissionRequests(user);
            for (Iterator it = roompermissionrequests.iterator(); it.hasNext(); ) {
                RoomPermissionRequest roompermissionrequest = (RoomPermissionRequest)it.next();
                element.addContent(XMLConverters.roompermissionrequestAsXML(roompermissionrequest));
            }
            return element;
        } catch (GeneralException gex) {
            return XMLConverters.resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return XMLConverters.resultXml(false, "Sorry, an unknown error occurred.");
        }
    }


}
