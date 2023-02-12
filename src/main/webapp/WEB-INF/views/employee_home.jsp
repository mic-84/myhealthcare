<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<c:if test="${user.isEmployee()}">
    <br/><br/>
    <div class="subtitle">Employee panel</div><br/>
    <div align="center">
        <table><c:forEach items="${structure_list}" var="elem">
            <tr class="row">
                <td>${elem.name}</td>
                <td>${elem.address},&nbsp;${elem.city}</td>
                <td>
                    <form action="getStructure" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                        <input type="hidden" id="id" name="id" value=${elem.id}>
                        <input type="submit" value="MANAGE" class="button">
                    </form>
                </td>
            </tr>
        </c:forEach></table>
    </div>
</c:if>
</body>
</html>