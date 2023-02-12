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
    <c:if test="${isEmployee}">
        <div class="subtitle">Services</div><br/>
        <div class="message">${message}</div>
        <div align="right"><table>
            <tr><td>
                <form action="getStructure" method="post">
                    <input type="hidden" id="userId" name="userId" value=${user.id}>
                    <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                    <input type="hidden" id="id" name="id" value=${structure.id}>
                    <input type="submit" value="BACK TO STRUCTURE MENU" class="button">
                </form>
            </td></tr>
            <tr><td>
                <form action="toAddStructureService" method="post">
                    <input type="hidden" id="userId" name="userId" value=${user.id}>
                    <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                    <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                    <input type="submit" value="ADD SERVICE" class="button">
                </form>
            </td></tr>
        </table></div>
        <table>
            <tr>
                <td>active</td>
                <td>code</td>
                <td>name</td>
                <td>rate</td>
            </tr>
            <c:forEach items="${structure.getServices()}" var="elem">
                <tr class="row">
                    <td>${elem.getGraphicActive()}</td>
                    <td class="border_table">${elem.code}</td>
                    <td class="border_table">${elem.name}</td>
                    <td class="border_table">&euro;&nbsp;${elem.rate}</td>
                    <td>
                        <form action="toStructureService" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="cryptedPassword" name="cryptedPassword" value=${user.password}>
                            <input type="hidden" id="id" name="id" value=${elem.id}>
                            <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                            <input type="submit" value="MANAGE" class="small_button">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>
</html>