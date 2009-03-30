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
<%@ page import="com.pingfit.dao.Musclegroup" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dao.Equipment" %>
<%@ page import="com.pingfit.dao.Genre" %>
<%@ page import="com.pingfit.helpers.ExercisePropertyValues" %>
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
    ArrayList<String> musclegroups = ExercisePropertyValues.getMusclegroupsAsStrings();
    List<Musclegroup> musclegroupObjs = ExercisePropertyValues.getMusclegroups();
    ArrayList<String> equipments = ExercisePropertyValues.getEquipmentsAsStrings();
    List<Equipment> equipmentObjs= ExercisePropertyValues.getEquipments();
    ArrayList<String> genres = ExercisePropertyValues.getGenresAsStrings();
    List<Genre> genreObjs = ExercisePropertyValues.getGenres();
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            exercise.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            exercise.setImage(Textbox.getValueFromRequest("image", "Image", false, DatatypeString.DATATYPEID));
            exercise.setImagecredit(Textbox.getValueFromRequest("imagecredit", "Image Credit", false, DatatypeString.DATATYPEID));
            exercise.setReps(Textbox.getIntFromRequest("reps", "Reps", true, DatatypeInteger.DATATYPEID));
            exercise.setDescription(Textarea.getValueFromRequest("description", "Description", true));
            exercise.getMusclegroups().removeAll(exercise.getMusclegroups());
            exercise.getEquipments().removeAll(exercise.getEquipments());
            exercise.getGenres().removeAll(exercise.getGenres());
            exercise.save();

            if (1==1){
                ArrayList<String> chosenmusclegroups = Checkboxes.getValueFromRequest("musclegroups", "Muscle Groups", false);
                for (Iterator<String> stringIterator=chosenmusclegroups.iterator(); stringIterator.hasNext();) {
                    String s=stringIterator.next();
                    for (Iterator<Musclegroup> objIt=musclegroupObjs.iterator(); objIt.hasNext();) {
                        Musclegroup musclegroup=objIt.next();
                        if (musclegroup.getName().equals(s)){
                            exercise.getMusclegroups().add(musclegroup);
                        }
                    }
                }
            }
            if (1==1){
                ArrayList<String> chosengenres = Checkboxes.getValueFromRequest("genres", "Genres", false);
                for (Iterator<String> stringIterator=chosengenres.iterator(); stringIterator.hasNext();) {
                    String s=stringIterator.next();
                    for (Iterator<Genre> objIt=genreObjs.iterator(); objIt.hasNext();) {
                        Genre genre=objIt.next();
                        if (genre.getName().equals(s)){
                            exercise.getGenres().add(genre);
                        }
                    }
                }
            }
            if (1==1){
                ArrayList<String> chosenequipments = Checkboxes.getValueFromRequest("equipments", "Equipment", false);
                for (Iterator<String> stringIterator=chosenequipments.iterator(); stringIterator.hasNext();) {
                    String s=stringIterator.next();
                    for (Iterator<Equipment> objIt=equipmentObjs.iterator(); objIt.hasNext();) {
                        Equipment equipment=objIt.next();
                        if (equipment.getName().equals(s)){
                            exercise.getEquipments().add(equipment);
                        }
                    }
                }
            }

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
<%
    ArrayList<String> selectedmusclegroups = new ArrayList<String>();
    for (Iterator<Musclegroup> exmgit=exercise.getMusclegroups().iterator(); exmgit.hasNext();) {
        Musclegroup musclegroup=exmgit.next();
        selectedmusclegroups.add(musclegroup.getName());
    }
%>
<%
    ArrayList<String> selectedequipments = new ArrayList<String>();
    for (Iterator<Equipment> exeqit=exercise.getEquipments().iterator(); exeqit.hasNext();) {
        Equipment equipment=exeqit.next();
        selectedequipments.add(equipment.getName());
    }
%>
<%
    ArrayList<String> selectedgenres = new ArrayList<String>();
    for (Iterator<Genre> exgenit=exercise.getGenres().iterator(); exgenit.hasNext();) {
        Genre genre=exgenit.next();
        selectedgenres.add(genre.getName());
    }
%>
<%

%>
<%@ include file="/template/header.jsp" %>


    

        <form action="/sysadmin/exercisedetail.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/exercisedetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">

            <table cellpadding="3" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("title", exercise.getTitle(), 100, 50, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Recommended Reps</font>
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
                        <font class="formfieldnamefont">Image/Animation</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("image", exercise.getImage(), 255, 50, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Image/Animation Credit</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("imagecredit", exercise.getImagecredit(), 255, 50, "", "")%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont"></font>
                    </td>
                    <td valign="top">
                        <table cellpadding="3" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <b>Genres</b><br/><br/>
                                    <%=Checkboxes.getHtml("genres", selectedgenres, genres, "", "")%>
                                </td>
                                <td valign="top">
                                    <b>Muscle Groups</b><br/><br/>
                                    <%=Checkboxes.getHtml("musclegroups", selectedmusclegroups, musclegroups, "", "")%>
                                </td>
                                <td valign="top">
                                    <b>Equipment Required</b><br/><br/>
                                    <%=Checkboxes.getHtml("equipments", selectedequipments, equipments, "", "")%>
                                </td>
                            </tr>
                        </table>
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



