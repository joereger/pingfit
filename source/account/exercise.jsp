<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.AccountIndex" %>
<%@ page import="com.pingfit.htmluibeans.AccountBalance" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.api.CoreMethods" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.pingfit.api.AjaxExercisePage" %>
<%@ page import="com.pingfit.dao.Exerciselist" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.api.CompletedExercise" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.util.Time" %>
<%@ page import="com.pingfit.htmluibeans.AccountExercise" %>
<%@ page import="com.pingfit.helpers.Userinterfaces" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "exercise";
String acl = "account";
%>

<%@ include file="/template/auth.jsp" %>
<%
AccountExercise accountExercise = (AccountExercise) Pagez.getBeanMgr().get("AccountExercise");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("completeexercise")) {
        try {
            if (request.getParameter("exerciseid")!=null && Num.isinteger(request.getParameter("exerciseid"))){
                int inexid = Integer.parseInt(request.getParameter("exerciseid"));
                int inreps = 0;
                if (request.getParameter("reps")!=null && Num.isinteger(request.getParameter("reps"))){
                    inreps = Integer.parseInt(request.getParameter("reps"));
                }
                int userinterface = Userinterfaces.WEB;
                if (Pagez.getUserSession().getIstrayui()){
                    userinterface = Userinterfaces.TRAY;
                }
                CoreMethods.doExercise(Pagez.getUserSession().getExerciser(), inexid, inreps, userinterface);
            }
        }catch(Exception ex){
            logger.error("", ex);
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("skipexercise")) {
        try {
            CoreMethods.skipCurrentOrNextExercise(Pagez.getUserSession().getExerciser());
        }catch(Exception ex){
            logger.error("", ex);
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("savesettings")) {
        try {
            if (request.getParameter("exerciseeveryxminutes")!=null && Num.isinteger(request.getParameter("exerciseeveryxminutes"))){
                CoreMethods.setExerciseEveryXMinutes(Pagez.getUserSession().getExerciser(), Integer.parseInt(request.getParameter("exerciseeveryxminutes")));
            }
            if (request.getParameter("exerciselistid")!=null && Num.isinteger(request.getParameter("exerciselistid"))){
                CoreMethods.setExerciselistid(Pagez.getUserSession().getExerciser(), Integer.parseInt(request.getParameter("exerciselistid")));
            }
        }catch(Exception ex){
            logger.error("", ex);
        }
    }
%>
<%@ include file="/template/header.jsp" %>




<%
    if (Pagez.getUserSession()==null){
        logger.debug("Pagez.getUserSession()==null");
    }
    if (Pagez.getUserSession().getExerciser()==null){
        logger.debug("Pagez.getUserSession().getExerciser()==null");
    }
    Exercise exercise = Exercise.get(Pagez.getUserSession().getExerciser().getNextexerciseid());
%>




<%if (1==2 && !Pagez.getUserSession().getIstrayui() && !Pagez.getUserSession().getIsfacebookui()){%>


<%} else {%>


<script type="text/javascript" src="/js/countdownpro/countdownpro.js" defer="defer"></script>
<meta scheme="countdown1" name="d_mindigits" content="1" />
<meta scheme="countdown1" name="d_unit" content=" day" />
<meta scheme="countdown1" name="d_units" content=" days" />
<meta scheme="countdown1" name="d_after" content=" " />
<meta scheme="countdown1" name="d_hidezero" content="1" />
<meta scheme="countdown1" name="h_mindigits" content="1" />
<meta scheme="countdown1" name="h_unit" content=" hour" />
<meta scheme="countdown1" name="h_units" content=" hours" />
<meta scheme="countdown1" name="h_after" content=" " />
<meta scheme="countdown1" name="h_hidezero" content="1" />
<meta scheme="countdown1" name="m_mindigits" content="1" />
<meta scheme="countdown1" name="m_unit" content=" minute" />
<meta scheme="countdown1" name="m_units" content=" minutes" />
<meta scheme="countdown1" name="m_after" content=" " />
<meta scheme="countdown1" name="m_hidezero" content="1" />
<meta scheme="countdown1" name="s_mindigits" content="1" />
<meta scheme="countdown1" name="s_unit" content=" second" />
<meta scheme="countdown1" name="s_units" content=" seconds" />
<meta scheme="countdown1" name="s_after" content=" " />
<meta scheme="countdown1" name="event_msg" content="Now!!">

<script type="text/javascript" src="/js/dynamiccountdownscript/dynamiccountdown.js"></script>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">
                <%--<div style="padding: 5px; margin: 0px; background: #e6e6e6;">--%>
                    <div style="padding: 0px; margin: 0px; background: #ffffff;">
                        <div id="mainexercisearea">
                            <form action="/account/exercise.jsp" method="post" STYLE="margin: 0px; padding: 0px;">
                            <input type="hidden" name="action" value="completeexercise">
                            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">
                            <div style="background: url('/images/exercise-sentence-bg.gif');">
                            <%if (!Pagez.getUserSession().getIstrayui()){%>
                                <img src="/images/clear.gif" width="1" height="14" alt=""/><br/>
                            <%} else {%>
                                <img src="/images/clear.gif" width="1" height="35" alt=""/><br/>
                            <%}%>
                            <table cellpadding="0" cellspacing="0" border="0" width="750">
                                <tr>
                                    <td width="30">
                                        <%if (!Pagez.getUserSession().getIstrayui()){%>
                                            <img src="/images/clear.gif" width="1" height="80" alt=""/><br/>
                                        <%} else {%>
                                            <img src="/images/clear.gif" width="1" height="60" alt=""/><br/>
                                        <%}%>
                                    </td>
                                    <td width="120" style="vertical-align: middle; text-align: center;">
                                        <%--<div id="doex" class="largefont" style="font-size: 30px; color: #cccccc;">--%>
                                        <font class="mediumfont" style="font-size: 12px; color: #666666;  font-weight: bold;"><b>start to do</b></font><br/>
                                        <%
                                            for (int i = 1; i <= 100; i++) {
                                                if (i==exercise.getReps()){
                                                    %><font class="mediumfont" style="font-size: 30px; color: #666666;font-weight: bold;"><%=i%></font><%
                                                }
                                            }
                                        %>
                                        <br/><font class="mediumfont" style="font-size: 14px; color: #666666;  font-weight: bold;"><b>&nbsp;</b></font>
                                        <!--</div>-->
                                    </td>
                                    <td width="25">
                                    </td>
                                    <td width="210" style="vertical-align: middle; text-align: center;">
                                        <div id="extitle" class="largefont" style="font-size: 30px; color: #666666;"><font class="mediumfont" style="font-size: 30px; color: #666666;  font-weight: bold;"><%=exercise.getTitle()%></font></div>
                                    </td>
                                    <td width="35">
                                    </td>
                                    <td width="135" style="vertical-align: middle; text-align: center;">
                                        <center>
                                        <%
                                            Calendar now = Calendar.getInstance();
                                            Calendar nextEx = Pagez.getUserSession().getExerciser().getNextexercisetime();
                                            double secondsuntilnextexercise = (nextEx.getTimeInMillis() - now.getTimeInMillis())/1000;
                                        %>
                                        <div id="countdowncontainer" class="largefont" style="font-size: 18px; color: #996699;"></div>
                                        <script type="text/javascript">
                                        var futuredate=new cdtime("countdowncontainer", <%=String.valueOf(secondsuntilnextexercise)%>)
                                        futuredate.displaycountdown("minutes", formatresults)
                                        </script>
                                        </center>
                                    </td>
                                    <td width="35">
                                    </td>
                                    <td width="60" style="vertical-align: middle;">
                                        <img src="/images/clear.gif" width="1" height="10" alt=""/><br/>
                                        <input type="submit" class="formsubmitbutton" value="I Did" style="font-size: 10px;">
                                        <br/><a href="/account/exercise.jsp?action=skipexercise"><font class="tinyfont" style="color: #666666;">skip it</font></a>
                                    </td>
                                    <td width="*" style="vertical-align: middle;">
                                        <img src="/images/clear.gif" width="1" height="10" alt=""/><br/>
                                        <select name="reps" style="font-size: 12px;">
                                        <%
                                            for (int i = 1; i <= 100; i++) {
                                                String sel = "";
                                                if (i==exercise.getReps()){
                                                    sel = " selected";
                                                }
                                                %><option value="<%=i%>" <%=sel%>><%=i%></option><%
                                            }
                                        %>
                                        </select>
                                        <br/><font class="tinyfont" style="color: #666666;">&nbsp;</font>
                                    </td>
                                </tr>
                            </table>
                            <img src="/images/clear.gif" width="1" height="10" alt=""/><br/>
                            </div>
                            </form>

                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td valign="top">
                                        <%if (exercise.getImage()!=null && !exercise.getImage().equals("")){%>
                                            <div style="padding: 10px; margin: 5px; background: #e6e6e6;">
                                                <%
                                                String widthStr = "";
                                                if (Pagez.getUserSession().getIstrayui()){
                                                    widthStr = " width=\"200\" ";
                                                }
                                                %>
                                                <img src="/images/exercises/<%=exercise.getImage()%>" <%=widthStr%> border="0">
                                                <%if (exercise.getImagecredit()!=null && !exercise.getImagecredit().equals("")){%>
                                                    <br/><font class="tinyfont" style="color: #666666;"><%=exercise.getImagecredit()%></font>
                                                <%}%>
                                            </div>
                                        <%}%>
                                    </td>
                                    <td valign="top">
                                        <img src="/images/clear.gif" width="1" height="8" alt=""/><br/>
                                        <font class="smallfont"><b>Instructions</b></font>
                                        <br/>
                                        <font class="smallfont"><%=exercise.getDescription()%></font>
                                        <br/><br/>
                                        <font class="smallfont"><b>Coming Up Soon</b><br/><%=AjaxExercisePage.getUpcomingExercisesHtml(Pagez.getUserSession().getExerciser())%></font>
                                    </td>
                                </tr>
                            </table>

                        </div>
                    </div>
                <!--</div>-->
            </td>
        </tr>
        <tr>
            <td valign="top">

                <div style="padding: 10px; margin: 5px; background: #e6e6e6;">
                    <form action="/account/exercise.jsp" method="post">
                        <input type="hidden" name="action" value="savesettings">
                        <select name="exerciseeveryxminutes" style="font-size: 10px;">
                        <%
                            for (int i = 5; i <= 90; i=i+5) {
                                String sel = "";
                                if (i==Pagez.getUserSession().getExerciser().getExerciseeveryxminutes()){
                                    sel = " selected";
                                }
                                %><option value="<%=i%>" <%=sel%>>Exercise Every <%=i%> Min</option><%
                            }
                        %>
                        </select>
                        <br/>
                        <select name="exerciselistid" style="font-size: 10px;">
                        <%
                            if (Pagez.getUserSession().getUser()!=null){
                                List<Exerciselist> userExerciseLists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                        .addOrder(Order.asc("exerciselistid"))
                                        .add(Restrictions.eq("issystem", false))
                                        .add(Restrictions.eq("useridofcreator", Pagez.getUserSession().getUser().getUserid()))
                                        .setCacheable(true)
                                        .list();
                                if (userExerciseLists != null) {
                                    for (Iterator<Exerciselist> iterator = userExerciseLists.iterator(); iterator.hasNext();) {
                                        Exerciselist exerciselist = iterator.next();
                                        String sel = "";
                                        if (exerciselist.getExerciselistid() == Pagez.getUserSession().getExerciser().getExerciselistid()) {
                                            sel = " selected";
                                        }
                                        %><option value="<%=exerciselist.getExerciselistid()%>" <%=sel%>>*<%=exerciselist.getTitle()%></option><%
                                    }
                                }
                            }
                        %>
                        <%
                            List<Exerciselist> exerciseLists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                    .addOrder(Order.asc("exerciselistid"))
                                    .add(Restrictions.eq("issystem", true))
                                    .setCacheable(true)
                                    .list();
                            if (exerciseLists != null) {
                                for (Iterator<Exerciselist> iterator = exerciseLists.iterator(); iterator.hasNext();) {
                                    Exerciselist exerciselist = iterator.next();
                                    String sel = "";
                                    if (exerciselist.getExerciselistid() == Pagez.getUserSession().getExerciser().getExerciselistid()) {
                                        sel = " selected";
                                    }
                                    %><option value="<%=exerciselist.getExerciselistid()%>" <%=sel%>><%=exerciselist.getTitle()%></option><%
                                }
                            }
                        %>
                        </select>
                        <br/>
                        <input type="submit" value="Save Settings" class="formsubmitbutton" style="font-size: 9px;">
                        <%if (Pagez.getUserSession().getIsloggedin()){%>
                            <br/><br/>
                            <a href="/account/exerciselistlist.jsp"><font class="tinyfont">More Exercise Lists</font></a>
                        <%}%>
                    </form>
                </div>


                <%if (!Pagez.getUserSession().getIsloggedin()){%>
                    <div style="padding: 15px; margin: 5px; background: #e6e6e6;">
                        <font class="smallfont" style="font-weight: bold; color: #666666;">Completed Exercises</font>
                        <br/><br/>
                        <%
                        ArrayList<CompletedExercise> completedExercises = Pagez.getUserSession().getExerciser().getCompletedexercises();
                        if (completedExercises != null && completedExercises.size() > 0) {
                            int size = completedExercises.size();
                            for (int i = (size-1); i >=0; i=i-1) {
                                CompletedExercise completedExercise = completedExercises.get(i);
                                Exercise cex = Exercise.get(completedExercise.getExerciseid());
                                %>
                                    <font class="tinyfont" style="font-weight: bold; color: #666666;"><%=Time.dateformatcompactwithtime(completedExercise.getDate())%></font>
                                    <br/>
                                    <font class="smallfont" style="font-weight: bold; color: #666666;"><%=cex.getTitle()%> x <%=completedExercise.getReps()%> reps</font>
                                    <br/><br/>
                                <%
                            }
                        } else {
                        %>
                            <font class="smallfont" style="color: #666666;">You haven't completed any exercises yet.  Get started by doing the exercise in the middle of the screen and clicking the I Did It button.</font>
                        <%}%>
                    </div>
                <%}%>
            </td>
        </tr>
    </table>



<%}%>

<%if (!Pagez.getUserSession().getIstrayui() && !Pagez.getUserSession().getIsfacebookui()){%>
    <table cellpadding="5" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">
                <div style="padding: 5px; margin: 5px; background: #e6e6e6;">
                    <div style="padding: 10px; margin: 5px; background: #ffffff;">
                        <b><a href="http://www.frogfire.com/pingfit/PingFit_Installer.exe">Download</a> and install the PingFit application.</b>  Once you start the application, right-click the icon in the system tray and choose Profile.  Enter your email and PingFit password.  PingFit will alert you when it's time to workout.
                    </div>
                </div>
            </td>
        </tr>
    </table>
<%}%>



<%@ include file="/template/footer.jsp" %>


