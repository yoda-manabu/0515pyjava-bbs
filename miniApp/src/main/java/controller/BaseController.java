package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * サーブレット共通の処理をまとめる基底クラス
 */
public abstract class BaseController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * JSP にフォワードする共通メソッド
     * 
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param path     JSP ファイルのパス（例: "board/board.jsp"）
     */
    protected void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/" + path);
        dispatcher.forward(req, res);
    }

}
