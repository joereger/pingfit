package com.pingfit.exerciseattributes;

/**
 * User: Joe Reger Jr
 * Date: Apr 11, 2009
 * Time: 4:25:24 PM
 */
public class Musclegroup {
    private int musclegroupid;
    private String name;

    public Musclegroup(int musclegroupid, String name){
        this.musclegroupid = musclegroupid;
        this.name = name;
    }

    public int getMusclegroupid() {
        return musclegroupid;
    }

    public void setMusclegroupid(int musclegroupid) {
        this.musclegroupid=musclegroupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}