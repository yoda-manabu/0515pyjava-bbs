//DB接続処理の基盤。

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {
	final String DB_URL = "jdbc:postgresql://localhost:5433/postgres";
	final String DB_USER = "postgres";
	final String DB_PASSWORD = "password";
	private Connection conn;
	
	public Connection getConnection() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
}
