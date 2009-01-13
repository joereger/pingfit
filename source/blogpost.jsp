<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.PublicBlogPost" %>
<%@ page import="com.pingfit.util.Time" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%@ page import="com.pingfit.util.Util" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.util.RandomString" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "blog";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicBlogPost publicBlogPost = (PublicBlogPost) Pagez.getBeanMgr().get("PublicBlogPost");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addcomment")) {
        try {
            publicBlogPost.setName(Textbox.getValueFromRequest("name", "Name", false, DatatypeString.DATATYPEID));
            publicBlogPost.setUrl(Textbox.getValueFromRequest("url", "Url", false, DatatypeString.DATATYPEID));
            publicBlogPost.setComment(Textarea.getValueFromRequest("comment", "Comment", true));
            publicBlogPost.postComment();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <font class="mediumfont" style="color: #3366cc;"><%=publicBlogPost.getBlogpost().getTitle()%></font>
    <br/>
    <font class="smallfont"><%=publicBlogPost.getBlogpost().getBody()%></font>
    <br/>
    <font class="tinyfont" style="color: #cccccc;">Posted by: <%=publicBlogPost.getBlogpost().getAuthor()%> at <%=Time.dateformatcompactwithtime(Time.getCalFromDate(publicBlogPost.getBlogpost().getDate()))%></font>
    <br/><br/>

     <%if (!Pagez.getUserSession().getIsfacebookui()){%>
        <%if (publicBlogPost.getBlogpost()==null || publicBlogPost.getBlogpost().getBlogpostcomments()==null || publicBlogPost.getBlogpost().getBlogpostcomments().size()==0){%>

        <%} else {%>
            <%
                StringBuffer comment=new StringBuffer();
                comment.append("<font class=\"tinyfont\"><$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$></font>\n"+
    "        <font class=\"smallfont\">Comment by: </font>\n" +
    "        <a href=\"<$url$>\">\n" +
    "            <font class=\"smallfont\" style=\"color: #0000ff;\"><$name$></font>\n" +
    "        </a>\n" +
    "        <br/>\n" +
    "        <font class=\"tinyfont\"><$comment$></font>\n" +
    "        <br/><br/>");

                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("", comment.toString(), false, "", ""));
            %>
            <%=Grid.render(Util.setToArrayList(publicBlogPost.getBlogpost().getBlogpostcomments()), cols, 100, "/blogpost.jsp?blogpostid" + publicBlogPost.getBlogpost().getBlogpostid(), "page")%>
        <%}%>

        <br/><br/>
        <div id="disqus_thread"></div><script type="text/javascript" src="http://disqus.com/forums/pingfit/embed.js"></script><noscript><a href="http://pingfit.disqus.com/?url=ref">View the discussion thread.</a></noscript><a href="http://disqus.com" class="dsq-brlink">blog comments powered by <span class="logo-disqus">Disqus</span></a>

        <%--<form action="/blogpost.jsp" method="post">--%>
            <%--<input type="hidden" name="dpage" value="/blogpost.jsp">--%>
            <%--<input type="hidden" name="action" value="addcomment">--%>
            <%--<input type="hidden" name="blogpostid" value="<%=publicBlogPost.getBlogpost().getBlogpostid()%>">--%>
            <%--<br/><br/>--%>
            <%--<font class="formfieldnamefont">Post a comment:</font>--%>
            <%--<br/>--%>
            <%--<table cellpadding="0" cellspacing="0" border="0">--%>
                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Name</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<%=Textbox.getHtml("name", publicBlogPost.getName(), 255, 35, "", "")%>--%>
                    <%--</td>--%>
                <%--</tr>--%>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Url</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<%=Textbox.getHtml("url", publicBlogPost.getUrl(), 255, 35, "", "")%>--%>
                    <%--</td>--%>
                <%--</tr>--%>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Comment</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<%=Textarea.getHtml("comment", publicBlogPost.getComment(), 5, 40, "", "")%>--%>
                    <%--</td>--%>
                <%--</tr>--%>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Prove You're a Human</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>

                    <%--</td>--%>
                <%--</tr>--%>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<input type="submit" class="formsubmitbutton" value="Post Comment">--%>
                    <%--</td>--%>
                <%--</tr>--%>



             <%--</table>--%>

         <!--</form>-->
     <%}%>

<%@ include file="/template/footer.jsp" %>