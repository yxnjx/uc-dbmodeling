package UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Table_exam2 extends JFrame {
	String[] title = { "이름", "나이", "주소" };
	JTextField[] jtf; //텍스트 입력 받음
	JTable jt;
	DefaultTableModel dtm; //얘가 있어야 추가, 수정, 삭제 가능(JTable에서는 불가능)
	
	public Table_exam2() {
		setTitle("테이블 연습 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(new northP(), BorderLayout.NORTH);
		add(new centerP(), BorderLayout.CENTER);
		add(new southP(), BorderLayout.SOUTH);
		
		setSize(500, 600); setVisible(true);
	}
	
	class northP extends JPanel { //JPanel - 기본적으로 FlowLayout(들어온 순서대로 들어감)
		public northP() {
			Vector<String> col = new Vector<String>();
			Vector<Vector<String>> rowdata = new Vector<Vector<String>>(); //2차원 배열은 Vector 안에 Vector를 넣음
			
			for(int i = 0; i < title.length; i++) {
				col.add(title[i]);
			}
			dtm = new DefaultTableModel(rowdata, col);
			jt = new JTable(dtm);
			JScrollPane jps = new JScrollPane(jt);
			add(jps); //JPanel에 추가
		}
	}
	
	class centerP extends JPanel {
		public centerP() {
			setLayout(new GridLayout(3, 2, 5, 5)); //3줄에 2칸, 여백 5씩
			jtf = new JTextField[3]; //JTextField를 담을 수 있는 배열만 생성
			
			for(int i = 0; i < jtf.length; i++) {
				add(new JLabel(title[i])); //label에 글자를 올려서 add해줌(바로 올릴 수 없어서)
				jtf[i] = new JTextField(10);
				add(jtf[i]);
			}
		}
	}
	
	class southP extends JPanel {
		public southP() {
			String[] menu = { "추가", "삭제" };
			JButton[] menubtn = new JButton[2]; //버튼을 넣을 수 있는 배열만 만들어짐
			
			for(int i = 0; i < menu.length; i++) {
				menubtn[i] = new JButton(menu[i]); //메뉴를 버튼에 넣음
				menubtn[i].addActionListener(new myAction()); //버튼 2개에 액션을 만듦(myAction 클래스로 이동)
				add(menubtn[i]);
			}
		}
	}
	
	class myAction implements ActionListener { //인터페이스 클래스라 implements로 상속받음
		@Override //꼭 오버라이드 해줘야 됨.
		public void actionPerformed(ActionEvent e) {
			//내가 받은 것의 원본을 가져옴(오브젝트로 가져오기 때문에 JButton으로 형변환 해줘야 됨.)
			JButton b = (JButton)e.getSource();
			DefaultTableModel m = (DefaultTableModel)jt.getModel(); //jt 값 가져와서 m에 저장
			
			switch(b.getText()) {
			//이름, 나이, 주소를 기억시켜서 2차원 배열에 저장시킴(벡터로)
			case "추가" : 
				Vector<String> v = new Vector<String>();
				
				for(int i = 0; i < jtf.length; i++) {
					v.add(jtf[i].getText()); 
				}
				m.addRow(v); //v의 값을 DefaultTableModel의 행에 추가
				for(int i = 0; i < 3; i++) jtf[i].setText(null); //값 입력 후 추가 버튼 누르면 입력한 값 지움
				jt.updateUI(); //화면 새로고침
				break;
				
			case "삭제" :
				int row = jt.getSelectedRow(); //선택한 jt의 행을 row에 저장
				
				if(row < 0) return; //행은 0부터 시작하기 때문에 0보다 작으면 return
				else m.removeRow(row); //0보다 크면 행을 삭제함
				
				break;
			}
		}
		
	}

	public static void main(String[] args) {
		new Table_exam2();
	}
}
