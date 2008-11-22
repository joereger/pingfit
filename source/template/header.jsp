<% if (Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="header-facebook.jsp" %>
<% } else if (Pagez.getUserSession().getIstrayui()) { %>
    <%@ include file="header-tray.jsp" %>
<% } else { %>
    <%@ include file="header-pingFit.jsp" %>
<% }%>