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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>

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
    if (request.getParameter("action") != null && request.getParameter("action").equals("setexerciseeveryxminutes")) {
        try {
            if (request.getParameter("exerciseeveryxminutes")!=null && Num.isinteger(request.getParameter("exerciseeveryxminutes"))){
                CoreMethods.setExerciseEveryXMinutes(Pagez.getUserSession().getExerciser(), Integer.parseInt(request.getParameter("exerciseeveryxminutes")));
            }
        }catch(Exception ex){
            logger.error("", ex);
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<%
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
            <td valign="top" width="100">
                <div class="rounded" style="padding: 5px; margin: 5px; background: #cccccc;">
                    <div class="rounded" style="padding: 15px; margin: 15px; background: #cccccc;">
                        <center><font class="formfieldnamefont" style="color: #e6e6e6;">Upcoming Exercises</font></center>
                    </div>
                    <%=AjaxExercisePage.getUpcomingExercisesHtml(Pagez.getUserSession().getExerciser())%>
                </div>
            </td>
            <td valign="top" width="350">
                <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <center>
                            <font class="mediumfont" style="color: #cccccc;">Do Next Exercise</font>
                            <br/>
                            <%--<font class="largefont" style="font-size: 35px; color: #996699;">--%>
                                <%--<span id="countdown1"><%=AjaxExercisePage.getNextExerciseTimeFormattedForCountdownJavascript(Pagez.getUserSession().getExerciser())%></span>--%>
                            <%--</font>--%>
                            <div id="countdowncontainer" class="largefont" style="font-size: 60px; color: #996699;"></div>
                            <script type="text/javascript">
                            var futuredate=new cdtime("countdowncontainer", "<%=Time.dateformatmonthdayyearmiltime(Pagez.getUserSession().getExerciser().getNextexercisetime())%>")
                            futuredate.displaycountdown("minutes", formatresults)
                            </script>
                        </center>
                    </div>
                </div>
                <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <%=AjaxExercisePage.getExerciseHtml(exercise.getExerciseid())%>
                    </div>
                </div>
            </td>
            <td valign="top" width="200">
                <div class="rounded" style="padding: 5px; margin: 5px; background: #cccccc;">
                    <div class="rounded" style="padding: 15px; margin: 15px; background: #cccccc;">
                        <center><font class="formfieldnamefont" style="color: #e6e6e6;">Actions</font></center>
                    </div>
                    <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                        <form action="/index.jsp" method="post">
                            <input type="hidden" name="action" value="completeexercise">
                            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">
                            <select name="reps" style="font-size: 10px;">
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
                            <input type="submit" class="formsubmitbutton" value="I Did It!" style="font-size: 10px;">
                        </form>
                    </div>
                    <img src="/images/clear.gif" alt="" width="1" height="5"/>
                    <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                        <form action="/index.jsp" method="post">
                            <input type="hidden" name="action" value="completeexercise">
                            <center>
                            <input type="submit" class="formsubmitbutton" value="Skip this Exercise" style="font-size: 10px;">
                            </center>
                        </form>
                    </div>
                </div>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <center>
                    <form action="/index.jsp" method="post">
                        <input type="hidden" name="action" value="setexerciseeveryxminutes">
                        <font class="normalfont" style="font-weight: bold;">Do an Exercise Every:</font>
                        <select name="exerciseeveryxminutes" style="font-size: 10px;">
                        <%
                            for (int i = 5; i <= 90; i=i+5) {
                                String sel = "";
                                if (i==Pagez.getUserSession().getExerciser().getExerciseeveryxminutes()){
                                    sel = " selected";
                                }
                                %><option value="<%=i%>" <%=sel%>><%=i%> Minutes</option><%
                            }
                        %>
                        </select>
                        <input type="submit" value="Save" style="font-size: 9px;">
                    </form>
                    </center>
                </div>
                <%if (!Pagez.getUserSession().getIsloggedin()){%>
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #BFE4FF;">
                        <font class="smallfont" style="font-weight: bold; color: #666666;">
                            Be sure to <a href="/login.jsp">log in</a> or <a href="/registration.jsp">sign up</a> so that your completed exercises are saved!  Signup is free!
                        </font>
                    </div>
                <%}%>
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
                        <font class="smallfont" style="font-weight: bold; color: #666666;">You haven't completed any exercises yet.  Get started by doing the exercise in the middle of the screen and clicking the I Did It button.</font>
                    <%
                    }
                %>
                </div>
            </td>
        </tr>
    </table>








<%@ include file="/template/footer.jsp" %>