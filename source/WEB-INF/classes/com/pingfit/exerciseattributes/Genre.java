package com.pingfit.exerciseattributes;

/**
 * User: Joe Reger Jr
 * Date: Apr 11, 2009
 * Time: 4:25:24 PM
 */
public class Genre {
    private int genreid;
    private String name;

    public Genre(int genreid, String name){
        this.genreid = genreid;
        this.name = name;
    }

    public int getGenreid() {
        return genreid;
    }

    public void setGenreid(int genreid) {
        this.genreid=genreid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}
