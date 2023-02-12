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
        <div class="subtitle">Rendered bookings per each user</div><br/>
        <table>
            <tr>
                <th>user</th>
                <th>quantity</th>
                <th>amount</th>
            </tr>
            <c:forEach items="${list}" var="elem">
                <tr class="row">
                    <td class="border_table">${elem.user.firstName}&nbsp;${elem.user.lastName}</td>
                    <td class="border_table">${elem.count}</td>
                    <td class="border_table">&euro;&nbsp;<fmt:formatNumber pattern="###,###,###,###.00" value="${elem.cost}" /></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>
</html>