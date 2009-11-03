<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.util.Io" %>
<%@ page import="com.pingfit.systemprops.WebAppRootDir" %>
<%@ page import="com.pingfit.systemprops.SystemProperty" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.util.Util" %>
<%
    Logger logger = Logger.getLogger(this.getClass().getName());
    String airVersion = Io.textFileRead(WebAppRootDir.getWebAppRootPath()  + "PingFitAirVersion.txt").toString();
    String airUrl = "http://"+ SystemProperty.getProp(SystemProperty.PROP_BASEURL) + "/PingFit.air";
    int plid = Pagez.getUserSession().getPl().getPlid();
    int refid = 0;
    if (request.getParameter("refid")!=null && Num.isinteger(request.getParameter("refid"))){
        refid = Integer.parseInt(request.getParameter("refid"));
    }
    if (refid==0){
        if (Pagez.getUserSession().getRefid()>0){
            refid = Pagez.getUserSession().getRefid();
        }
    }
    String flashVars = "refid="+refid+":"+"plid="+Pagez.getUserSession().getPl().getPlid()+":"+"some=var";

    StringBuffer flashVarsForPage = new StringBuffer();

//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.id", "air_badge"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.airversion", "1.5"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.appname", "pingFit"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.appurl", airUrl));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.appid", "com.pingFit"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.appversion", airVersion));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.appinstallarg", flashVars));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.applauncharg", flashVars));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.hidehelp", "true"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.skiptransition", "false"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.titlecolor", "#aaff"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.buttonlabelcolor", "#aaff"));
//    flashVarsForPage.append("&");
//    flashVarsForPage.append(Util.urlEncodeNameAndValue("flashvars.appnamecolor", "#aaff"));
//    flashVarsForPage.append("&");



    flashVarsForPage.append(Util.urlEncodeNameAndValue("appname", "pingFit"));
    flashVarsForPage.append("&");
    flashVarsForPage.append(Util.urlEncodeNameAndValue("appurl", airUrl));
    flashVarsForPage.append("&");
    flashVarsForPage.append(Util.urlEncodeNameAndValue("airversion", "1.5"));
    flashVarsForPage.append("&");
    flashVarsForPage.append(Util.urlEncodeNameAndValue("imageurl", "http://"+SystemProperty.getProp(SystemProperty.PROP_BASEURL)+"/badgefiles/fb/facebook_app_badge_image-v2.jpg"));
    flashVarsForPage.append("&");

%>


<center>
<img src="http://<%=SystemProperty.getProp(SystemProperty.PROP_BASEURL)%>/badgefiles/fb/facebook_app_header.gif" border="0" width="600" height="227">
<br/>

<table cellpadding="5" cellspacing="2" border="0" width="600">
    <tr>
        <td valign="top" width="50%">
            <center>
            <fb:swf
            swfbgcolor="000000" imgstyle="border-width:3px; border-color:white;"
            swfsrc='http://www.youtube.com/v/nixO_g7-1b8'
            imgsrc='http://img.youtube.com/vi/nixO_g7-1b8/2.jpg' width='215' height='130' />
            </center>
        </td>
        <td valign="top" width="50%">
            <center>
            <fb:swf
            swfbgcolor="FFFFFF"
            swfsrc='http://<%=SystemProperty.getProp(SystemProperty.PROP_BASEURL)%>/badgefiles/fb/badge.swf'
            imgsrc='http://<%=SystemProperty.getProp(SystemProperty.PROP_BASEURL)%>/badgefiles/fb/facebook_app_badge_image.jpg'
            flashvars='<%=flashVarsForPage%>'
            width='215'
            height='180'
            />
            </center>
        </td>
    </tr>
    <tr>
        <td valign="top" colspan="2">
        <font style="font-size: 20px;">pingFit is a social fitness application.  Every 20 minutes we'll ask you to do a simple exercise.  You'll do it and then go back to your day.  Over the course of the day you'll get a high quality workout.<br/><br/>Simple.  Give it a go.  Click Install Now.</font>
        <br/><br/>
        <font style="font-size: 15px;">Or, visit the <a href="http://www.facebook.com/apps/application.php?id=160194959586">Fan Page</a>.</font>
        </td>
    </tr>
</table>







</center>




