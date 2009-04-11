<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.pingfit.htmluibeans.SysadminUserList" %>
<%@ page import="com.pingfit.dbgrid.GridCol" %>
<%@ page import="com.pingfit.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.pingfit.htmlui.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pingfit.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.pingfit.util.Num" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.pingfit.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.pingfit.helpers.ExercisePropertyValues" %>
<%@ page import="com.pingfit.exerciseattributes.*" %>
<%@ page import="com.pingfit.finders.FindExercises" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="com.pingfit.dao.*" %>
<%@ page import="com.pingfit.exerciseattributes.Musclegroup" %>
<%@ page import="com.pingfit.exerciseattributes.Equipment" %>
<%@ page import="com.pingfit.exerciseattributes.Genre" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exercise List Detail";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Exerciselist exerciselist = new Exerciselist();
    exerciselist.setIssystem(true);
    exerciselist.setIspublic(true);
    exerciselist.setIsautoadvance(false);
    exerciselist.setIssystemdefault(false);
    exerciselist.setUseridofcreator(Pagez.getUserSession().getUser().getUserid());
    if (request.getParameter("exerciselistid") != null && !request.getParameter("exerciselistid").equals("0") && Num.isinteger(request.getParameter("exerciselistid"))) {
        exerciselist = Exerciselist.get(Integer.parseInt(request.getParameter("exerciselistid")));
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
            boolean redirectToList = true;
            if (exerciselist.getExerciselistid()==0){
                redirectToList = false;
            }
            exerciselist.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            exerciselist.setDescription(Textarea.getValueFromRequest("description", "Description", true));
            exerciselist.setIssystemdefault(CheckboxBoolean.getValueFromRequest("issystemdefault"));
            exerciselist.setIsautoadvance(CheckboxBoolean.getValueFromRequest("isautoadvance"));
            exerciselist.save();
            //If chosen as system default, make sure all others are turned off
            if (exerciselist.getIssystemdefault()){
                List<Exerciselist> systemlists = HibernateUtil.getSession().createCriteria(Exerciselist.class)
                                                   .add(Restrictions.ne("exerciselistid", exerciselist.getExerciselistid()))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Exerciselist> exerciselistIterator=systemlists.iterator(); exerciselistIterator.hasNext();) {
                    Exerciselist exl=exerciselistIterator.next();
                    exl.setIssystemdefault(false);
                    exl.save();
                }
            }
            Pagez.getUserSession().setMessage("Exercise list saved.");
            if (redirectToList){
                Pagez.sendRedirect("/sysadmin/exerciselistlist.jsp");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        } catch (Exception ex){
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addexercisetolist")) {
        try {
            int currentmaxnum = NumFromUniqueResult.getInt("select max(num) from Exerciselistitem where exerciselistid='"+exerciselist.getExerciselistid()+"'");
            Exerciselistitem eli = new Exerciselistitem();
            eli.setExerciselistid(exerciselist.getExerciselistid());
            eli.setTimeinseconds(Dropdown.getIntFromRequest("timeinseconds", "Time", true));
            eli.setExerciseid(Integer.parseInt(request.getParameter("exerciseid")));
            eli.setReps(Textbox.getIntFromRequest("reps", "Reps", true, DatatypeInteger.DATATYPEID));
            eli.setNum(currentmaxnum+1);
            eli.save();
            exerciselist.getExerciselistitems().add(eli);
            exerciselist.save();
            Pagez.getUserSession().setMessage("Exercise added.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        } catch (Exception ex) {
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deleteexerciselistitem")) {
        try {
            Exerciselistitem elitodelete = Exerciselistitem.get(Integer.parseInt(request.getParameter("exerciselistitemid")));
            for (Iterator<Exerciselistitem> iterator = exerciselist.getExerciselistitems().iterator(); iterator.hasNext();){
                Exerciselistitem exerciselistitem =  iterator.next();
                if (exerciselistitem.getExerciselistitemid()==elitodelete.getExerciselistitemid()){
                    iterator.remove();
                }
                if (exerciselistitem.getNum()>elitodelete.getNum()){
                    exerciselistitem.setNum(exerciselistitem.getNum()-1);
                    exerciselistitem.save();
                }
            }
            //elitodelete.delete();
            exerciselist.save();
            Pagez.getUserSession().setMessage("Exercise deleted.");
        } catch (Exception ex) {
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%
    ArrayList<Exercise> exercisesToAdd = new ArrayList<Exercise>();
    if (request.getParameter("action")!=null && (request.getParameter("action").equals("findexercisestoadd") || request.getParameter("action").equals("addexercisetolist"))) {
        try {
            int genreid = 0;
            if (Num.isinteger(request.getParameter("genreid"))){
                genreid = Integer.parseInt(request.getParameter("genreid"));
            }
            int musclegroupid = 0;
            if (Num.isinteger(request.getParameter("musclegroupid"))){
                musclegroupid = Integer.parseInt(request.getParameter("musclegroupid"));
            }
            int equipmentid = 0;
            if (Num.isinteger(request.getParameter("equipmentid"))){
                equipmentid = Integer.parseInt(request.getParameter("equipmentid"));
            }
            exercisesToAdd = FindExercises.find(genreid, musclegroupid, equipmentid, true, 0);
        } catch (Exception ex) {
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error... stuff may be smoking!");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<table cellpadding="3" cellspacing="0" border="0">

    <tr>
        <td valign="top">

            <form action="/sysadmin/exerciselistdetail.jsp" method="post">
                <input type="hidden" name="dpage" value="/sysadmin/exerciselistdetail.jsp">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="exerciselistid" value="<%=exerciselist.getExerciselistid()%>">

                <table cellpadding="3" cellspacing="0" border="0">

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">List Title</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("title", exerciselist.getTitle(), 255, 20, "", "")%>
                        </td>
                    </tr>


                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <%=CheckboxBoolean.getHtml("issystemdefault", exerciselist.getIssystemdefault(), "", "")%>
                            <font class="formfieldnamefont">System Default List?</font><br>
                            <font class="tinyfont">(only one allowed in system)</font>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <%=CheckboxBoolean.getHtml("isautoadvance", exerciselist.getIsautoadvance(), "", "")%>
                            <font class="formfieldnamefont">Auto Advance?</font><br>
                            <font class="tinyfont">(user doesn't get Do Exercise button)</font>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Description</font>
                        </td>
                        <td valign="top">
                            <%=Textarea.getHtml("description", exerciselist.getDescription(), 10, 25, "", "")%>
                        </td>
                    </tr>



                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <br/><br/>
                            <input type="submit" class="formsubmitbutton" value="Save Exercise List">
                            <br/><br/>
                            <a href="/sysadmin/exerciselistdelete.jsp?exerciselistid=<%=exerciselist.getExerciselistid()%>">Delete This List</a>
                        </td>
                    </tr>

                </table>

            </form>
            <%if (exerciselist.getExerciselistid()>0){%>
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                <font class="smallfont" style="font-weight: bold;">Exercises in this list:</font>
                <br/><br/>
                <%
                if (exerciselist.getExerciselistitems()==null || exerciselist.getExerciselistitems().size()==0){
                    %>No exercises in this list.<%
                } else {
                    for (Iterator<Exerciselistitem> iterator = exerciselist.getExerciselistitems().iterator(); iterator.hasNext();){
                        Exerciselistitem exerciselistitem =  iterator.next();
                        Exercise exercise = Exercise.get(exerciselistitem.getExerciseid());
                        %>
                        <font class="tinyfont">(Order:<%=exerciselistitem.getNum()%>) <%=exercise.getTitle()%> x <%=exerciselistitem.getReps()%></font>
                        <font class="tinyfont"><a href="/sysadmin/exerciselistdetail.jsp?action=deleteexerciselistitem&exerciselistid=<%=exerciselist.getExerciselistid()%>&exerciselistitemid=<%=exerciselistitem.getExerciselistitemid()%>">Delete</a></font>
                        <br/>
                        <%
                    }
                }
                %>
            </div>
            <%}%>
        </td>
        <td valign="top">
            <%if (exerciselist.getExerciselistid()>0){%>
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                    <form action="/sysadmin/exerciselistdetail.jsp" method="post">
                        <input type="hidden" name="dpage" value="/sysadmin/exerciselistdetail.jsp">
                        <input type="hidden" name="action" value="findexercisestoadd">
                        <input type="hidden" name="exerciselistid" value="<%=exerciselist.getExerciselistid()%>">
                        <select name="genreid">
                            <option value="0">All Genres</option>
                        <%
                            for (Iterator<Genre> iterator = genreObjs.iterator(); iterator.hasNext();) {
                                Genre genre = iterator.next();
                                String sel = "";
                                if (request.getParameter("genreid")!=null && request.getParameter("genreid").equals(String.valueOf(genre.getGenreid()))){
                                    sel = " selected ";
                                }
                                %><option value="<%=genre.getGenreid()%>" <%=sel%>><%=genre.getName()%></option><%
                            }
                        %>
                        </select><br/>
                        <select name="musclegroupid">
                            <option value="0">All Muscle Groups</option>
                        <%
                            for (Iterator<Musclegroup> iterator = musclegroupObjs.iterator(); iterator.hasNext();) {
                                Musclegroup musclegroup = iterator.next();
                                String sel = "";
                                if (request.getParameter("musclegroupid")!=null && request.getParameter("musclegroupid").equals(String.valueOf(musclegroup.getMusclegroupid()))){
                                    sel = " selected ";
                                }
                                %><option value="<%=musclegroup.getMusclegroupid()%>" <%=sel%>><%=musclegroup.getName()%></option><%
                            }
                        %>
                        </select><br/>
                        <select name="equipmentid">
                            <option value="0">All Equipment</option>
                        <%
                            for (Iterator<Equipment> iterator = equipmentObjs.iterator(); iterator.hasNext();) {
                                Equipment equipment = iterator.next();
                                String sel = "";
                                if (request.getParameter("equipmentid")!=null && request.getParameter("equipmentid").equals(String.valueOf(equipment.getEquipmentid()))){
                                    sel = " selected ";
                                }
                                %><option value="<%=equipment.getEquipmentid()%>" <%=sel%>><%=equipment.getName()%></option><%
                            }
                        %>
                        </select><br/>
                        <input type="submit" class="formsubmitbutton" value="Find Exercises to Add">
                    </form>
                </div>
                <%if (exercisesToAdd!=null && exercisesToAdd.size()>0){%>
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                    <table cellpadding="2" cellspacing="0" border="0">
                    <%
                        for (Iterator<Exercise> exerciseIterator=exercisesToAdd.iterator(); exerciseIterator.hasNext();) {
                            Exercise exercise=exerciseIterator.next();
                    %>
                        <tr>
                            <form action="/sysadmin/exerciselistdetail.jsp" method="post">
                            <input type="hidden" name="dpage" value="/sysadmin/exerciselistdetail.jsp">
                            <input type="hidden" name="action" value="addexercisetolist">
                            <input type="hidden" name="genreid" value="<%=request.getParameter("genreid")%>">
                            <input type="hidden" name="musclegroupid" value="<%=request.getParameter("musclegroupid")%>">
                            <input type="hidden" name="equipmentid" value="<%=request.getParameter("equipmentid")%>">
                            <input type="hidden" name="exerciselistid" value="<%=exerciselist.getExerciselistid()%>">
                            <input type="hidden" name="exerciseid" value="<%=exercise.getExerciseid()%>">
                            <td valign="top">
                                <font class="normalfont" style="font-weight: bold;"><%=exercise.getTitle()%></font>
                            </td>
                            <td valign="top">
                                <%=Textbox.getHtml("reps", "10", 255, 2, "", "")%>
                            </td>
                            <td valign="middle">
                                Reps
                            </td>
                            <td valign="top">
                                <%
                                    TreeMap<String, String> options = new TreeMap<String, String>();
                                    options.put("60", "1");
                                    options.put("120", "2");
                                    options.put("180", "3");
                                    options.put("240", "4");
                                    options.put("300", "5");
                                    options.put("600", "10");
                                    options.put("900", "15");
                                    options.put("1200", "20");
                                    options.put("2400", "40");
                                %>
                                <%=Dropdown.getHtml("timeinseconds", "1200", options, "", "")%>
                            </td>
                            <td valign="middle">
                                Mins
                            </td>
                            <td valign="top">
                                <input type="submit" class="formsubmitbutton" value="Add">
                            </td>
                            </form>
                        </tr>
                    <%}%>
                    </table>
                <%}%>
            </div>
            <%}%>
        </td>

    </tr>
</table>



<%@ include file="/template/footer.jsp" %>



