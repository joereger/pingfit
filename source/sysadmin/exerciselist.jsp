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
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercises";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



        <a href="/sysadmin/exercisedetail.jsp">New Exercise</a>
        <br/>

        <%
            List<Exercise> exercises = HibernateUtil.getSession().createCriteria(Exercise.class)
                    .addOrder(Order.desc("exerciseid"))
                    .setCacheable(true)
                    .list();
        %>

        <%if (exercises==null || exercises.size()==0){%>
            <font class="normalfont">No exercises!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Exerciseid", "<a href=\"/sysadmin/exercisedetail.jsp?exerciseid=<$exerciseid$>\"><$exerciseid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Title", "<a href=\"/sysadmin/exercisedetail.jsp?exerciseid=<$exerciseid$>\"><$title$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Reps", "<$reps$>", false, "", "tinyfont"));
            %>
            <%=Grid.render(exercises, cols, 200, "/sysadmin/exerciselist.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



