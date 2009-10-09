<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.SysadminUserList" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.dao.Pl" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.helpers.PlAdminHelper" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Users";
String navtab = "sysadmin";
String acl = "pladmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminUserList sysadminUserList = (SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("search")) {
        try {
            sysadminUserList.setSearchemail(Textbox.getValueFromRequest("searchemail", "Email", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchfacebookers(CheckboxBoolean.getValueFromRequest("searchfacebookers"));
            sysadminUserList.setSearchfirstname(Textbox.getValueFromRequest("searchfirstname", "Firstname", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchlastname(Textbox.getValueFromRequest("searchlastname", "Lastname", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchnickname(Textbox.getValueFromRequest("searchnickname", "Nickname", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchuserid(Textbox.getValueFromRequest("searchuserid", "Userid", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchplid(Textbox.getValueFromRequest("searchplid", "Plid", false, DatatypeString.DATATYPEID));
            sysadminUserList.initBean();
        } catch (com.pingfit.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <a href="/sysadmin/usernew.jsp"><font class="tinyfont">New User</font></a><br/><br/>

    <form action="/sysadmin/userlist.jsp" method="post">
        <input type="hidden" name="dpage" value="/sysadmin/userlist.jsp">
        <input type="hidden" name="action" value="search">
        
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="tinyfont">Private Label</font>    
                </td>
                <td valign="top">
                    <font class="tinyfont">Userid</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">First Name</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Last Name</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Nickname</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Email</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Facebook?</font>
                </td>
                <td valign="top">

                </td>
            </tr>
            <tr>
                <td valign="top">
                    <select name="searchplid">
                    <option value="0">All Private Labels</option>
                    <%
                    List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                            .addOrder(Order.asc("plid"))
                            .setCacheable(true)
                            .list();
                    if (pls!=null && pls.size()>0){
                        for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
                            Pl pl=plIterator.next();
                            //Only add pls that the logged in user can control
                            if (PlAdminHelper.canUserControlPl(Pagez.getUserSession().getUser().getUserid(), pl.getPlid())){
                                String sel = "";
                                if (String.valueOf(pl.getPlid()).equals(request.getParameter("searchplid"))){sel=" selected";}
                                %><option value="<%=pl.getPlid()%>" <%=sel%>><%=pl.getName()%></option><%
                            }
                        }
                    }%>
                    </select>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchuserrid", sysadminUserList.getSearchuserid(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchfirstname", sysadminUserList.getSearchfirstname(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchlastname", sysadminUserList.getSearchlastname(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchnickname", sysadminUserList.getSearchnickname(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchemail", sysadminUserList.getSearchemail(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("searchfacebookers", sysadminUserList.getSearchfacebookers(), "", "")%>
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Search">
                </td>
            </tr>
        </table>
    </form>

        <br/>

        <%if (sysadminUserList.getUsers()==null || sysadminUserList.getUsers().size()==0){%>
            <font class="normalfont">No users!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Userid", "<a href=\"/sysadmin/userdetail.jsp?userid=<$userid$>\"><$userid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Nickname", "<a href=\"/sysadmin/userdetail.jsp?userid=<$userid$>\"><$nickname$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Name", "<a href=\"/sysadmin/userdetail.jsp?userid=<$userid$>\"><$firstname$> <$lastname$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Email", "<$email$>", false, "", "tinyfont"));
                cols.add(new GridCol("Signup Date", "<$createdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                cols.add(new GridCol("Plid", "<a href=\"/sysadmin/privatelabeledit.jsp?plid=<$plid$>\"><$plid$></a>", false, "", "tinyfont"));
            %>
            <%=Grid.render(sysadminUserList.getUsers(), cols, 200, "/sysadmin/userlist.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



