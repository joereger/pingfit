package com.pingfit.helpers;

import com.pingfit.dao.*;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.htmlui.UserSession;
import com.pingfit.htmlui.Authorization;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Oct 3, 2009
 * Time: 10:41:19 AM
 */
public class PlAdminHelper {


    public static boolean canUserControlPl(int userid, int plid){
        return canUserControlPl(User.get(userid), Pl.get(plid));
    }

    public static boolean canUserControlPl(User user, Pl pl){
        //Sysadmin can do all
        if (Authorization.isUserSysadmin(user)){return true;}
        //Non-pladmin can do none
        if (!Authorization.isUserPladmin(user)){return false;}
        //The permission is very simple... if there's a row with plid and userid then the list is allowed in the pl
        List<Pladmin> pladmins = HibernateUtil.getSession().createCriteria(Pladmin.class)
                                           .add(Restrictions.eq("plid", pl.getPlid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        if (pladmins!=null && pladmins.size()>0){
            return true;
        } else {
            return false;
        }
    }

    public static void grantPlControlToUser(int userid, int plid){
        grantPlControlToUser(User.get(userid), Pl.get(plid));
    }

    public static void grantPlControlToUser(User user, Pl pl){
        Logger logger = Logger.getLogger(PlAdminHelper.class);
        //Remove any records
        revokePlControlFromUser(user, pl);
        //Now add one
        Pladmin pladmin = new Pladmin();
        pladmin.setPlid(pl.getPlid());
        pladmin.setUserid(user.getUserid());
        try{pladmin.save();}catch(Exception ex){logger.error("", ex);}
    }

    public static void revokePlControlFromUser(int userid, int plid){
        revokePlControlFromUser(User.get(userid), Pl.get(plid));
    }

    public static void revokePlControlFromUser(User user, Pl pl){
        HibernateUtil.getSession().createQuery("delete Pladmin e where e.plid='"+pl.getPlid()+"' and e.userid='"+user.getUserid()+"'").executeUpdate();
    }

}