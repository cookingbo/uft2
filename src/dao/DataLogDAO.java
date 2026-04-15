package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.DataLog;

public class DataLogDAO {
	private final String DRIVER_NAME = "org.h2.Driver";
	private final String JDBC_URL = "jdbc:h2:~/uft2";
	private final String DB_USER = "sa";
	private final String DB_PASS = "";

	public List<DataLog> findAll() {
	    // 1. ドライバクラス名を確認 (H2のバージョンにより org.h2.Driver でOK)
	    // 2. 接続URLに IFEXISTS=TRUE をつけると、DBがない場合にエラーが出るので切り分けに便利
	    // String JDBC_URL = "jdbc:h2:~/uft2;IFEXISTS=TRUE";


	    Connection conn = null;
	    List<DataLog> dataLogList = new ArrayList<>(); // ダイヤモンド演算子でスッキリ書けます
	    try {
	        Class.forName(DRIVER_NAME);
	        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

	        // SQL実行（ここは問題ありません）
	        String sql = "SELECT WRDATE FROM DATA_LOG ORDER BY ID DESC";
	        PreparedStatement pStmt = conn.prepareStatement(sql);
	        ResultSet rs = pStmt.executeQuery();

	        while(rs.next()) {
	            // カラム名はDBの定義（大文字小文字）に合わせて取得
	            String wrDate = rs.getString("WRDATE");
	            DataLog dataLog = new DataLog(wrDate);
	            dataLogList.add(dataLog);
	        }
	    } catch(SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // ここで null を返すと JSP で「データが存在しません」になります
	        return dataLogList; // エラー時も空のリストを返すとNullPointerExceptionを防げます
	    } finally {
	        // ...（接続クローズ処理は現状のままでOKです）
	    }
	    return dataLogList;
	}

//	public boolean create(Mutter mutter) {
//		Connection conn = null;
//		try {
//			// データベースへ接続
//
//			conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS);
//
//			// INSERT文の準備（idは自動連番なので指定しなくてよい）
//			String sql = "INSERT INTO MUTTER(NAME, TEXT) VALUES(?, ?)";
//			PreparedStatement pStmt = conn.prepareStatement(sql);
//			// INSERT文中の「?」に使用する値を設定し、SQLを完成
//			pStmt.setString(1, mutter.getUserName());
//			pStmt.setString(2, mutter.getText());
//
//			// INSERT文を実行
//			int result = pStmt.executeUpdate();
//
//			if(result != 1) {
//				return false;
//			}
//		} catch(SQLException e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			// データベース切断
//			if(conn != null) {
//				try {
//					conn.close();
//				} catch(SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return true;
//	}
}
