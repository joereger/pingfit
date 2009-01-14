<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1"/>
    <title>PingFit</title>
    <link rel="stylesheet" type="text/css" href="/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="/css/pingFit-tray.css"/>
</head>

<body LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0"><div class="mainbgdiv"><center>
<table width="400" cellspacing="0" border="0" cellpadding="0">
    <tr>
        <td valign="top">
            <img src="/images/clear.gif" width="10" height="1" align="left"/>
            <a href="/"><img src="/images/pingfit-logo.gif" border="0" width="200" height="48" align="left"/></a>
        </td>
        <td valign="top" style="text-align: right;">

        </td>
    </tr>
    <tr>
        <td valign="top" colspan="2">
                <img src="/images/clear.gif" width="10" height="10"/><br/>
                <%if (navtab.equals("exercise") && Pagez.getUserSession().getIsloggedin()){%><font class="navfont"><a href="/account/exercise.jsp"><b>Exercise</b></a></font>&nbsp;&nbsp;&nbsp;&nbsp;<%}%>
                <%if (!navtab.equals("exercise") && Pagez.getUserSession().getIsloggedin()){%><font class="navfont"><a href="/account/exercise.jsp"><b>Exercise</b></a></font>&nbsp;&nbsp;&nbsp;&nbsp;<%}%>
                <%if (!navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><font class="navfont"><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a></font><%}%>
                <%if (navtab.equals("help") && Pagez.getUserSession().getIsloggedin()){%><font class="navfont"><a href="/account/accountsupportissueslist.jsp"><b>Help</b></a></font><%}%>
        </td>
    </tr>



</table>

    <br/><img src="/images/clear.gif" width="10" height="10"/><br/>
    <table width="400" cellspacing="0" border="0" cellpadding="10">
        <tr>
            <td valign="top">
                <%if (pagetitle!=null && !pagetitle.equals("")){%>
                    <font class="pagetitlefont" size="+1"><b><%=pagetitle%></b></font>
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