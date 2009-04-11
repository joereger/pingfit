package com.pingfit.exerciseattributes;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 11, 2009
 * Time: 4:28:11 PM
 */
public class EquipmentFactory {

    private static ArrayList<Equipment> list;

    public static void populate(){
        if (list!=null && list.size()>0){
            return;
        }
        list = new ArrayList<Equipment>();
        //Add items
        //The IDS can never change!!!!
        //Items can be removed
        //Names can be edited
        //Items can be added
        //But never reuse an id
        list.add(new Equipment(1, "Balance Board"));
        list.add(new Equipment(2, "Bench"));
        list.add(new Equipment(3, "Chair"));
        list.add(new Equipment(4, "Desk"));
        list.add(new Equipment(5, "Exercise Balls"));
        list.add(new Equipment(6, "Exercise Mat"));
        list.add(new Equipment(7, "Exercise Tube"));
        list.add(new Equipment(8, "Free Weights"));
        list.add(new Equipment(9, "Machines"));
        list.add(new Equipment(10, "Medicine Ball"));
        list.add(new Equipment(11, "Toning Bar"));
        list.add(new Equipment(12, "Pullup Bar"));
        list.add(new Equipment(13, "Situp Bar"));
        //Sort the heck out of it
        Collections.sort( list , new Comparator() {
            public int compare( Object o1 , Object o2 ){
                Equipment e1 = (Equipment)o1 ;
                Equipment e2 = (Equipment)o2 ;
                return e1.getName().compareTo( e2.getName() );
            }
        });
    }

    public static ArrayList<Equipment> getAll(){
        populate();
        return list;
    }

    public static Equipment getById(int id){
        populate();
        for (Iterator<Equipment> iterator=list.iterator(); iterator.hasNext();) {
            Equipment equipment=iterator.next();
            if (equipment.getEquipmentid()==id){
                return equipment;
            }
        }
        return null;
    }


}
