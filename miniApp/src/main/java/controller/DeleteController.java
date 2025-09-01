package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.User;
import service.UserDeleteService;

@WebServlet("/delete")
public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		RequestDispatcher rd =
				req.getRequestDispatcher("/jsp/deleteConfirm.jsp");
		rd.forward(req,res);

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String loginId = req.getParameter("deleteLoginId");
		
		User user = new User(loginId,null,null);
		
		UserDeleteService deleteService = new UserDeleteService();
		boolean result = deleteService.userDeleteDo(user);
		

		if(result == true) {
			HttpSession session = req.getSession();
			session.invalidate();
			
			RequestDispatcher rd =
					req.getRequestDispatcher("/jsp/deleteDone.jsp");
			rd.forward(req,res);
		} else {
			req.setAttribute("deleteError","登録情報の削除に失敗しました。");
			
			RequestDispatcher rd=
					req.getRequestDispatcher("/jsp/deleteConfirm.jsp");
			rd.forward(req,res);
		}
	}
}
