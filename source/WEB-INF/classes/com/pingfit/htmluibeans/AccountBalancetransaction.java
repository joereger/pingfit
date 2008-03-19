package com.pingfit.htmluibeans;

import com.pingfit.util.SortableList;

import com.pingfit.util.Str;
import com.pingfit.util.Time;
import com.pingfit.htmlui.UserSession;
import com.pingfit.htmlui.Pagez;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.Balance;
import com.pingfit.dao.Balancetransaction;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountBalancetransaction implements Serializable {


    private List balances;

    public AccountBalancetransaction() {
        

    }



    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession!=null && userSession.getUser()!=null){
            List bals = HibernateUtil.getSession().createQuery("from Balancetransaction where userid='"+userSession.getUser().getUserid()+"' order by balancetransactionid desc").list();
            balances = new ArrayList<AccountBalancetransactionListItem>();
            for (Iterator iterator = bals.iterator(); iterator.hasNext();) {
                Balancetransaction balance = (Balancetransaction) iterator.next();
                AccountBalancetransactionListItem abli = new AccountBalancetransactionListItem();
                abli.setAmt("$"+ Str.formatForMoney(balance.getAmt()));
                abli.setBalancetransactionid(balance.getBalancetransactionid());
                abli.setDate(balance.getDate());
                abli.setDescription(balance.getDescription());
                abli.setNotes(balance.getNotes());
                abli.setUserid(balance.getUserid());
                abli.setIssuccessful(balance.getIssuccessful());
                balances.add(abli);
            }
        }
    }



    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }





    public List getBalances() {
        return balances;
    }

    public void setBalances(List balances) {
        this.balances = balances;
    }
}
