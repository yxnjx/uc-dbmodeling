package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
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

public class Meal_menu extends JFrame {
	int n;
	String[] select = { "한식", "중식", "일식", "양식" };
	Connection con; //db 연결 위한 connection
	Statement st = null; //sql문을 dbms에 전달

	//배열 크기에 따라 변하는 Vector
	Vector<String> nv = new Vector<String>(); //mealNo
	Vector<String> mv = new Vector<String>(); //mealName
	Vector<String> pv = new Vector<String>(); //price
	Vector<String> cv = new Vector<String>(); //maxCount
	Vector<bt> btv = new Vector<bt>(); //button(메뉴 버튼 넣어주는 벡터)
	
	JLabel p; //총 금액표시 레이블
	DecimalFormat df = new DecimalFormat("#,##0"); //천단위 구분기호 (리턴타입 double)
	double price = 0; //총금액 //천단위 구분기호의 리턴타입이 double이기 때문에 double로 초기화 해줌
	
	Vector<String> col = new Vector<String>() ;
	Vector<Vector<String>> row = new Vector<Vector<String>>();
	DefaultTableModel model;
	JTable jt;
	
	JTextField[] tf = new JTextField[2]; //텍스트필드의 배열 개수 = 2
	JButton[] nbt = new JButton[10]; //0~9 버튼
	JButton[] bt = new JButton[2]; //입력, 초기화 버튼
	
	//생성자
	public Meal_menu(int n) {
		this.n = n;
		con = Driver_connect.makeConnection("meal"); //meal 데이터베이스로 이동(연결)
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //x 표시로 창 닫기
		setTitle("결제");
		add(new NorthPanel(), BorderLayout.NORTH);
		add(new CenterPanel(), BorderLayout.CENTER);
		
		setSize(1000, 600);
		setVisible(true);
	}
	
	class NorthPanel extends JPanel { //클릭한 메뉴에 맞는 text 출력(한식.. 등) panel
		public NorthPanel() {
			setBackground(Color.white);
			JLabel title = new JLabel(select[n - 1]); //select 배열은 0부터 시작하기 때문에 -1을 해줘야 됨.
			title.setFont(new Font("Sanserif", Font.BOLD, 25));
			add(title);
		}
	}
	
	class CenterPanel extends JPanel {
		public CenterPanel() {
			setLayout(new GridLayout(1, 2)); //1줄에 2칸(여백 x)
			
			//GridLayout에서 왼쪽, 오른쪽으로 나눔
			add(new CenterLeft()); 
			add(new CenterRight());
		}
	}
	
