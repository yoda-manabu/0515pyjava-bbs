<!-- ログイン画面のフォームとエラーメッセージ表示。 -->
<!--  
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>変更確認画面</title>
<link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@500&family=Roboto&display=swap" rel="stylesheet">
</head>
<body>
	
	<h1>メンバー情報確認画面</h1>
	<p style ="text-align: right">${user.name}様ログイン中</p>
	<p style ="text-align: right"><a href="/miniApp/logout">ログアウト</a></p>

	<h2>変更確認画面</h2>
	<p>下記の内容でよろしければ、変更ボタンを押してください。</p>
	<form action="/miniApp/editConfirm" method="post">
		<table border = "1">
			<tr>
				<th>お名前</th>
				<td><input type ="hidden" name="editName" value="${editName}">${editName}</td>
			</tr>
			<tr>
				<th>パスワード</th>
				<td><input type ="hidden" name="editPassword" value="${editPassword}">${editPassword}</td>
			</tr>
			<input type ="hidden" name="loginId" value="${user.loginId}"/>
		</table>
		<input type ="submit" value="変更する">
	</form>
	<p><a href="/miniApp/jsp/login.jsp">トップ画面に戻る</a></p>
</body>
</html>
-->


<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会員情報編集確認</title>
    <link rel="stylesheet" type="text/css" href="/miniApp/css/style.css"/>
</head>
<body>
    <h1>会員情報編集 確認画面</h1>

    <p>以下の内容で更新してよろしいですか？</p>

    <table border="1">
        <tr>
            <th>お名前</th>
            <td>${editName}</td>
        </tr>
        <tr>
            <th>パスワード</th>
            <td>${editPassword}</td>
        </tr>
    </table>

    <!-- 更新ボタン -->
    <form action="<%= request.getContextPath() %>/editConfirm" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="editName" value="${editName}">
        <input type="hidden" name="editPassword" value="${editPassword}">
        <input type="submit" value="更新する">
    </form>

    <!-- 戻るボタン -->
    <form action="<%= request.getContextPath() %>/jsp/edit.jsp" method="get">
        <input type="submit" value="戻る">
    </form>
</body>
</html>
