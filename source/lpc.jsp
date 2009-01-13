<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.LostPasswordChoose" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.util.RandomString" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reset Password";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
LostPasswordChoose lostPasswordChoose=(LostPasswordChoose) Pagez.getBeanMgr().get("LostPasswordChoose");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("choose")) {
        try {
            lostPasswordChoose.setPassword(TextboxSecret.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            lostPasswordChoose.setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Password Verify", true, DatatypeString.DATATYPEID));
            lostPasswordChoose.setU(request.getParameter("u"));
            lostPasswordChoose.setK(request.getParameter("k"));
            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6LeIqAQAAAAAALFIlYeWpO4tV_mGwfssSd7nAiul", "6LeIqAQAAAAAAE9cMX9WGmGKEgQfXl-8PAPYmJyn", false);
            ReCaptchaResponse capResp = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
            if (capResp.isValid()) {
                lostPasswordChoose.choosePassword();
                Pagez.getUserSession().setMessage("Your password has been set.  Store it in a safe place.");
                Pagez.sendRedirect("/account/index.jsp");
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

<form action="/lpc.jsp" method="post">
    <input type="hidden" name="dpage" value="/lpc.jsp">
    <input type="hidden" name="action" value="choose">
    <input type="hidden" name="u" value="<%=request.getParameter("u")%>">
    <input type="hidden" name="k" value="<%=request.getParameter("k")%>">
    <table cellpadding="0" cellspacing="0" border="0">

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Password</font>
            </td>
            <td valign="top">
                <%=TextboxSecret.getHtml("password", lostPasswordChoose.getPassword(), 255, 35, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Password Verify</font>
                </td>
                <td valign="top">
                    <%=TextboxSecret.getHtml("passwordverify", lostPasswordChoose.getPasswordverify(), 255, 35, "", "")%>
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
                    <input type="submit" class="formsubmitbutton" value="Reset Password">
                </td>
            <tr>

        </table>
    </form>


<%@ include file="/template/footer.jsp" %>
