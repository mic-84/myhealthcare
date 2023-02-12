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
    <div class="subtitle">Change password</div><br/>
    <div class="message">${message}</div>
    <div align="center">
        <form action="changePassword" method="post" onsubmit="return validateChangePassword()">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
            <table>
                <tr><td>current password</td><td><input type="password" id="oldPassword" name="oldPassword"></td></tr>
                <tr><td>new password</td><td><input type="password" id="newPassword" name="newPassword"></td></tr>
                <tr><td colspan="2" align="center"><input type="submit" value="SAVE" class="button"></td></tr>
            </table>
        </form>
    </div>
</body>
</html>