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
    <div class="message">${message}</div>
    <c:if test="${empty user}">
        <div align="center">
            <form action="login" method="post" onsubmit="return validateLogin()">
                <table>
                    <tr><td>username</td><td><input id="username" name="username"></td></tr>
                    <tr><td>password</td><td><input type="password" id="password" name="password"></td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="LOGIN" class="button"></td></tr>
                </table>
            </form>
        </div>
    </c:if>
    <c:if test="${not empty user}">
        <div align="center">
            <form action="toSearchStructures" method="post">
                <input type="hidden" id="userId" name="userId" value=${user.id}>
                <input type="submit" value="SEARCH STRUCTURES" class="button">
            </form>
        </div>
        <br/><div align="center">
            <form action="toMyBookings" method="post">
                <input type="hidden" id="userId" name="userId" value=${user.id}>
                <input type="submit" value="MY BOOKINGS" class="button">
            </form>
        </div>
        <br/><div align="center">
            <form action="toMyReviews" method="post">
                <input type="hidden" id="userId" name="userId" value=${user.id}>
                <input type="submit" value="MY REVIEWS" class="button">
            </form>
        </div>
    </c:if>
    <br/>
    <jsp:include page="admin_home.jsp" />
    <jsp:include page="employee_home.jsp" />
</body>
</html>