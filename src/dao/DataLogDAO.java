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
	    Connection conn = null;
	    List<DataLog> dataLogList = new ArrayList<>(); // ダイヤモンド演算子でスッキリ書けます
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

	            int allData = 0;
	            if (clobData != null && !clobData.isEmpty()) {
	                try {
	                    // 文字列を数値（int）に変換する
	                    allData = Integer.parseInt(clobData.trim());
	                } catch (NumberFormatException e) {
	                    // ALLDATAの中身が数字以外だった場合のログ
	                    System.out.println("数値変換エラー: WRDATE=" + wrDate + " のデータは数値ではありません。");
	                }
	            }

	            dataLog = new DataLog(wrDate, allData);
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
}
