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
    <div class="subtitle">${title}</div><br/>
    <table>
        <c:forEach items="${structure_list}" var="elem">
            <tr class="row">
                <td>${elem.name}</td>
                <td>${elem.address},&nbsp;${elem.city}</td>
                <td>
                    <form action="getStructure" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="id" name="id" value=${elem.id}>
                        <input type="hidden" id="city" name="city" value=${city}>
                        <input type="hidden" id="neighbours" name="neighbours" value=${neighbours}>
                        <input type="hidden" id="service" name="service" value=${service}>
                        <input type="submit" value="VIEW DETAILS" class="button">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>