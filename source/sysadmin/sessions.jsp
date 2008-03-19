<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.htmluibeans.SysadminSessions" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Active Sessions";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminSessions sysadminSessions = (SysadminSessions) Pagez.getBeanMgr().get("SysadminSessions");
%>
<%@ include file="/template/header.jsp" %>




    <%=sysadminSessions.getSessionsashtml()%>



<%@ include file="/template/footer.jsp" %>