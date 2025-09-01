<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="dto.PostDTO" %>
<%@ page import="dto.ReplyDTO" %>
<%@ page import="java.util.List" %>
<%
    PostDTO post = (PostDTO) request.getAttribute("post");
    if (post == null) {
        response.sendRedirect("board");
        return;
    }
    List<ReplyDTO> replies = (List<ReplyDTO>) request.getAttribute("replyList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>投稿詳細</title>
    
</head>
<body>
    <h2>投稿詳細</h2>

    <p><strong>カテゴリ:</strong> <%= post.getCategory() %></p>
    <p><strong>タイトル:</strong> <%= post.getTitle() %></p>
    <p><strong>本文:</strong><br>
        <pre><%= post.getContent() %></pre>
    </p>
    <p><strong>投稿日:</strong> <%= post.getCreatedAt() %></p>
    <p><strong>投稿者AnonId:</strong> <%= post.getAnonId() %></p>

    <hr>

    <!-- 返信一覧 -->
    <h3>返信一覧</h3>
    <%
        if (replies != null && !replies.isEmpty()) {
            for (ReplyDTO r : replies) {
    %>
        <p>[<%= r.getCreatedAt() %>] <%= r.getAnonId() %>: <%= r.getContent() %></p>
    <%
            }
        } else {
    %>
        <p>返信はまだありません。</p>
    <%
        }
    %>

    <hr>

    <!-- 返信フォーム -->
    <h3>返信する</h3>
    <form action="board" method="post">
        <input type="hidden" name="action" value="reply">
        <input type="hidden" name="postId" value="<%= post.getId() %>">
        <textarea name="content" rows="3" cols="50" required></textarea><br>
        <button type="submit">返信投稿</button>
    </form>

    <p><a href="board">一覧に戻る</a></p>

</body>
</html>
