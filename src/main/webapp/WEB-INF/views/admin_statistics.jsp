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
    <div class="subtitle">Statistics</div><br/>
    <br/>
    <div class="th">Get cost and number of rendered bookings per structure...</div>
    <div align="centre">
        <form action="bookingsPerStructure" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            region&nbsp;<select id="region" name="region">
                <c:forEach items="${regions}" var="elem">
                    <option value=${elem}>${elem}</option>
                </c:forEach>
            </select>
            &nbsp;year&nbsp;<select id="year" name="year">
                <c:forEach items="${years}" var="elem">
                    <option value=${elem}>${elem}</option>
                </c:forEach>
            </select>
            &nbsp;<input type="submit" value="SHOW" class="button">
        </form>
    </div>
    <br/><br/>
    <div class="th">Get the cost of the most used structures...</div>
    <div align="centre">
        <form action="firstNStructureCost" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            region&nbsp;<select id="region" name="region">
                <c:forEach items="${regions}" var="elem">
                    <option value=${elem}>${elem}</option>
                </c:forEach>
            </select>
            &nbsp;year&nbsp;<select id="year" name="year">
                <c:forEach items="${years}" var="elem">
                    <option value=${elem}>${elem}</option>
                </c:forEach>
            </select>
            &nbsp;month&nbsp;<select id="month" name="month">
                <c:forEach items="${months}" var="elem">
                    <option value=${elem.getKey()}>${elem.getValue()}</option>
                </c:forEach>
            </select>
            &nbsp;structures to show&nbsp;<select id="limit" name="limit">
                <c:forEach items="${limits}" var="elem">
                    <option value=${elem}>${elem}</option>
                </c:forEach>
            </select>
            &nbsp;<input type="submit" value="SHOW" class="button">
        </form>
    </div>
</body>
</html>