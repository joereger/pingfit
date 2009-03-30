package com.pingfit.helpers;

import com.pingfit.dao.Musclegroup;
import com.pingfit.dao.Equipment;
import com.pingfit.dao.Genre;
import com.pingfit.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Mar 29, 2009
 * Time: 11:52:28 PM
 */
public class ExercisePropertyValues {

    public static ArrayList<String> getMusclegroupsAsStrings(){
        ArrayList<String> musclegroups = new ArrayList<String>();
        for (Iterator<Musclegroup> mgit=getMusclegroups().iterator(); mgit.hasNext();) {
            Musclegroup musclegroup=mgit.next();
            musclegroups.add(musclegroup.getName());
        }
        return musclegroups;
    }
    public static ArrayList<Musclegroup> getMusclegroups(){
        return (ArrayList)HibernateUtil.getSession().createQuery("from Musclegroup order by name asc").list();
    }


    public static ArrayList<String> getEquipmentsAsStrings(){
        ArrayList<String> equipments = new ArrayList<String>();
        for (Iterator<Equipment> eqit=getEquipments().iterator(); eqit.hasNext();) {
            Equipment equipment=eqit.next();
            equipments.add(equipment.getName());
        }
        return equipments;
    }
    public static ArrayList<Equipment> getEquipments(){
        return (ArrayList)HibernateUtil.getSession().createQuery("from Equipment order by name asc").list();
    }

    public static ArrayList<String> getGenresAsStrings(){
        ArrayList<String> genres = new ArrayList<String>();
        for (Iterator<Genre> genit=getGenres().iterator(); genit.hasNext();) {
            Genre genre=genit.next();
            genres.add(genre.getName());
        }
        return genres;
    }
    public static ArrayList<Genre> getGenres(){     
        return (ArrayList)HibernateUtil.getSession().createQuery("from Genre order by name asc").list();
    }


}
