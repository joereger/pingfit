package com.pingfit.exercisechoosers;

import com.pingfit.api.Exerciser;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.hibernate.NumFromUniqueResult;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.Exerciselist;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 11:27:15 AM
 */
public class ExerciseChooserList implements ExerciseChooser {

    public int getId(){
        return 1;
    }

    public String getName() {
        return "Lists";
    }


    public ArrayList<Integer> getNextExercises(Exerciser exerciser, int numbertoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<Integer> out = new ArrayList<Integer>();
        logger.debug("getNextExercise()");
        if (exerciser.getExerciselistid()<=0){
            exerciser.setExerciselistid(1);
        }

        int maxNumInList = NumFromUniqueResult.getInt("select max(num) from Exerciselistitem where exerciselistid='"+exerciser.getExerciselistid()+"'");
        logger.debug("maxNumInList="+maxNumInList);

        List<Exerciselistitem> exerciselistitems = HibernateUtil.getSession().createCriteria(Exerciselistitem.class)
                                               .add(Restrictions.eq("exerciselistid", exerciser.getExerciselistid()))
                                               .addOrder(Order.asc("num"))
                                               .setCacheable(true)
                                               .list();
        logger.debug("exerciselistitems.size()="+exerciselistitems.size());
        int attempts = 0;
        int tmpExNum = exerciser.getCurrentexercisenum();
        while(out.size()<=numbertoget && attempts<=(10+numbertoget)){
            attempts++;
            if (tmpExNum>maxNumInList){
                tmpExNum = 1;
            }
            logger.debug("exerciser.getCurrentexercisenum()="+exerciser.getCurrentexercisenum()+" tmpExNum="+tmpExNum);
            try{
                if (exerciselistitems.get(tmpExNum-1)!=null){
                    out.add(exerciselistitems.get(tmpExNum-1).getExerciseid());
                    logger.debug("added exerciselistitems.get("+(tmpExNum-1)+").getExerciseid()="+exerciselistitems.get(tmpExNum-1).getExerciseid());
                }
            } catch (Exception ex){
                logger.error("", ex);
            }
            tmpExNum++;
        }
        return out;
    }




}
