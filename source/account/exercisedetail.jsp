<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.SysadminUserList" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="com.pingfit.dao.Exercise" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.pingfit.util.Num" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercise Detail";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Exercise exercise = new Exercise();
    exercise.setReps(20);
    exercise.setImage("");
    exercise.setIssystem(false);
    exercise.setIspublic(true);
    exercise.setUseridofcreator(Pagez.getUserSession().getUser().getUserid());
    if (request.getParameter("exerciseid") != null && !request.getParameter("exerciseid").equals("0") && Num.isinteger(request.getParameter("exerciseid"))) {
        exercise = Exercise.get(Integer.parseInt(request.getParameter("exerciseid")));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            exercise.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            //exercise.setImage(Textbox.getValueFromRequest("image", "Image", false, DatatypeString.DATATYPEID));
            exercise.setReps(Textbox.getIntFromRequest("reps", "Reps", true, DatatypeInteger.DATATYPEID));
            exercise.setDescription(Textarea.getValueFromRequest("description", "Description", true));
            exercise.save();
            Pagez.getUserSession().setMessage("Exercise saved.  Now you can add it to an Exercise List.");
            Pagez.sendRedirect("/account/exerciselist.jsp");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    

        <form action="/account/exercisedetail.jsp" method="post">
            <input type="hidden" name="dpage" value="/account/exercisedetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">

            <table cellpadding="3" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Exercise Title</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("title", exercise.getTitle(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Default Reps</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("reps", String.valueOf(exercise.getReps()), 255, 20, "", "")%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Description/Instructions</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("description", exercise.getDescription(), 10, 50, "", "")%>
                    </td>
                </tr>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Image</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<%=Textbox.getHtml("image", exercise.getImage(), 255, 20, "", "")%>--%>
                    <%--</td>--%>
                <%--</tr>--%>



                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Save Exercise">
                    </td>
                </tr>

            </table>

    </form>





<%@ include file="/template/footer.jsp" %>



