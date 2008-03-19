package com.pingfit.htmluibeans;

import org.apache.log4j.Logger;


import com.pingfit.dao.Supportissue;
import com.pingfit.dao.Supportissuecomm;
import com.pingfit.dao.User;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.htmlui.UserSession;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;

import com.pingfit.util.GeneralException;
import com.pingfit.xmpp.SendXMPPMessage;
import com.pingfit.helpers.UserInputSafe;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountSupportIssueDetail implements Serializable {

    private int supportissueid;
    private String notes;
    private ArrayList<Supportissuecomm> supportissuecomms;
    private Supportissue supportissue;

    public AccountSupportIssueDetail(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called: supportissueid="+supportissueid);
        String tmpSupportissueid = Pagez.getRequest().getParameter("supportissueid");
        if (com.pingfit.util.Num.isinteger(tmpSupportissueid)){
            logger.debug("beginView called: found supportissueid in param="+tmpSupportissueid);
            Supportissue supportissue = Supportissue.get(Integer.parseInt(tmpSupportissueid));
            if (Pagez.getUserSession().getUser()!=null && supportissue.canEdit(Pagez.getUserSession().getUser())){
                this.supportissue = supportissue;
                this.supportissueid = supportissue.getSupportissueid();
                supportissuecomms = new ArrayList<Supportissuecomm>();
                for (Iterator<Supportissuecomm> iterator = supportissue.getSupportissuecomms().iterator(); iterator.hasNext();){
                    Supportissuecomm supportissuecomm = iterator.next();
                    supportissuecomms.add(supportissuecomm);
                }
            }
        } else {
            logger.debug("beginView called: NOT found supportissueid in param="+tmpSupportissueid);
        }
    }

    public void newNote() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(supportissueid<=0){
            logger.debug("supportissueid not found: "+supportissueid);
            vex.addValidationError("supportissueid not found.");
        } else {
            supportissue = Supportissue.get(supportissueid);
        }

        Supportissuecomm supportissuecomm = new Supportissuecomm();
        supportissuecomm.setSupportissueid(supportissueid);
        supportissuecomm.setDatetime(new Date());
        supportissuecomm.setIsfromdneeroadmin(false);
        supportissuecomm.setNotes(notes);

        supportissue.getSupportissuecomms().add(supportissuecomm);

        try{
            supportissuecomm.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error saving the record.  Please try again.");
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }


        initBean();
        
        //Notify sales group
        Supportissue si = Supportissue.get(supportissueid);

        //Mark as new again
        si.setStatus(Supportissue.STATUS_OPEN);
        try{si.save();}catch(Exception ex){logger.error("",ex);}

        //Send xmpp message
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New dNeero Customer Support Comment: "+si.getSubject()+" (supportissueid="+supportissueid+") ("+Pagez.getUserSession().getUser().getEmail()+") "+notes);
        xmpp.send();

    }

    public int getSupportissueid() {
        return supportissueid;
    }

    public void setSupportissueid(int supportissueid) {
        this.supportissueid = supportissueid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Supportissue getSupportissue() {
        return supportissue;
    }

    public void setSupportissue(Supportissue supportissue) {
        this.supportissue = supportissue;
    }

    public ArrayList<Supportissuecomm> getSupportissuecomms() {
        return supportissuecomms;
    }

    public void setSupportissuecomms(ArrayList<Supportissuecomm> supportissuecomms) {
        this.supportissuecomms = supportissuecomms;
    }

}
