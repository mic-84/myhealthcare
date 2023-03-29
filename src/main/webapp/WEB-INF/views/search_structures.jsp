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
    <div class="subtitle">Structure research</div><br/>
    <form action="toHomePage" method="post">
        <input type="hidden" id="userId" name="userId" value=${user.id}>
        <input type="image" title="BACK" src="/resources/img/back.png"  height="35" width="35"
            onMouseOver="this.src='/resources/img/back_hover.png';" onMouseOut="this.src='/resources/img/back.png';">
    </form><br/>
    <div align="center">
        <form action="searchStructures" method="post" onsubmit="return validateSearchStructures()">
            <input type="hidden" id="userId" name="userId" value=${user.id}>
            <table>
                <tr>
                    <td align="right">city</td>
                    <td>
                        <input list="cities" name="city">
                        <datalist id="cities">
                            <c:forEach items="${city_list}" var="elem">
                                <option value=${elem.name.replace(" ", "&#160;")}>
                            </c:forEach>
                        </datalist>
                    </td>
                    <td><input type="checkbox" id="neighbours" name="neighbours">&nbsp;include neighbour cities</td></tr>
                <tr>
                    <td align="right">service</td>
                    <td colspan="2">
                        <input list="services" name="service">
                        <datalist id="services">
                            <c:forEach items="${service_list}" var="elem">
                                <option value=${elem.name.replace(" ", "&#160;")}>
                            </c:forEach>
                        </datalist>
                    </td>
                </tr>
                <tr><td colspan="3" align="center"><input type="submit" value="SEARCH STRUCTURES" class="button"></td></tr>
            </table>
        </form>
    </div>
</body>
</html>