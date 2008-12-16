<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.htmluibeans.SystemStats" %>
<%@ page import="com.pingfit.util.Str" %>
<%@ page import="com.pingfit.htmluibeans.SysadminIndex" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SysAdmin Home";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminIndex sysadminIndex=(SysadminIndex) Pagez.getBeanMgr().get("SysadminIndex");
    SystemStats systemStats=(SystemStats) Pagez.getBeanMgr().get("SystemStats");
%>
<%@ include file="/template/header.jsp" %>




        <%=sysadminIndex.getServermemory()%>









<%@ include file="/template/footer.jsp" %>