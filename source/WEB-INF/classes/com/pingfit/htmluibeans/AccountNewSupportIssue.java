package com.pingfit.htmluibeans;

import com.pingfit.dao.Supportissue;
import com.pingfit.dao.Supportissuecomm;

import com.pingfit.util.GeneralException;
import com.pingfit.util.ErrorDissect;
import com.pingfit.util.Time;
import com.pingfit.xmpp.SendXMPPMessage;
import com.pingfit.helpers.UserInputSafe;
import com.pingfit.email.EmailTemplateProcessor;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.ValidationException;

import java.util.Date;
import java.util.Iterator;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 28, 2006
 * Time: 8:39:38 AM
 */
public class AccountNewSupportIssue implements Serializable {

    private String subject;
    private String notes;

    public AccountNewSupportIssue(){}

    public void initBean(){

    }

    public void newIssue() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        Supportissue supportissue = new Supportissue();
        supportissue.setStatus(Supportissue.STATUS_OPEN);
        supportissue.setSubject(subject);
        supportissue.setDatetime(new Date());
        supportissue.setUserid(Pagez.getUserSession().getUser().getUserid());
        try{
            supportissue.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error.");
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        Supportissuecomm supportissuecomm = new Supportissuecomm();
        supportissuecomm.setSupportissueid(supportissue.getSupportissueid());
        supportissuecomm.setDatetime(new Date());
        supportissuecomm.setIsfromdneeroadmin(false);
        supportissuecomm.setNotes(notes);
        supportissue.getSupportissuecomms().add(supportissuecomm);
        try{
            supportissue.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error.");
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        //Notify customer care group
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "dNeero Support Issue: "+supportissue.getSubject()+" (supportissueid="+supportissue.getSupportissueid()+") ("+Pagez.getUserSession().getUser().getEmail()+") "+notes);
        xmpp.send();

        //Send email to sysadmin
        try{
            StringBuffer body = new StringBuffer();
            for (Iterator<Supportissuecomm> iterator = supportissue.getSupportissuecomms().iterator(); iterator.hasNext();){
                Supportissuecomm sicom = iterator.next();
                if (sicom.getIsfromdneeroadmin()){
                    body.append("<b>From dNeero Admin</b>");
                } else {
                    body.append("<b>From "+Pagez.getUserSession().getUser().getFirstname()+" "+Pagez.getUserSession().getUser().getLastname()+"</b>");
                }
                body.append("<br>");
                body.append(Time.dateformatfordb(Time.getCalFromDate(sicom.getDatetime())));
                body.append("<br>");
                body.append(sicom.getNotes());
                body.append("<br><br>");
            }
            EmailTemplateProcessor.sendGenericEmail("support@pingfit.com", "PingFit Support Issue: "+supportissue.getSubject(), body.toString());
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
