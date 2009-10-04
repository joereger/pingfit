<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.SysadminUserDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.money.CurrentBalanceCalculator" %>
<%@ page import="com.pingfit.htmluibeans.StaticVariables" %>
<%@ page import="com.pingfit.dao.Pl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.htmluibeans.Registration" %>
<%@ page import="com.pingfit.eula.EulaHelper" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "New User";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
Registration registration = (Registration)Pagez.getBeanMgr().get("Registration");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("newuser")) {
         try {
            registration.setPlid(Textbox.getIntFromRequest("plid", "Plid", true, DatatypeInteger.DATATYPEID));
            registration.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            registration.setEula(EulaHelper.getMostRecentEula(Textbox.getIntFromRequest("plid", "Plid", true, DatatypeInteger.DATATYPEID)).getEula().trim());
            registration.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            registration.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            registration.setNickname(Textbox.getValueFromRequest("nickname", "Nickname", true, DatatypeString.DATATYPEID));
            registration.setPassword(TextboxSecret.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            registration.setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Password Verify", true, DatatypeString.DATATYPEID));
            registration.setIsflashsignup(true);
            registration.registerAction();
            Pagez.getUserSession().setMessage("User '"+Textbox.getValueFromRequest("nickname", "Nickname", true, DatatypeString.DATATYPEID)+"' Created.");
            Pagez.sendRedirect("/sysadmin/userlist.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <form action="/sysadmin/usernew.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/usernew.jsp">
            <input type="hidden" name="action" value="newuser">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                <table cellpadding="0" cellspacing="0" border="0">

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Private Label</font>
                        </td>
                        <td valign="top">
                            <select name="plid">
                            <%
                            if (1==1){
                            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                    .addOrder(Order.asc("plid"))
                                    .setCacheable(true)
                                    .list();
                            if (pls!=null && pls.size()>0){
                                for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
                                    Pl pl=plIterator.next();
                                    String sel = "";
                                    if (String.valueOf(pl.getPlid()).equals(request.getParameter("plid"))){sel=" selected";}
                                    %><option value="<%=pl.getPlid()%>" <%=sel%>><%=pl.getName()%></option><%
                                }
                            %>
                            <%}%>
                            <%}%>
                            </select>
                        </td>
                    </tr>


                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">First Name</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("firstname", request.getParameter("firstname"), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Last Name</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("lastname", request.getParameter("lastname"), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Nickname</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("nickname", request.getParameter("nickname"), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Email</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("email", request.getParameter("email"), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Password</font>
                        </td>
                        <td valign="top">
                            <%=TextboxSecret.getHtml("password", request.getParameter("password"), 255, 35, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Password Verify</font>
                        </td>
                        <td valign="top">
                            <%=TextboxSecret.getHtml("passwordverify", request.getParameter("passwordverify"), 255, 35, "", "")%>
                        </td>
                    </tr>



                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <br/><br/>
                            <input type="submit" class="formsubmitbutton" value="Create New User">
                        </td>
                    </tr>

                </table>
            </div>
        </form>







<%@ include file="/template/footer.jsp" %>


