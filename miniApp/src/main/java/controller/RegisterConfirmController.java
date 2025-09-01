package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.UserDTO;

@WebServlet("/registerConfirm")
public class RegisterConfirmController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字化け防止
        request.setCharacterEncoding("UTF-8");

        // フォームから値を取得
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");
        String displayName = request.getParameter("displayName");

        // デバッグログ
        System.out.println("=== RegisterConfirmController Debug ===");
        System.out.println("loginId: " + loginId);
        System.out.println("password: " + password);
        System.out.println("displayName: " + displayName);

        // DTOにデータをセット
        UserDTO dto = new UserDTO();
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setDisplayName(displayName);

        // リクエストスコープに保存
        request.setAttribute("user", dto);

        // registerConfirm.jsp へフォワード
        RequestDispatcher rd = request.getRequestDispatcher("/jsp/registerConfirm.jsp");
        rd.forward(request, response);
        
        
    }
    
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GETでアクセスされたらフォーム画面に戻す
        response.sendRedirect(request.getContextPath() + "/jsp/register.jsp");
    }
    
    
    
}


