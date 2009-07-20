package com.pingfit.privatelabel;

import com.pingfit.htmlui.Pagez;
import com.pingfit.util.Time;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import java.io.StringWriter;
import java.util.Properties;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2008
 * Time: 8:16:03 AM
 */
public class TemplateProcessor {

    public static String process(Template velocityTemplate, VelocityContext velocityContext){
        Logger logger = Logger.getLogger(TemplateProcessor.class);
        StringBuffer out = new StringBuffer();
        try{
            Properties props = new Properties();
            props.setProperty("resource.loader","string");
            props.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            Velocity.init(props);
            if (Pagez.getUserSession()!=null){
                velocityContext.put("userSession", Pagez.getUserSession());
            }
            StringWriter sw = new StringWriter();
            velocityTemplate.merge(velocityContext, sw);
            out.append(sw.toString());
        } catch (Exception ex){
            logger.error("", ex);
        }
        return out.toString();
    }

    public static String process(String templateName, String template, VelocityContext velocityContext){
        Logger logger = Logger.getLogger(TemplateProcessor.class);
        StringBuffer out = new StringBuffer();
        try{
            Properties props = new Properties();
            props.setProperty("resource.loader","string");
            props.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            Velocity.init(props);
            if (Pagez.getUserSession()!=null){
                velocityContext.put("userSession", Pagez.getUserSession());
            }
            Template velocityTemplate = null;
            try{
               StringResourceRepository repo = StringResourceLoader.getRepository();
               String myTemplateName = templateName;
               String myTemplate = template;
               repo.putStringResource(myTemplateName, myTemplate);
               velocityTemplate = Velocity.getTemplate(myTemplateName);
            } catch( ResourceNotFoundException rnfe ){
                logger.error("Couldn't find the template", rnfe);
            } catch( ParseErrorException pee ){
                logger.error("Syntax error: problem parsing the template", pee);
            } catch( MethodInvocationException mie ){
                logger.error("Something invoked in the template", mie);
            } catch( Exception e ){
                logger.error("", e);
            }
            StringWriter sw = new StringWriter();
            velocityTemplate.merge(velocityContext, sw);
            out.append(sw.toString());
        } catch (Exception ex){
            logger.error("", ex);
        }
        return out.toString();
    }

    public static String process(String templateName, String template){
        Logger logger = Logger.getLogger(TemplateProcessor.class);
        StringBuffer out = new StringBuffer();
        try{
            Properties props = new Properties();
            props.setProperty("resource.loader","string");
            props.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            Velocity.init(props);
            VelocityContext velocityContext = new VelocityContext();
            if (Pagez.getUserSession()!=null){
                velocityContext.put("userSession", Pagez.getUserSession());
            }
            Template velocityTemplate = null;
            try{
               StringResourceRepository repo = StringResourceLoader.getRepository();
               String myTemplateName = templateName;
               String myTemplate = template;
               repo.putStringResource(myTemplateName, myTemplate);
               velocityTemplate = Velocity.getTemplate(myTemplateName);
            } catch( ResourceNotFoundException rnfe ){
                logger.error("Couldn't find the template", rnfe);
            } catch( ParseErrorException pee ){
                logger.error("Syntax error: problem parsing the template", pee);
            } catch( MethodInvocationException mie ){
                logger.error("Something invoked in the template", mie);
            } catch( Exception e ){
                logger.error("", e);
            }
            StringWriter sw = new StringWriter();
            velocityTemplate.merge(velocityContext, sw);
            out.append(sw.toString());
        } catch (Exception ex){
            logger.error("", ex);
        }
        return out.toString();
    }


}
