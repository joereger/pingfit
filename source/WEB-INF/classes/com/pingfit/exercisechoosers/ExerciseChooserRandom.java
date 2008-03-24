package com.pingfit.exercisechoosers;

import com.pingfit.dao.Exercise;
import com.pingfit.dao.User;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.api.Exerciser;
import com.pingfit.util.Util;
import com.pingfit.util.GeneralException;
import com.pingfit.util.Num;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:27:15 AM
 */
public class ExerciseChooserRandom implements ExerciseChooser {

    public int getId(){
        return 2;
    }

    public String getName() {
        return "Random";
    }


    public ArrayList<Integer> getNextExercises(Exerciser exerciser, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<Integer> out = new ArrayList<Integer>();
        logger.debug("getNextExercise()");
        int maxexerciseid = (Integer)HibernateUtil.getSession().createQuery("select max(exerciseid) from Exercise").setCacheable(true).uniqueResult();
        boolean donthavevalidchoice = true;
        int attempts = 0;
        while(donthavevalidchoice && attempts<=(numbertoget+10)){
            attempts++;
            int randomExerciseid = Num.randomInt(maxexerciseid);
            List<Exercise> exs = HibernateUtil.getSession().createCriteria(Exercise.class)
                                               .add(Restrictions.eq("exerciseid", randomExerciseid))
                                               .setCacheable(true)
                                               .list();
            if (exs!=null && exs.size()>0){
                out.add(randomExerciseid);
            }
        }
        return out;
    }




}
