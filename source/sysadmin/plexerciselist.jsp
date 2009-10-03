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
<%@ page import="com.pingfit.dao.Exerciselist" %>
<%@ page import="com.pingfit.dao.Pl" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.helpers.PlExerciseListHelper" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "PL Exercise List Permissions";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("grant")) {
        try {
            int plid = 0;
            int exerciselistid = 0;
            if (Num.isinteger(request.getParameter("plid"))) { plid = Integer.parseInt(request.getParameter("plid")); }
            if (Num.isinteger(request.getParameter("exerciselistid"))) { exerciselistid = Integer.parseInt(request.getParameter("exerciselistid")); }
            PlExerciseListHelper.grantExerciseListAccessToPl(plid, exerciselistid);
            Pagez.getUserSession().setMessage("Changes saved.");
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("revoke")) {
        try {
            int plid = 0;
            int exerciselistid = 0;
            if (Num.isinteger(request.getParameter("plid"))) { plid = Integer.parseInt(request.getParameter("plid")); }
            if (Num.isinteger(request.getParameter("exerciselistid"))) { exerciselistid = Integer.parseInt(request.getParameter("exerciselistid")); }
            PlExerciseListHelper.revokeExerciseListAccessToPl(plid, exerciselistid);
            Pagez.getUserSession().setMessage("Changes saved.");
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>

<%@ include file="/template/header.jsp" %>



<table cellpadding="2" cellspacing="2" border="0">
        <%//Header row%>
        <tr>
            <td valign="top"></td>
            <%
            if (1==1){
            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                    .addOrder(Order.asc("plid"))
                    .setCacheable(true)
                    .list();
            if (pls!=null && pls.size()>0){
                for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
                    Pl pl=plIterator.next();
                    %><td valign="top"><b><%=pl.getName()%></b></td><%
                }
            %>
            <%}%>
            <%}%>
        </tr>

        <%//Body rows%>
        <%
            List<Exerciselist> exerciseLists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                    .add(Restrictions.eq("issystem", true))
                    .addOrder(Order.asc("exerciselistid"))
                    .setCacheable(true)
                    .list();
            if (exerciseLists!=null && exerciseLists.size()>0){
                for (Iterator<Exerciselist> exerciselistIterator=exerciseLists.iterator(); exerciselistIterator.hasNext();) {
                    Exerciselist exerciselist=exerciselistIterator.next();
                    %>
                    <tr>
                        <td valign="top"><b><%=exerciselist.getTitle()%></b></td>
                        <%
                        List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                .addOrder(Order.asc("plid"))
                                .setCacheable(true)
                                .list();
                        if (pls!=null && pls.size()>0){
                            for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
                                Pl pl=plIterator.next();
                                %>
                                <%
                                boolean hasaccess = PlExerciseListHelper.canPlUseExerciseList(pl.getPlid(), exerciselist.getExerciselistid());
                                %>
                                <%if (hasaccess){%>
                                    <td valign="top" bgcolor="#00ff00"><center><a href="/sysadmin/plexerciselist.jsp?action=revoke&plid=<%=pl.getPlid()%>&exerciselistid=<%=exerciselist.getExerciselistid()%>"><img src="/images/16x16-button-green.png" border="0" width="16" height="16"></a></center></td>
                                <%} else {%>
                                    <td valign="top" bgcolor="#e6e6e6"><center><a href="/sysadmin/plexerciselist.jsp?action=grant&plid=<%=pl.getPlid()%>&exerciselistid=<%=exerciselist.getExerciselistid()%>"><img src="/images/16x16-button-white.png" border="0" width="16" height="16"></a></center></td>
                                <%}%>
                            <%}%>
                        <%}%>
                    </tr>
                    <%
                }%>
            <%}%>

</table>



<%@ include file="/template/footer.jsp" %>



