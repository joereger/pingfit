package com.pingfit.systemexercises;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.pingfit.dao.Exercise;
import com.pingfit.dao.Exerciselist;
import com.pingfit.dao.Exerciselistitem;
import com.pingfit.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Mar 18, 2008
 * Time: 9:28:25 PM
 */
public class SystemExerciseLists {

    public static void makeSureDatabaseHasAllSystemExerciseLists(){
        Logger logger = Logger.getLogger(SystemExerciseLists.class);
        try{
            Exerciselist ex = new Exerciselist();
            ex.setTitle("Basic Exercises");
            ex.setDescription("This is a good set of starter exercises that you can use to get familiar with the system.");
            ex.setExerciseeveryxminutes(20);
            ex.setIssystemdefault(false);
            ex.setIspublic(true);
            ex.setIssystem(true);
            ex.save();

            if (1==1){
                Exerciselistitem eli = new Exerciselistitem();
                eli.setExerciselistid(ex.getExerciselistid());
                eli.setExerciseid(1);
                eli.setNum(1);
                eli.setReps(10);
                eli.save();
                ex.getExerciselistitems().add(eli);
            }

            if (1==1){
                Exerciselistitem eli = new Exerciselistitem();
                eli.setExerciselistid(ex.getExerciselistid());
                eli.setExerciseid(2);
                eli.setNum(2);
                eli.setReps(10);
                eli.save();
                ex.getExerciselistitems().add(eli);
            }

            if (1==1){
                Exerciselistitem eli = new Exerciselistitem();
                eli.setExerciselistid(ex.getExerciselistid());
                eli.setExerciseid(3);
                eli.setNum(3);
                eli.setReps(10);
                eli.save();
                ex.getExerciselistitems().add(eli);
            }

            if (1==1){
                Exerciselistitem eli = new Exerciselistitem();
                eli.setExerciselistid(ex.getExerciselistid());
                eli.setExerciseid(4);
                eli.setNum(4);
                eli.setReps(10);
                eli.save();
                ex.getExerciselistitems().add(eli);
            }

            ex.save();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }



}
