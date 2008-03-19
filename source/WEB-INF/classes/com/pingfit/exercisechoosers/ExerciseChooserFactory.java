package com.pingfit.exercisechoosers;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:53:37 AM
 */
public class ExerciseChooserFactory {

    public static ExerciseChooser get(int id){
        for (Iterator it = getAllExerciseChoosers().iterator(); it.hasNext(); ) {
            ExerciseChooser ec = (ExerciseChooser)it.next();
            if (ec.getId()==id){
                return ec;
            }
        }
        return null;
    }

    public static ArrayList<ExerciseChooser> getAllExerciseChoosers(){
        ArrayList<ExerciseChooser> out = new ArrayList<ExerciseChooser>();
        out.add(new ExerciseChooserRandom());
        return out;
    }

}
