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

	    // hiddenフィールドから選択された2つの日時を取得
	    String date1 = request.getParameter("date1");
	    String date2 = request.getParameter("date2");

	    // 【テスト用：ここを追加】送られてきたデータに関わらず、強制的に不正な形式にする
	    // date1 = "2026/05/24 22:00"; // スラッシュ区切り（不正な形式）
	    // date2 = "あいうえお";         // まったく関係ない文字列（不正な形式）

	    System.out.println("[サーバー検証] 受信データ - date1: [" + date1 + "], date2: [" + date2 + "]");

	    // バリデーション結果を管理するフラグ
	    boolean isValid = true;

	    // 1. まず必須入力・重複のチェック
	    if (date1 == null || date1.trim().isEmpty() || date2 == null || date2.trim().isEmpty() || date1.equals(date2)) {
	        System.out.println("[サーバー検証エラー] 日時が2つ正しく選択されていない、または同じ日時です。");
	        isValid = false;
	    }

	    // 2. 日時のフォーマット（yyyy-MM-dd HH:mm:ss）の厳密チェック
	    if (isValid) {
	        // チェック用のフォーマッタを用意
	        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	        try {
	            // 送られてきた文字列を解析（パース）してみる
	            java.time.LocalDateTime.parse(date1.trim(), formatter);
	            java.time.LocalDateTime.parse(date2.trim(), formatter);
	            System.out.println("[サーバー検証] 日時のフォーマット形式は正常です。");
	        } catch (java.time.format.DateTimeParseException e) {
	            // 形式が一致しない場合はここに入ります
	            System.out.println("[サーバー検証エラー] 日時の形式が yyyy-mm-dd hh:mm:ss ではありません。原因: " + e.getMessage());
	            request.setAttribute("errorMsg", "日時の形式が正しくありません。yyyy-mm-dd hh:mm:ss の形式で選択してください。");
	            isValid = false;
	        }
	    }

	    // 検証NG（不正なデータ）だった場合は、処理を中断して安全に画面を戻す
	    // 開発者ツールでdocument.getElementById('hidden-date1').value = '2026/05/24 10:00'; をコンソールに記載し、比較出力ボタンを押す
	    if (!isValid) {
	        System.out.println("[サーバー検証結果] 危険なデータ、または不正なデータを検知したため、処理を中断します。");

	        // エラー通知用にメッセージを詰める（任意）
	        request.setAttribute("errorMsg", "選択されたデータの形式が正しくありません。");

	        // 再度一覧データを取得し直して、安全に元の画面(index.jsp)へ戻す
	        dao.DataLogDAO dao = new dao.DataLogDAO();
	        List<model.DataLog> dataLogList = dao.findAll();
	        request.setAttribute("dataLogList", dataLogList);

	        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	        return; // 【最重要】ここで終了させ、以降のファイル出力・DB検索処理を実行させない
	    }

	    	DataLogDAO dao = new DataLogDAO();
	    	DataLog log1 = dao.findByDate(date1);
	    	DataLog log2 = dao.findByDate(date2);
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

	    response.sendRedirect("Main");
	}
}
