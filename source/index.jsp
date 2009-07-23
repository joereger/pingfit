<%@ page import="com.pingfit.htmlui.Pagez" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="com.pingfit.privatelabel.TemplateProcessor" %>
<%@ page import="com.pingfit.systemprops.WebAppRootDir" %>
<%@ page import="com.pingfit.util.Io" %>
<%@ page import="org.apache.velocity.VelocityContext" %>

    <%
    int refid = 0;
    if (request.getParameter("refid")!=null && Num.isinteger(request.getParameter("refid"))){
        refid = Integer.parseInt(request.getParameter("refid"));
        Pagez.getUserSession().setRefid(refid);
    }
    if (refid==0){
        if (Pagez.getUserSession().getRefid()>0){
            refid = Pagez.getUserSession().getRefid();
        }
    }
    %>


    <%
    if (1==1){
        String templateHomepageName= "";
        String templateHomepage= "";
        if (Pagez.getUserSession().getPl()!=null && Pagez.getUserSession().getPl().getHomepagetemplate()!=null && Pagez.getUserSession().getPl().getHomepagetemplate().length()>0){
            templateHomepage= Pagez.getUserSession().getPl().getHomepagetemplate();
            templateHomepageName= "index-plid-"+Pagez.getUserSession().getPl().getPlid();
        } else {
            templateHomepage= Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"index.vm").toString();
            templateHomepageName= "index-plid-default";
        }
        VelocityContext velocityContext = new VelocityContext();
        String homepage = TemplateProcessor.process(templateHomepageName, templateHomepage, velocityContext);
        %>
        <%=homepage%>
    <%}%>

