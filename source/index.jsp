<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.PublicIndex" %>
<%@ page import="com.pingfit.util.Time" %>
<%@ page import="com.pingfit.api.AjaxExercisePage" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="com.pingfit.api.Exerciser" %>
<%@ page import="com.pingfit.htmlui.ValidationException" %>
<%@ page import="com.pingfit.api.CoreMethods" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.api.CompletedExercise" %>
<%@ page import="com.pingfit.dao.Exerciselist" %>
<%@ page import="java.util.*" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    PublicIndex publicIndex = (PublicIndex) Pagez.getBeanMgr().get("PublicIndex");
%>
<%@ include file="/template/header.jsp" %>











<%@ include file="/template/footer.jsp" %>