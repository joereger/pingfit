package com.pingfit.helpers;

import com.pingfit.dao.Exerciselist;
import com.pingfit.dao.Pl;
import com.pingfit.dao.Plexerciselist;
import com.pingfit.dao.hibernate.HibernateUtil;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Oct 3, 2009
 * Time: 10:41:19 AM
 */
public class PlExerciseListHelper {


    public static boolean canPlUseExerciseList(int plid, int exerciselistid){
        return canPlUseExerciseList(Pl.get(plid), Exerciselist.get(exerciselistid));
    }

    public static boolean canPlUseExerciseList(Pl pl, Exerciselist exerciselist){
        //The permission is very simple... if there's a row with plid and exerciselistid then the list is allowed in the pl
        List<Plexerciselist> plexerciselists = HibernateUtil.getSession().createCriteria(Plexerciselist.class)
                                           .add(Restrictions.eq("plid", pl.getPlid()))
                                           .add(Restrictions.eq("exerciselistid", exerciselist.getExerciselistid()))
                                           .setCacheable(true)
                                           .list();
        if (plexerciselists!=null && plexerciselists.size()>0){
            return true;
        } else {
            return false;
        }
    }

    public static void grantExerciseListAccessToPl(int plid, int exerciselistid){
        grantExerciseListAccessToPl(Pl.get(plid), Exerciselist.get(exerciselistid));
    }

    public static void grantExerciseListAccessToPl(Pl pl, Exerciselist exerciselist){
        Logger logger = Logger.getLogger(Plexerciselist.class);
        //Remove any records
        revokeExerciseListAccessToPl(pl, exerciselist);
        //Now add one
        Plexerciselist plexli = new Plexerciselist();
        plexli.setPlid(pl.getPlid());
        plexli.setExerciselistid(exerciselist.getExerciselistid());
        try{plexli.save();}catch(Exception ex){logger.error("", ex);}    
    }

    public static void revokeExerciseListAccessToPl(int plid, int exerciselistid){
        revokeExerciseListAccessToPl(Pl.get(plid), Exerciselist.get(exerciselistid));
    }

    public static void revokeExerciseListAccessToPl(Pl pl, Exerciselist exerciselist){
        HibernateUtil.getSession().createQuery("delete Plexerciselist e where e.plid='"+pl.getPlid()+"' and e.exerciselistid='"+exerciselist.getExerciselistid()+"'").executeUpdate();
    }

}
