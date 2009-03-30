<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1"/>
    <title>pingFit</title>
    <link rel="stylesheet" type="text/css" href="/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="/css/pingFit.css"/>
    <link rel="stylesheet" type="text/css" href="/js/niftycube/niftyCorners.css"/>
    <link rel="alternate" type="application/rss+xml" title="pingFit"
          href="http://www.pingfit.com/rss.xml"/>
    <meta name="description"
          content="pingFit"/>
    <meta name="keywords" content="fitness,workout"/>
    <script type="text/javascript" src="/js/mootools/mootools-release-1.11.js"></script>
    <!--<script type="text/javascript" src="/js/niftycube/niftycube.js"></script>-->
    <!--<script type="text/javascript">-->
        <!--NiftyLoad = function() {-->
            <!--Nifty("div.rounded", "big");-->
        <!--}-->
    <!--</script>-->
    <!--[if IE]>
    <style type="text/css">
    p.iepara{ /*Conditional CSS- For IE (inc IE7), create 1em spacing between menu and paragraph that follows*/
    padding-top: 1em;
    }
    </style>
    <![endif]-->

</head>


<body background="/images/bg-v2.gif" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0"><center>
<table width="786" cellspacing="0" border="0" cellpadding="0">
<tr>
    <td width="200"><a href="/"><img src="/images/logo-v3.png" border="0" height="48" align="left"/></a></td>
    <td style="text-align: right; vertical-align: bottom;">
        <img src="/images/clear.gif" width="1" height="6" alt=""/><br/>
		<%if (navtab.equals("home")){%><font class="navtabfontlevel1"><a href="/index.jsp"><b>Home</b></a></font><%}%>
        <%if (!navtab.equals("home")){%><font class="navtabfontlevel1"><a href="/index.jsp"><b>Home</b></a></font><%}%>
        <%if (1==2 && navtab.equals("exercise") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/exercise.jsp"><b>Exercise</b></a></font><%}%>
        <%if (1==2 && !navtab.equals("exercise") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/exercise.jsp"><b>Exercise</b></a></font><%}%>
        <%if (1==2 && !navtab.equals("reports") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/reports.jsp"><b>Reports</b></a></font><%}%>
        <%if (1==2 && navtab.equals("reports") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/reports.jsp"><b>Reports</b></a></font><%}%>
        <%if (!navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsettings.jsp"><b>Settings</b></a></font><%}%>
        <%if (navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsettings.jsp"><b>Settings</b></a></font><%}%>
        <%if (!navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/registration.jsp"><b>Sign Up</b></a></font><%}%>
        <%if (navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/registration.jsp"><b>Sign Up</b></a></font><%}%>
        <%if (!navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/help.jsp"><b>Help</b></a></font><%}%>
        <%if (navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/help.jsp"><b>Help</b></a></font><%}%>
        <%if (1==2 && !navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a></font><%}%>
        <%if (1==2 && navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a></font><%}%>
        <%if (1==2 && !navtab.equals("help") && !Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a></font><%}%>
        <%if (1==2 && navtab.equals("help") && !Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a></font><%}%>
        <%if (Pagez.getUserSession().getIsSysadmin() && !navtab.equals("sysadmin")){%><font class="navtabfontlevel1"><a href="/sysadmin/index.jsp"><b>SysAdmin</a></font><%}%>
        <%if (Pagez.getUserSession().getIsSysadmin() && navtab.equals("sysadmin")){%><font class="navtabfontlevel1"><a href="/sysadmin/index.jsp"><b>SysAdmin</b></a></font><%}%>
    </td>
</tr>
<tr>
    <td><img src="/images/clear.gif" width="1" height="33" alt=""/></td>
    <td style="text-align: right; vertical-align: middle;">

        <%if (navtab.equals("home")){%>
            <img src="/images/clear.gif" alt="" width="10" height="1"/>
            <%if (1==1 && !Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><font class="subnavfont" style="color: #ffffff;">Sign Up</font></a><%}%>
            <img src="/images/clear.gif" alt="" width="10" height="1"/>
            <%if (1==1 && !Pagez.getUserSession().getIsloggedin()){%><a href="/login.jsp"><font class="subnavfont" style="color: #ffffff;">Log In</font></a><%}%>
            <img src="/images/clear.gif" alt="" width="10" height="1"/>
            <%if (1==1){%><a href="/blog.jsp"><font class="subnavfont" style="color: #ffffff;">Blog</font></a><%}%>
        <%}%>

        <%if (navtab.equals("exercise")){%>
            <%if (Pagez.getUserSession().getIsloggedin()){%>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/exerciselist.jsp"><font class="subnavfont" style="color: #ffffff;">Your Exercises</font></a>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/exerciselistlist.jsp"><font class="subnavfont" style="color: #ffffff;">Your Exercise Lists</font></a>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/reports.jsp"><font class="subnavfont" style="color: #ffffff;">Reports</font></a>
           <%}%>
        <%}%>

        <%if (navtab.equals("youraccount")){%>
            <%if (Pagez.getUserSession().getIsloggedin()){%>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/accountsettings.jsp"><font class="subnavfont" style="color: #ffffff;">Account Settings</font></a>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <%--<a href="/account/billing.jsp"><font class="subnavfont" style="color: #ffffff;">Billing Info</font></a>--%>
                <%--<img src="/images/clear.gif" alt="" width="10" height="1"/>--%>
                <%--<a href="/account/accountbalance.jsp"><font class="subnavfont" style="color: #ffffff;">Account Balance</font></a>--%>
                <%--<img src="/images/clear.gif" alt="" width="10" height="1"/>--%>
                <a href="/account/exerciselist.jsp"><font class="subnavfont" style="color: #ffffff;">Your Exercises</font></a>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/exerciselistlist.jsp"><font class="subnavfont" style="color: #ffffff;">Your Exercise Lists</font></a>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/reports.jsp"><font class="subnavfont" style="color: #ffffff;">Reports</font></a>
            <%}%>
        <%}%>

        <%if (navtab.equals("help")){%>
            <%if (Pagez.getUserSession().getIsloggedin()){%>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/accountsupportissueslist.jsp"><font class="subnavfont" style="color: #ffffff;">Private Questions</font></a>
           <%}%>
        <%}%>



        <%if (navtab.equals("sysadmin")){%>
            <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIsSysadmin()){%>
                <a href="/sysadmin/errorlist.jsp"><font class="subnavfont" style=" color: #ffffff;">Log</font></a>
                <a href="/sysadmin/transactions.jsp"><font class="subnavfont" style=" color: #ffffff;">Trans</font></a>
                <a href="/sysadmin/balance.jsp"><font class="subnavfont" style=" color: #ffffff;">Balance</font></a>
                <a href="/sysadmin/userlist.jsp"><font class="subnavfont" style=" color: #ffffff;">Users</font></a>
                <a href="/sysadmin/editeula.jsp"><font class="subnavfont" style=" color: #ffffff;">Eula</font></a>
                <a href="/sysadmin/sysadminsupportissueslist.jsp"><font class="subnavfont" style=" color: #ffffff;">Support</font></a>
                <a href="/sysadmin/manuallyrunscheduledtask.jsp"><font class="subnavfont" style=" color: #ffffff;">Schds</font></a>
                <a href="/sysadmin/systemprops.jsp"><font class="subnavfont" style=" color: #ffffff;">SysP</font></a>
                <a href="/sysadmin/instanceprops.jsp"><font class="subnavfont" style=" color: #ffffff;">InsP</font></a>
                <a href="/sysadmin/hibernatecache.jsp"><font class="subnavfont" style=" color: #ffffff;">Cache</font></a>
                <a href="/sysadmin/massemaillist.jsp"><font class="subnavfont" style=" color: #ffffff;">Email</font></a>
                <a href="/sysadmin/pageperformance.jsp"><font class="subnavfont" style=" color: #ffffff;">Perf</font></a>
                <a href="/sysadmin/exerciselist.jsp"><font class="subnavfont" style=" color: #ffffff;">Exercises</font></a>
                <a href="/sysadmin/exerciselistlist.jsp"><font class="subnavfont" style=" color: #ffffff;">ExLists</font></a>
                <a href="/sysadmin/simpleexercisereport.jsp"><font class="subnavfont" style=" color: #ffffff;">Reports</font></a>
                <a href="/sysadmin/blogpost.jsp"><font class="subnavfont" style=" color: #ffffff;">Blog</font></a>
            <%}%>
        <%}%>
	</td>
</tr>
</table><img src="/images/clear.gif" width="1" height="10" alt=""/><br/><table width="786" cellspacing="0" border="0" cellpadding="0">
<tr>
    <td valign="top">
        <%if (pagetitle!=null && !pagetitle.equals("")){%>
            <img src="/images/clear.gif" width="1" height="10" alt=""/><br/>
            <font class="pagetitlefont"><%=pagetitle%></font>
            <br/><br/>
        <%}%>
        <%
        logger.debug("Pagez.getUserSession().getMessage()="+Pagez.getUserSession().getMessage());
        if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){
            %>
            <br/>
            <center><div style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="formfieldnamefont"><%=Pagez.getUserSession().getMessage()%></font></div></center>
            <br/><br/>
            <%
            //Clear the message since it's been displayed
            Pagez.getUserSession().setMessage("");
        }
        %>
    </td>
    <td valign="top" style="text-align: right;">
        <%if (!Pagez.getUserSession().getIsloggedin()){%>
            <div style="text-align: right;">
                <font class="subnavfont">Already have an account?<img src="/images/clear.gif" width="20" height="1"/><a href="/login.jsp">Log In</a></font>
                <br/>
                <font class="subnavfont">Want to get one?<img src="/images/clear.gif" width="20" height="1"/><a href="/registration.jsp">Sign Up</a></font>
            </div>
        <%}%>
        <%if (Pagez.getUserSession().getIsloggedin()){%>
            <div style="text-align: right;">
                <font class="subnavfont">Hi, <%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%>! <a href="/login.jsp?action=logout">Log Out</a></font>
            </div>
        <%}%>
    </td>
    </tr>
</table>
<table width="786" cellspacing="0" border="0" cellpadding="0">
<tr>
    <td>
    <div style="text-align: left;">
<!-- Start Body -->