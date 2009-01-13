<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Help";
String navtab = "help";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <table cellpadding="5" border="0" cellpadding="0">
        <tr>
            <td valign="top"><iframe id="fdbk_iframe_inline" allowTransparency="true" width="100%" height="500" scrolling="no" frameborder="0" src="http://getsatisfaction.com/pingfit/feedback/topics/new?display=inline&amp;style=idea"></iframe></td>
            <td valign="top" width="25%">

                <style type="text/css" media="all">  div#gsfn_search_widget img { border: none; }
                  div#gsfn_search_widget { font-size: 12px; width: 280px; border: 6px solid #DDD; padding: 10px;}
                  div#gsfn_search_widget a.widget_title { color: #000; display: block; margin-bottom: 10px; font-weight: bold; }
                  div#gsfn_search_widget .powered_by { margin-top: 8px; padding-top: 8px; border-top: 1px solid #DDD; }
                  div#gsfn_search_widget .powered_by a { color: #333; font-size: 90%; }
                  div#gsfn_search_widget form { margin-bottom: 8px; }
                  div#gsfn_search_widget form label { margin-bottom: 5px; display: block; }
                  div#gsfn_search_widget form #gsfn_search_query { width: 60%; }
                  div#gsfn_search_widget div.gsfn_content { }
                  div#gsfn_search_widget div.gsfn_content li { text-align:left; margin-bottom:6px; }
                  div#gsfn_search_widget div.gsfn_content a.gsfn_link { line-height: 1; }
                  div#gsfn_search_widget div.gsfn_content span.time { font-size: 90%; padding-left: 3px; }
                  div#gsfn_search_widget div.gsfn_content p.gsfn_summary { margin-top: 2px }
                </style><div id="gsfn_search_widget">
                  <a href="http://getsatisfaction.com/pingfit" class="widget_title">People-Powered Customer Service for PingFit</a>
                  <div class="gsfn_content">
                    <form id="gsfn_search_form" action="http://getsatisfaction.com/pingfit" method="get" accept-charset="utf-8" onsubmit="gsfn_search(this); return false;">
                      <div>
                        <input type="hidden" name="limit" value="10" />
                        <input type="hidden" name="style" value="" />
                        <input type="hidden" name="utm_medium" value="widget_search" />
                        <input type="hidden" name="utm_source" value="widget_pingfit" />
                        <input type="hidden" name="callback" value="gsfnResultsCallback" />
                        <input type="hidden" name="format" value="widget" />
                        <label for="gsfn_search_query" class="gsfn_label">Ask a question, share an idea, report a problem, or just talk...</label>
                        <input type="text" name="query" value="" id="gsfn_search_query" maxlength="120" />
                        <input type="submit" id="continue" value="Continue" />
                      </div>
                    </form>
                    <div id="gsfn_search_results" style="height: auto;"></div>
                  </div>
                  <div class="powered_by"><a href="http://getsatisfaction.com/"><img alt="Favicon" src="http://www.getsatisfaction.com/favicon.gif" style="vertical-align: middle;" /></a> <a href="http://getsatisfaction.com/">Get Satisfaction support network</a></div>
                </div>
                <script src="http://getsatisfaction.com/pingfit/widgets/javascripts/172dcde/widgets.js" type="text/javascript"></script>

                <br/>

                <a href="/account/accountsupportissueslist.jsp"><font class="formfieldnamefont">Ask Private Question to Our Sysadmins</font></a>
            </td>
        </tr>
    </table>





<%@ include file="/template/footer.jsp" %>
