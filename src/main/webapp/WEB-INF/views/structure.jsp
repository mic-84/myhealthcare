<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>MyHealthCare</title>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="subtitle">${structure.name}</div><br/>
    <div class="message">${message}</div>
    <div align="right">
        <table>
            <c:if test="${not empty user}">
                <tr><td></td><td>
                    <form action="newBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="submit" value="NEW BOOKING" class="button">
                    </form>
                </td></tr>
                <tr><td></td><td>
                    <form action="toStructureReviews" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="submit" value="REVIEWS LIST" class="button">
                    </form>
                </td></tr>
                <c:if test="${isEmployee}">
                    <tr><td></td><td>
                        <form action="toServicesManagement" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="SERVICES MANAGEMENT" class="button">
                        </form>
                    </td></tr>
                    <tr><td></td><td>
                        <form action="toStructureBookings" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="BOOKING LIST" class="button">
                        </form>
                    </td></tr>
                    <tr><td></td><td>
                        <form action="bookingsPerService" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="BOOKINGS PER SERVICE" class="button">
                        </form>
                    </td></tr>
                    <tr>
                        <form action="bookingsPerMonth" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <td><select id="year" name="year">
                                <c:forEach items="${years}" var="elem">
                                    <option value=${elem}>${elem}</option>
                                </c:forEach>
                            </select></td>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <td><input type="submit" value="BOOKINGS PER MONTH" class="button"></td>
                        </form>
                    </tr>
                    <tr><td></td><td>
                        <form action="bookingsPerUser" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="BOOKINGS PER USER" class="button">
                        </form>
                    </td></tr>
                </c:if>
            </c:if>
        </table>
    </div>
    <table>
        <tr><th class="th">City</th><td>${structure.city}</td></tr>
        <tr><th class="th">Code</th><td>${structure.aslCode}-${structure.structureCode}</td></tr>
        <tr><th class="th">Services</th><td>${structure.getNumberOfActiveServices()}</td></tr>
        <tr><th class="th">Review Average</th><td>${average}</td></tr>
    </table>
    <br/>
    <table>
        <tr><th class="th">Reviews</th></tr>
        <c:forEach items="${reviews}" var="review">
            <tr>
                <th class="th">${review.getGraphic()}</th>
                <td>${review.getCount()}</td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <table>
        <tr><th class="th">Available services</th></tr>
        <c:forEach items="${structure.getActiveServices()}" var="elem">
            <tr class="row">
                <td>${elem.code}</td>
                <td>${elem.name}</td>
                <td>&euro;&nbsp;${elem.rate}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>