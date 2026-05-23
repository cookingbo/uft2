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

	// 日時一覧を表示
	public List<DataLog> findAll() {
	    Connection conn = null;
	    List<DataLog> dataLogList = new ArrayList<>();
	    try {
	        Class.forName(DRIVER_NAME);
	        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

	        // SQL実行（ここのSQLを間違えていました）
	        String sql = "SELECT WRDATE FROM DATA_LOG ORDER BY WRDATE ASC";
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

	public DataLog findByDate(String date) {
		DataLog dataLog = null;
		Connection conn = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

	        String sql = "SELECT WRDATE, ALLDATA FROM DATA_LOG WHERE WRDATE = ?";
	        PreparedStatement pStmt = conn.prepareStatement(sql);
	        pStmt.setString(1, date);
	        ResultSet rs = pStmt.executeQuery();

	        if (rs.next()) {
	            String wrDate = rs.getString("WRDATE");

	            // CLOB型を一旦 String として取得する
	            String clobData = rs.getString("ALLDATA");

	            dataLog = new DataLog(wrDate, clobData);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e){

			}
		}
		return dataLog;
	}

	// ファイルから読み込んだデータをDBに保存する
		public boolean insert(DataLog dataLog) {
			Connection conn = null;
			PreparedStatement pStmt = null;
			boolean result = false;

			try {
				Class.forName(DRIVER_NAME);
				conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

				// DATA_LOG テーブルに登録日時とデータ内容を挿入するSQL
				String sql = "INSERT INTO DATA_LOG (WRDATE, ALLDATA) VALUES (?, ?)";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, dataLog.getWrDate());

				// 既存の型（int）に合わせてセット
				pStmt.setString(2, dataLog.getAllData());

				// 実行された行数が1行以上であれば成功
				int rowsInserted = pStmt.executeUpdate();
				if (rowsInserted > 0) {
					result = true;
				}
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				// クローズ処理
				try {
					if (pStmt != null) pStmt.close();
					if (conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
}
