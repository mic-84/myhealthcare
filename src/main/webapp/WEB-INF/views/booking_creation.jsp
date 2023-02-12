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
            <tr><td>
                <form action="createBooking" method="post">
                    <input type="hidden" id="userId" name="userId" value=${user.id}>
                    <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                    booking date<input id="date" name="date" placeholder="YYYY-MM-DD">
                    <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                    <br/><br/><input type="submit" value="CONFIRM" class="button">
                </form>
            </td></tr>
        </table>
    </div>
    <table>
        <tr><th class="th">Structure</th><td>${structure.name}</td></tr>
        <tr><th class="th">City</th><td>${structure.city}</td></tr>
        <tr><th class="th">Code</th><td>${structure.aslCode}-${structure.structureCode}</td></tr>
        <tr><th class="th">Services</th><td>${structure.getNumberOfActiveServices()}</td></tr>
    </table>
    <br/>
    <c:if test="${not empty services}"><table>
        <tr><th class="th" colspan="4">Selected services</th></tr>
        <c:forEach items="${services}" var="elem">
            <tr>
                <td><img src="/resources/img/check.png"  height="20" width="20"></td>
                <td>${elem.name}</td>
                <td text-align="right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.rate}" /></td>
                <td>
                    <form action="removeServiceFromBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="hidden" id="serviceId" name="serviceId" value=${elem.id}>
                        <input type="submit" value="REMOVE" class="small_button">
                    </form>
                </td>
            </tr>
        </c:forEach>
        <tr><td></td>
            <td>TOTAL</td>
            <td text-align="right">&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${total}" /></td>
        </tr>
    </table>
    <br/></c:if>
    <table>
        <tr><th class="th">Available services</th></tr>
        <c:forEach items="${structure.getActiveServices()}" var="elem">
            <tr class="row">
                <td>${elem.name}</td>
                <td>&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.rate}" /></td>
                <td>
                    <form action="addServiceToBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="hidden" id="serviceId" name="serviceId" value=${elem.id}>
                        <input type="submit" value="ADD TO BOOKING" class="small_button">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>