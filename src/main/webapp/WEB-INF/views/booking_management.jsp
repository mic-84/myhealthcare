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
    <div class="subtitle">Booking</div><br/><br/>
    <div class="message">${message}</div>
    <div align="right">
        <table>
            <c:if test="${isEmployee}">
                <c:if test="${booking.status.description.equals('created')}">
                    <tr><td>
                        <form action="updateBooking" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            booking date<input id="date" name="date" value=${booking.getStringBookingDate()}>
                            <input type="hidden" id="operation" name="operation" value="confirm">
                            <input type="hidden" id="bookingId" name="bookingId" value=${booking.id}>
                            <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                            <br/><br/><input type="submit" value="CONFIRM" class="button">
                        </form>
                    </td></tr>
                </c:if>
                <c:if test="${booking.status.description.equals('confirmed')}">
                    <tr><td>
                        <form action="updateBooking" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="operation" name="operation" value="render">
                            <input type="hidden" id="bookingId" name="bookingId" value=${booking.id}>
                            <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                            <br/><br/><input type="submit" value="COMPLETE" class="button">
                        </form>
                    </td></tr>
                </c:if>
            </c:if>
            <c:if test="${booking.status.description.equals('created') || booking.status.description.equals('confirmed')}">
                <tr><td>
                    <form action="updateBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="operation" name="operation" value="cancel">
                        <input type="hidden" id="bookingId" name="bookingId" value=${booking.id}>
                        <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                        <br/><br/><input type="submit" value="CANCEL" class="button">
                    </form>
                </td></tr>
            </c:if>
            <c:if test="${not empty isPossibleReview}">
                <tr><td>
                    <form action="review" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="bookingId" name="bookingId" value=${booking.id}>
                        <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                        Rating&nbsp;<select id="rating" name="rating">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                        <br/><br/><textarea id="text" name="text"></textarea>
                        <br/><br/><input type="submit" value="CONFIRM REVIEW" class="button">
                    </form>
                </td></tr>
            </c:if>
        </table>
    </div>
    <table>
        <tr><th class="th">Code</th><td>${booking.id}</td></tr>
        <tr><th class="th">User</th><td>${booking.user.firstName}&nbsp;${booking.user.lastName}</td></tr>
        <tr><th class="th">Email</th><td>${booking.user.email}</td></tr>
        <tr><th class="th">Phone number</th><td>${booking.user.phoneNumber}</td></tr>
        <tr><th class="th">Structure</th><td>${booking.structure.name}</td></tr>
        <tr><th class="th">City</th><td>${booking.structure.city}</td></tr>
        <tr><th class="th">Status</th><td>${booking.status.description}</td></tr>
        <tr><th class="th">Creation date</th><td>${booking.getStringCreationDate()}</td></tr>
        <tr><th class="th">Confirmation date</th><td>${booking.getStringConfirmationDate()}</td></tr>
        <tr><th class="th">Booking date</th><td>${booking.getStringBookingDate()}</td></tr>
        <tr><th class="th">Services</th><td>${booking.getNumberOfServices()}</td></tr>
        <tr><th class="th">Total</th>
            <td text-align="right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${booking.total}" /></td>
        </tr>
    </table>
    <br/>
    <table>
        <tr><th class="th" colspan="3">Selected services</th></tr>
        <c:forEach items="${booking.services}" var="elem">
            <tr>
                <td><img src="/resources/img/check.png"  height="20" width="20"></td>
                <td>${elem.name}</td>
                <td text-align="right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.rate}" /></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>