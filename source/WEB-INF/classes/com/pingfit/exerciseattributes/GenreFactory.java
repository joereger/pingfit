package com.pingfit.exerciseattributes;

import com.pingfit.db.Db;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 11, 2009
 * Time: 4:28:11 PM
 */
public class GenreFactory {

    private static ArrayList<Genre> list;

    public static void populate(){
        if (list!=null && list.size()>0){
            return;
        }
        list = new ArrayList<Genre>();
        //Add items
        //The IDS can never change!!!!
        //Items can be removed
        //Names can be edited
        //Items can be added
        //But never reuse an id
        list.add(new Genre(1, "General Strength"));
        list.add(new Genre(2, "Stretching"));
        list.add(new Genre(3, "Plyometrics"));
        list.add(new Genre(4, "Pilates"));
        list.add(new Genre(5, "Yoga"));
        list.add(new Genre(6, "Aerobics"));
        list.add(new Genre(7, "Relaxation"));
        list.add(new Genre(8, "Step"));
        list.add(new Genre(9, "Core Strength"));
        list.add(new Genre(10, "Strength"));
        list.add(new Genre(11, "Office Friendly"));
        list.add(new Genre(12, "Pregnant Friendly"));
        list.add(new Genre(13, "Geriatric Friendly"));
        list.add(new Genre(14, "Obese Friendly"));
        list.add(new Genre(15, "Multiple Person"));
        //Sort the heck out of it
        Collections.sort( list , new Comparator() {
            public int compare( Object o1 , Object o2 ){
                Genre e1 = (Genre)o1 ;
                Genre e2 = (Genre)o2 ;
                return e1.getName().compareTo( e2.getName() );
            }
        });
    }

    public static ArrayList<Genre> getAll(){
        populate();
        return list;
    }

    public static Genre getById(int id){
        populate();
        for (Iterator<Genre> iterator=list.iterator(); iterator.hasNext();) {
            Genre genre=iterator.next();
            if (genre.getGenreid()==id){
                return genre;
            }
        }
        return null;
    }


}