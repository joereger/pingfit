package com.pingfit.eula;

import com.pingfit.dao.Eula;
import com.pingfit.dao.User;
import com.pingfit.dao.Usereula;
import com.pingfit.dao.Pl;
import com.pingfit.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:03:56 PM
 */
public class EulaHelper {

    //public static Eula eula;

    public static Eula getMostRecentEula(int plid){
        Eula eula = new Eula();
        List eulas = HibernateUtil.getSession().createQuery("from Eula where plid='"+plid+"' order by eulaid desc").list();
        if (eulas!=null && eulas.size()>0){
            eula = (Eula)eulas.get(0);
        } else {
            //But since none was found in DB, create a blank empty one
            eula.setDate(new Date());
            eula.setEula("End User License Agreement");
        }
        return eula;
    }



    public static boolean isUserUsingMostRecentEula(User user){
        Logger logger = Logger.getLogger(EulaHelper.class);
        int highestEulaidForUser = 0;
        List results = HibernateUtil.getSession().createQuery("from Usereula where userid='"+user.getUserid()+"'").list();
        for (Iterator<Usereula> iterator = results.iterator(); iterator.hasNext();) {
            Usereula usereula = iterator.next();
            logger.debug("userid="+user.getUserid()+" usereulaid="+usereula.getUsereulaid()+" eulaid="+usereula.getEulaid());
            if (usereula.getEulaid()>highestEulaidForUser){
                highestEulaidForUser = usereula.getEulaid();
                logger.debug("setting highestEulaidForUser="+highestEulaidForUser);
            }
        }
        logger.debug("highestEulaidForUser="+highestEulaidForUser);
        logger.debug("getMostRecentEula().getEulaid()="+getMostRecentEula(user.getPlid()).getEulaid());
        if (highestEulaidForUser>=getMostRecentEula(user.getPlid()).getEulaid()){
            logger.debug("returning true because highesteulaidforuser>=getMostRecentEula().getEulaid()");
            return true;
        }                   
        logger.debug("returning false");
        return false;
    }


}
