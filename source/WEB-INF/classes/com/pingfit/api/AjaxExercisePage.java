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
        for (Iterator<Integer> it = exerciser.getUpcomingexercises().iterator(); it.hasNext(); ) {
            Integer exerciseid = (Integer)it.next();
            Exercise exercise = Exercise.get(exerciseid);
            out.append(
                "<div class=\"rounded\" style=\"padding: 10px; margin: 5px; background: #e6e6e6;\">\n" +
                "                        <font class=\"formfieldfont\" style=\"color: #666666;\"><b>"+exercise.getTitle()+"</b></font>\n" +
                "                        <!--<br/>-->\n" +
                "                        <!--<font class=\"tinyfont\" style=\"color: #666666;\">"+exercise.getDescription()+"</font>-->\n" +
                "                    </div>\n" +
                "                    <img src=\"/images/clear.gif\" alt=\"\" width=\"1\" height=\"5\"/>\n"
            );
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
