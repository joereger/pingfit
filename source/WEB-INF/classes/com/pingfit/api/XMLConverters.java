package com.pingfit.api;

import org.jdom.Element;
import org.jdom.Text;
import org.apache.log4j.Logger;
import com.pingfit.dao.*;
import com.pingfit.util.Time;
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.exercisechoosers.ExerciseExtended;
import com.pingfit.friends.RoomPermissionRequest;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Mar 31, 2009
 * Time: 11:39:34 AM
 */
public class XMLConverters {


    public static Element resultXml(boolean issuccessful, String message, String errorcode) {
        Element element = new Element("result");
        if (issuccessful){
            element.setAttribute("success", "true");
            Element succ = new Element("success");
            succ.setContent(new Text("true"));
            element.addContent(succ);
        } else {
            //Fail
            element.setAttribute("success", "false");
            Element succ = new Element("success");
            succ.setContent(new Text("false"));
            element.addContent(succ);
            //Message
            if (message==null || message.equals("")){message="Unspecified Error";}
            Element msg = new Element("apimessage");
            msg.setContent(new Text(message));
            element.addContent(msg);
            //Errorcode
            if (errorcode==null || errorcode.equals("")){errorcode="0";}
            Element errcd = new Element("errorcode");
            errcd.setContent(new Text(errorcode));
            element.addContent(errcd);
        }
        return element;
    }

    public static Element roompermissionrequestAsXML(RoomPermissionRequest roompermissionrequest) {
        Element element = new Element("roompermissionrequest");
        element.addContent(nameValueElement("roomid", String.valueOf(roompermissionrequest.getRoomid())));
        element.addContent(nameValueElement("useridofrequestor", String.valueOf(roompermissionrequest.getUseridofrequestor())));
        element.addContent(nameValueElement("nicknameofrequestor", roompermissionrequest.getNicknameofrequestor()));
        element.addContent(nameValueElement("roomname", roompermissionrequest.getRoomname()));
        return element;
    }


    public static Element eulaAsXML(Eula eula) {
        Element element = new Element("eula");
        element.addContent(nameValueElement("eulaid", String.valueOf(eula.getEulaid())));
        element.addContent(nameValueElement("eula", String.valueOf(eula.getEula())));
        element.addContent(nameValueElement("date", Time.dateformatcompactwithtime(Time.getCalFromDate(eula.getDate()))));
        return element;
    }

    public static Element plPublicInfoAsXML(Pl pl) {
        Element element = new Element("pl");
        element.addContent(nameValueElement("plid", String.valueOf(pl.getPlid())));
        element.addContent(nameValueElement("name", String.valueOf(pl.getNameforui())));
        element.addContent(nameValueElement("airlogo", String.valueOf(pl.getAirlogo())));
        element.addContent(nameValueElement("airbgcolor", String.valueOf(pl.getAirbgcolor())));
        return element;
    }

    public static Element exerciseAsXML(Exercise exercise) {
        Element element = new Element("exercise");
        element.addContent(nameValueElement("exerciseid", String.valueOf(exercise.getExerciseid())));
        element.addContent(nameValueElement("title", String.valueOf(exercise.getTitle())));
        element.addContent(nameValueElement("description", String.valueOf(exercise.getDescription())));
        String imageUrl = "http://"+ SystemProperty.getProp(SystemProperty.PROP_BASEURL) + "/images/exercises/" + exercise.getImage();
        element.addContent(nameValueElement("image", imageUrl));
        element.addContent(nameValueElement("imagecredit", String.valueOf(exercise.getImagecredit())));
        element.addContent(nameValueElement("reps", String.valueOf(exercise.getReps())));
        element.addContent(nameValueElement("ispublic", String.valueOf(exercise.getIspublic())));
        element.addContent(nameValueElement("issystem", String.valueOf(exercise.getIssystem())));
        return element;
    }

