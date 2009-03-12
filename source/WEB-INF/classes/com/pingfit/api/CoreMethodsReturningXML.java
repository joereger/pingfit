package com.pingfit.api;

import com.pingfit.dao.User;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.Exerciselist;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.util.GeneralException;
import com.pingfit.util.Time;
import com.pingfit.exercisechoosers.ExerciseExtended;
import com.pingfit.systemprops.SystemProperty;
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

    public static Element doExercise(User user, int exerciseid, int reps, String exerciseplaceinlist) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.doExercise(user, exerciseid, reps, exerciseplaceinlist);
            return getCurrentExercise(user);
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element testApi(User user) {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            if (CoreMethods.testApi(user)){
                return resultXml(true, "");    
            } else {
                return resultXml(false, "Sorry, an error occurred and this test has failed.");
            }
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getCurrentExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            ExerciseExtended exExt = CoreMethods.getCurrentExercise(user);
            return exerciseAsXML(exExt);
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getExerciseLists(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("exerciselists");
            ArrayList<Exerciselist> exerciselists = CoreMethods.getExerciseLists(user);
            for (Iterator it = exerciselists.iterator(); it.hasNext(); ) {
                Exerciselist exerciselist = (Exerciselist)it.next();
                element.addContent(exerciseListAsXML(exerciselist, false, false));
            }
            return element;
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element bigRefresh(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Element element = new Element("bigrefresh");
            element.addContent(getCurrentExercise(user));
            element.addContent(getUserSettings(user));
            element.addContent(getExerciseLists(user));
            return element;
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getExercise(int exerciseid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Exercise exExt = CoreMethods.getExercise(exerciseid);
            return exerciseAsXML(exExt);
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getExerciselist(int exerciselistid) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            Exerciselist exerciselist = CoreMethods.getExerciselist(exerciselistid);
            return exerciseListAsXML(exerciselist, true, true);
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
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
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }



    public static Element skipExercise(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.skipExercise(user);
            return getCurrentExercise(user);
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element getUserSettings(User user) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            return userSettingsAsXML(user);
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element setExerciseEveryXMinutes(User user, int minutes) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.setExerciseEveryXMinutes(user, minutes);
            return bigRefresh(user);
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
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
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    public static Element signUp(String email, String password, String passwordverify, String firstname, String lastname) throws GeneralException {
        Logger logger = Logger.getLogger(CoreMethods.class);
        try{
            CoreMethods.signUp(email, password, passwordverify, firstname, lastname);
            return resultXml(true, "");
        } catch (GeneralException gex) {
            return resultXml(false, gex.getErrorsAsSingleStringNoHtml());
        } catch (Exception ex) {
            logger.error("", ex);
            return resultXml(false, "Sorry, an unknown error occurred.");
        }
    }

    protected static Element resultXml(boolean issuccessful, String message) {
        Element element = new Element("result");
        if (issuccessful){
            element.setAttribute("success", "true");
        } else {
            element.setAttribute("success", "false");
        }
        if (message!=null && !message.equals("")){
            Element msg = new Element("apimessage");
            msg.setContent(new Text(message));
            element.addContent(msg);
        }
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

    private static Element userSettingsAsXML(User user) {
        Element element = new Element("usersettings");
        element.addContent(nameValueElement("userid", String.valueOf(user.getUserid())));
        element.addContent(nameValueElement("email", String.valueOf(user.getEmail())));
        element.addContent(nameValueElement("firstname", String.valueOf(user.getFirstname())));
        element.addContent(nameValueElement("lastname", String.valueOf(user.getLastname())));
        element.addContent(nameValueElement("exerciselistid", String.valueOf(user.getExerciselistid())));
        element.addContent(nameValueElement("exercisechooserid", String.valueOf(user.getExercisechooserid())));
        element.addContent(nameValueElement("exerciseeveryxminutes", String.valueOf(user.getExerciseeveryxminutes())));
        element.addContent(nameValueElement("createdate", String.valueOf(Time.dateformatUtc(Time.getCalFromDate(user.getCreatedate())))));
        return element;
    }

    public static Element exerciseAsXML(ExerciseExtended exExt) {
        Element element = new Element("exercise");
        element.addContent(nameValueElement("exerciseid", String.valueOf(exExt.getExercise().getExerciseid())));
        element.addContent(nameValueElement("title", String.valueOf(exExt.getExercise().getTitle())));
        element.addContent(nameValueElement("description", String.valueOf(exExt.getExercise().getDescription())));
        String imageUrl = "http://"+ SystemProperty.getProp(SystemProperty.PROP_BASEURL) + "/images/exercises/" + exExt.getExercise().getImage();
        element.addContent(nameValueElement("image", imageUrl));
        element.addContent(nameValueElement("imagecredit", String.valueOf(exExt.getExercise().getImagecredit())));
        element.addContent(nameValueElement("reps", String.valueOf(exExt.getExercise().getReps())));
        element.addContent(nameValueElement("repsfromlist", String.valueOf(exExt.getRepsfromlist())));
        element.addContent(nameValueElement("ispublic", String.valueOf(exExt.getExercise().getIspublic())));
        element.addContent(nameValueElement("issystem", String.valueOf(exExt.getExercise().getIssystem())));
        element.addContent(nameValueElement("exerciseplaceinlist", exExt.getExerciseplaceinlist()));
        element.addContent(nameValueElement("secondsuntilnextexercise", String.valueOf(exExt.getSecondsuntilnextexercise())));
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
        element.addContent(nameValueElement("useridofcreator", String.valueOf(exerciselist.getUseridofcreator())));
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
                element.addContent(getExercise(exerciselistitem.getExerciseid()));
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

    private static Element nameValueElement(String name, String value){
        Element element = new Element(name);
        element.setContent(new Text(value));
        return element;
    }



    


}
