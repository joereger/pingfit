package com.pingfit.htmluibeans;

import com.pingfit.util.SortableList;
import com.pingfit.util.Num;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.User;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminUserList implements Serializable {

    private List users;
    private String searchplid="";
    private String searchuserid="";
    private String searchfirstname="";
    private String searchlastname="";
    private String searchnickname="";
    private String searchemail="";
    private boolean searchfacebookers=false;


    public SysadminUserList() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load()");
        logger.debug("searchfirstname="+searchfirstname);
        logger.debug("searchlastname="+searchlastname);
        logger.debug("searchemail="+searchemail);
        Criteria crit = HibernateUtil.getSession().createCriteria(User.class);
        if (searchplid!=null && !searchplid.equals("") && Num.isinteger(searchplid) && !searchplid.equals("0")){
            crit.add(Restrictions.eq("plid", Integer.parseInt(searchplid)));
        } else {
            crit.add(Restrictions.gt("plid", 0));
        }
        if (searchuserid!=null && !searchuserid.equals("") && Num.isinteger(searchuserid)){
            crit.add(Restrictions.eq("userid", Integer.parseInt(searchuserid)));
        } else {
            crit.add(Restrictions.gt("userid", 0));
        }
        if (searchfirstname!=null && !searchfirstname.equals("")){
            crit.add(Restrictions.like("firstname", "%"+searchfirstname+"%"));
        }
        if (searchlastname!=null && !searchlastname.equals("")){
            crit.add(Restrictions.like("lastname", "%"+searchlastname+"%"));
        }
        if (searchnickname!=null && !searchnickname.equals("")){
            crit.add(Restrictions.like("nickname", "%"+searchnickname+"%"));
        }
        if (searchemail!=null && !searchemail.equals("")){
            crit.add(Restrictions.like("email", "%"+searchemail+"%"));
        }
        if(searchfacebookers){
            crit.add(Restrictions.gt("facebookuserid", 0));
        }
        users = crit.addOrder(Order.desc("userid")).list();
    }
    


    public List getUsers() {
        //logger.debug("getListitems");
        return users;
    }

    public void setUsers(List users) {
        //logger.debug("setListitems");
        this.users = users;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }




    public String getSearchfirstname() {
        return searchfirstname;
    }

    public void setSearchfirstname(String searchfirstname) {
        this.searchfirstname = searchfirstname;
    }

    public String getSearchlastname() {
        return searchlastname;
    }

    public void setSearchlastname(String searchlastname) {
        this.searchlastname = searchlastname;
    }

    public String getSearchnickname() {
        return searchnickname;
    }

    public void setSearchnickname(String searchnickname) {
        this.searchnickname=searchnickname;
    }

    public String getSearchemail() {
        return searchemail;
    }

    public void setSearchemail(String searchemail) {
        this.searchemail = searchemail;
    }

    public String getSearchuserid() {
        return searchuserid;
    }

    public void setSearchuserid(String searchuserid) {
        this.searchuserid = searchuserid;
    }

    public boolean getSearchfacebookers() {
        return searchfacebookers;
    }

    public void setSearchfacebookers(boolean searchfacebookers) {
        this.searchfacebookers = searchfacebookers;
    }

    public String getSearchplid() {
        return searchplid;
    }

    public void setSearchplid(String searchplid) {
        this.searchplid=searchplid;
    }
}
