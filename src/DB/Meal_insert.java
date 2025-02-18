package DB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Meal_insert {
	public Meal_insert() {
		String insertmeal = "insert into meal values(?, ?, ?, ?, ?, ?)";
		Connection con = Driver_connect.makeConnection("meal"); //meal 데이터베이스 진입
		PreparedStatement psmt = null; //insert는 반복 작업이 많기 때문에 메모리를 많이 잡아먹지 않는 PreparedStatement를 사용
		Scanner fscanner = null;
		
		try {
			//파일 스캔
			fscanner = new Scanner(new FileInputStream("C:\\DBmodeling\\Meal_project\\DataFiles\\meal.txt"));
			psmt = con.prepareStatement(insertmeal); //preparedStatement를 생성할 때 sql문을 넣어줌.
			
			fscanner.nextLine(); //제목 줄 가져와서 버림
			
			while (fscanner.hasNext()) { //파일에서 다음 내용이 있을 때 true
				String line = fscanner.nextLine(); //한 줄씩 잘라서 line에 넣음
				System.out.println(line);
				
				StringTokenizer st = new StringTokenizer(line, "\t"); //line을 탭을 기준으로 자름
				
				int i = 1;
				while (st.hasMoreTokens()) { //토큰이 존재할 때 true
					psmt.setString(i, st.nextToken()); //i번째에 자른 토큰을 넣음.
					i++;
				}
				
				int re = psmt.executeUpdate(); //리턴 타입이 int이기 때문에 int타입의 re에 넣어줌
				if(re > 0) System.out.println("insert ok"); //re가 0보다 크면 insert가 진행됨.
				else System.out.println("insert error");
			}
		} catch (FileNotFoundException e) {
			System.out.println("file error");
		} catch (SQLException e) {
			System.out.println("sql error");
		}
	}

	public static void main(String[] args) {
		new Meal_table();
		new Meal_insert();
	}
}
