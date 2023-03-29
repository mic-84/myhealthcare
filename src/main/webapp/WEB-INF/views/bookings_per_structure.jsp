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
    <div class="subtitle">Rendered bookings per each structure</div><br/>
    <table>
        <tbody class="fixed_table scrollable_tbody">
            <tr>
                <th class="th">structure</th>
                <c:if test="${not empty bookingsPerStructure}">
                    <th class="th">quantity</th>
                </c:if>
                <th class="th">amount</th>
            </tr>
            <c:forEach items="${list}" var="elem">
                <tr class="row">
                    <td class="border_table">${elem.description}</td>
                    <c:if test="${not empty bookingsPerStructure}">
                        <td class="border_table align_right">${elem.count}</td>
                    </c:if>
                    <td class="border_table align_right">&euro;&nbsp;<fmt:formatNumber pattern="###,###,###,###.00" value="${elem.cost}" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>