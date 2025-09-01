package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.User;
import service.UserEditService;

@WebServlet("/editConfirm")
public class EditConfirmController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		String loginId = req.getParameter("loginId");
		String password = req.getParameter("editPassword");
		String name = req.getParameter("editName");
		
		// 更新後の会員情報をDomainに格納
		User user =new User(loginId,password,name);
		
		UserEditService editService =new UserEditService();
		boolean result = editService.userEditDo(user);
		
		// 更新に成功した場合はresultにtrueが格納されている。
		if(result == true) {
			RequestDispatcher rd =req.getRequestDispatcher("/jsp/editDone.jsp");
			rd.forward(req,res);
		}else {
			
			// 会員情報の更新に失敗した場合はエラーメッセージを用意して編集画面へ戻す
			req.setAttribute("editError", "メンバー情報の更新に失敗しました。");
			RequestDispatcher rd = req.getRequestDispatcher("/jsp/edit.jsp");
			rd.forward(req,res);
		}
	}
}	
