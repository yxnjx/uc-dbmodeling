package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver_connect {
	public static Connection makeConnection(String db) {
		String url = "jdbc:mysql://localhost:3308/" + db;
		String id = "root";
		String pwd = "1234";
		
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); //connection 연결
			System.out.println("ok");
			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("connect ok");
		} catch (ClassNotFoundException e) {
			System.out.println("connect error");
		} catch (SQLException e) {
			System.out.println("error");
		}
		return con;
	}

//	public static void main(String[] args) {
//		Driver_connect.makeConnection(""); //공백을 입력하면 localhost까지 진입
//										   //"madang" 입력하면 madang 데이터베이스 진입
//	}
}
