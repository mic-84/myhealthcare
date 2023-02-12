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
    <div class="message">${message}</div>
    <c:if test="${list.size() > 0}"><table>
        <tr>
            <th text-align="center">rating</th>
            <th text-align="center">date</th>
            <th text-align="center">structure</th>
            <th text-align="center">text</th>
        </tr>
        <c:forEach items="${list}" var="elem">
            <tr class="row">
                <td class="border_table">${elem.getGraphicRating()}</td>
                <td class="border_table">${elem.getStringDate()}</td>
                <td class="border_table">${elem.structure}</td>
                <td class="border_table">${elem.text}</td>
            </tr>
        </c:forEach>
    </table></c:if>
</body>
</html>