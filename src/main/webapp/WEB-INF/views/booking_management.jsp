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
    <c:if test="${provenance.equals('user')}">
        <form action="toMyBookings" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
                onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
        </form><br/>
    </c:if>
    <div class="message">${message}</div>
    <div align="right">
        <table>
            <c:if test="${isEmployee}">
                <c:if test="${booking.status.equals('created')}">
                    <tr><td>
                        <form action="confirmBooking" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            booking date<input id="date" name="date" value=${booking.getStringBookingDate()}>
                            &nbsp;time&nbsp;<select id="time" name="time">
                                <c:forEach items="${hours}" var="elem">
                                    <option value=${elem}
                                        <c:if test="${elem.equals(booking.getStringBookingTime())}">
                                            selected="selected"
                                        </c:if>
                                    >${elem}</option>
                                </c:forEach>
                            </select>
                            <input type="hidden" id="operation" name="operation" value="confirm">
                            <input type="hidden" id="bookingCode" name="bookingCode" value=${booking.code}>
                            <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                            <br/><br/><input type="submit" value="CONFIRM" class="button">
                        </form>
                    </td></tr>
                </c:if>
                <c:if test="${booking.status.equals('confirmed')}">
                    <tr><td>
                        <form action="updateBooking" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="hidden" id="operation" name="operation" value="render">
                            <input type="hidden" id="bookingCode" name="bookingCode" value=${booking.code}>
                            <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                            <br/><br/><input type="submit" value="COMPLETE" class="button">
                        </form>
                    </td></tr>
                </c:if>
            </c:if>
            <c:if test="${booking.status.equals('created') || booking.status.equals('confirmed')}">
                <tr><td>
                    <form action="updateBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="hidden" id="operation" name="operation" value="cancel">
                        <input type="hidden" id="bookingCode" name="bookingCode" value=${booking.code}>
                        <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                        <br/><br/><input type="submit" value="CANCEL" class="button">
                    </form>
                </td></tr>
            </c:if>
            <c:if test="${not empty isPossibleReview}">
                <tr><td>
                    <form action="review" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="bookingCode" name="bookingCode" value=${booking.code}>
                        <input type="hidden" id="provenance" name="provenance" value=${provenance}>
                        Rating&nbsp;<select id="rating" name="rating">
                            <c:forEach items="${ratings}" var="elem">
                                <option value=${elem.key}>${elem.value}</option>
                            </c:forEach>
                        </select>
                        <br/><br/><textarea id="text" name="text"></textarea>
                        <br/><br/><input type="submit" value="CONFIRM REVIEW" class="button">
                    </form>
                </td></tr>
            </c:if>
        </table>
    </div>
    <table>
        <tr><th class="th">Code</th><td>${booking.code}</td></tr>
        <c:if test="${provenance.equals('structure')}">
            <tr><th class="th">User</th><td>${user.firstName}&nbsp;${user.lastName}</td></tr>
        </c:if>
        <tr><th class="th">Email</th><td>${user.email}</td></tr>
        <tr><th class="th">Phone number</th><td>${user.phoneNumber}</td></tr>
        <c:if test="${provenance.equals('user')}">
            <tr><th class="th">Structure</th><td>${booking.structure.name}</td></tr>
            <tr><th class="th">City</th><td>${booking.structure.city.toString()}</td></tr>
        </c:if>
        <tr><th class="th">Status</th><td>${booking.status}</td></tr>
        <tr><th class="th">Creation date</th><td>${booking.getStringCreationDate()}</td></tr>
        <tr><th class="th">Confirmation date</th><td>${booking.getStringConfirmationDate()}</td></tr>
        <tr><th class="th">Booking date</th><td>${booking.getStringCompleteBookingDate()}</td></tr>
    </table>
    <br/>
    <table>
        <tr><th class="th" colspan="3">Services checked</th></tr>
        <c:forEach items="${booking.services}" var="elem">
            <tr>
                <td><img src="/resources/img/check.png"  height="20" width="20"></td>
                <td>${elem.name}</td>
                <td class="align_right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.rate}" /></td>
            </tr>
        </c:forEach>
        <tr><td></td><td>Total</td>
            <td class="border_table"><b>&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${booking.total}" /></b></td>
        </tr>
    </table>
</body>
</html>