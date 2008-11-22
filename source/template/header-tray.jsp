<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1"/>
    <title>PingFit</title>
    <link rel="stylesheet" type="text/css" href="/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="/css/pingFit-tray.css"/>
    <link rel="alternate" type="application/rss+xml" title="pingFit"
          href="http://www.pingFit.com/rss.xml"/>
    <meta name="description"
          content="PingFit"/>
    <meta name="keywords" content="fitness,workout"/>
</head>
<body>

<table width="400" cellspacing="0" border="0" cellpadding="0">
    <tr>
        <td valign="top">
            <img src="/images/clear.gif" width="10" height="1" align="left"/>
            <a href="/"><img src="/images/pingfit-logo.gif" border="0" height="75" align="left"/></a>
        </td>
        <td valign="top" style="text-align: right;">

        </td>
    </tr>
    <tr>
        <td valign="top" bgcolor="#dadada" colspan="2">
                <%if (navtab.equals("exercise") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/exercise.jsp"><b>Exercise</b></a>&nbsp;&nbsp;&nbsp;&nbsp;<%}%>
                <%if (!navtab.equals("exercise") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/exercise.jsp"><b>Exercise</b></a>&nbsp;&nbsp;&nbsp;&nbsp;<%}%>
                <%if (!navtab.equals("reports") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/reports.jsp"><b>Reports</b></a>&nbsp;&nbsp;&nbsp;&nbsp;<%}%>
                <%if (navtab.equals("reports") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/reports.jsp"><b>Reports</b></a>&nbsp;&nbsp;&nbsp;&nbsp;<%}%>
                <%if (!navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a><%}%>
                <%if (navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a><%}%>
        </td>
    </tr>



</table>


    <table width="400" cellspacing="0" border="0" cellpadding="10">
        <tr>
            <td valign="top">
                <%if (pagetitle!=null && !pagetitle.equals("")){%>
                    <font class="pagetitlefont"><%=pagetitle%></font>
                    <br/>
                <%}%>
                <%
                logger.debug("Pagez.getUserSession().getMessage()="+Pagez.getUserSession().getMessage());
                if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){
                    %>
                    <br/>
                    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="formfieldnamefont"><%=Pagez.getUserSession().getMessage()%></font></div></center>
                    <br/><br/>
                    <%
                    //Clear the message since it's been displayed
                    Pagez.getUserSession().setMessage("");
                }
                %>
                <!-- Begin Body -->