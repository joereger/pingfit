package com.pingfit.htmlui;

import org.apache.log4j.Logger;


import java.util.Iterator;

import com.pingfit.dao.Userrole;
import com.pingfit.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jun 23, 2006
 * Time: 1:06:30 PM
 */
public class Authorization {


    public static boolean check(String acl) {
        Logger logger = Logger.getLogger(Authorization.class);
        logger.debug("check() called");
        if (isAuthorized(acl)){
            return true;
        }
        return false;
    }

    private static boolean isAuthorized(String acl) {
        Logger logger = Logger.getLogger(Authorization.class);
        
        if (acl!=null && acl.equals("public")){
            return true;
        }

        if (Pagez.getUserSession().getUser()!=null){
            if (acl!=null && acl.equals("user")){
                for (Iterator<Userrole> iterator = Pagez.getUserSession().getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()==Userrole.USER){
                        logger.debug("Authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("pladmin")){
                for (Iterator<Userrole> iterator = Pagez.getUserSession().getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    //PLadmin or Sysadmin rights
                    if (userrole.getRoleid()==Userrole.PLADMIN || userrole.getRoleid()==Userrole.SYSADMIN){
                        logger.debug("Authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("sysadmin")){
                return isUserSysadmin(Pagez.getUserSession().getUser());
            }

            if (acl!=null && acl.equals("account")){
                return true;
            }
        }

        return false;

    }

    public static boolean isUserSysadmin(int userid){ return isUserSysadmin(User.get(userid));}
    public static boolean isUserSysadmin(User user){
        if (user!=null){
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSADMIN){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUserPladmin(int userid){ return isUserPladmin(User.get(userid));}
    public static boolean isUserPladmin(User user){
        if (user!=null){
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.PLADMIN){
                    return true;
                }
            }
        }
        return false;
    }







}
