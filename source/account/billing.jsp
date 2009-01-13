<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.htmluibeans.AccountBilling" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Billing Details";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
    AccountBilling billing= (AccountBilling) Pagez.getBeanMgr().get("AccountBilling");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            billing.setCccity(Textbox.getValueFromRequest("cccity", "City", true, DatatypeString.DATATYPEID));
            billing.setCcexpmo(Dropdown.getIntFromRequest("ccexpmo", "Expiration Month", true));
            billing.setCcexpyear(Dropdown.getIntFromRequest("ccexpyear", "Expiration Year", true));
            billing.setCcnum(Textbox.getValueFromRequest("ccnum", "Credit Card Number", true, DatatypeString.DATATYPEID));
            billing.setCcstate(Textbox.getValueFromRequest("ccstate", "State", true, DatatypeString.DATATYPEID));
            billing.setCctype(Dropdown.getIntFromRequest("cctype", "Credit Card Type", true));
            billing.setCvv2(Textbox.getValueFromRequest("cvv2", "CVV2", true, DatatypeString.DATATYPEID));
            billing.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            billing.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            billing.setPostalcode(Textbox.getValueFromRequest("postalcode", "Zip", true, DatatypeString.DATATYPEID));
            billing.setStreet(Textbox.getValueFromRequest("street", "Street", true, DatatypeString.DATATYPEID));
            billing.saveAction();
            Pagez.getUserSession().setMessage("Billing information saved.");
            Pagez.sendRedirect("/researcher/index.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            Pagez.getUserSession().setMessage("There has been an error. Please try again.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <div style="padding: 5px; margin: 5px; background: #e6e6e6;">
        <div style="padding: 10px; margin: 5px; background: #ffffff;">
            <font class="normalfont">We're not charging for this service while we get it up and running. Once it is going we may charge a small monthly fee like $1 to keep the power and electricity on.</font>
        </div>
    </div>

    <form action="/account/billing.jsp" method="post">
        <input type="hidden" name="dpage" value="/account/billing.jsp">
        <input type="hidden" name="action" value="save">
           <table cellpadding="0" cellspacing="0" border="0">
               <tr>

                   <td valign="top" align="left">
                        <font class="mediumfont" style="color: #000000;">Credit Card Info</font>
                            <br/>

                            <table cellpadding="3" cellspacing="0" border="0">

                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">Name</font>
                                    <br/>
                                    <font class="tinyfont">(first then last)</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Textbox.getHtml("firstname", billing.getFirstname(), 255, 15, "", "")%>
                                    <%=Textbox.getHtml("lastname", billing.getLastname(), 255, 15, "", "")%>
                               </td>
                            </tr>

                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">Street Address</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Textbox.getHtml("street", billing.getStreet(), 255, 30, "", "")%>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">City, State, Zip</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Textbox.getHtml("cccity", billing.getCccity(), 255, 20, "", "")%>
                                    <%=Textbox.getHtml("ccstate", billing.getCcstate(), 255, 2, "", "")%>
                                    <%=Textbox.getHtml("postalcode", billing.getPostalcode(), 255, 6, "", "")%>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">Credit Card Type</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Dropdown.getHtml("cctype", String.valueOf(billing.getCctype()), billing.getCreditcardtypes(), "","")%>
                               </td>
                            </tr>

                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">Credit Card Number</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Textbox.getHtml("ccnum", billing.getCcnum(), 255, 18, "", "")%>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">Expiration Date</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Dropdown.getHtml("ccexpmo", String.valueOf(billing.getCcexpmo()), billing.getMonthsForCreditcard(), "","")%>
                                    /
                                    <%=Dropdown.getHtml("ccexpyear", String.valueOf(billing.getCcexpyear()), billing.getYearsForCreditcard(), "","")%>
                               </td>
                            </tr>



                            <tr>
                               <td valign="top" align="left">
                                    <font class="formfieldnamefont">CVV2</font>
                                    <br/>
                                    <font class="tinyfont">(three digit number on back of card)</font>
                               </td>
                               <td valign="top" align="left">
                                    <%=Textbox.getHtml("cvv2", billing.getCvv2(), 255, 3, "", "")%>
                               </td>
                            </tr>

                            </table>


                   </td>
               </tr>
           </table>

            <table cellpadding="0" cellspacing="0" border="0">
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Save Billing Details">
                </td>
                <td valign="top">
                </td>
                <td valign="top">
                </td>
            </table>


    </form>

<%@ include file="/template/footer.jsp" %>


