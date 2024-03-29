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
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.dao.hibernate.HibernateUtil;
import com.pingfit.dao.User;
import com.pingfit.session.UrlSplitter;
import com.google.code.facebookapi.FacebookXmlRestClient;


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
        StringBuffer errorMsg = new StringBuffer();
        String errorcode = "";
        UrlSplitter urlSplitter = new UrlSplitter(request);
        logger.debug("RESTXmlAPI: "+urlSplitter.getParametersAsQueryStringNoQuestionMark());
        logger.debug("request.getParameter(\"email\")="+request.getParameter("email"));
        logger.debug("request.getParameter(\"password\")="+request.getParameter("password"));
        logger.debug("request.getParameter(\"method\")="+request.getParameter("method"));
        //Put incoming stuff into variables
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String method = request.getParameter("method");
        String facebook_session_key = request.getParameter("facebook_session_key");
        String facebook_session_secret = request.getParameter("facebook_session_secret");
        String facebookuid = request.getParameter("facebookuid");
        String facebookapikey = request.getParameter("facebookapikey");
        //Cache off for now
        boolean cache = false;
//        if (request.getParameter("cache")!=null && request.getParameter("cache").equals("true")){
//            cache = true;
//        } else if (request.getParameter("cache")!=null && request.getParameter("cache").equals("1")){
//            cache = true;
//        }
        //Find a user
        User user = null;
        //Is this a facebook user I need to make certain the session key is valid
        boolean isFacebookAuth = false;
        boolean isFacebookAuthSuccess = false;
        if (facebook_session_key!=null && !facebook_session_key.equals("") && facebookuid!=null && !facebookuid.equals("")){
            isFacebookAuth = true;
        }
        //Not facebookauth
        if (!isFacebookAuth){
            logger.debug("!isFacebookAuth");
            List users = HibernateUtil.getSession().createQuery("FROM User as user WHERE user.email='"+ Str.cleanForSQL(email)+"' AND user.password='"+Str.cleanForSQL(password)+"'").setMaxResults(1).setCacheable(true).list();
            if (users.size()>0){
                try{user = (User)users.get(0);}catch(Exception ex){logger.error("", ex);}
            }
        }
        //Is facebookauth
        if (isFacebookAuth){
            logger.debug("isFacebookAuth");
            List users = HibernateUtil.getSession().createQuery("FROM User as user WHERE user.facebookuid='"+Str.cleanForSQL(facebookuid)+"'").setMaxResults(1).setCacheable(true).list();
            if (users.size()>0){
                try{user = (User)users.get(0);}catch(Exception ex){logger.error("", ex);}
            }
            if (user!=null){
                //Make sure session key's valid
                isFacebookAuthSuccess = isFacebookSessionKeyValid(facebookapikey, facebook_session_key, facebook_session_secret, facebookuid);
                if (!isFacebookAuthSuccess){
                    logger.debug("!isFacebookAuthSuccess");
                    //If it's a failed facebook session, null the user to prevent access
                    user = null;
                    errorMsg.append("Facebook session_key invalid.");
                    errorcode = "FACEBOOKAUTHFAILSESSIONKEYINVALID";
                }  else {
                    logger.debug("isFacebookAuthSuccess");
                }
            } else {
                errorMsg.append("No user found with that facebookuid.");
                errorcode = "FACEBOOKAUTHFAILFACEBOOKUIDNOTFOUND";
            }
        }
        //See whether this call requires an authenticated user
        boolean callRequiresAuthenticatedUser = true;
        if (method.equals("signUp")){callRequiresAuthenticatedUser=false;}
        if (method.equals("getPl")){callRequiresAuthenticatedUser=false;}
        if (method.equals("getUserByFacebookuid")){callRequiresAuthenticatedUser=false;}
        //Build the response
        if (callRequiresAuthenticatedUser && (user==null || user.getUserid()<=0 || !user.getIsenabled() || errorMsg.length()>0)){
            logger.debug("API requirements not met.  errorMsg="+errorMsg);
            //Element root = new Element("result");
            //root.setAttribute("success", "false");
            if (errorMsg.length()==0){errorMsg.append("Sorry, username/password incorrect, account disabled or unauthorized access detected.");}
            //root.addContent(XMLConverters.resultXml(false, errorMsg.toString(), errorcode));
            outDoc = new Document(XMLConverters.resultXml(false, errorMsg.toString(), errorcode));
        } else {
            logger.debug("API requirements met... will respond with dataz");
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
                        } else if (method.equalsIgnoreCase("getNextExercises")){
                            element = CoreMethodsReturningXML.getNextExercises(user);
                        } else if (method.equalsIgnoreCase("signUp")){
                            String signupemail = request.getParameter("signupemail");
                            String signuppassword = request.getParameter("signuppassword");
                            String signuppasswordverify = request.getParameter("signuppasswordverify");
                            String firstname = request.getParameter("firstname");
                            String lastname = request.getParameter("lastname");
                            String nickname = request.getParameter("nickname");
                            int plid = 1;
                            if (Num.isinteger(request.getParameter("plid"))){
                                plid = Integer.parseInt(request.getParameter("plid"));
                            }
                            element = CoreMethodsReturningXML.signUp(signupemail, signuppassword, signuppasswordverify, firstname, lastname, nickname, facebookuid, plid);
                        } else if (method.equalsIgnoreCase("testApi")){
                            element = CoreMethodsReturningXML.testApi(user);
                        } else if (method.equalsIgnoreCase("getCurrentEula")){
                            int plid = 1;
                            if (Num.isinteger(request.getParameter("plid"))){
                                plid = Integer.parseInt(request.getParameter("plid"));
                            }
                            element = CoreMethodsReturningXML.getCurrentEula(plid);
                        } else if (method.equalsIgnoreCase("getPl")){
                            int plid = 1;
                            if (Num.isinteger(request.getParameter("plid"))){
                                plid = Integer.parseInt(request.getParameter("plid"));
                            }
                            element = CoreMethodsReturningXML.getPlPublicInfo(plid);
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
                        } else if (method.equalsIgnoreCase("getUser")){
                            int userid = 0;
                            if (Num.isinteger(request.getParameter("userid"))){
                                userid = Integer.parseInt(request.getParameter("userid"));
                            }
                            element = CoreMethodsReturningXML.getUser(userid);
                        } else if (method.equalsIgnoreCase("getUserByFacebookuid")){
                            element = CoreMethodsReturningXML.getUserByFacebookuid(request.getParameter("facebookuid"));
                        } else if (method.equalsIgnoreCase("inviteByEmail")){
                            String emailtoinvite = request.getParameter("emailtoinvite");
                            String custommessage = "";
                            if (request.getParameter("custommessage")!=null){
                                custommessage = request.getParameter("custommessage");
                            }
                            element = CoreMethodsReturningXML.inviteByEmail(user, emailtoinvite, custommessage);
                        } else if (method.equalsIgnoreCase("getLoggedInUser")){
                            element = CoreMethodsReturningXML.getLoggedInUser(user);
                        } else if (method.equalsIgnoreCase("bigRefresh")){
                            element = CoreMethodsReturningXML.bigRefresh(user);
                        } else if (method.equalsIgnoreCase("getNotifications")){
                            element = CoreMethodsReturningXML.getNotifications(user);
                        } else if (method.equalsIgnoreCase("getRooms")){
                            element = CoreMethodsReturningXML.getRooms(user);
                        } else if (method.equalsIgnoreCase("getExercise")){
                            int exerciseid = 0;
                            if (Num.isinteger(request.getParameter("exerciseid"))){
                                exerciseid = Integer.parseInt(request.getParameter("exerciseid"));
                            }
                            element = CoreMethodsReturningXML.getExercise(exerciseid);
                        } else if (method.equalsIgnoreCase("getExerciselist")){
                            int exerciselistid = 0;
                            if (Num.isinteger(request.getParameter("exerciselistid"))){
                                exerciselistid = Integer.parseInt(request.getParameter("exerciselistid"));
                            }
                            element = CoreMethodsReturningXML.getExerciselist(exerciselistid);
                        } else if (method.equalsIgnoreCase("getRecentExercises")){
                            int useridToShowRecentExercisesFor = 0;
                            if (Num.isinteger(request.getParameter("useridToShowRecentExercisesFor"))){
                                useridToShowRecentExercisesFor = Integer.parseInt(request.getParameter("useridToShowRecentExercisesFor"));
                            }
                            int numberToGet = 0;
                            if (Num.isinteger(request.getParameter("numberToGet"))){
                                numberToGet = Integer.parseInt(request.getParameter("numberToGet"));
                            }
                            element = CoreMethodsReturningXML.getRecentExercises(user, useridToShowRecentExercisesFor, numberToGet);
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
                        } else if (method.equalsIgnoreCase("setNickname")){
                            String nickname = request.getParameter("nickname");
                            element = CoreMethodsReturningXML.setNickname(user, nickname);
                        } else if (method.equalsIgnoreCase("setExerciseList")){
                            int exerciselistid = 0;
                            if (Num.isinteger(request.getParameter("exerciselistid"))){
                                exerciselistid = Integer.parseInt(request.getParameter("exerciselistid"));
                            }
                            element = CoreMethodsReturningXML.setExerciselist(user, exerciselistid);
                        } else if (method.equalsIgnoreCase("getFriends")){
                            element = CoreMethodsReturningXML.getFriends(user);
                        } else if (method.equalsIgnoreCase("getFriendRequests")){
                            element = CoreMethodsReturningXML.getFriendRequests(user);
                        } else if (method.equalsIgnoreCase("addFriend")){
                            int useridoffriend = 0;
                            if (Num.isinteger(request.getParameter("useridoffriend"))){
                                useridoffriend = Integer.parseInt(request.getParameter("useridoffriend"));
                            }
                            element = CoreMethodsReturningXML.addFriend(user, useridoffriend);
                        } else if (method.equalsIgnoreCase("addFriendsByFacebookuid")){
                            element = CoreMethodsReturningXML.addFriendsByFacebookuid(user, request.getParameter("facebookuids"));
                        } else if (method.equalsIgnoreCase("breakFriendship")){
                            int useridoffriend = 0;
                            if (Num.isinteger(request.getParameter("useridoffriend"))){
                                useridoffriend = Integer.parseInt(request.getParameter("useridoffriend"));
                            }
                            element = CoreMethodsReturningXML.breakFriendship(user, useridoffriend);
                        } else if (method.equalsIgnoreCase("createRoom")){
                            String name = request.getParameter("name");
                            String description = request.getParameter("description");
                            int exerciselistid = 0;
                            if (Num.isinteger(request.getParameter("exerciselistid"))){
                                exerciselistid = Integer.parseInt(request.getParameter("exerciselistid"));
                            }
                            boolean isprivate = false;
                            if (request.getParameter("isprivate")!=null && (request.getParameter("isprivate").equals("true") || request.getParameter("isprivate").equals("1"))){
                                isprivate = true;
                            }
                            boolean isfriendautopermit = false;
                            if (request.getParameter("isfriendautopermit")!=null && (request.getParameter("isfriendautopermit").equals("true") || request.getParameter("isfriendautopermit").equals("1"))){
                                isfriendautopermit = true;
                            }
                            element = CoreMethodsReturningXML.createRoom(user, name, description, exerciselistid, isprivate, isfriendautopermit);
                        } else if (method.equalsIgnoreCase("editRoom")){
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            String name = request.getParameter("name");
                            String description = request.getParameter("description");
                            int exerciselistid = 0;
                            if (Num.isinteger(request.getParameter("exerciselistid"))){
                                exerciselistid = Integer.parseInt(request.getParameter("exerciselistid"));
                            }
                            boolean isprivate = false;
                            if (request.getParameter("isprivate")!=null && (request.getParameter("isprivate").equals("true") || request.getParameter("isprivate").equals("1"))){
                                isprivate = true;
                            }
                            boolean isfriendautopermit = false;
                            if (request.getParameter("isfriendautopermit")!=null && (request.getParameter("isfriendautopermit").equals("true") || request.getParameter("isfriendautopermit").equals("1"))){
                                isfriendautopermit = true;
                            }
                            element = CoreMethodsReturningXML.editRoom(user, roomid, name, description, exerciselistid, isprivate, isfriendautopermit);
                        } else if (method.equalsIgnoreCase("deleteRoom")){
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.deleteRoom(user, roomid);
                        } else if (method.equalsIgnoreCase("getRoomsIModerate")){
                            element = CoreMethodsReturningXML.getRoomsIModerate(user);
                        } else if (method.equalsIgnoreCase("getMyRooms")){
                            element = CoreMethodsReturningXML.getMyRooms(user);
                        } else if (method.equalsIgnoreCase("joinRoom")){
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.joinRoom(user, roomid);
                        } else if (method.equalsIgnoreCase("removeFromMyRooms")){
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.removeFromMyRooms(user, roomid);
                        } else if (method.equalsIgnoreCase("getRoomsMyFriendsAreIn")){
                            element = CoreMethodsReturningXML.getRoomsMyFriendsAreIn(user);
                        } else if (method.equalsIgnoreCase("getRoomsMyFriendsModerate")){
                            element = CoreMethodsReturningXML.getRoomsMyFriendsModerate(user);
                        } else if (method.equalsIgnoreCase("areFriends")){
                            int userid1 = 0;
                            if (Num.isinteger(request.getParameter("userid1"))){
                                userid1 = Integer.parseInt(request.getParameter("userid1"));
                            }
                            int userid2 = 0;
                            if (Num.isinteger(request.getParameter("userid2"))){
                                userid2 = Integer.parseInt(request.getParameter("userid2"));
                            }
                            element = CoreMethodsReturningXML.areFriends(userid1, userid2);
                        } else if (method.equalsIgnoreCase("isAllowedInRoom")){
                            int userid = 0;
                            if (Num.isinteger(request.getParameter("userid"))){
                                userid = Integer.parseInt(request.getParameter("userid"));
                            }
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.isAllowedInRoom(userid, roomid);
                        } else if (method.equalsIgnoreCase("isModeratorOfRoom")){
                            int userid = 0;
                            if (Num.isinteger(request.getParameter("userid"))){
                                userid = Integer.parseInt(request.getParameter("userid"));
                            }
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.isModeratorOfRoom(userid, roomid);
                        } else if (method.equalsIgnoreCase("grantRoomPermission")){
                            int useridtogivepermissionto = 0;
                            if (Num.isinteger(request.getParameter("useridtogivepermissionto"))){
                                useridtogivepermissionto = Integer.parseInt(request.getParameter("useridtogivepermissionto"));
                            }
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.grantRoomPermission(user, useridtogivepermissionto, roomid);
                        } else if (method.equalsIgnoreCase("grantRoomMod")){
                            int useridtogivepermissionto = 0;
                            if (Num.isinteger(request.getParameter("useridtogivepermissionto"))){
                                useridtogivepermissionto = Integer.parseInt(request.getParameter("useridtogivepermissionto"));
                            }
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.grantRoomMod(user, useridtogivepermissionto, roomid);
                        } else if (method.equalsIgnoreCase("revokeRoomPermission")){
                            int useridtorevokefrom = 0;
                            if (Num.isinteger(request.getParameter("useridtorevokefrom"))){
                                useridtorevokefrom = Integer.parseInt(request.getParameter("useridtorevokefrom"));
                            }
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.revokeRoomPermission(user, useridtorevokefrom, roomid);
                        } else if (method.equalsIgnoreCase("revokeRoomMod")){
                            int useridtorevokefrom = 0;
                            if (Num.isinteger(request.getParameter("useridtorevokefrom"))){
                                useridtorevokefrom = Integer.parseInt(request.getParameter("useridtorevokefrom"));
                            }
                            int roomid = 0;
                            if (Num.isinteger(request.getParameter("roomid"))){
                                roomid = Integer.parseInt(request.getParameter("roomid"));
                            }
                            element = CoreMethodsReturningXML.revokeRoomMod(user, useridtorevokefrom, roomid);
                        } else if (method.equalsIgnoreCase("getRoomPermissionRequests")){
                            element = CoreMethodsReturningXML.getRoomPermissionRequests(user);
                        }
                        //Add to doc and cache
                        if (element==null){
                            outDoc.addContent(XMLConverters.resultXml(false, "There was some sort of error building the response.", ""));
                        } else {
                            //Add to the outDoc
                            Element root = new Element("result");
                            root.setAttribute("success", "true");
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

    private static boolean isFacebookSessionKeyValid(String facebookapikey, String facebook_session_key, String facebook_session_secret, String facebookuid){
        Logger logger = Logger.getLogger(RESTXmlApi.class);
        try{
            //@todo need to cache this request so that I don't have to go back to facebook every request from the user
            logger.debug("isFacebookSessionKeyValid("+facebook_session_key+", "+facebookuid+") called");

            //if (apikey==null || apikey.equals("")){apikey = SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY);}
            logger.debug("facebookapikey="+facebookapikey);
            logger.debug("facebook_session_key="+facebook_session_key);
            logger.debug("facebook_session_secret="+facebook_session_secret);
            logger.debug("facebookuid="+facebookuid);

            FacebookXmlRestClient facebookRestClient = new FacebookXmlRestClient(facebookapikey, facebook_session_secret, facebook_session_key);
            long loggedInUserUid = facebookRestClient.users_getLoggedInUser();
            logger.debug("facebookuid="+facebookuid+" loggedInUserUid="+loggedInUserUid);
            if (facebookuid.trim().equals(String.valueOf(loggedInUserUid))){
                logger.debug("return true... this is a valid facebook session");
                return true;
            }
        } catch (Exception ex){
            logger.debug("", ex);
        }
        logger.debug("return false... not valid facebook session");
        return false;
    }

}
