package com.pingfit.exercisechoosers;

import com.pingfit.api.Exerciser;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.Exercise;
import com.pingfit.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:27:15 AM
 */
public class ExerciseChooserList implements ExerciseChooser {

    public int getId(){
        return 2;
    }

    public String getName() {
        return "Exercise List";
    }


    public int getNextExercise(Exerciser exerciser){
        Logger logger = Logger.getLogger(this.getClass().getName());
        int maxexerciseid = (Integer) HibernateUtil.getSession().createQuery("select max(exerciseid) from Exercise").setCacheable(true).uniqueResult();
        boolean donthavevalidchoice = true;
        int attempts = 0;
        while(donthavevalidchoice && attempts<=10){
            attempts++;
            int randomExerciseid = Num.randomInt(maxexerciseid);
            List<Exercise> exs = HibernateUtil.getSession().createCriteria(Exercise.class)
                                               .add(Restrictions.eq("exerciseid", randomExerciseid))
                                               .setCacheable(true)
                                               .list();
            if (exs!=null && exs.size()>0){
                return randomExerciseid;
            }
        }
        logger.debug("Had to return 0 from getSingleExerciseAtRandom()");
        return 0;
    }




}
