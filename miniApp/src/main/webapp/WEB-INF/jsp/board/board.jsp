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

    <p><a href="<%= request.getContextPath() %>/home.jsp">ホームへ戻る</a></p>

</body>
</html>
