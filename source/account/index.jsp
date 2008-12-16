<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.AccountIndex" %>
<%@ page import="com.pingfit.htmluibeans.AccountBalance" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Settings";
String navtab = "youraccount";
String acl = "account";
%>
<%
AccountIndex accountIndex = (AccountIndex) Pagez.getBeanMgr().get("AccountIndex");
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

   <%if (!accountIndex.getMsg().equals("")) {%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont"><%=((AccountIndex)Pagez.getBeanMgr().get("AccountIndex")).getMsg()%></font>
        </div>
   <%}%>

   <%if (Pagez.getRequest().getParameter("msg")!=null && Pagez.getRequest().getParameter("msg").equals("autologin")){%>
        <!--<div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">Your previous session timed out so you've been logged-in automatically!</font>
        </div>-->
   <%}%>
   <%if (!Pagez.getUserSession().getUser().getIsactivatedbyemail()){%>
        <br/>
        <font class="mediumfont" style="color: #666666;">Your account has not yet been activated by email.</font>
        <br/>
        <font class="smallfont">You must activate within 3 days of signup.  Check your email inbox for an activation message.  If you've lost that message... no problem: <img src="/images/clear.gif" width="2" height="1"/><a href="/emailactivationresend.jsp">re-send it</a>.</font>
    <%}%>

    <br/><br/>
    <a href="/account/exerciselist.jsp"><font class="mediumfont" style="color: #666666;">Your Exercises</font></a>
    <br/>
    <font class="smallfont">Create and manage your own exercises.</font>

    <br/><br/>
    <a href="/account/exerciselistlist.jsp"><font class="mediumfont" style="color: #666666;">Your Exercise Lists</font></a>
    <br/>
    <font class="smallfont">Create and manage your own exercise lists.</font>


    <br/><br/>
    <a href="/account/accountsettings.jsp"><font class="mediumfont" style="color: #666666;">Account Settings</font></a>
    <br/>
    <font class="smallfont">General account settings, email address, etc.</font>

    <br/><br/>
    <a href="/account/accountbalance.jsp"><font class="mediumfont" style="color: #666666;">Account Balance</font></a>
    <br/>
    <font class="smallfont">Check your account balance.</font>

    <br/><br/>
    <a href="/account/changepassword.jsp"><font class="mediumfont" style="color: #666666;">Change Password</font></a>
    <br/>
    <font class="smallfont">Change your password for super duper high security.</font>






<%@ include file="/template/footer.jsp" %>


