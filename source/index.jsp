<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
int refid = 0;
if (request.getParameter("refid")!=null && Num.isinteger(request.getParameter("refid"))){
    refid = Integer.parseInt(request.getParameter("refid"));
    Pagez.getUserSession().setRefid(refid);
}
if (refid==0){
    if (Pagez.getUserSession().getRefid()>0){
        refid = Pagez.getUserSession().getRefid();
    }
}
%>

<html>
<head>
	<title>pingFit</title>
	<script type='text/javascript' src='badgefiles/swfobject.js?refid=<%=refid%>'></script>
    <script type='text/javascript' src='badgefiles/badgeInstall.js?refid=<%=refid%>'></script>
	<link type='text/css' rel='stylesheet' href='badgefiles/style.css' />
	<style type="text/css">
	body {
	   background-image: url(images/hpv2-bg.gif);
	   background-repeat: repeat-x;
	   background-attachment: fixed;
	}
	</style>
</head>

<body LEFTMARGIN="0" RIGHTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0" bgcolor="#238bae">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td><center><img src="images/pingFit-logo.gif" width="274" height="168" alt="" border="0"></center></td>
</tr>
<tr>
<td><center>
	<table cellpadding="0" cellspacing="0" border="0" width="901" height="321">
		<tr><td background="images/module.jpg">
			<table cellpadding="0" cellspacing="0" border="0" width="901" height="321">
				<tr>
					<td valign="top" width="60">
						<img src="images/clear.gif" width="1" height="60" alt="" border="0"><br/>
					</td>
					<td valign="top" width="240">
						<img src="images/clear.gif" width="1" height="20" alt="" border="0"><br/>
						<object width="215" height="180"><param name="movie" value="http://www.youtube.com/v/nixO_g7-1b8&hl=en&fs=1&color1=0x2b405b&color2=0x6b8ab6"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed src="http://www.youtube.com/v/nixO_g7-1b8&hl=en&fs=1&color1=0x2b405b&color2=0x6b8ab6" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="215" height="180"></embed></object>
                    </td>
					<td valign="top" width="300">
					<img src="images/clear.gif" width="300" height="1" alt="" border="0"><br/>
					</td>
					<td valign="top" width="*">
						<img src="images/clear.gif" width="1" height="20" alt="" border="0"><br/>
						<div id='badge_div'>
							To install pingFit you will need the <a href='http://www.adobe.com/products/flashplayer/'>Adobe Flash Player</a>
						</div>
					</td>
				</tr>
			</table>
		</td></tr>
	</table>
</center></td>
</tr>
</table>


</body>
</html>
