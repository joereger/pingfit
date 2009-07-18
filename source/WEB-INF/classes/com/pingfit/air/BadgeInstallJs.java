package com.pingfit.air;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import com.pingfit.cache.providers.CacheFactory;
import com.pingfit.session.UrlSplitter;
import com.pingfit.session.PersistentLogin;
import com.pingfit.dao.User;
import com.pingfit.util.Time;
import com.pingfit.util.Io;
import com.pingfit.util.Num;
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.systemprops.BaseUrl;
import com.pingfit.systemprops.WebAppRootDir;
import com.pingfit.facebook.FacebookAuthorizationJsp;
import com.pingfit.eula.EulaHelper;
import com.pingfit.xmpp.SendXMPPMessage;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.BeanMgr;
import com.pingfit.htmlui.UserSession;


public class BadgeInstallJs extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        String airVersion = Io.textFileRead(WebAppRootDir.getWebAppRootPath()  + "PingFitAirVersion.txt").toString();
        String airUrl = "http://"+ SystemProperty.getProp(SystemProperty.PROP_BASEURL) + "/PingFit.air";
        int refid = 0;
        if (request.getParameter("refid")!=null && Num.isinteger(request.getParameter("refid"))){
            refid = Integer.parseInt(request.getParameter("refid"));
        }
        if (refid==0){
            if (Pagez.getUserSession().getRefid()>0){
                refid = Pagez.getUserSession().getRefid();
            }
        }    

        String flashVars = "refid="+refid+":"+"plid="+"23"+":"+"some=var";
        String jsFile = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "badgefiles" + java.io.File.separator  + "badgeInstall.js").toString();
        String output = jsFile;
        output = output.replaceAll("---VERSION---", airVersion);
        output = output.replaceAll("---AIRURL---", airUrl);
        output = output.replaceAll("---FLASHVARS---", flashVars);
        PrintWriter out = response.getWriter();
        out.print(output);
    }


}