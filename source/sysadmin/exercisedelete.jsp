<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.SysadminUserList" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.util.Util" %>
<%@ page import="com.pingfit.dao.hibernate.NumFromUniqueResult" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercise Delete, orly?";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Exercise exercise = new Exercise();
    exercise.setReps(20);
    exercise.setImage("");
    exercise.setIssystem(true);
    exercise.setIspublic(true);
    exercise.setImagecredit("");
    exercise.setImagecrediturl("");
    exercise.setUseridofcreator(Pagez.getUserSession().getUser().getUserid());
    if (request.getParameter("exerciseid") != null && !request.getParameter("exerciseid").equals("0") && Num.isinteger(request.getParameter("exerciseid"))) {
        exercise = Exercise.get(Integer.parseInt(request.getParameter("exerciseid")));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            int rowsupdated = HibernateUtil.getSession().createQuery("DELETE FROM Exerciselistitem WHERE exerciseid='"+exercise.getExerciseid()+"'").executeUpdate();
            int rowsupdates = HibernateUtil.getSession().createQuery("DELETE FROM Pingback WHERE exerciseid='"+exercise.getExerciseid()+"'").executeUpdate();
            exercise.delete();
            Pagez.getUserSession().setMessage("Exercise deleted.");
            Pagez.sendRedirect("/sysadmin/exerciselist.jsp");
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    

        <form action="/sysadmin/exercisedelete.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/exercisedelete.jsp">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">

            <table cellpadding="3" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Title</font>
                    </td>
                    <td valign="top">
                        <%=exercise.getTitle()%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Be Careful Plz!</font>
                    </td>
                    <td valign="top">
                        This exercise will be removed from all exercise lists that use it.  And it'll be removed from any exercise histories of people that used it.  This is hardcore stuff here.
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Number of Exercise Lists</font>
                    </td>
                    <td valign="top">
                        <%
                        int numlists = NumFromUniqueResult.getInt("select count(*) from Exerciselistitem where exerciseid='"+exercise.getExerciseid()+"'");
                        %>
                        This exercise appears in <%=numlists%> exercise lists.
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Number of Times People've Exercised</font>
                    </td>
                    <td valign="top">
                        <%
                        int numtimesused = NumFromUniqueResult.getInt("select count(*) from Pingback where exerciseid='"+exercise.getExerciseid()+"'");
                        %>
                        This exercise has been recorded <%=numtimesused%> times by users.
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Yes, Delete It">
                        <br/><br/>
                        <a href="/sysadmin/exercisedetail.jsp?exerciseid=<%=exercise.getExerciseid()%>">Nevermind, Just Kidding</a>
                    </td>
                </tr>

            </table>

    </form>





<%@ include file="/template/footer.jsp" %>