    public static Element pingbackAsXML(Pingback pingback) {
        Element element = new Element("pingback");
        element.addContent(nameValueElement("pingbackid", String.valueOf(pingback.getPingbackid())));
        element.addContent(nameValueElement("userid", String.valueOf(pingback.getUserid())));
        element.addContent(nameValueElement("date", String.valueOf(Time.dateformatUtc(Time.getCalFromDate(pingback.getDate())))));
        element.addContent(nameValueElement("reps", String.valueOf(pingback.getReps())));
        Exercise exercise = Exercise.get(pingback.getExerciseid());
        element.addContent(exerciseAsXML(exercise));
        return element;
    }

    public static Element userAsXML(User user) {
        Logger logger = Logger.getLogger(XMLConverters.class);
        Element element = new Element("user");
        element.addContent(nameValueElement("userid", String.valueOf(user.getUserid())));
        element.addContent(nameValueElement("email", String.valueOf(user.getEmail())));
        element.addContent(nameValueElement("firstname", String.valueOf(user.getFirstname())));
        element.addContent(nameValueElement("lastname", String.valueOf(user.getLastname())));
        element.addContent(nameValueElement("nickname", String.valueOf(user.getNickname())));
        element.addContent(nameValueElement("exerciselistid", String.valueOf(user.getExerciselistid())));
        element.addContent(nameValueElement("exercisechooserid", String.valueOf(user.getExercisechooserid())));
        element.addContent(nameValueElement("exerciseeveryxminutes", String.valueOf(user.getExerciseeveryxminutes())));
        String createdate = "";
        try{createdate=String.valueOf(Time.dateformatUtc(Time.getCalFromDate(user.getCreatedate())));}catch(Exception ex){logger.error("");}
        element.addContent(nameValueElement("createdate", createdate));
        element.addContent(nameValueElement("plid", String.valueOf(user.getPlid())));
        element.addContent(nameValueElement("roomid", String.valueOf(user.getRoomid())));
        element.addContent(nameValueElement("facebookuid", String.valueOf(user.getFacebookuid())));
        element.addContent(nameValueElement("profileimageurl", ""));
        element.addContent(CoreMethodsReturningXML.isUserEulaUpToDate(user));
        element.addContent(CoreMethodsReturningXML.getRecentExercises(user, user.getUserid(), 20));
        try{
            Room currentRoom = CoreMethods.getCurrentRoom(user);
            element.addContent(roomAsXML(currentRoom));
        } catch (Exception ex){
            logger.error("", ex);
        }
        return element;
    }

   

    public static Element exerciseAsXML(ExerciseExtended exExt) {
        Element element = new Element("exercise");
        if (exExt!=null && exExt.getExercise()!=null){
            element.addContent(nameValueElement("exerciseid", String.valueOf(exExt.getExercise().getExerciseid())));
            element.addContent(nameValueElement("title", String.valueOf(exExt.getExercise().getTitle())));
            element.addContent(nameValueElement("description", String.valueOf(exExt.getExercise().getDescription())));
            String imageUrl = "http://"+ SystemProperty.getProp(SystemProperty.PROP_BASEURL) + "/images/exercises/" + exExt.getExercise().getImage();
            element.addContent(nameValueElement("image", imageUrl));
            element.addContent(nameValueElement("imagecredit", String.valueOf(exExt.getExercise().getImagecredit())));
            element.addContent(nameValueElement("reps", String.valueOf(exExt.getReps())));
            element.addContent(nameValueElement("ispublic", String.valueOf(exExt.getExercise().getIspublic())));
            element.addContent(nameValueElement("issystem", String.valueOf(exExt.getExercise().getIssystem())));
            element.addContent(nameValueElement("exerciseplaceinlist", exExt.getExerciseplaceinlist()));
            element.addContent(nameValueElement("timeinseconds", String.valueOf(exExt.getTimeinseconds())));
            element.addContent(nameValueElement("exerciselistid", String.valueOf(exExt.getExerciselistid())));
            element.addContent(nameValueElement("exerciselistitemid", String.valueOf(exExt.getExerciselistitemid())));
        }
        return element;
    }

