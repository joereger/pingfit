package com.pingfit.htmluibeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;


import com.pingfit.util.Str;
import com.pingfit.dao.Balancetransaction;
import com.pingfit.dao.hibernate.HibernateUtil;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminTransactions implements Serializable {

    private List transactions;


    public SysadminTransactions(){


    }


    public void initBean(){
        //Load transaction info
            List trans = HibernateUtil.getSession().createQuery("from Balancetransaction order by balancetransactionid desc").list();
            transactions = new ArrayList<AccountBalancetransactionListItem>();
            for (Iterator iterator = trans.iterator(); iterator.hasNext();) {
                Balancetransaction transaction = (Balancetransaction) iterator.next();
                AccountBalancetransactionListItem abli = new AccountBalancetransactionListItem();
                abli.setAmt("$"+ Str.formatForMoney(transaction.getAmt()));
                abli.setBalancetransactionid(transaction.getBalancetransactionid());
                abli.setDate(transaction.getDate());
                abli.setDescription(transaction.getDescription());
                abli.setNotes(transaction.getNotes());
                abli.setUserid(transaction.getUserid());
                abli.setIssuccessful(transaction.getIssuccessful());
                transactions.add(abli);
            }
    }


    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }
}
