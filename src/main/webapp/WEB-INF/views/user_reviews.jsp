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
    <div class="subtitle">My reviews</div><br/>
    <form action="toHomePage" method="post">
        <input type="hidden" id="userId" name="userId" value=${user.id}>
        <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
            onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
    </form><br/>
    <div class="message">${message}</div>
    <c:if test="${list.size() > 0}"><table>
        <tr>
            <th class="th" style="width: 7%;">rating</th>
            <th class="th" style="width: 7%;">date</th>
            <th class="th">structure</th>
            <th class="th">text</th>
        </tr>
        <c:forEach items="${list}" var="elem">
            <tr class="row">
                <td class="border_table no_hover" style="width: 7%;">${elem.getGraphicRating()}</td>
                <td class="border_table" style="width: 7%;">${elem.getStringDate()}</td>
                <td class="border_table">${elem.getStructure().getShortDescription()}</td>
                <td class="border_table">${elem.getText()}</td>
            </tr>
        </c:forEach>
    </table></c:if>
</body>
</html>