<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="header-pingFit.jsp" %>
<% } else { %>
    <%@ include file="header-facebook.jsp" %>
<% }%>