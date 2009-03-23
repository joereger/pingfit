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
import com.pingfit.systemprops.SystemProperty;
import com.pingfit.systemprops.BaseUrl;
import com.pingfit.systemprops.WebAppRootDir;
import com.pingfit.facebook.FacebookAuthorizationJsp;
import com.pingfit.eula.EulaHelper;
import com.pingfit.xmpp.SendXMPPMessage;
import com.pingfit.htmlui.Pagez;
import com.pingfit.htmlui.BeanMgr;
import com.pingfit.htmlui.UserSession;


public class PingFitAirAppVersionXml extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        String airVersion = Io.textFileRead(WebAppRootDir.getWebAppRootPath()  + "PingFitAirVersion.txt").toString();
        String xmlFile = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "PingFitAirAppVersion.xml").toString();
        String output = xmlFile;
        output = output.replaceAll("---VERSION---", airVersion);
        PrintWriter out = response.getWriter();
        out.print(output);
    }


}