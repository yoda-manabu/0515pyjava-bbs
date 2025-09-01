<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.PostDTO" %>
<%
List<PostDTO> postList = (List<PostDTO>) request.getAttribute("postList");
dto.UserDTO user = (dto.UserDTO) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>掲示板一覧</title>
<link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
<link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap" rel="stylesheet">

<!-- 削除確認用のJavaScript -->
<script>
function confirmDelete(postId, postTitle) {
    if (confirm('投稿「' + postTitle + '」とそのコメントを削除してもよろしいですか？\n※この操作は取り消せません。')) {
        // 削除処理のためのフォームを動的に作成して送信
        var form = document.createElement('form');
        form.method = 'POST';
        form.action = '<%= request.getContextPath() %>/board';
        
        var actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = 'delete';
        form.appendChild(actionInput);
        
        var idInput = document.createElement('input');
        idInput.type = 'hidden';
        idInput.name = 'id';
        idInput.value = postId;
        form.appendChild(idInput);
        
        document.body.appendChild(form);
        form.submit();
    }
}
</script>

</head>
<body>
<h2>掲示板</h2>

<!-- 新規投稿リンク -->
<p>

<a href="<%= request.getContextPath() %>/board?action=form">＋ 新規投稿</a>
</p>

<table border="1" cellpadding="5">
<tr>
<th>ID</th>
<th>カテゴリ</th>
<th>タイトル</th>
<th>投稿日</th>
<th>操作</th> <!-- 削除ボタン用の列を追加 -->
</tr>
<%
if (postList != null && !postList.isEmpty()) {
    for (PostDTO post : postList) {
%>
<tr>
<td><%= post.getId() %></td>
<td><%= post.getCategory() %></td>
<td>
<a href="<%= request.getContextPath() %>/board?action=detail&id=<%= post.getId() %>">
<%= post.getTitle() %>
</a>
</td>
<td><%= post.getCreatedAt() %></td>
<td>
    <!-- 削除ボタン（投稿者本人のみ表示） -->
    <% if (user != null && user.getAnonId().equals(post.getAnonId())) { %>
        <button type="button" 
                onclick="confirmDelete('<%= post.getId() %>', '<%= post.getTitle().replace("'", "\\'") %>')"
                style="background-color: #ff4444; color: white; border: none; padding: 3px 8px; cursor: pointer; font-size: 12px;">
            削除
        </button>
    <% } else { %>
        -
    <% } %>
</td>
</tr>
<%
    }
} else {
%>
<tr>
<td colspan="5">投稿はまだありません。</td> <!-- colspanを5に変更 -->
</tr>
<%
}
%>
</table>

<p>
	<a href="<%= request.getContextPath() %>/jsp/home.jsp" class="button-link">ホームへ戻る</a>
</p>

</body>
</html>







<%--  

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.PostDTO" %>
<%
    List<PostDTO> postList = (List<PostDTO>) request.getAttribute("postList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>掲示板一覧</title>
    <link rel="stylesheet" type="text/css" href ="/miniApp/css/style.css"/>
	<link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap" rel="stylesheet">
    
</head>
<body>
    <h2>掲示板</h2>

    <!-- 新規投稿リンク -->
    <p><a href="<%= request.getContextPath() %>/board?action=form">＋ 新規投稿</a></p>

    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>カテゴリ</th>
            <th>タイトル</th>
            <th>投稿日</th>
        </tr>
        <%
            if (postList != null && !postList.isEmpty()) {
                for (PostDTO post : postList) {
        %>
        <tr>
            <td><%= post.getId() %></td>
            <td><%= post.getCategory() %></td>
            <td>
                <a href="<%= request.getContextPath() %>/board?action=detail&id=<%= post.getId() %>">

                    <%= post.getTitle() %>
                </a>
            </td>
            <td><%= post.getCreatedAt() %></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="4">投稿はまだありません。</td>
        </tr>
        <%
            }
        %>
    </table>

    <p><a href="<%= request.getContextPath() %>/jsp/home.jsp">ホームへ戻る</a></p>

</body>
</html>
--%>