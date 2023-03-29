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
    <div class="subtitle">My account</div><br/>
    <div align="right">
        <form action="toChangePassword" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="submit" value="CHANGE PASSWORD" class="button">
        </form>
    </div>
    <div class="message">${message}</div>
    <div align="center">
        <form action="updateProfile" method="post" onsubmit="return validateUpdateProfile()">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <table>
                <tr><td>first name</td><td><input id="firstname" name="firstname" value=${user.firstName}></td></tr>
                <tr><td>last name</td><td><input id="lastname" name="lastname" value=${user.lastName}></td></tr>
                <tr><td>email</td><td><input id="email" name="email" value=${user.email}></td></tr>
                <tr><td>phone number</td><td><input id="phonenumber" name="phonenumber" value=${user.phoneNumber}></td></tr>
                <tr><td>city</td><td><select id="city" name="city">
                    <option value=""></option>
                    <c:forEach items="${city_list}" var="elem">
                        <option value=${elem.id}
                            <c:if test="${elem.id.equals(user.city.id)}">
                                selected="selected"
                            </c:if>
                        >${elem.name}&nbsp;(${elem.province})</option>
                    </c:forEach>
                </select></td></tr>
                <tr><td>address</td><td><input id="address" name="address" value=${user.address}></td></tr>
                <tr><td>zip code</td><td><input id="zipcode" name="zipcode" value=${user.zipCode}></td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td colspan="2" align="center"><input type="submit" value="SAVE" class="button"></td></tr>
            </table>
        </form>
    </div>
</body>
</html>