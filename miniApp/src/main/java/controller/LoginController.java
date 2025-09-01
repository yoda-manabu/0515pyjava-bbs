package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import dto.UserDTO;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // ログイン画面表示
        RequestDispatcher rd = req.getRequestDispatcher("/jsp/login.jsp");
        rd.forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String loginId = req.getParameter("loginId");
        String password = req.getParameter("password");

        System.out.println("[LoginController] loginId=" + loginId + ", password=" + password);

        // ここは本来パスワード照合などをする想定
        UserDAO dao = new UserDAO();
        UserDTO user = dao.selectByLoginId(loginId);

        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            System.out.println("[LoginController] set session loginId=" + user.getLoginId());

            // ホームへ
            res.sendRedirect(req.getContextPath() + "/jsp/home.jsp");
        } else {
            req.setAttribute("loginError", "ログインに失敗しました。");
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/login.jsp");
            rd.forward(req, res);
        }
    }
}
