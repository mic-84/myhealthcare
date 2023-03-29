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
        <form action="getStructure" method="post">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <input type="hidden" id="id" name="id" value=${structure.id}>
            <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
                onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
        </form><br/>
        <div class="message">${message}</div>
        <div align="right"><table>
            <tr><td>
                <form action="toAddStructureService" method="post">
                    <input type="hidden" id="userId" name="userId" value=${user.id}>
                    <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                    <input type="submit" value="ADD SERVICE" class="button">
                </form>
            </td></tr>
        </table></div>
        <div align="center">
            <form action="toServicesManagement" method="post">
                <input type="hidden" id="userId" name="userId" value=${user.id}>
                <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                <input type="text" size="50" id="filter" name="filter" value=${filter}>
                <input type="submit" value="SEARCH" class="button">
            </form>
        </div>
        <br/>
        <table>
            <tbody class="fixed_table scrollable_tbody">
                <tr>
                    <th class="th"></th>
                    <th class="th">code</th>
                    <th class="th">name</th>
                    <th class="th">rate</th>
                </tr>
                <c:forEach items="${service_list}" var="elem">
                    <tr class="row">
                        <td class="no_hover">${elem.getGraphicActive()}</td>
                        <td class="border_table">${elem.code}</td>
                        <td class="border_table">${elem.name}</td>
                        <td class="border_table">&euro;&nbsp;${elem.rate}</td>
                        <td class="no_hover">
                            <form action="toStructureService" method="post">
                                <input type="hidden" id="userId" name="userId" value=${user.id}>
                                <input type="hidden" id="id" name="id" value=${elem.id}>
                                <input type="hidden" id="structureId" name="structureId" value=${structure.id}>
                                <input type="hidden" id="filter" name="filter" value=${filter}>
                                <input type="submit" value="MANAGE" class="small_button">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</body>
</html>