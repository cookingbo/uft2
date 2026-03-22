package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DataLog;
import model.GetDataLogListLogic;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // ロジックからリストを取得
	    GetDataLogListLogic getDataLogListLogic = new GetDataLogListLogic();
	    List<DataLog> dataLogList = getDataLogListLogic.execute();

	    // 【重要】ここでセットした名前 "dataLogList" がJSP側と一致している必要があります
	    request.setAttribute("dataLogList", dataLogList);

	    // フォワード先を統一します
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
	    dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");

	    // POST時もデータを再取得して表示させる必要があります（再表示する場合）
	    GetDataLogListLogic getDataLogListLogic = new GetDataLogListLogic();
	    List<DataLog> dataLogList = getDataLogListLogic.execute();
	    request.setAttribute("dataLogList", dataLogList);

	    // 【修正】doGetと同じ index.jsp にフォワードさせます
	    // WEB-INF/jsp/main.jsp になっていると、そちらのJSPにリストを渡す設定が必要です
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
	    dispatcher.forward(request, response);
	}
}
