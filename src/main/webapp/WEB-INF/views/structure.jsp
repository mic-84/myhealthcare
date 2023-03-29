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
    <c:if test="${fromSearch}">
        <form action="searchStructures" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="city" name="city" value=${city}>
            <input type="hidden" id="neighbours" name="neighbours" value=${neighbours}>
            <input type="hidden" id="service" name="service" value=${service}>
            <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
                onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
        </form><br/>
    </c:if>
    <c:if test="${!fromSearch}">
        <form action="toHomePage" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
                onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
        </form><br/>
    </c:if>
    <div class="message">${message}</div>
    <table><tr><td style="width: 20%;">
    <table>
        <tr><th class="th">City</th><td>${structure.city}</td></tr>
        <tr><th class="th">Code</th><td>${structure.aslCode}-${structure.structureCode}</td></tr>
        <tr><th class="th">Services</th><td>${structure.getNumberOfActiveServices()}</td></tr>
    </table>
    </td><td align="right" style="width: 20%;">
        <table>
            <c:if test="${not empty user}">
                <tr><td></td><td>
                    <form action="newBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="hidden" id="filter" name="filter" value=${filter}>
                        <input type="submit" value="NEW BOOKING" class="button">
                    </form>
                </td></tr>
                <tr><td></td><td>
                    <form action="toStructureReviews" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="submit" value="REVIEWS" class="button">
                    </form>
                </td></tr>
                <c:if test="${isEmployee}">
                    <tr><td></td><td>
                        <form action="toServicesManagement" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="SERVICES MANAGEMENT" class="button">
                        </form>
                    </td></tr>
                    <tr><td></td><td>
                        <form action="toStructureBookings" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="hidden" id="criteria" name="criteria" value="incomplete">
                            <input type="submit" value="INCOMPLETE BOOKINGS" class="button">
                        </form>
                    </td></tr>
                    <tr><td></td><td>
                        <form action="toStructureBookings" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="hidden" id="criteria" name="criteria" value="all">
                            <input type="submit" value="ALL BOOKINGS" class="button">
                        </form>
                    </td></tr>
                    <tr><td></td><td>
                        <form action="bookingsPerServiceLastMonth" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="SERVICES USE LAST MONTH" class="button">
                        </form>
                    </td></tr>
                    <tr>
                        <form action="bookingsPerMonth" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <td class="align_right"><select id="year" name="year">
                                <c:forEach items="${years}" var="elem">
                                    <option value=${elem}>${elem}</option>
                                </c:forEach>
                            </select></td>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <td><input type="submit" value="BOOKINGS PER MONTH" class="button"></td>
                        </form>
                    </tr>
                    <tr>
                        <form action="bookingsPerUser" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <td>threshold&nbsp;<select id="threshold" name="threshold">
                                <option value="0">0</option>
                                <option value="250"> 250</option>
                                <option value="500"> 500</option>
                                <option value="1000">1000</option>
                                <option value="2000">2000</option>
                                <option value="3000">3000</option>
                                <option value="4000">4000</option>
                                <option value="5000">5000</option>
                            </select></td>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <td><input type="submit" value="USERS MOST EXPENSIVE" class="button"></td>
                        </form>
                    </tr>
                </c:if>
            </c:if>
        </table>
    </td></tr></table>
    <br/>
    <div align="center">
        <form action="getStructure" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="id" name="id" value=${structure.id}>
            <input type="text" size="50" id="filter" name="filter" value=${filter}>
            <input type="submit" value="SEARCH SERVICES" class="button">
        </form>
        <br/>
        <form action="getStructure" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="id" name="id" value=${structure.id}>
            <input type="submit" value="ALL SERVICES" class="button">
        </form>
    </div>
    <br/>
    <div class="th">Available services</div>
    <table>
        <tbody class="fixed_table scrollable_tbody">
            <c:forEach items="${services}" var="elem">
                <tr class="row">
                    <td style="width: 10%;">${elem.code}</td>
                    <td>${elem.name}</td>
                    <td>&euro;&nbsp;${elem.rate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>