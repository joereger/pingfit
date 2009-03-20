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
<%@ page import="com.pingfit.dao.Exerciselist" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dao.Exerciselistitem" %>
<%@ page import="com.pingfit.dao.hibernate.NumFromUniqueResult" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercise List Delete, Orly?";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Exerciselist exerciselist = new Exerciselist();
    exerciselist.setIssystem(true);
    exerciselist.setIspublic(true);
    exerciselist.setIssystemdefault(false);
    exerciselist.setExerciseeveryxminutes(20);
    exerciselist.setUseridofcreator(Pagez.getUserSession().getUser().getUserid());
    if (request.getParameter("exerciselistid") != null && !request.getParameter("exerciselistid").equals("0") && Num.isinteger(request.getParameter("exerciselistid"))) {
        exerciselist = Exerciselist.get(Integer.parseInt(request.getParameter("exerciselistid")));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            int rowsupdated = HibernateUtil.getSession().createQuery("DELETE FROM Exerciselistitem WHERE exerciselistid='"+exerciselist.getExerciselistid()+"'").executeUpdate();
            int rowsupdates = HibernateUtil.getSession().createQuery("UPDATE User SET exerciselistid='1' WHERE exerciselistid='"+exerciselist.getExerciselistid()+"'").executeUpdate();
            exerciselist.delete();
            Pagez.getUserSession().setMessage("Exercise list deleted.");
            Pagez.sendRedirect("/sysadmin/exerciselistlist.jsp");
            return;
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<table cellpadding="3" cellspacing="0" border="0">

    <tr>
        <td valign="top">

            <form action="/sysadmin/exerciselistdelete.jsp" method="post">
                <input type="hidden" name="dpage" value="/sysadmin/exerciselistdelete.jsp">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="exerciselistid" value="<%=exerciselist.getExerciselistid()%>">

                <table cellpadding="3" cellspacing="0" border="0">

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">List Title</font>
                        </td>
                        <td valign="top">
                            <%=exerciselist.getTitle()%>
                        </td>
                    </tr>


                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <br/><br/>
                            <input type="submit" class="formsubmitbutton" value="Yes, Delete Exercise List">
                            <br/><br/>
                            <a href="/sysadmin/exerciselistdetail.jsp?exerciselistid=<%=exerciselist.getExerciselistid()%>">Nevermind, Just Kidding</a>
                        </td>
                    </tr>

                </table>

            </form>
        </td>

    </tr>
</table>



<%@ include file="/template/footer.jsp" %>



