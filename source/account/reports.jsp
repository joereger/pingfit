<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.AccountIndex" %>
<%@ page import="com.pingfit.htmluibeans.AccountBalance" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="com.pingfit.dao.Pingback" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.pingfit.htmluibeans.AccountSupportIssuesList" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reports";
String navtab = "exercise";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

   <%
       ArrayList<PingbackListItem> pingbacklist = new ArrayList<PingbackListItem>();
       List<Pingback> pbs = HibernateUtil.getSession().createCriteria(Pingback.class)
               .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
               .addOrder(Order.desc("pingbackid"))
               .setCacheable(true)
               .list();
       for (Iterator<Pingback> iterator = pbs.iterator(); iterator.hasNext();) {
           Pingback pingback = iterator.next();
           PingbackListItem pbli = new PingbackListItem();
           pbli.setPingback(pingback);
           pbli.setExercise(Exercise.get(pingback.getExerciseid()));
           pingbacklist.add(pbli);
       }
   %>
    <%if ( pingbacklist==null || pingbacklist.size() == 0) {%>
        <font class="formfieldfont">You haven't yet done any exercises.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols = new ArrayList<GridCol>();
            cols.add(new GridCol("Date", "<$pingback.date$>", true, "", "tinyfont", "", ""));
            cols.add(new GridCol("Exercise", "<$exercise.title$>", false, "", "tinyfont"));
            cols.add(new GridCol("Reps", "<$pingback.reps$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(pingbacklist, cols, 50, "/account/reports.jsp", "page")%>
    <%}%>

<%@ include file="/template/footer.jsp" %>


