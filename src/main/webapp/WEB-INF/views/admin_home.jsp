<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<c:if test="${user.isAdmin()}">
    <br/><br/>
    <div class="subtitle">Admin panel</div><br/>
    <div align="center">
        <table>
            <tr>
                <td>
                    <form action="toAdminStatistics" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="submit" value="STATISTICS" class="button">
                    </form>
                </td>
            </tr>
            <tr>
                <td>
                    <form action="toNewService" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="submit" value="NEW SERVICE" class="button">
                    </form>
                </td>
            </tr>
        </table>
    </div>
</c:if>
</body>
</html>