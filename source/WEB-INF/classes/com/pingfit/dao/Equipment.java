package com.pingfit.dao;

import com.pingfit.dao.hibernate.BasePersistentClass;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;


public class Equipment extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int equipmentid;
     private String name;



    public static Equipment get(int id) {
        Logger logger = Logger.getLogger("com.pingfit.dao.Equipment");
        try {
            logger.debug("Equipment.get(" + id + ") called.");
            Equipment obj = (Equipment) HibernateUtil.getSession().get(Equipment.class, id);
            if (obj == null) {
                logger.debug("Equipment.get(" + id + ") returning new instance because hibernate returned null.");
                return new Equipment();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.pingfit.dao.Equipment", ex);
            return new Equipment();
        }
    }

    // Constructors

    /** default constructor */
    public Equipment() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

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