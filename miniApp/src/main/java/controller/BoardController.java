package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PostDAO;
import dao.ReplyDAO;
import dto.PostDTO;
import dto.ReplyDTO;
import dto.UserDTO;
import service.PostService;

@WebServlet("/board")
public class BoardController extends BaseController {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("post".equals(action)) {
            // ログイン中ユーザーの取得
            dto.UserDTO user = (dto.UserDTO) req.getSession().getAttribute("user");

            // フォームの値を取得
            String category = req.getParameter("category");
            String title = req.getParameter("title");
            String content = req.getParameter("content");

            // DTOに詰める
            PostDTO dto = new PostDTO();
            dto.setAnonId(user.getAnonId()); // ログイン中のanon_id
            dto.setCategory(category);
            dto.setTitle(title);
            dto.setContent(content);

            // 投稿処理
            int result = PostService.getInstance().createPost(dto);

            if (result > 0) {
                res.sendRedirect("board");
            } else {
                req.setAttribute("error", "投稿に失敗しました。");
                forward(req, res, "board/postForm.jsp");
            }
        } else if ("reply".equals(action)) {
            UserDTO user = (UserDTO) req.getSession().getAttribute("user");
            
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            
            int postId = Integer.parseInt(req.getParameter("postId"));
            String content = req.getParameter("content");

            ReplyDTO replyDto = new ReplyDTO();
            replyDto.setPostsId(postId);
            replyDto.setAnonId(user.getAnonId());
            replyDto.setContent(content);

            int result = new ReplyDAO().insert(replyDto);
            if (result > 0) {
                res.sendRedirect("board?action=detail&id=" + postId);
            } else {
                req.setAttribute("error", "返信に失敗しました。");
                forward(req, res, "board/postDetail.jsp");
            }
        } else if ("delete".equals(action)) {
            // 投稿削除処理（返信も含む）
            deletePost(req, res);
        } else if ("deleteReply".equals(action)) {
            // 返信削除処理
            deleteReply(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    	
        // --- ここからデバッグコードを追加 ---
        String jspPath = "/jsp/board/board.jsp";
        String realPath = req.getServletContext().getRealPath(jspPath);
        System.out.println("=== BoardController.doGet() ===");
        System.out.println("forward先の相対パス: " + jspPath);
        System.out.println("サーバー上の絶対パス: " + realPath);
        System.out.println("ファイル存在チェック: " + new java.io.File(realPath).exists());
        // --- ここまで追加 ---    	
    	
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        System.out.println("BoardController doGet start, action=" + req.getParameter("action"));
        System.out.println("=== BoardController.doGet() ===");
        System.out.println("action = " + action);
        System.out.println("user = " + req.getSession().getAttribute("user"));
        
        if (action == null || action.isEmpty()) {
            // 一覧表示
            List<PostDTO> postList = PostService.getInstance().getAllPosts();
            req.setAttribute("postList", postList);
            forward(req, res, "board/board.jsp");

        } else if ("form".equals(action)) {
            // 新規投稿フォーム
            forward(req, res, "board/postForm.jsp");
        } else if ("detail".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                // id が無い場合は一覧ページに戻す
                res.sendRedirect(req.getContextPath() + "/board");
                return;
            }
            int id = Integer.parseInt(idStr);

            PostDTO post = PostService.getInstance().getPostById(id);
            if (post == null) {
                res.sendRedirect(req.getContextPath() + "/board");
                return;
            }

            List<ReplyDTO> replyList = new ReplyDAO().selectByPostId(id);
            req.setAttribute("post", post);
            req.setAttribute("replyList", replyList);

            forward(req, res, "board/postDetail.jsp");
        }
    }
    
    /**
     * 投稿削除処理（返信も含む）
     */
    private void deletePost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        try {
            UserDTO user = (UserDTO) req.getSession().getAttribute("user");
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                res.sendRedirect(req.getContextPath() + "/board");
                return;
            }
            
            int postId = Integer.parseInt(idStr);
            
            // 投稿の存在確認と投稿者チェック
            PostDTO post = new PostDAO().selectById(postId);
            if (post == null) {
                res.sendRedirect(req.getContextPath() + "/board");
                return;
            }
            
