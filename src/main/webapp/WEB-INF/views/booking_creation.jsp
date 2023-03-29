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
    <form action="getStructure" method="post">
        <input type="hidden" id="userId" name="userId" value=${user.id}>
        <input type="hidden" id="id" name="id" value=${structure.id}>
        <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
            onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
    </form><br/>
    <div class="message">${message}</div>
    <div align="right">
        <table>
            <tr><td>
                <form action="createBooking" method="post">
                    <input type="hidden" id="userId" name="userId" value=${user.id}>
                    booking date<input id="date" name="date" placeholder="YYYY-MM-DD">
                    &nbsp;time&nbsp;<select id="time" name="time">
                        <c:forEach items="${hours}" var="elem">
                            <option value=${elem}>${elem}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                    <input type="hidden" id="filter" name="filter" value=${filter}>
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
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="hidden" id="serviceId" name="serviceId" value=${elem.id}>
                        <input type="hidden" id="filter" name="filter" value=${filter}>
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
    </c:if>
    <br/>
    <div align="center">
        <form action="filterServices" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
            <input type="text" size="50" id="filter" name="filter" value=${filter}>
            <input type="submit" value="SEARCH SERVICES" class="button">
        </form>
        <br/>
        <form action="filterServices" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
            <input type="submit" value="ALL SERVICES" class="button">
        </form>
    </div>
    <br/>
    <table>
        <tr><th class="th">Available services</th></tr>
        <c:forEach items="${structure_services}" var="elem">
            <tr class="row">
                <td>${elem.name}</td>
                <td>&euro;&nbsp;<fmt:formatNumber pattern="###,###.00" value="${elem.rate}" /></td>
                <td class="no_hover">
                    <form action="addServiceToBooking" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                        <input type="hidden" id="serviceId" name="serviceId" value=${elem.id}>
                        <input type="hidden" id="filter" name="filter" value=${filter}>
                        <input type="submit" value="ADD TO BOOKING" class="small_button">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>