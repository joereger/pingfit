<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.dao.Exerciselist" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dao.Exerciselistitem" %>
<%@ page import="com.pingfit.api.CoreMethods" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercises";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("setexerciselistid")) {
        try {
            if (request.getParameter("exerciselistid") != null && Num.isinteger(request.getParameter("exerciselistid"))) {
                CoreMethods.setExerciselistid(Pagez.getUserSession().getExerciser(), Integer.parseInt(request.getParameter("exerciselistid")));
            }
            Pagez.getUserSession().setMessage("Your exercise list choice has been saved.");
            Pagez.sendRedirect("/index.jsp");
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }
%>


<%@ include file="/template/header.jsp" %>

<%
    Exerciselist exerciselist = null;
    if (request.getParameter("exerciselistid") != null && Num.isinteger(request.getParameter("exerciselistid"))) {
        exerciselist = Exerciselist.get(Integer.parseInt(request.getParameter("exerciselistid")));
    }

    Exercise exercise = null;
    if (request.getParameter("exerciseid") != null && Num.isinteger(request.getParameter("exerciseid"))) {
        exercise = Exercise.get(Integer.parseInt(request.getParameter("exerciseid")));
    }
%>
    <br/><br/>
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" width="25%">
                <!-- Start exercise lists -->
                <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont" style="font-weight: bold;">Exercise Lists</font><br/>
                        <%if (Pagez.getUserSession().getUser()!=null){%>
                            <a href="/account/exerciselistdetail.jsp"><font class="tinyfont" style="font-weight: bold;">Create a New List</font></a><br/>
                            <br/>
                        <%} else {%>
                            <font class="tinyfont" style="font-weight: bold;">Sign Up or Log In to create your own Exercise Lists.</font><br/>
                            <br/>
                        <%}%>
                        <font class="tinyfont">An exercise list is a collection of exercises.  When you workout you move through one list at a time.</font><br/>
                        <br/>
                    <%
                        if (Pagez.getUserSession().getUser()!=null){
                            List<Exerciselist> userexerciselists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                    .addOrder(Order.asc("exerciselistid"))
                                    .add(Restrictions.eq("issystem", false))
                                    .add(Restrictions.eq("useridofcreator", Pagez.getUserSession().getUser().getUserid()))
                                    .setCacheable(true)
                                    .list();
                            for (Iterator<Exerciselist> iterator = userexerciselists.iterator(); iterator.hasNext();) {
                                Exerciselist el =  iterator.next();
                                if (exerciselist!=null && el.getExerciselistid()==exerciselist.getExerciselistid()){
                                    %>
                                    <a href="/exercises.jsp?exerciselistid=<%=el.getExerciselistid()%>"><font class="normalfont" style="font-weight: bold;">*<%=el.getTitle()%></font></a>
                                    <br/><br/>
                                    <%
                                } else {
                                    %>
                                    <a href="/exercises.jsp?exerciselistid=<%=el.getExerciselistid()%>"><font class="normalfont">*<%=el.getTitle()%></font></a>
                                    <br/><br/>
                                    <%
                                }
                            }
                        }
                    %>
                    <%
                        List<Exerciselist> exerciselists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                .addOrder(Order.asc("exerciselistid"))
                                .add(Restrictions.eq("issystem", true))
                                .setCacheable(true)
                                .list();
                        for (Iterator<Exerciselist> iterator = exerciselists.iterator(); iterator.hasNext();) {
                            Exerciselist el =  iterator.next();
                            if (exerciselist!=null && el.getExerciselistid()==exerciselist.getExerciselistid()){
                                %>
                                <a href="/exercises.jsp?exerciselistid=<%=el.getExerciselistid()%>"><font class="normalfont" style="font-weight: bold;"><%=el.getTitle()%></font></a>
                                <br/><br/>
                                <%
                            } else {
                                %>
                                <a href="/exercises.jsp?exerciselistid=<%=el.getExerciselistid()%>"><font class="normalfont"><%=el.getTitle()%></font></a>
                                <br/><br/>
                                <%
                            }
                        }
                    %>
                </div>
                <!-- End exercise lists -->
            </td>
            <td valign="top" width="25%">
                <!-- Start exercises -->
                <%if (exerciselist!=null){%>
                    <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                        <font class="mediumfont" style="font-weight: bold;"><%=exerciselist.getTitle()%></font><br/>
                        <a href="/exercises.jsp?exerciselistid=<%=exerciselist.getExerciselistid()%>&action=setexerciselistid"><font class="tinyfont" style="font-weight: bold;">Use this List</font></a><br/>
                        <br/>
                        <%if (Pagez.getUserSession().getUser()!=null && exerciselist.getUseridofcreator()==Pagez.getUserSession().getUser().getUserid()){%>
                            <a href="/account/exerciselistdetail.jsp?exerciselistid=<%=exerciselist.getExerciselistid()%>"><font class="tinyfont" style="font-weight: bold;">Edit this List</font></a><br/>
                            <br/>
                        <%}%>
                        <font class="tinyfont"><%=exerciselist.getDescription()%></font><br/>
                        <br/>
                        <%
                        if (exerciselist.getExerciselistitems()==null || exerciselist.getExerciselistitems().size()==0){
                            %>No exercises in this list.<%
                        } else {
                            for (Iterator<Exerciselistitem> iterator = exerciselist.getExerciselistitems().iterator(); iterator.hasNext();) {
                                Exerciselistitem exerciselistitem = iterator.next();
                                Exercise ex = Exercise.get(exerciselistitem.getExerciseid());
                                if (exercise!=null && ex.getExerciseid()==exercise.getExerciseid()){
                                    %>
                                    <a href="/exercises.jsp?exerciselistid=<%=exerciselist.getExerciselistid()%>&exerciseid=<%=ex.getExerciseid()%>"><font class="smallfont" style="font-weight: bold;"><%=ex.getTitle()%> x <%=exerciselistitem.getReps()%></font>
                                    <br/><br/>
                                    <%
                                } else {
                                    %>
                                    <a href="/exercises.jsp?exerciselistid=<%=exerciselist.getExerciselistid()%>&exerciseid=<%=ex.getExerciseid()%>"><font class="smallfont"><%=ex.getTitle()%> x <%=exerciselistitem.getReps()%></font>
                                    <br/><br/>
                                    <%
                                }
                            }
                        }
                        %>
                    </div>
                <%}%>
                <!-- End exercises -->
            </td>
            <td valign="top">
                <!-- Start exercise detail -->
                <%if (exercise!=null){%>
                    <div class="rounded" style="padding: 10px; margin: 5px; background: #e6e6e6;">
                        <font class="mediumfont" style="font-weight: bold;"><%=exercise.getTitle()%></font>
                        <br/><br/>
                        <%if (exercise.getImage()!=null && !exercise.getImage().equals("")){%>
                            <center>
                                <img src="/images/exercises/<%=exercise.getImage()%>" border="0">
                            </center>
                            <br/>
                        <%}%>
                        <font class="smallfont" style="font-weight: bold;"><%=exercise.getTitle()%>: </font>
                        <font class="smallfont"><%=exercise.getDescription()%></font>
                    </div>
                <%}%>
                <!-- End exercise detail -->
            </td>
        </tr>
    </table>


<%@ include file="/template/footer.jsp" %>
