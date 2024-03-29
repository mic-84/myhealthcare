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
        <div class="subtitle">Service&nbsp;${service.name}</div><br/>
        <form action="toServicesManagement" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="structureId" name="structureId" value=${structureId}>
            <input type="hidden" id="filter" name="filter" value=${filter}>
            <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
                onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
        </form><br/>
        <div class="message">${message}</div>
        <div align="center">
            <form action="updateStructureService" method="post">
                <table>
                    <input type="hidden" id="userId" name="userId" value=${user.id}>
                    <input type="hidden" id="id" name="id" value=${service.id}>
                    <input type="hidden" id="structureId" name="structureId" value=${structureId}>
                    <input type="hidden" id="filter" name="filter" value=${filter}>
                    <tr><td>rate</td><td><input id="rate" name="rate" value=${service.rate}></td></tr>
                    <tr><td>status</td><td><select id="active" name="active">
                        <option value="true"
                            <c:if test="${service.isActive()}">selected="selected"</c:if>
                        >active</option>
                        <option value="false"
                            <c:if test="${not service.isActive()}">selected="selected"</c:if>
                        >not active</option>
                    </select></td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="SAVE" class="button"></td></tr>
                </table>
            </form>
        </div>
    </c:if>
</body>
</html>