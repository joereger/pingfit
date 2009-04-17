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
<%@ page import="com.pingfit.dao.User" %>
<%@ page import="com.pingfit.helpers.Userinterfaces" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reports";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

   <%
       ArrayList<PingbackListItem> pingbacklist = new ArrayList<PingbackListItem>();
       List<Pingback> pbs = HibernateUtil.getSession().createCriteria(Pingback.class)
               .addOrder(Order.desc("pingbackid"))
               .setMaxResults(1000)
               .setCacheable(true)
               .list();
       for (Iterator<Pingback> iterator = pbs.iterator(); iterator.hasNext();) {
           Pingback pingback = iterator.next();
           User user = User.get(pingback.getUserid());
           String userinterfaceStr = "Web";
           if (pingback.getUserinterface()==Userinterfaces.TRAY){
              userinterfaceStr = "OldCrappyMiniTrayApp";
           } else if (pingback.getUserinterface()==Userinterfaces.FLASH){
              userinterfaceStr = "NewAirDesktopApp";
           }
           PingbackListItem pbli = new PingbackListItem();
           pbli.setPingback(pingback);
           pbli.setExercise(Exercise.get(pingback.getExerciseid()));
           pbli.setUser(user);
           pbli.setUserinterfaceStr(userinterfaceStr);
           pingbacklist.add(pbli);
       }
   %>
    <%if ( pingbacklist==null || pingbacklist.size() == 0) {%>
        <font class="formfieldfont">You haven't yet done any exercises.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols = new ArrayList<GridCol>();
            cols.add(new GridCol("Date", "<$pingback.date$>", true, "", "tinyfont", "", ""));
            cols.add(new GridCol("User", "<a href=\"/sysadmin/userdetail.jsp?userid=<$user.userid$>\"><$user.firstname$> <$user.lastname$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Exercise", "<$exercise.title$>", false, "", "tinyfont"));
            cols.add(new GridCol("Reps", "<$pingback.reps$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(pingbacklist, cols, 500, "/account/reports.jsp", "page")%>
    <%}%>

<%@ include file="/template/footer.jsp" %>


