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

@WebServlet("/edit")
public class EditController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        UserDTO sessionUser = (UserDTO) session.getAttribute("user");
        
        String loginId = (sessionUser != null) ? sessionUser.getLoginId() : req.getParameter("loginId");
        String displayName = req.getParameter("editName");
        String password = req.getParameter("editPassword");

        System.out.println("=== EditController Debug ===");
        System.out.println("loginId: " + loginId);
        System.out.println("displayName: " + displayName);
        System.out.println("editPassword: " + password);

        // loginIdが取れなかったらログイン画面へ戻す
        if (loginId == null || loginId.isEmpty()) {
            req.setAttribute("errorMsg", "ログイン情報が取得できません。再度ログインしてください。");
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/login.jsp");
            rd.forward(req, res);
            return;
        }

        // DTOに詰める
        UserDTO dto = new UserDTO();
        dto.setLoginId(loginId);
        dto.setDisplayName(displayName);
        dto.setPassword(password);

        // DAOで更新
        UserDAO dao = new UserDAO();
        int result = dao.edit(dto);

        if (result == 1) {
            req.setAttribute("successMsg", "更新しました。");
            // セッションのユーザー情報も更新
            sessionUser.setDisplayName(displayName);
            sessionUser.setPassword(password);
            session.setAttribute("user", sessionUser);
        } else {
            req.setAttribute("errorMsg", "更新に失敗しました。");
        }

        RequestDispatcher rd = req.getRequestDispatcher("/jsp/edit.jsp");
        rd.forward(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/jsp/edit.jsp");
        rd.forward(req, res);
    }
}
