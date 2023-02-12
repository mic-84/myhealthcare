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
        <div class="subtitle">Bookings</div><br/>
        <div class="message">${message}</div>
        <c:if test="${list.size() > 0}"><table>
            <tr>
                <th text-align="center">code</th>
                <th text-align="center">booking<br/>date</th>
                <th text-align="center">creation<br/>date</th>
                <th text-align="center">confirmation<br/>date</th>
                <th text-align="center">status</th>
                <th text-align="center">user</th>
                <th text-align="center">services</th>
                <th text-align="center">total</th>
            </tr>
            <c:forEach items="${list}" var="elem">
                <tr class="row">
                    <td class="border_table">${elem.id}</td>
                    <td class="border_table">${elem.getStringBookingDate()}</td>
                    <td class="border_table">${elem.getStringCreationDate()}</td>
                    <td class="border_table">${elem.getStringConfirmationDate()}</td>
                    <td class="border_table">${elem.status.description}</td>
                    <td class="border_table">${elem.user.firstName}&nbsp;${elem.user.lastName}</td>
                    <td class="border_table">${elem.getNumberOfServices()}</td>
                    <td class="border_table" text-align="right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.total}"/></td>
                    <td>
                        <form action="bookingManagement" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="provenance" name="provenance" value="structure">
                            <input type="hidden" id="bookingId" name="bookingId" value=${elem.id}>
                            <input type="submit" value="DETAIL" class="small_button">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table></c:if>
    </c:if>
</body>
</html>