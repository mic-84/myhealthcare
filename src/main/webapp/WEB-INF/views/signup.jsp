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
    <div class="subtitle">Account registration</div><br/>
    <div class="message">${message}</div>
    <div align="center">
        <form action="signup" method="post" onsubmit="return validateSignin()">
            <table>
                <tr><td>username</td><td><input id="username" name="username" value=${username}></td></tr>
                <tr><td>password</td><td><input type="password" id="password" name="password" value=${password}></td></tr>
                <tr><td>first name</td><td><input id="firstname" name="firstname" value=${firstname}></td></tr>
                <tr><td>last name</td><td><input id="lastname" name="lastname" value=${lastname}></td></tr>
                <tr><td>email</td><td><input id="email" name="email" value=${email}></td></tr>
                <tr><td>phone number</td><td><input id="phonenumber" name="phonenumber" value=${phonenumber}></td></tr>
                <tr><td>city</td><td><select id="city" name="city">
                    <option value=""></option>
                    <c:forEach items="${city_list}" var="elem">
                        <option value=${elem.id}>${elem.name}&nbsp;(${elem.province})</option>
                    </c:forEach>
                </select></td></tr>
                <tr><td>address</td><td><input id="address" name="address" value=${address}></td></tr>
                <tr><td>zip code</td><td><input id="zipcode" name="zipcode" value=${zipcode}></td></tr>
                <tr><td colspan="2" align="center"><input type="submit" value="CREATE ACCOUNT" class="button"></td></tr>
            </table>
        </form>
    </div>
</body>
</html>