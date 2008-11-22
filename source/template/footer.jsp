<%@ page import="com.pingfit.pageperformance.PagePerformanceUtil" %>
<% if (Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="footer-facebook.jsp" %>
<% } else if (Pagez.getUserSession().getIstrayui()) { %>
    <%@ include file="footer-tray.jsp" %>
<% } else { %>
    <%@ include file="footer-pingFit.jsp" %>
<% }%>

<%
    //Performance recording
    try {
        String prePendPageId = "";
        if (Pagez.getUserSession().getIsfacebookui()){
            prePendPageId = "[FB]";
        }
        long elapsedtimeFooter = Pagez.getElapsedTime();
        PagePerformanceUtil.add(prePendPageId+request.getServletPath(), InstanceProperties.getInstancename(), elapsedtimeFooter);
    } catch (Exception ex) {
        logger.error("", ex);
    }

%>
