<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "About Us";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <font class="mediumfont"><b>We're Awesome.</b>  What else could you possibly want to know?</font>




<%@ include file="/template/footer.jsp" %>
