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
        Criteria criteria = HibernateUtil.getSession().createCriteria(Object.class);

        if (onlyreturnsystemexercises){
            criteria = criteria.add(Restrictions.eq("issystem", true));
        }
        if (onlyReturnCreatedByUserid>0){
            criteria = criteria.add(Restrictions.eq("useridofcreator", onlyReturnCreatedByUserid));
        }
        if (genre!=null && genre.length()>0){
            criteria = criteria.createCriteria("genres").add(Restrictions.eq("name", genre));
        }
        if (musclegroup!=null && musclegroup.length()>0){
            criteria = criteria.createCriteria("musclegroups").add(Restrictions.eq("name", musclegroup));
        }
        if (equipment!=null && equipment.length()>0){
            criteria = criteria.createCriteria("equipments").add(Restrictions.eq("name", equipment));
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
        Criteria criteria = HibernateUtil.getSession().createCriteria(Object.class);

        if (onlyreturnsystemexercises){
            criteria = criteria.add(Restrictions.eq("issystem", true));
        }
        if (onlyReturnCreatedByUserid>0){
            criteria = criteria.add(Restrictions.eq("useridofcreator", onlyReturnCreatedByUserid));
        }
        if (genreid>0){
            criteria = criteria.createCriteria("genres").add(Restrictions.eq("genreid", genreid));
        }
        if (musclegroupid>0){
            criteria = criteria.createCriteria("musclegroups").add(Restrictions.eq("musclegroupid", musclegroupid));
        }
        if (equipmentid>0){
            criteria = criteria.createCriteria("equipments").add(Restrictions.eq("equipmentid", equipmentid));
        }

        List<Exercise> exercises = criteria.setCacheable(true).list();
        for (Iterator<Exercise> exerciseIterator=exercises.iterator(); exerciseIterator.hasNext();) {
            Exercise exercise=exerciseIterator.next();
            out.add(exercise);
        }
        return out;
    }


}
