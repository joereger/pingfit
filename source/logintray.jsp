<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Welcome to PingFit!";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<font class="normalfont">
<br/><br/><br/><b>Before you can use PingFit you must enter your account login information into a Profile.<br/><br/>To do this, right click the tray icon an choose Profile.  Enter your email and password.  If you don't have a PingFit account please visit www.pingfit.com to get one.</b>
</font>

<br/><br/><br/><br/><br/><br/>

<%@ include file="/template/footer.jsp" %>