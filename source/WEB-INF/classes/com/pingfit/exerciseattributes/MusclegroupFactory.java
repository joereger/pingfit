package com.pingfit.exerciseattributes;

import com.pingfit.db.Db;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 11, 2009
 * Time: 4:28:11 PM
 */
public class MusclegroupFactory {

    private static ArrayList<Musclegroup> list;

    public static void populate(){
        if (list!=null && list.size()>0){
            return;
        }
        list = new ArrayList<Musclegroup>();
        //Add items
        //The IDS can never change!!!!
        //Items can be removed
        //Names can be edited
        //Items can be added
        //But never reuse an id
        list.add(new Musclegroup(1, "Abs:Lower"));
        list.add(new Musclegroup(2, "Abs:Side"));
        list.add(new Musclegroup(3, "Abs:Upper"));
        list.add(new Musclegroup(4, "Back"));
        list.add(new Musclegroup(5, "Back:Lower"));
        list.add(new Musclegroup(6, "Biceps"));
        list.add(new Musclegroup(7, "Buttocks"));
        list.add(new Musclegroup(8, "Calves"));
        list.add(new Musclegroup(9, "Chest"));
        list.add(new Musclegroup(10, "Forearms"));
        list.add(new Musclegroup(11, "Groin/Inner Thigh"));
        list.add(new Musclegroup(12, "Hamstring"));
        list.add(new Musclegroup(13, "Outer Thigh/Hips"));
        list.add(new Musclegroup(14, "Quads"));
        list.add(new Musclegroup(15, "Shoulders:Rear"));
        list.add(new Musclegroup(16, "Shoulders:Side"));
        list.add(new Musclegroup(17, "Shoulders:Front"));
        list.add(new Musclegroup(18, "Neck/Trapezius"));
        list.add(new Musclegroup(19, "Triceps"));
        list.add(new Musclegroup(20, "Wrist"));
        //Sort the heck out of it
        Collections.sort( list , new Comparator() {
            public int compare( Object o1 , Object o2 ){
                Musclegroup e1 = (Musclegroup)o1 ;
                Musclegroup e2 = (Musclegroup)o2 ;
                return e1.getName().compareTo( e2.getName() );
            }
        });
    }

    public static ArrayList<Musclegroup> getAll(){
        populate();
        return list;
    }

    public static Musclegroup getById(int id){
        populate();
        for (Iterator<Musclegroup> iterator=list.iterator(); iterator.hasNext();) {
            Musclegroup musclegroup=iterator.next();
            if (musclegroup.getMusclegroupid()==id){
                return musclegroup;
            }
        }
        return null;
    }


}