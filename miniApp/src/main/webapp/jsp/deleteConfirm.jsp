<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>削除確認画面</title>
<link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@500&family=Roboto&display=swap" rel="stylesheet">

</head>
<body>
	<h1>会員情報削除確認画面</h1>
	<p>下記の内容でよろしければ、退会ボタンを押してください。</p>
	<c:if test ="${not empty deleteError}">
		<div style="color:red;">
			<p>${deleteError}</p>
		</div>
	</c:if>
	
	<h2 >会員情報</h2>
	<form action="/miniApp/delete" method="post">
		<table border="1">
			<tr>
				<th>ユーザーID</th>
				<td><input type ="hidden" name="deleteLoginId" value="${user.loginId}">${user.loginId}</td>
			</tr>
			<tr>
				<th>お名前</th>
				<td>${user.displayName}</td>
			</tr>
			
			<tr>
				<th>パスワード</th>
				<td>${user.password}</td>
			</tr>
		</table>
		<input type ="submit" value="退会する">
	</form>
	<a href="${pageContext.request.contextPath}/jsp/home.jsp">ホームに戻る</a>
</body>

</html>