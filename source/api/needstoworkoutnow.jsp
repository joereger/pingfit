<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.Login" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.htmlui.DatatypeString" %>
<%@ page import="com.pingfit.htmlui.ValidationException" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.pingfit.util.Time" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
Login login = (Login) Pagez.getBeanMgr().get("Login");
try {
    login.setEmail(com.pingfit.htmlui.Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
    login.setPassword(com.pingfit.htmlui.Textbox.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
    login.login();
    if (Pagez.getUserSession().getIsloggedin()){
        %>true<%
    } else {
        %>login failure<%
    }
} catch (ValidationException vex) {
    Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
}
//@todo Leaving this userSession in memory is a big waste because it'll be created again the next time this API gets called
%>