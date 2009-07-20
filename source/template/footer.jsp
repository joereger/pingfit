<%@ page import="com.pingfit.pageperformance.PagePerformanceUtil" %>
<% if (Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="footer-facebook.jsp" %>
<% } else if (Pagez.getUserSession().getIstrayui()) { %>
    <%@ include file="footer-tray.jsp" %>
<% } else { %>
    <%
    String templateFName = "";
    String templateF= "";
    if (Pagez.getUserSession().getPl()!=null && Pagez.getUserSession().getPl().getWebhtmlfooter()!=null && Pagez.getUserSession().getPl().getWebhtmlfooter().length()>0){
        templateF= Pagez.getUserSession().getPl().getWebhtmlfooter();
        templateFName = "pageheader-plid-"+Pagez.getUserSession().getPl().getPlid();
    } else {
        templateF= Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/footer-pingfit.vm").toString();
        templateFName = "pagefooter-plid-default";
    }
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pagetitle", pagetitle);
    velocityContext.put("navtab", navtab);
    velocityContext.put("acl", acl);
    velocityContext.put("instancename", InstanceProperties.getInstancename());
    velocityContext.put("elapsedTime", Pagez.getElapsedTime());
    String footer = TemplateProcessor.process(templateFName, templateF, velocityContext);
    %>
    <%=footer%>
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
