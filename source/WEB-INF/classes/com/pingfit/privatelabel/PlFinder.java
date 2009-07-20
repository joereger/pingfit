package com.pingfit.privatelabel;

import com.pingfit.dao.Pl;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.cache.providers.CacheFactory;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 17, 2008
 * Time: 8:56:35 AM
 */
public class PlFinder {

    public static String CACHEGROUP = "PlFinder";

    public static Pl find(HttpServletRequest request){
        Logger logger = Logger.getLogger(PlFinder.class);
        logger.debug("request.getServerName()="+request.getServerName());
        String key = request.getServerName().toLowerCase();
        try{
            Object obj = CacheFactory.getCacheProvider().get(key, CACHEGROUP);
            if (obj!=null && (obj instanceof Pl)){
                Pl pl = (Pl)obj;
                logger.debug("Pl found in cache plid="+pl.getPlid()+" name="+pl.getName());
                return pl;
            } else {
                Pl pl = doDatabaseSearching(request);
                CacheFactory.getCacheProvider().put(key, CACHEGROUP, pl);
                return pl;
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return Pl.get(1);
    }

    private static Pl doDatabaseSearching(HttpServletRequest request){
        Logger logger = Logger.getLogger(PlFinder.class);
        logger.debug("doDatabaseSearching() called");

        //Search via subdomain
        String subdomain = "";
        String[] servernameSplit = request.getServerName().split("\\.");
        logger.debug("servernameSplit.length="+servernameSplit.length);
        if (servernameSplit!=null && servernameSplit.length>0 && servernameSplit[0]!=null){
            subdomain = servernameSplit[0];
            logger.debug("subdomain="+subdomain);
            List<Pl> plsSubdomain = HibernateUtil.getSession().createCriteria(Pl.class)
                           .add(Restrictions.eq("subdomain", subdomain.toLowerCase()))
                           .setCacheable(true)
                           .list();
            logger.debug("plsSubdomain.size()="+plsSubdomain.size());
            if (plsSubdomain!=null && plsSubdomain.size()>1){
                logger.error("More than one dNeero pl with subdomain="+subdomain);
            }
            for (Iterator<Pl> plIterator=plsSubdomain.iterator(); plIterator.hasNext();) {
                Pl pl=plIterator.next();
                logger.debug("Found pl via subdomain="+subdomain+": plid="+pl.getPlid()+" name="+pl.getName());
                return pl;
            }
        }
        //Search via customdomain1
        List<Pl> plsCustomdomain1 = HibernateUtil.getSession().createCriteria(Pl.class)
                       .add(Restrictions.eq("customdomain1", request.getServerName().toLowerCase()))
                       .setCacheable(true)
                       .list();
        for (Iterator<Pl> plIterator=plsCustomdomain1.iterator(); plIterator.hasNext();) {
            Pl pl=plIterator.next();
            logger.debug("Found pl via customdomain1="+pl.getCustomdomain1()+": plid="+pl.getPlid()+" name="+pl.getName());
            return pl;
        }
        //Search via customdomain2
        List<Pl> plsCustomdomain2 = HibernateUtil.getSession().createCriteria(Pl.class)
                       .add(Restrictions.eq("customdomain2", request.getServerName().toLowerCase()))
                       .setCacheable(true)
                       .list();
        for (Iterator<Pl> plIterator=plsCustomdomain2.iterator(); plIterator.hasNext();) {
            Pl pl=plIterator.next();
            logger.debug("Found pl via customdomain2="+pl.getCustomdomain2()+": plid="+pl.getPlid()+" name="+pl.getName());
            return pl;
        }
        //Search via customdomain3
        List<Pl> plsCustomdomain3 = HibernateUtil.getSession().createCriteria(Pl.class)
                       .add(Restrictions.eq("customdomain3", request.getServerName().toLowerCase()))
                       .setCacheable(true)
                       .list();
        for (Iterator<Pl> plIterator=plsCustomdomain3.iterator(); plIterator.hasNext();) {
            Pl pl=plIterator.next();
            logger.debug("Found pl via customdomain3="+pl.getCustomdomain3()+": plid="+pl.getPlid()+" name="+pl.getName());
            return pl;
        }


        //None found, return the basic
        return Pl.get(1);
    }

}