	class CenterLeft extends JPanel { //클릭한 메뉴에 맞는 메뉴 panel
		public CenterLeft() { //한식 = 1, 중식 2 ... 
			String selectmenu = "select mealNo, mealName, price, maxCount, todayMeal from meal "
					+ "where cuisineNo = " + n + " and todayMeal = 1";
			
			int row;
			try {
				st = con.createStatement();
				ResultSet rs = st.executeQuery(selectmenu); //ResultSet = select문 결과 저장하는 객체
				
				while(rs.next()) { //다음이 있을 때까지 반복
					//System.out.println(rs.getString(2) + rs.getString(4));
					nv.add(rs.getString(1));
					mv.add(rs.getString(2));
					pv.add(rs.getString(3));
					cv.add(rs.getString(4));
				}
				
//				for(int i = 0; i < mv.size(); i++) {
//					System.out.println(mv.get(i)); //mv에 저장돼 있는 값 출력
//				}
				
				if(mv.size() % 5 == 0) row = mv.size() / 5;
				else row = mv.size() / 5 + 1; //나머지가 있는 경우는 (몫 + 1)줄 만듦
				
				setLayout(new GridLayout(row, 5)); //5줄에 5칸
				
				for(int i = 0; i < mv.size(); i++) { //mv 크기만큼 버튼 하나씩 만듦. (bt 클래스)
					//이름, 가격, 개수 가져와서 버튼으로 변경 -> btv 벡터에 추가
					btv.add(new bt(mv.get(i), pv.get(i), cv.get(i))); 
					add(btv.get(i)); //화면에 버튼 던져줌
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	class bt extends JButton {
		String m, p, c;
		
		public bt(String m, String p, String c) { //이름, 가격, 개수 가져옴
			this.m = m; this.p = p; this.c = c;
			this.setText("<html><center>" + m + "<br><br>" + p + "원</center></html>"); //이름만 출력
			
			if(c.equals("0")) this.setEnabled(false); //개수 글자가 0이면 비활성화
			
			//버튼 액션 마우스 이벤트 처리하기
			this.addMouseListener(new mbtAL());
		}
		
		public String getM() {
			return m;
		}
	}
	
	class mbtAL extends MouseAdapter {
		//mouseclick 보다는 우선으로 선택되기 때문에 mousePressed가 더 많이 쓰임.
		//mouseClick은 그 객체를 딱 선택해야 됨. mousePressed는 좀 더 유하게(?) 선택됨.
		@Override
		public void mousePressed(MouseEvent e) { 
			JButton z = (JButton)e.getSource();
			String tmp = z.getText();
			StringTokenizer t = new StringTokenizer(tmp, "<html><center>");
			tf[0].setText(t.nextToken());
		}
	}
	
	class CenterRight extends JPanel {
		public CenterRight() {
			setLayout(new GridLayout(2, 1)); //2줄에 1칸
			add(new CenterRigth1()); //위 Grid
			add(new CenterRigth2()); //아래 Grid
		}
	}
	
	//중앙의 오른쪽 위
	class CenterRigth1 extends JPanel {
		public CenterRigth1() {
			setLayout(new BorderLayout());
			add(new CR1N(), BorderLayout.NORTH); //CenterRight1 North
			add(new CR1C(), BorderLayout.CENTER); //CenterRight1 Center
			add(new CR1S(), BorderLayout.SOUTH); //CenterRight1 South
		}
	}
	
	//Center - 오른쪽 - 위 - north
	class CR1N extends JPanel {
		public CR1N() {
			setLayout(new GridLayout(1, 2)); //1줄에 2칸
			JLabel allprice = new JLabel("총 결제금액");
			allprice.setFont(new Font("돋움체", Font.BOLD, 20));
			
			p = new JLabel(df.format(price) + "원");
			p.setFont(new Font("돋움체", Font.BOLD, 20));
			
			allprice.setHorizontalAlignment(JLabel.LEFT); //JLabel의 왼쪽으로 정렬
			p.setHorizontalAlignment(JLabel.RIGHT); //JLabel의 오른쪽으로 정렬
			
			add(allprice); add(p);
		}
	}
	
	//Center - 오른쪽 - 위 - center
	class CR1C extends JPanel {
		public CR1C() {
			setLayout(new GridLayout(1, 1));
			String[] m = { "상품번호", "품명", "수량", "금액" };
			for(int i = 0; i < m.length; i++) {
				col.add(m[i]);
			}
			
			model = new DefaultTableModel(row, col);
			jt = new JTable(model);
			jt.addMouseListener(new jtAL());
			JScrollPane jps = new JScrollPane(jt);
			add(jps);
		}
	}
	
	//테이블 더블 클릭 시 동작처리
	//MouseAdapter는 class이기 때문에 extends로 상속받음
	class jtAL extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			int srow = jt.getSelectedRow(); //행을 가져와서 srow에 저장
			if(e.getClickCount() == 2) { //몇 번 클릭했는지(2번 클릭했으면)
				for(int i = 0; i < mv.size(); i++) {
					//모델 객체(2차원 배열(row, col))의 몇 번째 줄의 몇 번째 칸의 텍스트를 가져와서 비교
					if(mv.get(i).equals(model.getValueAt(srow, 1))) {//내가 더블클릭한 행의 품명이 버튼 이름과 같으면
						btv.get(i).setEnabled(true); //비활성화된 메뉴를 다시 활성화
						
						int restoreCount = Integer.parseInt((String)model.getValueAt(srow, 2)); //선택한 행의 수량
	                    int newCount = Integer.parseInt(cv.get(i)) + restoreCount; //조리가능수량 복원
	                    cv.set(i, Integer.toString(newCount)); //cv 벡터 업데이트
	                    
	                    // 데이터베이스에 조리가능수량 update
	                    String updateQuery = "UPDATE meal SET maxCount = " + newCount + " WHERE mealNo = " + nv.get(i);
	                    try {
	                        st = con.createStatement();
	                        st.executeUpdate(updateQuery);
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                    }
	                    
						break;
					}
				}
				int del_price = Integer.parseInt((String)model.getValueAt(srow, 3)); //선택한 행의 가격
				price -= del_price; //총금액에서 내가 선택한 행의 금액을 뺌
				p.setText(df.format(price) + "원");
				model.removeRow(srow); //더블클릭한 행을 지움
			}
		}
	}
	
	//Center - 오른쪽 - 위 - south
	class CR1S extends JPanel {
		public CR1S() {
			String[] t = { "선택품명", "수량" };
			
			for(int i = 0; i < t.length; i++) {
				add(new JLabel(t[i])); //JLabel에 t배열의 글자가 들어감
				tf[i] = new JTextField(15); //배열을 만든 후 add 해줘야 됨.
				add(tf[i]);
			}
		}
	}
	
	//중앙의 오른쪽 아래
	class CenterRigth2 extends JPanel {
		public CenterRigth2() {
			setLayout(new BorderLayout());
			add(new CR2C(), BorderLayout.CENTER); //0~9버튼
			add(new CR2E(), BorderLayout.EAST); //입력, 초기화 버튼
		}
	}
	
	//Center - 오른쪽 - 아래 - center
	class CR2C extends JPanel {
		public CR2C() {
			//9개의 버튼이 들어가는 center, 입력 버튼이 들어가는 east
			setLayout(new BorderLayout());
			nbt[9] = new JButton("0"); //버튼 배열의 9번째에는 "0"이 들어감.
			nbt[9].addActionListener(new nbtAL());
			
			add(new CR2CC(), BorderLayout.CENTER); //1~9 버튼
			add(nbt[9], BorderLayout.SOUTH); //0 버튼(클래스를 따로 만들지 않고 9번째 값을 바로 넣어줌)
		}
	}
	
	//Center - 오른쪽 - 아래 - center - center(버튼 1~9)
	class CR2CC extends JPanel {
		public CR2CC() {
			//1~9버튼
			setLayout(new GridLayout(3, 3)); //1~9 버튼이 들어가니 3X3
			
			for(int i = 0; i < nbt.length - 1; i++) { //1~9까지 먼저 넣고 0을 제일 마지막에 넣음(1버튼은 0)
				nbt[i] = new JButton(Integer.toString(i + 1)); //버튼에 숫자를 String으로 바꿔서 넣음(i+1해줘야 1이 들어감)
				nbt[i].addActionListener(new nbtAL()); //숫자 버튼의 액션
				add(nbt[i]);
			}
			
		}
	}
	
	//숫자 버튼 액션
	class nbtAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String tmp = tf[1].getText() + e.getActionCommand(); //원래 있던 숫자의 텍스트를 가져와서 더해줌.
			if(tmp.length() < 3) tf[1].setText(tmp); //3글자 이상 안 들어감.
		}
	}
	
