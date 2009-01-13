<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.LostPassword" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.util.RandomString" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Lost Password";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
LostPassword lostPassword = (LostPassword)Pagez.getBeanMgr().get("LostPassword");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("go")) {
        try {
            lostPassword.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6LeIqAQAAAAAALFIlYeWpO4tV_mGwfssSd7nAiul", "6LeIqAQAAAAAAE9cMX9WGmGKEgQfXl-8PAPYmJyn", false);
            ReCaptchaResponse capResp = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
            if (capResp.isValid()) {
                lostPassword.recoverPassword();
                Pagez.sendRedirect("/lostpasswordsent.jsp");
                return;
            } else {
                Pagez.getUserSession().setMessage("Sorry, you need to type the squiggly letters properly.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <form action="/lostpassword.jsp" method="post">
        <input type="hidden" name="dpage" value="/lostpassword.jsp">
        <input type="hidden" name="action" value="go">
        <table cellpadding="0" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Email</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("email", lostPassword.getEmail(), 255, 35, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Prove You're a Human</font>
                </td>
                <td valign="top">
                    <%
                    ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6LeIqAQAAAAAALFIlYeWpO4tV_mGwfssSd7nAiul", "6LeIqAQAAAAAAE9cMX9WGmGKEgQfXl-8PAPYmJyn", false);
                    String captchaScript = captcha.createRecaptchaHtml(request.getParameter("error"), null);
                    out.print(captchaScript);
                    %>
                </td>
            </tr>

            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Recover Password by Email">
                </td>
            </tr>

        </table>
    </form>


<%@ include file="/template/footer.jsp" %>
