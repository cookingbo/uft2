package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DataLogDAO;
import model.DataLog;
import model.GetDataLogListLogic;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--- doGetが動き出しました！ ---"); // これを追加
	    // ロジックからリストを取得
	    GetDataLogListLogic getDataLogListLogic = new GetDataLogListLogic();
	    List<DataLog> dataLogList = getDataLogListLogic.execute();
	    System.out.println("取得件数:" + dataLogList.size());

	    // 【重要】ここでセットした名前 "dataLogList" がJSP側と一致している必要があります
	    request.setAttribute("dataLogList", dataLogList);

	    // フォワード先を統一します
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/index.jsp");
	    dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	    System.out.println("--- doPostが動き出しました！ ---");
	    // hiddenフィールドの名前(name属性)で取得
	    String date1 = request.getParameter("date1");
	    String date2 = request.getParameter("date2");

	    if (date1 != null && date2 != null) {
	    	DataLogDAO dao = new DataLogDAO();
	    	DataLog log1 = dao.findByDate(date1);
	    	DataLog log2 = dao.findByDate(date2);
	    	System.out.println("受け取ったdate1: [" + date1 + "]");
	    	System.out.println("受け取ったdate2: [" + date2 + "]");

	    	if (log1 != null && log2 != null) {
	    		// ファイル出力処理
	    		response.setContentType("text/plain; charset=UTF-8");
	    		response.setHeader("Content-Disposition", "attachment; filename=\"data_export.txt\"");

	    		try(PrintWriter out = response.getWriter()) {
	    			out.println("選択データ1: " + log1.getWrDate() + "[ALLDATA: " + log1.getAllData() + "]");
	    			out.println("選択データ2: " + log2.getWrDate() + "[ALLDATA: " + log2.getAllData() + "]");
	    		}
	    		return;
	    	}
	    }
	    response.sendRedirect("Main");



//	    // POST時もデータを再取得して表示させる必要があります（再表示する場合）
//	    GetDataLogListLogic getDataLogListLogic = new GetDataLogListLogic();
//	    List<DataLog> dataLogList = getDataLogListLogic.execute();
//	    request.setAttribute("dataLogList", dataLogList);
//
//	    // 【修正】doGetと同じ index.jsp にフォワードさせます
//	    // WEB-INF/jsp/main.jsp になっていると、そちらのJSPにリストを渡す設定が必要です
//	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/index.jsp");
//	    dispatcher.forward(request, response);
	}
}
