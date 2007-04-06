 <%--
  ** status bar
  ** @author Matthias L. Jugel
  ** @version $Id: statusbar.jsp 1606 2004-05-17 10:56:18Z leo $
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<fmt:setBundle basename="i18n.setup" scope="page" />

<c:set var="percentage" value="${param.statusCurrent * 100 / param.statusMax}" scope="request"/>
<fmt:message key="${param.statusMessage}">
  <fmt:param value="${percentage}"/>
</fmt:message><br/>
<table class="statusbar">
  <tr>
    <c:forEach begin="0" end="99" step="10" var="completed" >
      <td <c:if test="${percentage > completed}">class="completed"</c:if>></td>
    </c:forEach>
  </tr>
</table>