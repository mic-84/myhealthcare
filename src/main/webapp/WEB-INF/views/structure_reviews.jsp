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
    <div class="subtitle">Reviews</div><br/>
    <form action="getStructure" method="post">
        <input type="hidden" id="userId" name="userId" value=${user.id}>
        <input type="hidden" id="id" name="id" value=${structureId}>
        <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
            onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
    </form><br/>
    <div class="message">${message}</div>
    <table>
        <tr><th class="th">Review Average</th><td>${average}</td></tr>
        <c:forEach items="${reviews}" var="review">
            <tr>
                <th class="th">${review.getGraphic()}</th>
                <td>${review.getCount()}</td>
            </tr>
        </c:forEach>
    </table><br/><br/>
    <c:if test="${list.size() > 0}"><table>
        <tr>
            <th class="th" style="width: 7%;">rating</th>
            <th class="th" style="width: 7%;">date</th>
            <th class="th">user</th>
            <th class="th">text</th>
        </tr>
        <c:forEach items="${list}" var="elem">
            <tr class="row">
                <td class="border_table no_hover" style="width: 7%;">${elem.getGraphicRating()}</td>
                <td class="border_table" style="width: 7%;">${elem.getStringDate()}</td>
                <td class="border_table">${elem.user.username}</td>
                <td class="border_table">${elem.text}</td>
            </tr>
        </c:forEach>
    </table></c:if>
</body>
</html>