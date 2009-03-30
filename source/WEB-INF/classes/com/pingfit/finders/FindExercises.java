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

    public static ArrayList<Exercise> find(String genre, String musclegroup, String equipment, boolean onlyreturnsystemexercises, int onlyReturnCreatedByUserid){
        ArrayList<Exercise> out = new ArrayList<Exercise>();
        Criteria criteria = HibernateUtil.getSession().createCriteria(Exercise.class);

        if (onlyreturnsystemexercises){
            criteria.add(Restrictions.eq("issystem", true));
        }
        if (onlyReturnCreatedByUserid>0){
            criteria.add(Restrictions.eq("useridofcreator", onlyReturnCreatedByUserid));
        }
        if (genre!=null && genre.length()>0){
            criteria.createAlias("genres", "gen");
            criteria.add(Restrictions.eq("gen.name", genre));
        }
        if (musclegroup!=null && musclegroup.length()>0){
            criteria.createAlias("musclegroups", "mgp");
            criteria.add(Restrictions.eq("mgp.name", musclegroup));
        }
        if (equipment!=null && equipment.length()>0){
            criteria.createAlias("equipments", "eqp");
            criteria.add(Restrictions.eq("eqp.name", equipment));
        }

        List<Exercise> exercises = criteria.setCacheable(true).list();
        for (Iterator<Exercise> exerciseIterator=exercises.iterator(); exerciseIterator.hasNext();) {
            Exercise exercise=exerciseIterator.next();
            out.add(exercise);
        }
        return out;
    }

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
            criteria.createAlias("genres", "gen");
            criteria.add(Restrictions.eq("gen.genreid", genreid));
        }
        if (musclegroupid>0){
            criteria.createAlias("musclegroups", "mgp");
            criteria.add(Restrictions.eq("mgp.musclegroupid", musclegroupid));
        }
        if (equipmentid>0){
            criteria.createAlias("equipments", "eqp");
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
