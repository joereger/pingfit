package com.pingfit.api;

import com.pingfit.dao.Exercise;

import java.util.Iterator;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Mar 17, 2008
 * Time: 1:08:01 PM
 */
public class AjaxExercisePage {

    public static String getUpcomingExercisesHtml(Exerciser exerciser){
        StringBuffer out = new StringBuffer();
        boolean isfirst = true;
        for (Iterator<Integer> it = exerciser.getUpcomingexercises().iterator(); it.hasNext(); ) {
            Integer exerciseid = (Integer)it.next();
            Exercise exercise = Exercise.get(exerciseid);
            if (!isfirst){
                out.append(exercise.getTitle());
                if (it.hasNext()){
                    out.append(", ");
                }
            }
            isfirst = false;
        }
        return out.toString();
    }

    public static String getNextExerciseTimeFormattedForCountdownJavascript(Exerciser exerciser){
        return "2008-3-17 00:00:00 GMT+00:00";    
    }



    public static String getExerciseHtml(int exerciseid){
        Exercise exercise =  Exercise.get(exerciseid);
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"largefont\">"+exercise.getTitle()+"</font>");
        out.append("<br/>");
        out.append("<center>");
        out.append("<img src=\"/images/exercises/"+exercise.getImage()+"\" border=\"0\">");
        out.append("</center>");
        out.append("<br/>");
        out.append("<font class=\"formfieldnamefont\">"+exercise.getDescription()+"</font>");
        return out.toString();
    }



}
