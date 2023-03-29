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
    <div class="subtitle">My bookings</div><br/>
    <form action="toHomePage" method="post">
        <input type="hidden" id="userId" name="userId" value=${user.id}>
        <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
            onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
    </form><br/>
    <div class="message">${message}</div>
    <div align="right">
        <form action="toSearchStructures" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="submit" value="NEW BOOKING" class="button">
        </form>
    </div>
    <c:if test="${list.size() > 0}"><table>
        <tr>
            <th class="th">booking<br/>date</th>
            <th class="th">creation<br/>date</th>
            <th class="th">confirmation<br/>date</th>
            <th class="th">status</th>
            <th class="th">structure</th>
            <th class="th">services</th>
            <th class="th">total</th>
        </tr>
        <c:forEach items="${list}" var="elem">
            <tr class="row">
                <td class="border_table">${elem.getStringCompleteBookingDate()}</td>
                <td class="border_table">${elem.getStringCreationDate()}</td>
                <td class="border_table">${elem.getStringConfirmationDate()}</td>
                <td class="border_table">${elem.status}</td>
                <td class="border_table">${elem.structure.getShortDescription()}</td>
                <td class="border_table">${elem.getNumberOfServices()}</td>
                <td class="border_table" text-align="right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.total}"/></td>
                <td class="no_hover">
                    <form action="bookingManagement" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="structureId" name="structureId" value=${structureId}>
                        <input type="hidden" id="provenance" name="provenance" value="user">
                        <input type="hidden" id="bookingCode" name="bookingCode" value=${elem.code}>
                        <input type="submit" value="DETAIL" class="small_button">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table></c:if>
</body>
</html>