package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ReplyDAO;

@WebServlet("/reply/delete")
public class ReplyDeleteController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        // パラメータ取得
        String replyIdStr = request.getParameter("replyId");
        String postIdStr = request.getParameter("postId");

        // パラメータが正しいかチェック
        if (replyIdStr == null || postIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/board");
            return;
        }
        

        int replyId = Integer.parseInt(replyIdStr);
        int postId = Integer.parseInt(postIdStr);

        // DAO呼び出し
        ReplyDAO dao = new ReplyDAO();
        boolean deleted = dao.deleteById(replyId);

        // 削除失敗時のエラーメッセージ設定
        if (!deleted) {
            request.setAttribute("errorMessage", "コメント削除に失敗しました。");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/board/postDetail.jsp");

            rd.forward(request, response);
            return;
        }

        // 削除後は投稿詳細ページにリダイレクト
        response.sendRedirect(request.getContextPath() + "/board?action=detail&postId=" + postId);

    }
}