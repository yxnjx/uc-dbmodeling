package UI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Table_exam extends JFrame {
	//JTable(행들 들어 있는 벡터(배열) - 2차원, (제목)열 들어 있는 벡터(배열) - 1차원)
	JTable jt;
	
	public Table_exam() {
		setTitle("테이블 만들기 연습");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String[] col = { "이름", "나이", "주소" }; //속성명(1차원 배열)
		String[][] data = { {"kim", "15", "seoul"}, {"lee", "19", "ulsan"} }; //2차원 배열
		
		jt = new JTable(data, col);
		JScrollPane jps = new JScrollPane(jt); //내용이 많으면 스크롤바로 볼 수 있음.
		JButton btn = new JButton("확인");
		add(btn, BorderLayout.SOUTH); //확인 버튼 south에 위치
		add(jps, BorderLayout.CENTER); //jps는 center에 위치
		
		setSize(500, 300);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Table_exam();
	}

}
