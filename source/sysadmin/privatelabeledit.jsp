<%@ page import="com.pingfit.cache.providers.CacheFactory" %>
<%@ page import="com.pingfit.dao.Pl" %>
<%@ page import="com.pingfit.privatelabel.PlFinder" %>
<%@ page import="com.pingfit.privatelabel.PlVerification" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.util.Util" %>
<%@ page import="com.pingfit.util.Str" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.log4j.Logger" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Private Label";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    String defaultWebHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/header-pingfit.vm").toString();
    String defaultWebFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/footer-pingfit.vm").toString();
    String defaultEmailHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"emailtemplates/emailheader.html").toString();
    String defaultEmailFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"emailtemplates/emailfooter.html").toString();
    String defaultHomepagetemplate = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"index.vm").toString();
%>
<%
    Pl pl = new Pl();
    pl.setWebhtmlheader(defaultWebHeader);
    pl.setWebhtmlfooter(defaultWebFooter);
    pl.setIshttpson(false);
    pl.setTwitterusername("");
    pl.setTwitterpassword("");
    pl.setHomepagetemplate("");
    pl.setPeers("0");
    if (request.getParameter("plid")!=null && Num.isinteger(request.getParameter("plid"))){
        pl = Pl.get(Integer.parseInt(request.getParameter("plid")));
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("save")){
                StringBuffer peers = new StringBuffer();
                String[] peersStrArray = Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("peers", "Peers", false));
                if (peersStrArray!=null){
                    for (int i=0; i<peersStrArray.length; i++) {
                        String s=peersStrArray[i];
                        peers.append(s);
                        if (i<peersStrArray.length-1){
                            peers.append(",");
                        }
                    }
                }
                pl.setPeers(peers.toString());
                pl.setName(Textbox.getValueFromRequest("name", "Name", true, DatatypeString.DATATYPEID));
                pl.setNameforui(Textbox.getValueFromRequest("nameforui", "Name For UI", true, DatatypeString.DATATYPEID));
                pl.setCustomdomain1(Textbox.getValueFromRequest("customdomain1", "Customdomain1", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setCustomdomain2(Textbox.getValueFromRequest("customdomain2", "Customdomain2", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setCustomdomain3(Textbox.getValueFromRequest("customdomain3", "Customdomain3", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setSubdomain(Textbox.getValueFromRequest("subdomain", "subdomain", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setWebhtmlheader(Textarea.getValueFromRequest("webhtmlheader", "Web Html Header", false));
                pl.setWebhtmlfooter(Textarea.getValueFromRequest("webhtmlfooter", "Web Html Footer", false));
                pl.setEmailhtmlheader(Textarea.getValueFromRequest("emailhtmlheader", "Email Html Header", false));
                pl.setEmailhtmlfooter(Textarea.getValueFromRequest("emailhtmlfooter", "Email Html Footer", false));
                pl.setHomepagetemplate(Textarea.getValueFromRequest("homepagetemplate", "Homepage Html Template", false));
                pl.setIshttpson(CheckboxBoolean.getValueFromRequest("ishttpson"));
                pl.setTwitterusername(Textbox.getValueFromRequest("twitterusername", "Twitter Username", false, DatatypeString.DATATYPEID));
                pl.setTwitterpassword(Textbox.getValueFromRequest("twitterpassword", "Twitter Password", false, DatatypeString.DATATYPEID));
                pl.setAirlogo(Textbox.getValueFromRequest("airlogo", "Air Logo", false, DatatypeString.DATATYPEID));
                pl.setAirbgcolor(Textbox.getValueFromRequest("airbgcolor", "Air BgColor", false, DatatypeString.DATATYPEID));
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Saved!");
                    Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
                    return;
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setwebhtmlheadertodefault")){
                pl.setWebhtmlheader(defaultWebHeader);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("makedefault")){
                //Set all to not default
                List pls = HibernateUtil.getSession().createQuery("from Pl").setCacheable(true).list();
                for (Iterator iterator=pls.iterator(); iterator.hasNext();) {
                    Pl plTmp=(Pl) iterator.next();
                    plTmp.setIsdefault(false);
                    try{plTmp.save();} catch (Exception vex) {Pagez.getUserSession().setMessage(vex.toString());}
                }
                //Now update the correct one
                pl.setIsdefault(true);
                pl.save();
                CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                Pagez.getUserSession().setMessage("Done! Is default.");
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setwebhtmlfootertodefault")){
                pl.setWebhtmlfooter(defaultWebFooter);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setemailhtmlheadertodefault")){
                pl.setEmailhtmlheader(defaultEmailHeader);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("sethomepagetemplatetodefault")){
                pl.setHomepagetemplate(defaultHomepagetemplate);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setemailhtmlfootertodefault")){
                pl.setEmailhtmlfooter(defaultEmailFooter);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



        <form action="/sysadmin/privatelabeledit.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/privatelabeledit.jsp">
            <input type="hidden" name="action" value="save" id="action">
            <input type="hidden" name="plid" value="<%=pl.getPlid()%>">

            <table cellpadding="3" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("name", pl.getName(), 255, 35, "", "")%>
                        <br>
                        <font class="tinyfont"><a href="/sysadmin/editeula.jsp?plid=<%=pl.getPlid()%>">View/Edit EULA for this PL</a></font>
                        <br/>
                        <font class="tinyfont"><a href="/sysadmin/plexerciselist.jsp">Exercise List Permissions</a></font>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Default PL?</font>
                    </td>
                    <td valign="top">
                        <%if (pl.getIsdefault()){%>
                            <font class="tinyfont">This is the default pl.</font>
                        <%} else {%>
                            <font class="tinyfont">This is not the default pl. <a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlheadertodefault">(make it the default PL)</a></font>
                        <%}%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name for UI</font>
                        <br/><font class="tinyfont">Ex("dNeero")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("nameforui", pl.getNameforui(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Ishttpson?</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("ishttpson", pl.getIshttpson(), "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Subdomain</font>
                        <br/><font class="tinyfont">Ex("www", "someconame")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("subdomain", pl.getSubdomain(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain1</font>
                        <br/><font class="tinyfont">Ex("www.mypldomain.com")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain1", pl.getCustomdomain1(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain2</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain2", pl.getCustomdomain2(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain3</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain3", pl.getCustomdomain3(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Twitter Username</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("twitterusername", pl.getTwitterusername(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Twitter Password</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("twitterpassword", pl.getTwitterpassword(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Air Logo</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("airlogo", pl.getAirlogo(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Air BgColor</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("airbgcolor", pl.getAirbgcolor(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Private Label Peers</font>
                        <br/><font class="tinyfont">Selecting All will make you peered with all pls.</font>
                        <br/><font class="tinyfont"><%=pl.getPeers()%></font>
                    </td>
                    <td valign="top">
                        <%
                        String[] peersSelected = new String[0];
                        if (pl.getPeers()!=null && pl.getPeers().length()>0){
                            peersSelected = pl.getPeers().split(",");
                        }
                        %>
                        <%
                        TreeMap<String, String> plsToChooseFrom = new TreeMap<String, String>();
                        plsToChooseFrom.put("0", "All Private Labels");
                        List results = HibernateUtil.getSession().createQuery("from Pl").list();
                        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                            Pl plOption = (Pl) iterator.next();
                            plsToChooseFrom.put(String.valueOf(plOption.getPlid()), Str.truncateString(plOption.getName(), 40));
                        }
                        %>
                        <%=DropdownMultiselect.getHtml("peers", Util.stringArrayToArrayList(peersSelected), plsToChooseFrom, 6, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Web Html Header</font>
                        <br/><font class="tinyfont">Uses <a href="http://velocity.apache.org/engine/releases/velocity-1.5/vtl-reference-guide.html">VTL</a></font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlheadertodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("webhtmlheader", pl.getWebhtmlheader(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Web Html Footer</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlfootertodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("webhtmlfooter", pl.getWebhtmlfooter(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Homepage Html Template</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=sethomepagetemplatetodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("homepagetemplate", pl.getHomepagetemplate(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email Html Header</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setemailhtmlheadertodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                        <br/><font class="tinyfont">
                            totalConvoJoins,
                            totalConvoEmbeds,
                            usersWithMostConvos,
                            donationsMiniReport,
                            recentDonations,
                            recentConvos,
                            recentConvoJoins,
                            twitterAnswers,
                            recentlyPaid,
                            newestUsers,
                            blogPosts
                        </font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("emailhtmlheader", pl.getEmailhtmlheader(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email Html Footer</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setemailhtmlfootertodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("emailhtmlfooter", pl.getEmailhtmlfooter(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>






             </table>
             <input type="submit" class="formsubmitbutton" value="Save"><br/>




        </form>



<%@ include file="/template/footer.jsp" %>



