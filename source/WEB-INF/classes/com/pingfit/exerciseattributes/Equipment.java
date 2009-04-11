package com.pingfit.exerciseattributes;

/**
 * User: Joe Reger Jr
 * Date: Apr 11, 2009
 * Time: 4:25:24 PM
 */
public class Equipment {
    private int equipmentid;
    private String name;

    public Equipment(int equipmentid, String name){
        this.equipmentid = equipmentid;
        this.name = name;
    }

    public int getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(int equipmentid) {
        this.equipmentid=equipmentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}