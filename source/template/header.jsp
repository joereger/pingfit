<%@ page import="com.pingfit.privatelabel.TemplateProcessor" %>
<%@ page import="org.apache.velocity.VelocityContext" %>
<% if (Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="header-facebook.jsp" %>
<% } else if (Pagez.getUserSession().getIstrayui()) { %>
    <%@ include file="header-tray.jsp" %>
<% } else { %>
    <%
    String templateHName = "";
    String templateH= "";
    if (Pagez.getUserSession().getPl()!=null && Pagez.getUserSession().getPl().getWebhtmlheader()!=null && Pagez.getUserSession().getPl().getWebhtmlheader().length()>0){
        templateH= Pagez.getUserSession().getPl().getWebhtmlheader();
        templateHName = "pageheader-plid-"+Pagez.getUserSession().getPl().getPlid();
    } else {
        templateH= Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/header-pingfit.vm").toString();
        templateHName = "pageheader-plid-default";
    }
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pagetitle", pagetitle);
    velocityContext.put("navtab", navtab);
    velocityContext.put("acl", acl);
    String header = TemplateProcessor.process(templateHName, templateH, velocityContext);
    %>
    <%=header%>
    <%Pagez.getUserSession().setMessage("");%>
<% }%>