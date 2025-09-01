<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会員登録確認</title>
    <link rel="stylesheet" type="text/css" href="/miniApp/css/style.css"/>
</head>
<body>
    <h1>会員登録確認</h1>
    <p>以下の内容で登録します。よろしいですか？</p>

    <table border="1">
        <tr>
            <th>ユーザーID</th>
            <td>${user.loginId}</td>
        </tr>
        <tr>
            <th>お名前</th>
            <td>${user.displayName}</td>
        </tr>
        <tr>
            <th>パスワード</th>
            <td>********（入力済み）</td>
        </tr>
    </table>

    <!-- 登録実行 -->
    <form action="/miniApp/register" method="post">
        <input type="hidden" name="loginId" value="${user.loginId}">
        <input type="hidden" name="password" value="${user.password}">
        <input type="hidden" name="displayName" value="${user.displayName}">
        <input type="submit" value="登録する">
    </form>

    <!-- 戻るボタン -->
    <form action="/miniApp/register" method="get">
        <input type="submit" value="戻る">
    </form>
</body>
</html>