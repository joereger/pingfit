package com.pingfit.finders;

import com.pingfit.dao.Exercise;
import com.pingfit.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;
import org.hibernate.Criteria;

/**
 * User: Joe Reger Jr
 * Date: Mar 29, 2009
 * Time: 9:04:32 PM
 */
public class FindExercises {

    public static ArrayList<Exercise> find(int genreid, int musclegroupid, int equipmentid, boolean onlyreturnsystemexercises, int onlyReturnCreatedByUserid){
        ArrayList<Exercise> out = new ArrayList<Exercise>();
        Criteria criteria = HibernateUtil.getSession().createCriteria(Exercise.class);

        if (onlyreturnsystemexercises){
            criteria.add(Restrictions.eq("issystem", true));
        }
        if (onlyReturnCreatedByUserid>0){
            criteria.add(Restrictions.eq("useridofcreator", onlyReturnCreatedByUserid));
        }
        if (genreid>0){
            criteria.createAlias("exercisegenres", "gen");
            criteria.add(Restrictions.eq("gen.genreid", genreid));
        }
        if (musclegroupid>0){
            criteria.createAlias("exercisemusclegroups", "mgp");
            criteria.add(Restrictions.eq("mgp.musclegroupid", musclegroupid));
        }
        if (equipmentid>0){
            criteria.createAlias("exerciseequipments", "eqp");
            criteria.add(Restrictions.eq("eqp.equipmentid", equipmentid));
        }

        List<Exercise> exercises = criteria.setCacheable(true).list();
        for (Iterator<Exercise> exerciseIterator=exercises.iterator(); exerciseIterator.hasNext();) {
            Exercise exercise=exerciseIterator.next();
            out.add(exercise);
        }
        return out;
    }


}
