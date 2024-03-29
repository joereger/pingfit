package com.pingfit.email;

import com.pingfit.dao.User;
import com.pingfit.util.RandomString;
import com.pingfit.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;

/**
 * User: Joe Reger Jr
 * Date: Jul 12, 2006
 * Time: 2:25:52 PM
 */
public class EmailActivationSend {

    public static void sendActivationEmail(User user){
        Logger logger = Logger.getLogger(EmailActivationSend.class);

        user.setIsactivatedbyemail(false);
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());
        try{user.save();} catch (GeneralException gex){logger.error("registerAction failed: " + gex.getErrorsAsSingleString());}
        EmailTemplateProcessor.sendMail("pingFit Account Activation", "accountactivation", user);
    }

}
