package com.pingfit.api;

import org.apache.log4j.Logger;
import org.apache.catalina.connector.ClientAbortException;
import org.jdom.Document;
import org.jdom.Element;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;

import com.pingfit.cache.providers.CacheFactory;
import com.pingfit.util.Str;
import com.pingfit.util.Util;
import com.pingfit.util.Num;
import com.pingfit.pageperformance.PagePerformanceUtil;
import com.pingfit.systemprops.InstanceProperties;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.User;
import com.pingfit.session.UrlSplitter;


/**
 * User: Joe Reger Jr
 * Date: Mar 9, 2009
 * Time: 10:48:39 AM
 */
public class RESTXmlApi extends HttpServlet {



    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        long timestart = new java.util.Date().getTime();
        Document outDoc = new Document();
        UrlSplitter urlSplitter = new UrlSplitter(request);
        logger.debug("RESTXmlAPI: "+urlSplitter.getParametersAsQueryStringNoQuestionMark());
        logger.debug("request.getParameter(\"email\")="+request.getParameter("email"));
        logger.debug("request.getParameter(\"password\")="+request.getParameter("password"));
        logger.debug("request.getParameter(\"method\")="+request.getParameter("method"));

        //Put incoming stuff into variables
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String method = request.getParameter("method");
        //@todo turn on cache by default after initial testing
        boolean cache = false;
        if (request.getParameter("cache")!=null && request.getParameter("cache").equals("true")){
            cache = true;
        } else if (request.getParameter("cache")!=null && request.getParameter("cache").equals("1")){
            cache = true;
        }
        //Find a user
        User user = null;
        List users = HibernateUtil.getSession().createQuery("FROM User as user WHERE user.email='"+ Str.cleanForSQL(email)+"' AND user.password='"+Str.cleanForSQL(password)+"'").setMaxResults(1).setCacheable(true).list();
        if (users.size()>0){
            try{user = (User)users.get(0);}catch(Exception ex){logger.error("", ex);}
        }
        //Build the response
        if (!method.equals("signUp") && (user==null || user.getUserid()<=0 || !user.getIsenabled())){
            Element root = new Element("result");
            root.addContent(CoreMethodsReturningXML.resultXml(false, "Sorry, username/password incorrect or this account is disabled."));
            outDoc = new Document(root);
        } else {
            String nameInCache = "xmlapi";
            String cacheGroup =  "xmlapi"+"/"+"method-"+method;
            Object fromCache = CacheFactory.getCacheProvider("DbcacheProvider").get(nameInCache, cacheGroup);
            if (fromCache!=null && cache){
                logger.debug("returning element from cache");
                //responseAsXML = (String)fromCache;
                outDoc.addContent((Element)fromCache);
            } else {
                logger.debug("rebuilding responseAsXML and putting into cache");
                try{
                    try{
                        Element element = null;
                        if (method.equalsIgnoreCase("doExercise")){
                            int exerciseid = 0;
                            if (Num.isinteger(request.getParameter("exerciseid"))){
                                exerciseid = Integer.parseInt(request.getParameter("exerciseid"));
                            }
                            int reps = 0;
                            if (Num.isinteger(request.getParameter("reps"))){
                                reps = Integer.parseInt(request.getParameter("reps"));
                            }
                            element = CoreMethodsReturningXML.doExercise(user, exerciseid, reps, request.getParameter("exerciseplaceinlist"));
                        } else if (method.equalsIgnoreCase("getCurrentExercise")){
                            element = CoreMethodsReturningXML.getCurrentExercise(user);
                        } else if (method.equalsIgnoreCase("signUp")){
                            String signupemail = request.getParameter("signupemail");
                            String signuppassword = request.getParameter("signuppassword");
                            String signuppasswordverify = request.getParameter("signuppasswordverify");
                            String firstname = request.getParameter("firstname");
                            String lastname = request.getParameter("lastname");
                            element = CoreMethodsReturningXML.signUp(signupemail, signuppassword, signuppasswordverify, firstname, lastname);
                        } else if (method.equalsIgnoreCase("testApi")){
                            element = CoreMethodsReturningXML.testApi(user);
                        } else if (method.equalsIgnoreCase("getCurrentEula")){
                            element = CoreMethodsReturningXML.getCurrentEula();
                        } else if (method.equalsIgnoreCase("getCurrentRoom")){
                            element = CoreMethodsReturningXML.getCurrentRoom(user);
                        } else if (method.equalsIgnoreCase("isUserEulaUpToDate")){
                            element = CoreMethodsReturningXML.isUserEulaUpToDate(user);
                        } else if (method.equalsIgnoreCase("agreeToEula")){
                            int eulaid = 0;
                            if (Num.isinteger(request.getParameter("eulaid"))){
                                eulaid = Integer.parseInt(request.getParameter("eulaid"));
                            }
                            String ip = request.getRemoteAddr();
                            element = CoreMethodsReturningXML.agreeToEula(user, eulaid, ip);
                        } else if (method.equalsIgnoreCase("getUserSettings")){
                            element = CoreMethodsReturningXML.getUserSettings(user);
                        } else if (method.equalsIgnoreCase("bigRefresh")){
                            element = CoreMethodsReturningXML.bigRefresh(user);
                        } else if (method.equalsIgnoreCase("getRooms")){
                            element = CoreMethodsReturningXML.getRooms(user);
                        } else if (method.equalsIgnoreCase("getExercise")){
                            int exerciseid = 0;
                            if (Num.isinteger(request.getParameter("exerciseid"))){
                                exerciseid = Integer.parseInt(request.getParameter("exerciseid"));
                            }
                            element = CoreMethodsReturningXML.getExercise(exerciseid);
                        } else if (method.equalsIgnoreCase("joinRoom")){
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.joinRoom(user, roomid);
                        } else if (method.equalsIgnoreCase("getExerciselist")){
                            int exerciselistid = 0;
                            if (Num.isinteger(request.getParameter("exerciselistid"))){
                                exerciselistid = Integer.parseInt(request.getParameter("exerciselistid"));
                            }
                            element = CoreMethodsReturningXML.getExerciselist(exerciselistid);
                        } else if (method.equalsIgnoreCase("skipExercise")){
                            element = CoreMethodsReturningXML.skipExercise(user);
                        } else if (method.equalsIgnoreCase("getSecondsUntilNextExercise")){
                            element = CoreMethodsReturningXML.getSecondsUntilNextExercise(user);
                        } else if (method.equalsIgnoreCase("getExerciseLists")){
                            element = CoreMethodsReturningXML.getExerciseLists(user);
                        } else if (method.equalsIgnoreCase("setExerciseEveryXMinutes")){
                            int minutes = 0;
                            if (Num.isinteger(request.getParameter("minutes"))){
                                minutes = Integer.parseInt(request.getParameter("minutes"));
                            }
                            element = CoreMethodsReturningXML.setExerciseEveryXMinutes(user, minutes);
                        } else if (method.equalsIgnoreCase("setExerciseChooser")){
                            int exercisechooserid = 0;
                            if (Num.isinteger(request.getParameter("exercisechooserid"))){
                                exercisechooserid = Integer.parseInt(request.getParameter("exercisechooserid"));
                            }
                            element = CoreMethodsReturningXML.setExerciseChooser(user, exercisechooserid);    
                        } else if (method.equalsIgnoreCase("setExerciseList")){
                            int exerciselistid = 0;
                            if (Num.isinteger(request.getParameter("exerciselistid"))){
                                exerciselistid = Integer.parseInt(request.getParameter("exerciselistid"));
                            }
                            element = CoreMethodsReturningXML.setExerciselist(user, exerciselistid);
                        }
                        //Add to doc and cache
                        if (element==null){
                            outDoc.addContent(CoreMethodsReturningXML.resultXml(false, "There was some sort of error building the response."));
                        } else {
                            //Add to the outDoc
                            Element root = new Element("result");
                            root.addContent(element);
                            outDoc = new Document(root);
                            //outDoc.addContent(element);
                            //Put bytes into cache
                            CacheFactory.getCacheProvider("DbcacheProvider").put(nameInCache, cacheGroup, element);
                        }
                    } catch (Exception ex){
                        logger.error("Error with transform in bottom section",ex);
                    }
                } catch (Exception ex){
                    logger.error("Error getting survey from cache: ex.getMessage()="+ex.getMessage(), ex);
                }
            }
        }
        //Output to client
        try{
            //Get servlet outputstream, set content type and send swf to browser client
            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("text/xml");
            String responseAsString = Util.jdomDocAsString(outDoc);
            outStream.write(responseAsString.getBytes());
            outStream.close();
        } catch (ClientAbortException cex){
            logger.debug("Client aborted", cex);
        } catch (java.net.SocketException sex){
            logger.debug("Trouble writing survey to browser", sex);
        } catch (Exception e){
            logger.error("Error writing survey to browser", e);
        }
        //Performance recording
        try {
            long timeend = new java.util.Date().getTime();
            long elapsedtime = timeend - timestart;
            PagePerformanceUtil.add("/RESTXmlApi", InstanceProperties.getInstancename(), elapsedtime);
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

}
