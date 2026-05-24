package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.DataLogDAO;
import model.DataLog;

@WebServlet("/FileRegister")
@MultipartConfig // 【重要】ファイルアップロードを有効にするアノテーション
public class FileRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("--- ファイル登録のdoPostが動き出しました！ ---");
		String actionType = request.getParameter("actionType");

		if ("upload".equals(actionType)) {
			try {
				// name="uploadFile" のパーツを取得
				Part filePart = request.getPart("uploadFile");

				if (filePart != null && filePart.getSize() > 0) {
					// 修正後：明示的に "\n" で結合し、重複した余分な改行を排除する
					String fileContent;
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), "UTF-8"))) {
					    fileContent = reader.lines()
					                        .map(line -> line.replace("\r", "")) // 行データに \r が残っていたら完全に消去
					                        .collect(Collectors.joining("\n"));  // 綺麗な \n だけで行を結合
					}

					// 現在の日時（String型）を生成
					// ※既存のDB設計に合わせてStringで生成しています。必要に応じてフォーマットを変更してください。
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentDateTime = sdf.format(new java.util.Date());

					// 【修正】fileContent（文字列）をそのままDataLogモデルにセットします
					DataLog dataLog = new DataLog(currentDateTime, fileContent);

					// DAOを使ってDBに保存
					DataLogDAO dao = new DataLogDAO();
					boolean isSuccess = dao.insert(dataLog);

					if (isSuccess) {
						System.out.println("DBへの保存に成功しました: " + currentDateTime);
					} else {
						System.out.println("DBへの保存に失敗しました。");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 処理が終わったらメイン画面（一覧）にリダイレクトして戻る
		response.sendRedirect("Main");
	}
}