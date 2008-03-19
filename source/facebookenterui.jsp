<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.htmluibeans.PublicFacebookenterui" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Enter Facebook App";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <a href="<%=((com.pingfit.htmluibeans.PublicFacebookenterui)Pagez.getBeanMgr().get("PublicFacebookenterui")).getUrl()%>">Please click here to continue.</a>

<%@ include file="/template/footer.jsp" %>
