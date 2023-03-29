<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>MyHealthCare</title>
</head>
<body>
    <jsp:include page="header.jsp" />
    <c:if test="${isEmployee}">
        <div class="subtitle">Rendered bookings per each month of ${year}</div><br/>
        <form action="getStructure" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="id" name="id" value=${structureId}>
            <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
                onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
        </form><br/>
        <table>
            <tr>
                <th class="th">month</code>
                <th class="th">quantity</code>
                <th class="th">amount</code>
            </tr>
            <c:forEach items="${list}" var="elem">
                <tr class="row">
                    <td class="border_table">${elem.description}</td>
                    <td class="border_table align_right">${elem.count}</td>
                    <td class="border_table align_right">&euro;&nbsp;<fmt:formatNumber pattern="###,###,###,###.00" value="${elem.cost}" /></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>
</html>