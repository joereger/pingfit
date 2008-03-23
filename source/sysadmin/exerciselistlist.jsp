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
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercise Lists";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



        <a href="/sysadmin/exerciselistdetail.jsp">New Exercise List</a>
        <br/>

        <%
            List<Exerciselist> exerciseLists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                    .addOrder(Order.desc("exerciselistid"))
                    .setCacheable(true)
                    .list();
        %>

        <%if (exerciseLists==null || exerciseLists.size()==0){%>
            <font class="normalfont">No exercise lists!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Exerciselistid", "<a href=\"/sysadmin/exerciselistdetail.jsp?exerciselistid=<$exerciselistid$>\"><$exerciselistid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Title", "<a href=\"/sysadmin/exerciselistdetail.jsp?exerciselistid=<$exerciselistid$>\"><$title$></a>", false, "", "tinyfont"));
            %>
            <%=Grid.render(exerciseLists, cols, 200, "/sysadmin/exerciselistlist.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



