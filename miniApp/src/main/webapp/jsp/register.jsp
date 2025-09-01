<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規会員登録</title>
<link rel="stylesheet" type="text/css" href="/miniApp/css/style.css"/>
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@500&family=Roboto&display=swap" rel="stylesheet">
</head>
<body>
    <h1>新規会員登録</h1>
    
    <!-- エラーメッセージ表示 -->
    <c:if test="${not empty errorMsg}">
        <ul style="color:red;">
            <c:forEach var="msg" items="${errorMsg}">
                <li>${msg}</li>
            </c:forEach>
        </ul>
    </c:if>
    
    <c:if test="${not empty registerError}">
        <div style="color:red;">
            <p>${registerError}</p>
        </div>
    </c:if>
    
    <h2>会員登録フォーム</h2>
    <p>下記のフォームより、ユーザー情報をご登録ください</p>
    
    <!-- 確認画面に送信 -->
    <form action="/miniApp/registerConfirm" method="post">
        <table border="1">
            <tr>
                <th>ユーザーID</th>
                <td><input type="text" name="loginId" value="${param.loginId}" placeholder="1～8文字" required></td>
            </tr>
            <tr>
                <th>パスワード</th>
                <td><input type="password" name="password" placeholder="2～10文字" required></td>
            </tr>
            <tr>
                <th>お名前</th>
                <td><input type="text" name="displayName" value="${param.displayName}" placeholder="1～10文字" required></td>
            </tr>
        </table>
        <input type="submit" value="確認画面へ">
    </form>
    
    <p><a href="/miniApp/login">ログイン画面に戻る</a></p>
</body>
</html>