package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Meal_table {
	public Meal_table() {
		String createMealDB = "create database if not exists Meal";
		String createMeal = "create table if not exists meal("
				+ "mealNo int not null auto_increment, "
				+ "cuisineNo int, mealName varchar(20), "
				+ "price int, maxCount int, todayMeal tinyint(1), "
				+ "primary key(mealNo))";
		
		Connection con = Driver_connect.makeConnection(""); //""이어야 db를 생성할 수 있음.
															//이미 db에 진입한 상태에서는 db 생성 불가
		
		Statement st = null;
		
		try {
			st = con.createStatement(); //sql문을 dbms에 전달하는 Statement 생성
			st.executeUpdate(createMealDB); //Meal 데이터베이스 생성
			st.executeUpdate("use Meal"); //Meal 데이터베이스 사용
			st.executeUpdate(createMeal); //meal 테이블 생성
		} catch (SQLException e) {
			System.out.println("db make error");
		}
	}

//	public static void main(String[] args) {
//		new Meal_table();
//	}
}
