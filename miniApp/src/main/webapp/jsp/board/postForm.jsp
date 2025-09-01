<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="dto.UserDTO" %>
<%@ page import="dto.*" %>
<%
    // ログインチェック
    
    UserDTO user = (UserDTO) session.getAttribute("user");
    if (user == null) {
    	response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");

        return;
    }
    
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新規投稿</title>
    <link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
	<link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap" rel="stylesheet">
    
</head>
<body>
    <h2>新規投稿フォーム</h2>

    <form action="<%= request.getContextPath() %>/board" method="post">
        <input type="hidden" name="action" value="post">

        <div>
            <label>カテゴリ:</label>
            <input type="text" name="category" required>
        </div>

        <div>
            <label>タイトル:</label>
            <input type="text" name="title" required>
        </div>

        <div>
            <label>本文:</label><br>
            <textarea name="content" rows="5" cols="50" required></textarea>
        </div>

        <div>
            <button type="submit">投稿する</button>
        </div>
    </form>

    <p><a href="<%= request.getContextPath() %>/board">掲示板に戻る</a></p>
</body>
</html>
