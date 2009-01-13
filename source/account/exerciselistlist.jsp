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
String pagetitle = "Your Exercise Lists";
String navtab = "exercise";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


        <a href="/account/exerciselistdetail.jsp"><b>Create a New Exercise List</b></a>
        <br/><br/>

        <%
            List<Exerciselist> exerciseLists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                    .addOrder(Order.asc("exerciselistid"))
                    .add(Restrictions.eq("useridofcreator", Pagez.getUserSession().getUser().getUserid()))
                    .add(Restrictions.eq("issystem", false))
                    .setCacheable(true)
                    .list();
        %>

        <%if (exerciseLists==null || exerciseLists.size()==0){%>
            <font class="normalfont">You haven't created exercise lists.</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Title", "<a href=\"/account/exerciselistdetail.jsp?exerciselistid=<$exerciselistid$>\"><$title$></a>", false, "", "tinyfont"));
            %>
            <%=Grid.render(exerciseLists, cols, 200, "/account/exerciselistlist.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



