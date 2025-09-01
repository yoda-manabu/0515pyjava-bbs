package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.User;
import service.UserRegisterService;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // JSPから送信された値を取得
        String loginId = req.getParameter("loginId");
        String password = req.getParameter("password");
        String displayName = req.getParameter("displayName");

        System.out.println("[RegisterController] loginId=" + loginId);
        System.out.println("[RegisterController] password=" + password);
        System.out.println("[RegisterController] displayName=" + displayName);

        // バリデーション
        List<String> errorMsg = new ArrayList<>();
        
        if (loginId == null || loginId.trim().isEmpty()) {
            errorMsg.add("ユーザーIDを入力してください。");
        } else if (loginId.length() < 1 || loginId.length() > 8) {
            errorMsg.add("ユーザーIDは1～8文字で入力してください。");
        }
        
        if (password == null || password.trim().isEmpty()) {
            errorMsg.add("パスワードを入力してください。");
        } else if (password.length() < 2 || password.length() > 10) {
            errorMsg.add("パスワードは2～10文字で入力してください。");
        }
        
        if (displayName == null || displayName.trim().isEmpty()) {
            errorMsg.add("お名前を入力してください。");
        } else if (displayName.length() < 1 || displayName.length() > 10) {
            errorMsg.add("お名前は1～10文字で入力してください。");
        }

        // エラーがある場合は元の画面に戻る
        if (!errorMsg.isEmpty()) {
            req.setAttribute("errorMsg", errorMsg);
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/register.jsp");
            rd.forward(req, res);
            return;
        }

        // 実際の登録処理（確認画面から来た場合）
        try {
            User user = new User();
            user.setLoginId(loginId.trim());
            user.setPassword(password.trim());
            user.setDisplayName(displayName.trim());

            UserRegisterService service = new UserRegisterService();
            
            // 重複チェック
            if (!service.userEntryConfirm(user)) {
                req.setAttribute("registerError", "このユーザーIDは既に使用されています。");
                req.setAttribute("errorMsg", new ArrayList<>());
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/register.jsp");
                rd.forward(req, res);
                return;
            }
            
            // 登録実行
            boolean success = service.userEntryDo(user);
            
            if (success)  {
                // 登録成功時：セッションにユーザー情報を設定
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                
                System.out.println("[RegisterController] 登録成功 - セッションにユーザー設定: " + user.getDisplayName());
            	
                // 登録完了画面へ
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/registerDone.jsp");
                rd.forward(req, res);
            } else {
                req.setAttribute("registerError", "登録に失敗しました。再度お試しください。");
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/register.jsp");
                rd.forward(req, res);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("registerError", "システムエラーが発生しました。");
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/register.jsp");
            rd.forward(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // 登録フォームを表示
        RequestDispatcher rd = req.getRequestDispatcher("/jsp/register.jsp");
        rd.forward(req, res);
    }
}