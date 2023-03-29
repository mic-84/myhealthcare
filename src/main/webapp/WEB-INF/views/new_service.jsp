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
    <c:if test="${user.isAdmin()}">
        <div class="subtitle">New service</div><br/>
        <div class="message">${message}</div>
        <div align="center">
            <form action="newService" method="post">
                <input type="hidden" id="userId" name="userId" value=${user.id}>
                <table>
                    <tr><td>code</td><td><input id="code" name="code"></td></tr>
                    <tr><td>name</td><td><input id="name" name="name"></td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="SAVE" class="button"></td></tr>
                </table>
            </form>
        </div>
    </c:if>
</body>
</html>