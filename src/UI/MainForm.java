package UI;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainForm extends JFrame { //JFrame 상속
	String[] s = {"사용자", "관리자", "사원등록", "종료"};
	JButton[] btns;
	
	public MainForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X 표시로 창 닫기
		setTitle("메인");
		Container c = getContentPane(); //컨텐츠 영역을 가져옴
		c.setLayout(new GridLayout(4, 1)); //가져온 컨텐츠 영역의 Layout 변경
										   //기본이 BorderLayout이기 때문에 GridLayout으로 변경
		
		btns = new JButton[s.length]; //s의 길이만큼 JButton 틀 만들어줌
		for(int i = 0; i < s.length; i++) {
			btns[i] = new JButton(s[i]); //버튼의 i번째에 s의 i번째 값을 넣어줌
			btns[i].addActionListener(new ActionListener() { //i번째 버튼 클릭 시 일어나는 이벤트
				@Override
				public void actionPerformed(ActionEvent e) {
					switch(e.getActionCommand()) { //e의 텍스트 값 가져옴
						case "사용자" : dispose(); new SelectForm(); break; //텍스트가 "사용자"일 때 SelectForm 이동
						case "관리자" : dispose(); break;
						case "사원등록" : dispose(); break;
						case "종료" : dispose(); break;
					}
				}
			});
			c.add(btns[i]); //컨테이너에 버튼 추가
		}
		setSize(250, 250); //프레임 크기
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainForm();
	}

}
