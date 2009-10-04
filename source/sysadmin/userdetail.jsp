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
<%@ page import="com.pingfit.helpers.PlExerciseListHelper" %>
<%@ page import="com.pingfit.helpers.PlAdminHelper" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "User: "+((SysadminUserDetail) Pagez.getBeanMgr().get("SysadminUserDetail")).getEmail();
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminUserDetail sysadminUserDetail = (SysadminUserDetail)Pagez.getBeanMgr().get("SysadminUserDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminUserDetail.setPlid(Textbox.getIntFromRequest("plid", "Private Label", true, DatatypeInteger.DATATYPEID));
            sysadminUserDetail.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setNickname(Textbox.getValueFromRequest("nickname", "Nickname", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setEmail(Textbox.getValueFromRequest("email", "Email", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.setPassword(Textbox.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setFacebookuid(Textbox.getValueFromRequest("facebookuserid", "Facebookuserid", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.save();
            Pagez.getUserSession().setMessage("User details saved");
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("togglesysadmin")) {
        try {
            sysadminUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.togglesysadminprivs();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("togglepladmin")) {
        try {
            sysadminUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.togglepladminprivs();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deleteuser")) {
        try {
            sysadminUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.deleteuser();
            Pagez.getUserSession().setMessage("User deleted");
            Pagez.sendRedirect("/sysadmin/userlist.jsp");
            return;
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("toggleisenabled")) {
        try {
            sysadminUserDetail.toggleisenabled();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("giveusermoney")) {
        try {
            sysadminUserDetail.setAmt(Textbox.getDblFromRequest("amt", "Amount", true, DatatypeDouble.DATATYPEID));
            sysadminUserDetail.setReason(Textbox.getValueFromRequest("reason", "Reason", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setFundstype(Dropdown.getIntFromRequest("fundstype", "Funds Type", true));
            sysadminUserDetail.giveusermoney();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("takeusermoney")) {
        try {
            sysadminUserDetail.setAmt(Textbox.getDblFromRequest("amt", "Amount", true, DatatypeDouble.DATATYPEID));
            sysadminUserDetail.setReason(Textbox.getValueFromRequest("reason", "Reason", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setFundstype(Dropdown.getIntFromRequest("fundstype", "Funds Type", true));
            sysadminUserDetail.takeusermoney();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("passwordresetemail")) {
        try {
            sysadminUserDetail.sendresetpasswordemail();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("reactivationemail")) {
        try {
            sysadminUserDetail.reactivatebyemail();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("onlyshowsuccessfultransactions") != null && request.getParameter("onlyshowsuccessfultransactions").equals("1")) {
        try {
            sysadminUserDetail.setOnlyshowsuccessfultransactions(true);
            sysadminUserDetail.initBean();
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.getMessage());
        }
    }
%>
<%
    if (request.getParameter("onlyshownegativeamountbalance")!=null && request.getParameter("onlyshownegativeamountbalance").equals("1")) {
        try {
            sysadminUserDetail.setOnlyshownegativeamountbalance(true);
            sysadminUserDetail.initBean();
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.getMessage());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("grantpladmin")) {
        try {
            int pladminplid = 0;
            if (Num.isinteger(request.getParameter("pladminplid"))) { pladminplid = Integer.parseInt(request.getParameter("pladminplid")); }
            PlAdminHelper.grantPlControlToUser(sysadminUserDetail.getUserid(), pladminplid);
            Pagez.getUserSession().setMessage("Changes saved.");
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("revokepladmin")) {
        try {
            int pladminplid = 0;
            if (Num.isinteger(request.getParameter("pladminplid"))) { pladminplid = Integer.parseInt(request.getParameter("pladminplid")); }
            PlAdminHelper.revokePlControlFromUser(sysadminUserDetail.getUserid(), pladminplid);
            Pagez.getUserSession().setMessage("Changes saved.");
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <%
            CurrentBalanceCalculator cbc=new CurrentBalanceCalculator(sysadminUserDetail.getUser());
        %>

        <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top" width="50%">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
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
                                                    if (pl.getPlid()==sysadminUserDetail.getUser().getPlid()){sel=" selected";}
                                                    %><option value="<%=pl.getPlid()%>" <%=sel%>><%=pl.getName()%></option><%
                                                }
                                            }
                                            }%>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">First Name</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("firstname", sysadminUserDetail.getFirstname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Last Name</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("lastname", sysadminUserDetail.getLastname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Nickname</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("nickname", sysadminUserDetail.getNickname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Email</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("email", sysadminUserDetail.getEmail(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Password</font>
                                        </td>
                                        <td valign="top">
                                            <%=TextboxSecret.getHtml("password", sysadminUserDetail.getPassword(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Facebook uid</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("facebookuserid", String.valueOf(sysadminUserDetail.getFacebookuid()), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Currentbalance</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont">$<%=cbc.getCurrentbalance()%></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">

                                        </td>
                                        <td valign="top">
                                            <input type="submit" class="formsubmitbutton" value="Save User Details">
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="passwordresetemail">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Send Password Reset Email">
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="reactivationemail">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Force Re-Activation By Email">
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="researcherremainingbalanceoperations">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="ResearcherRemainingBalanceOperations">
                            </form>
                            <br/>
                            <font class="tinyfont">This will process account balances, remaining impressions, credit card transfers, etc for only this account.  Only does something if this user has a researcher record.</font>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="togglesysadmin">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <%if (sysadminUserDetail.getIssysadmin()){%>
                                    <font class="mediumfont">User is a Sysadmin.</font>
                                <%} else {%>
                                    <font class="mediumfont">User is not a Sysadmin.</font>
                                <%}%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Toggle Sysadmin Privileges">
                                <%=Textbox.getHtml("activitypin", String.valueOf(sysadminUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="togglepladmin">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <%if (sysadminUserDetail.getIspladmin()){%>
                                    <font class="mediumfont">User is a PLAdmin.</font>
                                <%} else {%>
                                    <font class="mediumfont">User is not a PLAdmin.</font>
                                <%}%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Toggle PLAdmin Privileges">
                                <%=Textbox.getHtml("activitypin", String.valueOf(sysadminUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                            <%if (sysadminUserDetail.getIspladmin()){%>
                                <div class="rounded" style="padding: 15px; margin: 5px; background: #cccccc;">
                                    <table cellpadding="2" cellspacing="2" border="0">

                                            <%
                                            if (1==1){
                                            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                                    .addOrder(Order.asc("plid"))
                                                    .setCacheable(true)
                                                    .list();
                                            if (pls!=null && pls.size()>0){
                                                for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
                                                    Pl pl=plIterator.next();
                                                    %>
                                                    <%
                                                    boolean hasaccess = PlAdminHelper.canUserControlPl(sysadminUserDetail.getUserid(), pl.getPlid());
                                                    %>
                                                    <tr>
                                                    <td valign="top"><font class="tinyfont"><%=pl.getName()%></font></td>
                                                    <%if (hasaccess){%>
                                                        <td valign="top" bgcolor="#00ff00"><center><a href="/sysadmin/userdetail.jsp?action=revokepladmin&pladminplid=<%=pl.getPlid()%>&userid=<%=sysadminUserDetail.getUserid()%>"><img src="/images/16x16-button-green.png" border="0" width="16" height="16"></a></center></td>
                                                    <%} else {%>
                                                        <td valign="top" bgcolor="#e6e6e6"><center><a href="/sysadmin/userdetail.jsp?action=grantpladmin&pladminplid=<%=pl.getPlid()%>&userid=<%=sysadminUserDetail.getUserid()%>"><img src="/images/16x16-button-white.png" border="0" width="16" height="16"></a></center></td>
                                                    <%}%>
                                                    </tr>
                                                <%}%>
                                            <%}%>
                                        <%}%>
                                    </table>
                                </div>
                            <%}%>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="deleteuser">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Delete User">
                                <%=Textbox.getHtml("activitypin", String.valueOf(sysadminUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                    </td>
                 
                    <td valign="top" width="50%">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="toggleisenabled">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <%if (sysadminUserDetail.getIsenabled()){%>
                                    <font class="mediumfont">This Account is Currently Enabled.</font>
                                    <br/>
                                    <input type="submit" class="formsubmitbutton" value="Disable Account">
                                <%} else {%>
                                    <font class="mediumfont">This Account is Currently Disabled.</font>
                                    <br/>
                                    <input type="submit" class="formsubmitbutton" value="Enable Account">
                                <%}%>
                            </form>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="giveusermoney">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <font class="mediumfont">Give User Money</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to give:</font>
                                <br/>
                                <%=Textbox.getHtml("amt", String.valueOf(sysadminUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <%=Dropdown.getHtml("fundstype", String.valueOf(sysadminUserDetail.getFundstype()), StaticVariables.getFundsTypes(), "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <font class="tinyfont">(user will see)</font>
                                <br/>
                                <%=Textbox.getHtml("reason", sysadminUserDetail.getReason(), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Give User Money">
                            </form>
                        </div>


                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="takeusermoney">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <font class="mediumfont">Take Money from User</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to take:</font>
                                <br/>
                                <%=Textbox.getHtml("amt", String.valueOf(sysadminUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <%=Dropdown.getHtml("fundstype", String.valueOf(sysadminUserDetail.getFundstype()), StaticVariables.getFundsTypes(), "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <font class="tinyfont">(user will see)</font>
                                <br/>
                                <%=Textbox.getHtml("reason", sysadminUserDetail.getReason(), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Take User Money">
                            </form>
                        </div>


                   
                    </td>
                </tr>
            </table>



        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Account Balance (Internal Account Money Movement)</font>
            <br/>
            <a href="/sysadmin/userdetail.jsp?userid=<%=sysadminUserDetail.getUser().getUserid()%>&onlyshownegativeamountbalance=1"><font class="tinyfont">Only Show Negative Amts</font></a>
            <br/>
            <%if (sysadminUserDetail.getBalances()==null || sysadminUserDetail.getBalances().size()==0){%>
                <font class="normalfont">There are not yet any balance updates.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Id", "<$balanceid$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
                    cols.add(new GridCol("Description", "<$description$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Funds", "<$fundstype$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Amount", "<$amt$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Balance", "<$currentbalance$>", true, "", "tinyfont"));
                %>
                <%=Grid.render(sysadminUserDetail.getBalances(), cols, 50, "/sysadmin/userdetail.jsp?userid="+sysadminUserDetail.getUser().getUserid()+"&onlyshownegativeamountbalance="+request.getParameter("onlyshownegativeamountbalance"), "pagetransactions")%>
            <%}%>
        </div>


        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Account Transactions (Real World Money Movement)</font>
            <br/>
            <a href="/sysadmin/userdetail.jsp?userid=<%=sysadminUserDetail.getUser().getUserid()%>&onlyshowsuccessfultransactions=1"><font class="tinyfont">Only Show Successful Transactions</font></a>
            <br/>
            <%if (sysadminUserDetail.getTransactions()==null || sysadminUserDetail.getTransactions().size()==0){%>
                <font class="normalfont">There are not yet any financial transactions.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Id", "<$balancetransactionid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
                    cols.add(new GridCol("Successful?", "<$issuccessful$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Desc", "<$description$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Notes", "<$notes$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Amount", "<$amt$>", false, "", "tinyfont"));
                %>
                <%=Grid.render(sysadminUserDetail.getTransactions(), cols, 50, "/sysadmin/userdetail.jsp?userid="+sysadminUserDetail.getUser().getUserid()+"&onlyshowsuccessfultransactions="+request.getParameter("onlyshowsuccessfultransactions"), "pagetransactions")%>
            <%}%>
        </div>







<%@ include file="/template/footer.jsp" %>


