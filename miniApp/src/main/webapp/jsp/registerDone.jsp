<!-- 登録完了画面。 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員登録完了</title>
<link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@500&family=Roboto&display=swap" rel="stylesheet">
</head>
<body>
	<h1>ユーザー登録完了</h1>
	<h2>ユーザー登録が完了しました</h2>
	<div>
		<a href="${pageContext.request.contextPath}/jsp/home.jsp">ホームに戻る</a>
	</div>
</body>
</html>