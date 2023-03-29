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
    <form action="toSearchStructures" method="post">
        <input type="hidden" id="userId" name="userId" value=${user.id}>
        <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
            onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
    </form><br/>
    <div class="th">${quantity}&nbsp;available structures</div>
    <table>
        <tbody class="fixed_table scrollable_tbody2">
            <c:forEach items="${structure_list}" var="elem">
                <tr class="row">
                    <td>${elem.name}</td>
                    <td>${elem.address},&nbsp;${elem.city}</td>
                    <td class="no_hover">
                        <form action="getStructure" method="post">
                            <input type="hidden" id="userId" name="userId" value=${user.id}>
                            <input type="hidden" id="id" name="id" value=${elem.id}>
                            <input type="hidden" id="city" name="city" value=${city}>
                            <input type="hidden" id="neighbours" name="neighbours" value=${neighbours}>
                            <input type="hidden" id="service" name="service" value=${service}>
                            <input type="hidden" id="filter" name="filter" value=${service}>
                            <input type="hidden" id="fromSearch" name="fromSearch" value="true">
                            <input type="submit" value="VIEW DETAILS" class="button">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>