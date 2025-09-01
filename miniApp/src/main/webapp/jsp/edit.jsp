<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<%@ page import="dto.UserDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員情報編集</title>
<link rel="stylesheet" type="text/css" href="/miniApp/css/style.css"/>
</head>
<body>
    <h1>会員情報編集</h1>

    <!-- セッションからUserDTOを取得 -->
    <%
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            // セッション切れ時はログイン画面へリダイレクト
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    %>

    <!-- エラーメッセージ -->
    <c:if test="${not empty errorMsg}">
        <p style="color:red">${errorMsg}</p>
    </c:if>

    <!-- 成功メッセージ -->
    <c:if test="${not empty successMsg}">
        <p style="color:green">${successMsg}</p>
    </c:if>

    <form action="/miniApp/edit" method="post">
        <!-- loginIdはhiddenで送信 -->
        <input type="hidden" name="loginId" value="<%= user.getLoginId() %>">

        <table>
            <tr>
                <th>表示名</th>
                <td><input type="text" name="editName" value="<%= user.getDisplayName() %>" required></td>
            </tr>
            <tr>
                <th>パスワード</th>
                <td><input type="password" name="editPassword" value="" required></td>
            </tr>
        </table>

        <br>
        <input type="submit" value="更新">
    </form>
    <a href="${pageContext.request.contextPath}/jsp/home.jsp">ホームに戻る</a>
</body>
</html>