            // 投稿者本人でない場合は削除拒否
            if (!user.getAnonId().equals(post.getAnonId())) {
                req.setAttribute("error", "自分の投稿のみ削除できます。");
                res.sendRedirect(req.getContextPath() + "/board");
                return;
            }
            
            // 削除処理：1. 返信削除 → 2. 投稿削除
            ReplyDAO replyDAO = new ReplyDAO();
            PostDAO postDAO = new PostDAO();
            
            // 投稿に紐づく返信をすべて削除
            deleteRepliesByPostId(postId);
            
            // 投稿を削除
            boolean result = postDAO.deleteById(postId);
            
            if (result) {
                res.sendRedirect(req.getContextPath() + "/board");
            } else {
                req.setAttribute("error", "投稿の削除に失敗しました。");
                res.sendRedirect(req.getContextPath() + "/board");
            }
            
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/board");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "削除処理中にエラーが発生しました。");
            res.sendRedirect(req.getContextPath() + "/board");
        }
    }
    
    /**
     * 返信削除処理
     */
    private void deleteReply(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        try {
            UserDTO user = (UserDTO) req.getSession().getAttribute("user");
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            
            String replyIdStr = req.getParameter("replyId");
            String postIdStr = req.getParameter("postId");
            
            if (replyIdStr == null || postIdStr == null) {
                res.sendRedirect(req.getContextPath() + "/board");
                return;
            }
            
            int replyId = Integer.parseInt(replyIdStr);
            int postId = Integer.parseInt(postIdStr);
            
            // 返信の存在確認と投稿者チェック
            ReplyDTO reply = getReplyById(replyId);
            if (reply == null) {
                res.sendRedirect(req.getContextPath() + "/board?action=detail&id=" + postId);
                return;
            }
            
            // 返信者本人でない場合は削除拒否
            if (!user.getAnonId().equals(reply.getAnonId())) {
                req.setAttribute("error", "自分の返信のみ削除できます。");
                res.sendRedirect(req.getContextPath() + "/board?action=detail&id=" + postId);
                return;
            }
            
            // 返信削除
            ReplyDAO replyDAO = new ReplyDAO();
            boolean result = replyDAO.deleteById(replyId);
            
            if (result) {
                res.sendRedirect(req.getContextPath() + "/board?action=detail&id=" + postId);
            } else {
                req.setAttribute("error", "返信の削除に失敗しました。");
                res.sendRedirect(req.getContextPath() + "/board?action=detail&id=" + postId);
            }
            
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/board");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "削除処理中にエラーが発生しました。");
            res.sendRedirect(req.getContextPath() + "/board");
        }
    }
    
    /**
     * 投稿IDに紐づくすべての返信を削除
     */
    private void deleteRepliesByPostId(int postId) {
        try {
            ReplyDAO replyDAO = new ReplyDAO();
            List<ReplyDTO> replyList = replyDAO.selectByPostId(postId);
            
            for (ReplyDTO reply : replyList) {
                replyDAO.deleteById(reply.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 返信IDで返信を取得（権限チェック用）
     */
    private ReplyDTO getReplyById(int replyId) {
        try {
            // 簡易的な実装：すべての返信から該当IDを検索
            // 本来はReplyDAO.selectByIdメソッドを作成すべき
            ReplyDAO replyDAO = new ReplyDAO();
            // ここでは仮実装：実際のプロジェクトではselectByIdメソッドを追加してください
            return null; // 実装が必要
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


//package controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import dao.ReplyDAO; // ← これ追加
//import dto.PostDTO;
//import dto.ReplyDTO; // ← これ追加
//import dto.UserDTO;
//import service.PostService;
//
//
//@WebServlet("/board")
//public class BoardController extends BaseController {
//    private static final long serialVersionUID = 1L;
//    
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse res)
//            throws ServletException, IOException {
//
//        req.setCharacterEncoding("UTF-8");
//        String action = req.getParameter("action");
//
//        if ("post".equals(action)) {
//            // ログイン中ユーザーの取得
//            dto.UserDTO user = (dto.UserDTO) req.getSession().getAttribute("user");
//
//            // フォームの値を取得
//            String category = req.getParameter("category");
//            String title = req.getParameter("title");
//            String content = req.getParameter("content");
//
//            // DTOに詰める
//            PostDTO dto = new PostDTO();
//            dto.setAnonId(user.getAnonId()); // ログイン中のanon_id
//            dto.setCategory(category);
//            dto.setTitle(title);
//            dto.setContent(content);
//
//            // 投稿処理
//            int result = PostService.getInstance().createPost(dto);
//
//            if (result > 0) {
//                res.sendRedirect("board");
//            } else {
//                req.setAttribute("error", "投稿に失敗しました。");
//                forward(req, res, "board/postForm.jsp");
//            }
//        } else if ("reply".equals(action)) {
//            UserDTO user = (UserDTO) req.getSession().getAttribute("user");
//            int postId = Integer.parseInt(req.getParameter("postId"));
//            String content = req.getParameter("content");
//
//            ReplyDTO replyDto = new ReplyDTO();
//            replyDto.setPostsId(postId);
//            replyDto.setAnonId(user.getAnonId());
//            replyDto.setContent(content);
//
//            int result = new ReplyDAO().insert(replyDto);
//            if (result > 0) {
//                res.sendRedirect("board?action=detail&id=" + postId);
//            } else {
//                req.setAttribute("error", "返信に失敗しました。");
//                forward(req, res, "board/postDetail.jsp");
//            }
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse res)
//            throws ServletException, IOException {
//    	
//    	
//
//    	 // --- ここからデバッグコードを追加 ---
//        String jspPath = "/jsp/board/board.jsp";
//        String realPath = req.getServletContext().getRealPath(jspPath);
//        System.out.println("=== BoardController.doGet() ===");
//        System.out.println("forward先の相対パス: " + jspPath);
//        System.out.println("サーバー上の絶対パス: " + realPath);
//        System.out.println("ファイル存在チェック: " + new java.io.File(realPath).exists());
//        
//        // --- ここまで追加 ---    	
//    	
//        req.setCharacterEncoding("UTF-8");
//        String action = req.getParameter("action");
//
//        System.out.println("BoardController doGet start, action=" + req.getParameter("action"));
//        System.out.println("=== BoardController.doGet() ===");
//        System.out.println("action = " + action);
//        System.out.println("user = " + req.getSession().getAttribute("user"));
//        
//        if (action == null || action.isEmpty()) {
//            // 一覧表示
//            List<PostDTO> postList = PostService.getInstance().getAllPosts();
//            req.setAttribute("postList", postList);
//            forward(req, res, "board/board.jsp");
//
//        } else if ("form".equals(action)) {
//            // 新規投稿フォーム
//            forward(req, res, "board/postForm.jsp");
//        }
//
//            else if ("detail".equals(action)) {
//                String idStr = req.getParameter("id");
//                if (idStr == null || idStr.isEmpty()) {
//                    // id が無い場合は一覧ページに戻す
//                    res.sendRedirect(req.getContextPath() + "/board");
//                    return;
//                }
//                int id = Integer.parseInt(idStr);
//
//                PostDTO post = PostService.getInstance().getPostById(id);
//                if (post == null) {
//                    res.sendRedirect(req.getContextPath() + "/board");
//                    return;
//                }
//
//                List<ReplyDTO> replyList = new ReplyDAO().selectByPostId(id);
//                req.setAttribute("post", post);
//                req.setAttribute("replyList", replyList);
//
//                forward(req, res, "board/postDetail.jsp");
//            }
//       	
//        	// 投稿詳細
//            int id = Integer.parseInt(req.getParameter("id"));
//
//            PostDTO post = PostService.getInstance().getPostById(id);
//            List<ReplyDTO> replyList = new ReplyDAO().selectByPostId(id);
//
//            req.setAttribute("post", post);
//            req.setAttribute("replyList", replyList);
//
//            forward(req, res, "board/postDetail.jsp");
//    }
//    
//}