	//Center - 오른쪽 - 아래 - east(입력, 초기화 버튼)
	class CR2E extends JPanel {
		public CR2E() {
			setLayout(new BorderLayout());
			String[] btname = { "입력", "초기화" };
			
			for(int i = 0; i < bt.length; i++) {
				bt[i] = new JButton(btname[i]);
				bt[i].addActionListener(new btAL()); //입력, 초기화 버튼 액션
			}
			add(bt[0], BorderLayout.CENTER);
			add(bt[1], BorderLayout.SOUTH);
		}
	}
	
	//입력, 초기화 버튼 액션
	class btAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
			case "입력" : isInsertJt(); break;
			case "초기화" : 
				for(int i = 0; i < tf.length; i++) {
					tf[i].setText("");
				}
				break;
			}
		}
	}
	
	public void isInsertJt() {
		if(tf[0].getText().equals("")) //선택 품명 텍스트필드가 공백일 때(아무것도 입력하지 않았을 때)
			//null, 출력 메세지, 메세지 제목, 메세지 종류(에러, 경고 등)
			JOptionPane.showMessageDialog(null, "품명을 선택해주세요.", "Message", JOptionPane.ERROR_MESSAGE);
		else if(tf[1].getText().equals("")) //수량 텍스트필드가 공백일 때
			JOptionPane.showMessageDialog(null, "수량을 지정해주세요.", "Message", JOptionPane.ERROR_MESSAGE);
		else {
			for(int i = 0; i < btv.size(); i++) {
				if(tf[0].getText().equals(mv.get(i))) { //품명의 이름과 선택 품명 텍스트 필드가 같으면
					//내가 입력한 수량이 조리 가능 수량보다 크면
					if(Integer.parseInt(tf[1].getText()) > Integer.parseInt(cv.get(i))) { 
						JOptionPane.showMessageDialog(null, "조리가능수량을 초과하였습니다.", 
								"Message", JOptionPane.ERROR_MESSAGE);
					} else { //조리 가능 수량에 맞게 수량을 입력했을 때
							Vector<String> v = new Vector<String>();
								
							//금액 계산(수량 * 메뉴 금액(String이기 때문에 int로 바꿔줌))
							int menup = Integer.parseInt(tf[1].getText()) * Integer.parseInt(pv.get(i));
							price += menup; //총 금액
							p.setText(df.format(price) + "원");
								
							v.add(nv.get(i)); //상품 번호
							v.add(mv.get(i)); //메뉴 이름
							v.add(tf[1].getText()); //내가 입력한 수량을 가져옴.
							v.add(Integer.toString(menup)); //menup는 int, Vector는 String이라 toString 사용해서 형변환 해줌
							row.add(v);
							btv.get(i).setEnabled(false); //선택한 메뉴는 비활성화 시켜줌.
							
							//----------과제 플러스 점수----------
							//데이터베이스에 조리가능수량 update
							int newCount = Integer.parseInt(cv.get(i)) - Integer.parseInt(tf[1].getText());
		                    cv.set(i, Integer.toString(newCount)); // cv 벡터 업데이트
		                  
		                    String updateQuery = "UPDATE meal SET maxCount = " + newCount + " WHERE mealNo = " + nv.get(i); 
		                    try {
		                        st = con.createStatement();
		                        st.executeUpdate(updateQuery);
		                    } catch (SQLException ex) {
		                        ex.printStackTrace();
		                    }
						}
						jt.updateUI();
				}
			}
		}
		for(int i = 0; i < tf.length; i++) { //입력 버튼 누른 뒤 텍스트 필드 초기화
			tf[i].setText("");
		}
	}
}