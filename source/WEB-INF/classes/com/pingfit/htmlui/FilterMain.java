package com.pingfit.htmlui;

import com.pingfit.cache.providers.CacheFactory;
import com.pingfit.dao.Pl;
import com.pingfit.dao.User;
import com.pingfit.eula.EulaHelper;
import com.pingfit.facebook.FacebookAuthorizationJsp;
import com.pingfit.privatelabel.PlFinder;
import com.pingfit.session.PersistentLogin;
import com.pingfit.session.UrlSplitter;
import com.pingfit.systemprops.BaseUrl;
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.util.Time;
import com.pingfit.xmpp.SendXMPPMessage;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;


/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 4:24:22 PM
 */
public class FilterMain implements Filter {

    private FilterConfig filterConfig = null;
    Logger logger = Logger.getLogger(this.getClass().getName());
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        //logger.debug("++++++ httpServletRequest.getRequestURL()="+httpServletRequest.getRequestURL());
        //Set up Pagez
        Pagez.setRequest(httpServletRequest);
        Pagez.setResponse(httpServletResponse);
        Pagez.setBeanMgr(new BeanMgr());
        try{
            if (httpServletRequest.getRequestURL().indexOf("api.xml")==-1 && httpServletRequest.getRequestURL().indexOf("PingFit.air")==-1 && httpServletRequest.getRequestURL().indexOf("PingFitAirAppVersion.xml")==-1 && httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1 && httpServletRequest.getRequestURL().indexOf("/js/")==-1){
                logger.debug("Start FilterMain");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("------");
//                logger.debug("-------------");
//                logger.debug("---------------------------START REQUEST: "+httpServletRequest.getRequestURL());
//                logger.debug("httpServletRequest.getSession().getId()="+httpServletRequest.getSession().getId());

                //Find the PL
                Pl pl = PlFinder.find(httpServletRequest);

                //Find the userSession
                Object obj = CacheFactory.getCacheProvider().get(httpServletRequest.getSession().getId(), "userSession");
                if (obj!=null && (obj instanceof UserSession)){
                    logger.debug("found a userSession in the cache");
                    Pagez.setUserSession((UserSession)obj);
                } else {
                    logger.debug("no userSession in cache");
                    UserSession userSession = new UserSession();
                    userSession.setPl(pl);                    
                    Pagez.setUserSessionAndUpdateCache(userSession);
                    //Make sure the pl is set properly before AssignAdNetwork
                    Pagez.getUserSession().setPl(pl); 
                }

                //Set Private Label (Pl)
                Pagez.getUserSession().setPl(pl);

                //Production redirect to www.pingfit.com for https
                //@todo make this configurable... i.e. no hard-coded urls
                UrlSplitter urlSplitter = new UrlSplitter(httpServletRequest);
                if (urlSplitter.getRawIncomingServername().equals("pingfit.com")){
                    if (urlSplitter.getMethod().equals("GET")){
                        httpServletResponse.sendRedirect(urlSplitter.getScheme()+"://"+"www.pingfit.com"+urlSplitter.getServletPath()+urlSplitter.getParametersAsQueryStringQuestionMarkIfRequired());
                        return;
                    } else {
                        httpServletResponse.sendRedirect(urlSplitter.getScheme()+"://"+"www.pingfit.com/");
                        return;
                    }
                }

                //Redirect login page to https
                if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1") && urlSplitter.getScheme().equals("http") && urlSplitter.getServletPath().equals("login.jsp")){
                    try{
                        httpServletResponse.sendRedirect(BaseUrl.get(true)+"login.jsp");
                        return;
                    } catch (Exception ex){
                        logger.error("",ex);
                        return;
                    }
                }

                //Set Private Label (Pl)
                Pagez.getUserSession().setPl(pl);

                //Facebook start
                FacebookAuthorizationJsp.doAuth();
                logger.debug("before persistent login and isfacebookui="+Pagez.getUserSession().getIsfacebookui());
                //Facebook end

                //Decide whether to wrap/override the HttpServletRequest
                if (Pagez.getUserSession().getIsfacebookui()){
                    httpServletRequest = wrapRequest(httpServletRequest);
                    Pagez.setRequest(httpServletRequest);
                }

                //Persistent login start
                boolean wasAutoLoggedIn = false;
                if (!Pagez.getUserSession().getIsfacebookui()){
                    if (!Pagez.getUserSession().getIsloggedin()){
                        //See if the incoming request has a persistent login cookie
                        Cookie[] cookies = httpServletRequest.getCookies();
                        logger.debug("looking for cookies");
                        if (cookies!=null && cookies.length>0){
                            logger.debug("cookies found.");
                            for (int i = 0; i < cookies.length; i++) {
                                logger.debug("cookies[i].getName()="+cookies[i].getName());
                                logger.debug("cookies[i].getValue()="+cookies[i].getValue());
                                if (cookies[i].getName().equals(PersistentLogin.cookieName)){
                                    logger.debug("persistent cookie found.");
                                    int useridFromCookie = PersistentLogin.checkPersistentLogin(cookies[i]);
                                    if (useridFromCookie>-1){
                                        logger.debug("setting userid="+useridFromCookie);
                                        User user = User.get(useridFromCookie);
                                        if (user!=null && user.getUserid()>0 && user.getIsenabled()){
                                            UserSession newUserSession = new UserSession();
                                            newUserSession.setUser(user);
                                            newUserSession.setIsloggedin(true);
                                            newUserSession.setIsLoggedInToBeta(true);
                                            newUserSession.setPl(pl);
                                            //Check the eula
                                            if (!EulaHelper.isUserUsingMostRecentEula(user)){
                                                newUserSession.setIseulaok(false);
                                            } else {
                                                newUserSession.setIseulaok(true);
                                            }
                                            Pagez.setUserSessionAndUpdateCache(newUserSession);
                                            wasAutoLoggedIn = true;
                                            //Notify via XMPP
                                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Auto-Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                                            xmpp.send();
                                            break;
                                            //Now dispatch request to the same page so that header is changed to reflect logged-in status
//                                            if (wasAutoLoggedIn){
//                                                try{
//                                                    if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
//                                                        try{
//                                                            httpServletResponse.sendRedirect(BaseUrl.get(true)+"account/index.jsp?msg=autologin");
//                                                            return;
//                                                        } catch (Exception ex){
//                                                            logger.error("",ex);
//                                                            httpServletResponse.sendRedirect("/account/index.jsp?msg=autologin");
//                                                            return;
//                                                        }
//                                                    } else {
//                                                        httpServletResponse.sendRedirect("/account/index.jsp?msg=autologin");
//                                                        return;
//                                                    }
//                                                } catch (Exception ex){
//                                                    logger.error("",ex);
//                                                    ex.printStackTrace();
//                                                    httpServletResponse.sendRedirect("/index.jsp");
//                                                    return;
//                                                }
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //Persistent login end
                logger.debug("after persistent login and wasAutoLoggedIn="+wasAutoLoggedIn);
                logger.debug("after persistent login and Pagez.getUserSession().getIsloggedin()="+Pagez.getUserSession().getIsloggedin());
                logger.debug("after persistent login and isfacebookui="+Pagez.getUserSession().getIsfacebookui());


                //Account activation
                if (Pagez.getUserSession().getUser()!=null && !Pagez.getUserSession().getUser().getIsactivatedbyemail()){
                    //User isn't activated but they get a grace period
                    int daysInGracePeriod = 3;
                    Calendar startOfGracePeriod = Time.xDaysAgoStart(Calendar.getInstance(), daysInGracePeriod);
                    if (Pagez.getUserSession().getUser().getCreatedate().before(startOfGracePeriod.getTime())){
                        if (urlSplitter.getRequestUrl().indexOf("emailactivation")==-1 && urlSplitter.getRequestUrl().indexOf("lpc.jsp")==-1 && urlSplitter.getRequestUrl().indexOf("login")==-1 && urlSplitter.getRequestUrl().indexOf("jcaptcha")==-1 && urlSplitter.getRequestUrl().indexOf("eas")==-1){
                            //logger.debug("redirecting userid="+Pagez.getUserSession().getUser().getUserid()+" "+Pagez.getUserSession().getUser().getFirstname()+" "+Pagez.getUserSession().getUser().getLastname()+" to awaitingactivation page");
                            //httpServletResponse.sendRedirect("/emailactivationwaiting.jsp");
                            //return;
                        }
                    }
                }

                //Now check the eula
                if (Pagez.getUserSession().getIsloggedin() && !Pagez.getUserSession().getIseulaok()){
                    System.out.println("redirecting to force eula accept");
                    if (urlSplitter.getRequestUrl().indexOf("loginagreeneweula.jsp")==-1){
                        httpServletResponse.sendRedirect("/loginagreeneweula.jsp");
                        return;
                    }
                }

                //Start with the sessionid
                String exerciserKey = "sessionid:"+httpServletRequest.getSession().getId();

                //Look for long-term non-login cookie
                String cookieName = "pingFitExerciserCookieID";
                boolean havecookie = false;
                Cookie[] cookies = Pagez.getRequest().getCookies();
                if (cookies!=null){
                    for (int i = 0; i < cookies.length; i++) {
                        Cookie cooky = cookies[i];
                        if (cooky.getName().equals(cookieName)){
                            String value = cooky.getValue();
                            logger.debug("found a "+cookieName+" with value="+value);
                            //Set the exerciserKey to the value of the cookie
                            exerciserKey = "cookieid:"+value;
                            havecookie = true;
                        }
                    }
                }

                //Build long-term non-login cookie if it's not already there
                if (!havecookie){
                    Cookie cooky = new Cookie(cookieName, httpServletRequest.getSession().getId());
                    cooky.setMaxAge(Integer.MAX_VALUE);
                    Pagez.getResponse().addCookie(cooky);
                    //Set the exerciserKey to the value of the cookie
                    exerciserKey = "cookieid:"+cooky.getValue();
                }


                //If there's a user, use that as the exerciserKey
                if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getUserid()>0){
                    exerciserKey = "userid:"+Pagez.getUserSession().getUser().getUserid();
                }



            } else {
                //It's an image, js, etc
                Pagez.setUserSession(new UserSession());
            }
        }catch(Exception ex){logger.error("", ex);}

        //Call the rest of the filters
        chain.doFilter(request, response);

        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1 && httpServletRequest.getRequestURL().indexOf("/js/")==-1){
//                logger.debug("---------------------------END REQUEST: "+httpServletRequest.getRequestURL());
//                logger.debug("-------------");
//                logger.debug("------");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
                logger.debug("End FilterMain");
            }
        }catch(Exception ex){logger.error("", ex);}
    }



    private HttpServletRequest wrapRequest(HttpServletRequest request){

        //Treat dpage vars as real request vars... oy
        //Start inner class
        //Basically we override the getParameter() method of HttpServletRequest and parse the dpage param looking for encoded params
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
           public String getParameter(java.lang.String name) {
              Logger logger = Logger.getLogger(this.getClass().getName());
              //logger.debug("getParameter("+name+") called");
              try{
                  if (super.getParameter("dpage")==null){
                        //There's no dpage, just return as normal
                        return super.getParameter(name);
                  } else {
                      //There's a dpage
                      String dpage = super.getParameter("dpage");
                      String[] dpagesplit = dpage.split("\\?");
                      if (dpagesplit.length>1){
                            //We have a dpage with params
                            String querystring = dpagesplit[1];
                            String[] nvpair = querystring.split("&");
                            if (nvpair.length>0){
                                //Process each pair
                                for (int i=0; i<nvpair.length; i++) {
                                    String querynvpair=nvpair[i];
                                    String[] nvpairsplit = querynvpair.split("=");
                                    if (nvpairsplit.length>1){
                                        String nvname = nvpairsplit[0];
                                        String nvvalue = nvpairsplit[1];
                                        //Now see if it matches the requested name
                                        if (nvname.equals(name)){
                                            return nvvalue;
                                        }
                                    }
                                }
                            }
                      }
                  }
              } catch (Exception ex){
                logger.error("", ex);
              }
              return super.getParameter(name);
           }
        };
        //End inner class
        return (HttpServletRequest)wrappedRequest;

    }



}
