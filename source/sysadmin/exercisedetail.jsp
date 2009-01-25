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
String pagetitle = "Exercise";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Exercise exercise = new Exercise();
    exercise.setReps(20);
    exercise.setImage("");
    exercise.setIssystem(true);
    exercise.setIspublic(true);
    exercise.setImagecredit("");
    exercise.setUseridofcreator(Pagez.getUserSession().getUser().getUserid());
    if (request.getParameter("exerciseid") != null && !request.getParameter("exerciseid").equals("0") && Num.isinteger(request.getParameter("exerciseid"))) {
        exercise = Exercise.get(Integer.parseInt(request.getParameter("exerciseid")));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            exercise.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            exercise.setImage(Textbox.getValueFromRequest("image", "Image", false, DatatypeString.DATATYPEID));
            exercise.setImagecredit(Textbox.getValueFromRequest("imagecredit", "Image Credit", false, DatatypeString.DATATYPEID));
            exercise.setReps(Textbox.getIntFromRequest("reps", "Reps", true, DatatypeInteger.DATATYPEID));
            exercise.setDescription(Textarea.getValueFromRequest("description", "Description", true));
            exercise.save();
            Pagez.getUserSession().setMessage("Exercise saved.");
            //Pagez.sendRedirect("/sysadmin/exerciselist.jsp");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    

        <form action="/sysadmin/exercisedetail.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/exercisedetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">

            <table cellpadding="3" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Title</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("title", exercise.getTitle(), 255, 50, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Reps</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("reps", String.valueOf(exercise.getReps()), 255, 5, "", "")%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Description</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("description", exercise.getDescription(), 10, 70, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Image</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("image", exercise.getImage(), 255, 50, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Image Credit</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("imagecredit", exercise.getImagecredit(), 255, 50, "", "")%>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Save">
                        <br/><br/>
                        <a href="/sysadmin/exercisedelete.jsp?exerciseid=<%=exercise.getExerciseid()%>">Delete This Exercise</a>
                    </td>
                </tr>

                <tr>
                    <td valign="top" colspan="2">
                        <%if (exercise.getImage()!=null && !exercise.getImage().equals("")){%>
                            <center>
                            <div style="padding: 10px; margin: 5px; background: #e6e6e6;">
                                <img src="/images/exercises/<%=exercise.getImage()%>" border="0">
                            </div>
                            </center>
                        <%}%>
                    </td>
                </tr>




            </table>

    </form>





<%@ include file="/template/footer.jsp" %>



