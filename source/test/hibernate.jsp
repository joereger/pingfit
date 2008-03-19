<%@ page import="com.pingfit.htmlui.UserSession" %>
<%@ page import="com.pingfit.htmlui.Authorization" %>
<%
//Hide from snooping eyes... only sysadmins can play
UserSession userSession = (UserSession) session.getAttribute("userSession");
if (userSession == null || !userSession.getIsloggedin() || !Authorization.isUserSysadmin(userSession.getUser())) {
    response.sendRedirect("/");
    return;
}
%>
<%



%>