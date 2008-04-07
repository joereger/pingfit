<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.PublicIndex" %>
<%@ page import="com.pingfit.util.Time" %>
<%@ page import="com.pingfit.api.AjaxExercisePage" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="com.pingfit.api.Exerciser" %>
<%@ page import="com.pingfit.htmlui.ValidationException" %>
<%@ page import="com.pingfit.api.CoreMethods" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.api.CompletedExercise" %>
<%@ page import="com.pingfit.dao.Exerciselist" %>
<%@ page import="java.util.*" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    PublicIndex publicIndex = (PublicIndex) Pagez.getBeanMgr().get("PublicIndex");
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
                CoreMethods.doExercise(Pagez.getUserSession().getExerciser(), inexid, inreps);
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



<table cellpadding="5" cellspacing="0" border="0" width="100%">
        <tr>
            <%--<td valign="top">--%>
                <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #cccccc;">--%>
                    <%--<div class="rounded" style="padding: 8px; margin: 15px; background: #cccccc;">--%>
                        <%--<center><font class="formfieldnamefont" style="color: #e6e6e6;">Upcoming Exercises</font></center>--%>
                    <%--</div>--%>
                    <%--<%=AjaxExercisePage.getUpcomingExercisesHtml(Pagez.getUserSession().getExerciser())%>--%>
                <%--</div>--%>
            <%--</td>--%>
            <td valign="top">
                <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                    <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                        <div id="mainexercisearea">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td valign="top">
                                        <!--<center>-->
                                            <font class="mediumfont" style="color: #666666; font-size: 30px; font-weight: bold;"><%=exercise.getTitle()%></font>
                                            <br/>
                                            <%--<font class="largefont" style="font-size: 35px; color: #996699;">--%>
                                                <%--<span id="countdown1"><%=AjaxExercisePage.getNextExerciseTimeFormattedForCountdownJavascript(Pagez.getUserSession().getExerciser())%></span>--%>
                                            <%--</font>--%>
                                            <%
                                                Calendar now = Calendar.getInstance();
                                                Calendar nextEx = Pagez.getUserSession().getExerciser().getNextexercisetime();
                                                double secondsuntilnextexercise = (nextEx.getTimeInMillis() - now.getTimeInMillis())/1000;
                                            %>
                                            <div id="countdowncontainer" class="largefont" style="font-size: 50px; color: #996699;"></div>
                                            <script type="text/javascript">
                                            var futuredate=new cdtime("countdowncontainer", <%=String.valueOf(secondsuntilnextexercise)%>)
                                            futuredate.displaycountdown("minutes", formatresults)
                                            </script>
                                        <!--</center>-->
                                    </td>
                                    <td valign="top" width="175">
                                        <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                                            <font class="tinyfont">
                                            <form action="/index.jsp" method="post">
                                                <input type="hidden" name="action" value="completeexercise">
                                                <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">
                                                <select name="reps" style="font-size: 12px; height: 25px;">
                                                <%
                                                    for (int i = 1; i <= 100; i++) {
                                                        String sel = "";
                                                        if (i==exercise.getReps()){
                                                            sel = " selected";
                                                        }
                                                        %><option value="<%=i%>" <%=sel%>><%=i%> reps</option><%
                                                    }
                                                %>
                                                </select>
                                                <input type="submit" class="formsubmitbutton" value="I Did It" style="font-size: 10px;">
                                            </form>
                                            </font>
                                        </div>
                                        <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                                            <form action="/index.jsp" method="post">
                                                <input type="hidden" name="action" value="skipexercise">
                                                <center>
                                                <input type="submit" class="formsubmitbutton" value="Skip this Exercise" style="font-size: 10px;">
                                                </center>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">--%>
                            <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>
                            <%if (exercise.getImage()!=null && !exercise.getImage().equals("")){%>
                                <center>
                                <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                                    <img src="/images/exercises/<%=exercise.getImage()%>" border="0">
                                </div>
                                </center>
                                <br/>
                            <%}%>
                            <font class="smallfont" style="font-weight: bold;"><%=exercise.getTitle()%>: </font>
                            <font class="smallfont"><%=exercise.getDescription()%></font>
                            <br/><br/>
                            <font class="tinyfont"><b>Coming Up:</b> <%=AjaxExercisePage.getUpcomingExercisesHtml(Pagez.getUserSession().getExerciser())%></font>
                        </div>
                    </div>
                </div>
            </td>
            <td valign="top" width="200">

                <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                    <!--<center>-->
                    <form action="/index.jsp" method="post">
                        <input type="hidden" name="action" value="savesettings">
                        <%--<font class="normalfont" style="font-weight: bold;">Exercise Every:</font>--%>
                        <%--<br/>--%>
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
                        <%--<font class="normalfont" style="font-weight: bold;">Exercise Type:</font>--%>
                        <%--<br/>--%>
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
                        <br/><br/>
                        <center><a href="/exercises.jsp"><font class="tinyfont">More Exercise Lists</font></a></center>
                    </form>
                    <!--</center>-->
                </div>

                <%if (!Pagez.getUserSession().getIsloggedin()){%>
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #BFE4FF;">
                        <font class="smallfont" style="font-weight: bold; color: #666666;">
                            Be sure to <a href="/login.jsp">log in</a> or <a href="/registration.jsp">sign up</a> so that your completed exercises are saved!  Signup is free!
                        </font>
                    </div>
                <%}%>

                <%if (!Pagez.getUserSession().getIsloggedin()){%>
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
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








<%@ include file="/template/footer.jsp" %>