    public static Element roomAsXML(Room room) {
        Element element = new Element("room");
        element.addContent(nameValueElement("roomid", String.valueOf(room.getRoomid())));
        element.addContent(nameValueElement("isenabled", String.valueOf(room.getIsenabled())));
        element.addContent(nameValueElement("issystem", String.valueOf(room.getIssystem())));
        element.addContent(nameValueElement("isprivate", String.valueOf(room.getIsprivate())));
        element.addContent(nameValueElement("isfriendautopermit", String.valueOf(room.getIsfriendautopermit())));
        element.addContent(nameValueElement("useridofcreator", String.valueOf(room.getUseridofcreator())));
        element.addContent(nameValueElement("name", String.valueOf(room.getName())));
        element.addContent(nameValueElement("description", String.valueOf(room.getDescription())));
        element.addContent(nameValueElement("exerciselistid", String.valueOf(room.getExerciselistid())));
        Exerciselist exerciselist = Exerciselist.get(room.getExerciselistid());
        if (exerciselist!=null && exerciselist.getExerciselistid()>0){
            element.addContent(exerciseListAsXML(exerciselist, false, false));
        }
        return element;
    }

    public static Element exerciseListAsXML(Exerciselist exerciselist) {
        return exerciseListAsXML(exerciselist, false, false);
    }

    public static Element exerciseListAsXML(Exerciselist exerciselist, boolean includeExerciselistitems) {
        return exerciseListAsXML(exerciselist, includeExerciselistitems);
    }

    public static Element exerciseListAsXML(Exerciselist exerciselist, boolean includeExerciselistitems, boolean includeExercise) {
        Element element = new Element("exerciselist");
        element.addContent(nameValueElement("exerciselistid", String.valueOf(exerciselist.getExerciselistid())));
        element.addContent(nameValueElement("title", String.valueOf(exerciselist.getTitle())));
        element.addContent(nameValueElement("description", String.valueOf(exerciselist.getDescription())));
        element.addContent(nameValueElement("ispublic", String.valueOf(exerciselist.getIspublic())));
        element.addContent(nameValueElement("issystem", String.valueOf(exerciselist.getIssystem())));
        element.addContent(nameValueElement("issystemdefault", String.valueOf(exerciselist.getIssystemdefault())));
        element.addContent(nameValueElement("useridofcreator", String.valueOf(exerciselist.getUseridofcreator())));
        element.addContent(nameValueElement("isautoadvance", String.valueOf(exerciselist.getIsautoadvance())));
        if (includeExerciselistitems){
            for (Iterator<Exerciselistitem> iterator=exerciselist.getExerciselistitems().iterator(); iterator.hasNext();) {
                Exerciselistitem exerciselistitem=iterator.next();
                element.addContent(exerciseListItemAsXML(exerciselistitem, includeExercise));
            }
        }
        return element;
    }

    public static Element exerciseListItemAsXML(Exerciselistitem exerciselistitem) {
        return exerciseListItemAsXML(exerciselistitem, false);
    }

    public static Element exerciseListItemAsXML(Exerciselistitem exerciselistitem, boolean includeExercise) {
        Element element = new Element("exerciselistitem");
        element.addContent(nameValueElement("exerciselistitemid", String.valueOf(exerciselistitem.getExerciselistitemid())));
        if (includeExercise){
            try{
                element.addContent(CoreMethodsReturningXML.getExercise(exerciselistitem.getExerciseid()));
            } catch (Exception ex){
                element.addContent(nameValueElement("exerciseid", String.valueOf(exerciselistitem.getExerciseid())));
            }
        } else {
            element.addContent(nameValueElement("exerciseid", String.valueOf(exerciselistitem.getExerciseid())));
        }
        element.addContent(nameValueElement("exerciselistid", String.valueOf(exerciselistitem.getExerciselistid())));
        element.addContent(nameValueElement("num", String.valueOf(exerciselistitem.getNum())));
        element.addContent(nameValueElement("reps", String.valueOf(exerciselistitem.getReps())));
        return element;
    }

    public static Element nameValueElement(String name, String value){
        Element element = new Element(name);
        element.setContent(new Text(value));
        return element;
    }

    public static Element booleanAsElement(boolean bool, String elementname){
        if (bool){
            return nameValueElement(elementname, "true");
        } else {
            return nameValueElement(elementname, "false");
        }
    }
}
