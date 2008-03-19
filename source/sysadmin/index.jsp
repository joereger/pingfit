<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.htmluibeans.SystemStats" %>
<%@ page import="com.pingfit.util.Str" %>
<%@ page import="com.pingfit.htmluibeans.SysadminIndex" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SysAdmin Home";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminIndex sysadminIndex=(SysadminIndex) Pagez.getBeanMgr().get("SysadminIndex");
    SystemStats systemStats=(SystemStats) Pagez.getBeanMgr().get("SystemStats");
%>
<%@ include file="/template/header.jsp" %>



    

    <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
        <%=sysadminIndex.getServermemory()%>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
        <font class="normalfont"><b>Pending Balance:</b> </font>
        <font class="normalfont"><%=Str.formatForMoney(systemStats.getSystembalance())%></font>
        <br/><font class="tinyfont">The amount of accrued balance that users are holding.  A positive number means we are holding this much money for people.  But keep in mind that this also includes money for researchers who are running surveys.</font>
        <br/><br/>
        <font class="normalfont"><b>System Balance Real World:</b> </font>
        <font class="normalfont"><%=Str.formatForMoney(systemStats.getSystembalancerealworld())%></font>
        <br/><font class="tinyfont">Total real world money taken in and sent out.   A negative number means that dNeero has not collected all the money it has sent out... which shouldn't happen.</font>
        <br/><br/>
        <font class="normalfont"><b>System Overall Balance:</b> </font>
        <font class="normalfont"><%=Str.formatForMoney(systemStats.getSystembalancetotal())%></font>
        <br/><font class="tinyfont">Estimate of gross income.</font>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
        <%=sysadminIndex.getFinancialStatsHtml()%>
    </div>




<%@ include file="/template/footer.jsp" %>