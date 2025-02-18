package UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DB.Driver_connect;

public class Table_exam3 extends JFrame {
	DefaultTableModel dtm;
	JTable jt;
	String[] menu = { "학생이름", "전공", "아이디" };
	String[] btmenu = { "확인", "추가", "수정", "삭제" };
	JButton[] btn;
	Vector<Vector<String>> rowdata; //2차원 벡터
	Vector<String> col;
	JTextField[] jf; //값 입력받을 수 있는 TextField
	
	//DB
	Connection con = Driver_connect.makeConnection("meal"); //meal 데이터베이스 진입
	Statement st;
	ResultSet rs;
	
	public Table_exam3() {
		setTitle("학생관리프로그램");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(new northP(), BorderLayout.NORTH);
		add(new centerP(), BorderLayout.CENTER);
		add(new southP(), BorderLayout.SOUTH);
		
		setSize(500, 600);
		setVisible(true);
	}
	
	class northP extends JPanel {
		public northP() {
			rowdata = new Vector<Vector<String>>(); //위에서는 선언만 했기 때문에 여기서 만들어줘야됨.(안 그러면 null)
			col = new Vector<String>();
			
			for(int i = 0; i < menu.length; i++) col.add(menu[i]);
			
			dtm = new DefaultTableModel(rowdata, col);
			
			jt = new JTable(dtm);
			jt.addMouseListener(new MouseAdapter() { //Adapter 클래스 - 메소드들을 필요한 것만 가지고 와서 쓸 수 있음.
				@Override
				public void mouseClicked(MouseEvent e) { //마우스르 클릭했을 때
					int select = jt.getSelectedRow(); //몇 번째 줄을 선택했는지 가져옴
					Vector<String> vc = rowdata.get(select); //벡터이기 때문에 get으로 가져올 수 있음.(배열이면 안 됨.)
					
					for(int i = 0; i < 3; i++) {
						jf[i].setText(vc.get(i)); //vc의 i번째 값을 가져와서 텍스트 필드에 넣음
					}
				}
			});
			add(new JScrollPane(jt));
		}
	}
	
	class centerP extends JPanel {
		public centerP() {
			setLayout(new GridLayout(3, 2, 5, 5));
			
			jf = new JTextField[3];
			for(int i = 0; i < menu.length; i++) {
				add(new JLabel(menu[i])); //menu의 i번째 텍스트를 label에 추가
				jf[i] = new JTextField(10); //10글자
				add(jf[i]);
			}
		}
	}
	
	class southP extends JPanel { //버튼
		public southP() {
			btn = new JButton[4]; //버튼 4개 넣을 수 있는 배열만 존재
			
			for(int i = 0; i < btn.length; i++) {
				btn[i] = new JButton(btmenu[i]); //btmenu의 i번째 텍스트 가져와서 버튼에 저장
				btn[i].addActionListener(new myAction());
				add(btn[i]);
			}
		}
	}
	
	class myAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			switch(b.getText()) {
				case "확인" :
					listStudent(); break;
				case "추가" :
					insertStudent(); listStudent(); break;
				case "수정" :
					updateStudent(); listStudent(); break;
				case "삭제" :
					deleteStudent(); listStudent(); break;
			}
			jfclear();
		}
	}
	
	public void jfclear() {
		for(int i = 0; i < jf.length; i++) {
			jf[i].setText(""); //텍스트 필드에 있는 텍스트를 지워줌
		}
	}

	public void listStudent() {
		String sql = "select * from student";
		try {
			rowdata.clear(); //기존에 있던 벡터를 지우고 다시 가져와서 리스트 띄워줌
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				Vector<String> v = new Vector<String>(); //한 번 가져올 때마다 새로운 벡터 만들어 처리
				for(int i = 0; i < 3; i++) {
					v.add(rs.getString(i + 1)); //0부터 시작하기 때문에 +1
				}
				rowdata.add(v);
			}
			jt.updateUI(); //화면 새로고침
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertStudent() {
		Vector<String> v = new Vector<String>();
		
		for(int i = 0; i < jf.length; i++) {
			v.add(jf[i].getText()); //테이블의 글자를 가져와서 추가
		}
		String sql = "insert into student values(?, ?, ?)";
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			
			for(int i = 0; i < 3; i++) {
				psmt.setString(i + 1, v.get(i)); 
				//0번째 - 이름, 1번째 - 과, 2번째 - 이름(+1 해서 가져와서 1번째부터 들어감)
			}
			
			int re = psmt.executeUpdate();
			if(re > 0) {
				JOptionPane.showMessageDialog(this, "등록완료"); //등록완료 알림창 띄우기
			} else {
				JOptionPane.showMessageDialog(this, "등록실패");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateStudent() {
		Vector<String> v = new Vector<String>();
		
		for(int i = 0; i < jf.length; i++) {
			v.add(jf[i].getText()); //테이블의 글자를 가져와서 벡터에 추가
		}
		
		String sql = "update student set name = ?, dep = ? where id = ?";
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			
			for(int i = 0; i < 3; i++) {
				psmt.setString(i + 1, v.get(i)); //0부터 시작하기 때문에 +1 해줌
			}
			
			int re = psmt.executeUpdate();
			if(re > 0) {
				JOptionPane.showMessageDialog(this, "수정완료"); //수정완료 알림창 띄우기
			} else {
				JOptionPane.showMessageDialog(this, "수정실패");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStudent() {
		String id = jf[2].getText();
		String sql = "delete from student where id = ?";
		
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			
			int re = psmt.executeUpdate();
			if(re > 0) {
				JOptionPane.showMessageDialog(this, "삭제완료"); //삭제완료 알림창 띄우기
			} else {
				JOptionPane.showMessageDialog(this, "삭제실패");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Table_exam3();
	}
}