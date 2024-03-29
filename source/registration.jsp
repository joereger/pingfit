<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.Registration" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.systemprops.SystemProperty" %>
<%@ page import="com.pingfit.util.RandomString" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Sign Up for an Account";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
Registration registration = (Registration)Pagez.getBeanMgr().get("Registration");
%>
<%
if (Pagez.getUserSession().getIsloggedin()){
    Pagez.sendRedirect("/account/index.jsp");
    return;
}
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("register")) {
        try {
            registration.setIsflashsignup(false);
            registration.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            registration.setEula(Textarea.getValueFromRequest("eula", "Eula", true));
            registration.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            registration.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            registration.setPassword(TextboxSecret.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            registration.setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Password Verify", true, DatatypeString.DATATYPEID));

            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6LeIqAQAAAAAALFIlYeWpO4tV_mGwfssSd7nAiul", "6LeIqAQAAAAAAE9cMX9WGmGKEgQfXl-8PAPYmJyn", false);
            ReCaptchaResponse capResp = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
            if (capResp.isValid()) {
                registration.registerAction();
                //Redir if https is on
                if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")) {
                    try {
                        logger.debug("redirecting to https - " + BaseUrl.get(true) + "/account/index.jsp");
                        Pagez.sendRedirect(BaseUrl.get(true) + "/account/index.jsp");
                        return;
                    } catch (Exception ex) {
                        logger.error("", ex);
                        //@todo setIsfirsttimelogin(true) on AccountIndex bean
                        Pagez.sendRedirect("/account/index.jsp");
                        return;
                    }
                } else {
                    //@todo setIsfirsttimelogin(true) on AccountIndex bean
                    Pagez.sendRedirect("/account/index.jsp");
                    return;
                }
            } else {
                Pagez.getUserSession().setMessage("Sorry, you need to type the squiggly letters properly.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    String captchaId=RandomString.randomAlphanumeric(10);
%>
<%@ include file="/template/header.jsp" %>



        <div style="width: 250px; float: right; padding-left: 20px;">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <font class="mediumfont" style="color: #333333">Existing Users</font><br/>
                <font class="smallfont">If you've already got an account you can simply log in.</font><br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                    <%=GreenRoundedButton.get("<a href=\"/login.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff; font-weight: bold;\">Log In</font></a>")%>
                </div>
            </div>
        </div>
        <form action="/registration.jsp" method="post">
            <input type="hidden" name="dpage" value="/registration.jsp">
            <input type="hidden" name="action" value="register">
            <input type="hidden" name="captchaId" value="<%=captchaId%>">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                <font class="mediumfont" style="color: #333333">Get started!</font>
                <br/>
                <font class="smallfont">Sign Up is free.  On this page we collect some basic information.  Your account is completely free to set up and explore.</font><br/><br/>

                <table cellpadding="0" cellspacing="0" border="0">

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">First Name</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("firstname", registration.getFirstname(), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Last Name</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("lastname", registration.getLastname(), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Email</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("email", registration.getEmail(), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Password</font>
                        </td>
                        <td valign="top">
                            <%=TextboxSecret.getHtml("password", registration.getPassword(), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Password Verify</font>
                        </td>
                        <td valign="top">
                            <%=TextboxSecret.getHtml("passwordverify", registration.getPasswordverify(), 255, 35, "", "")%>
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
                            <font class="formfieldnamefont">End User License Agreement</font>
                        </td>
                        <td valign="top">
                            <%=Textarea.getHtml("eula", registration.getEula(), 3, 40, "", "")%>
                        </td>
                    </tr>


                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <br/><br/>
                            <input type="submit" class="formsubmitbutton" value="Sign Up">
                        </td>
                    </tr>

                </table>
            </div>
        </form>


<%@ include file="/template/footer.jsp" %>