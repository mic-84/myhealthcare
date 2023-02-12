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
    <c:if test="${isEmployee}">
        <div class="subtitle">Add service</div><br/>
        <div class="message">${message}</div>
        <div align="center">
            <form action="addStructureService" method="post">
                <input type="hidden" id="userId" name="userId" value=${user.id}>
                <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                <input type="hidden" id="structureId" name="structureId" value=${structureId}>
                <table>
                    <tr><td>service</td><td><select id="id" name="id">
                        <option value=""></option>
                        <c:forEach items="${services}" var="elem">
                            <option value=${elem.id}>${elem.code}&nbsp;${elem.name}</option>
                        </c:forEach>
                    </select></td></tr>
                    <tr><td>rate</td><td><input id="rate" name="rate" value=${service.rate}></td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="ADD" class="button"></td></tr>
                </table>
            </form>
        </div>
    </c:if>
</body>
</html>