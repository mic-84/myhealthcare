<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <link rel="stylesheet" href="/resources/css/style.css">
    <script type="text/javascript" src="/resources/js/app.js"></script>
</head>
<body onload="startTime()">
    <div align="right">
        <table><tr>
            <td><div id="currentDateTime"></div></td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <c:if test="${empty user}">
                <td>
                    <form action="toSignupPage" method="post">
                        <input type="image" title="SIGN UP" src="/resources/img/signup.png"  height="35" width="35"
                        onMouseOver="this.src='/resources/img/signup_hover.png';" onMouseOut="this.src='/resources/img/signup.png';">
                    </form>
                </td>
            </c:if>
            <c:if test="${not empty user}">
                <td vertical-align="middle"><span font-size: 30px;><strong>${user.username.toUpperCase()}</strong></span></td>
                <td>
                    <form action="toProfilePage" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="image" title="PROFILE" src="/resources/img/user.png"  height="35" width="35"
                        onMouseOver="this.src='/resources/img/user_hover.png';" onMouseOut="this.src='/resources/img/user.png';">
                    </form>
                </td>
                <td>
                    <form action="logout" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="image" title="LOGOUT" src="/resources/img/logout.png"  height="35" width="35"
                        onMouseOver="this.src='/resources/img/logout_hover.png';" onMouseOut="this.src='/resources/img/logout.png';">
                    </form>
                </td>
                <td>
                    <form action="toHomePage" method="post">
                        <input type="hidden" id="userId" name="userId" value=${user.id}>
                        <input type="image" title="HOME PAGE" src="/resources/img/home.png"  height="35" width="35"
                            onMouseOver="this.src='/resources/img/home_hover.png';" onMouseOut="this.src='/resources/img/home.png';">
                    </form>
                </td>
            </c:if>
        </tr></table>
    </div>
    <div class="app_title">MyHealthCare</div><br/>
</body>
</html>