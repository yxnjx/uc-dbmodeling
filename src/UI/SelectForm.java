package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class SelectForm extends JFrame { //public class는 1개밖에 못 만듦
	//이미지(모든 클래스가 다 사용할 수 있게 부모 클래스에 작성)
	String[] img = { "menu_1.png", "menu_2.png", "menu_3.png", "menu_4.png" };
	ImageIcon[] imgs; //이미지 파일로부터 이미지를 읽어옴
	JButton[] btns;
	String[] menu = { "한식", "중식", "일식", "양식" }; //툴팁 배열(이미지 위에 마우스 커서 올렸을 때 뜨는 글자)
	int choicemenu = 0;
	
	//생성자
	public SelectForm() { 
		setTitle("식권 발매 프로그램");
		Container c = getContentPane();
		c.add(new NorthP(), BorderLayout.NORTH); //JFrame은 기본적으로 BorderLayout이라 변경할 필요X
		c.add(new CenterP(), BorderLayout.CENTER);
		c.add(new SouthP(), BorderLayout.SOUTH);
		
		setSize(500, 800);
		setVisible(true);
	}
	
	//title 들어가는 패널
	class NorthP extends JPanel { 
		//생성자
		public NorthP() {
			JLabel label = new JLabel("식권 발매 프로그램");
			label.setFont(new Font("SanSerif", Font.BOLD, 30)); //폰트 설정
			add(label); //label 추가
		}
	}
	
	//이미지 들어가는 패널(4개)
	class CenterP extends JPanel { 
		public CenterP() {
			JTabbedPane tab = new JTabbedPane();
			tab.add("메뉴", new ImgP());
			add(tab);
		}
	}
	
	class ImgP extends JPanel { //기본적으로 Flow Layout
		public ImgP() {
			setLayout(new GridLayout(2, 2)); //FlowLayout -> GridLayout(2X2, 여백 없음)
			imgs = new ImageIcon[img.length]; //이미지 배열의 길이만큼 이미지 아이콘 만듦(비어 있음)
			btns = new JButton[img.length]; //이미지를 만들어서 버튼에 넣어줌
			
			for(int i = 0; i < img.length; i++) {
				//가져올 이미지가 다르기 때문에 img배열의 i번째 이미지를 가져오도록 + img[i]를 해줌
				imgs[i] = new ImageIcon("C:\\DBmodeling\\Meal_project\\DataFiles\\" + img[i]);
				btns[i] = new JButton(imgs[i]);
				btns[i].setToolTipText(menu[i]); //툴팁 설정
				btns[i].addActionListener(new myAction()); //버튼을 클릭하면 myAction이라는 액션 실행(버튼 4개 다)
				this.add(btns[i]); //버튼을 올림
			}
		}
		
		class myAction implements ActionListener { //클래스를 상속 받으려면 extends, 클래스처럼 생긴 인터페이스를 상속 받을 땐 implements
			 //ActionListener 인터페이스 메소드를 꼭 오버라이드 해줘야 됨.
			@Override
			public void actionPerformed(ActionEvent e) { //내가 누른 정보를 e가 가지고 있음.
				JButton b = (JButton)e.getSource(); //형변환(source가 버튼이라는 걸 모르기 때문에)
				//System.out.println(b.getToolTipText());
				
				for(int k = 0; k < menu.length; k++) {
					if(menu[k].equals(b.getToolTipText())) { //클릭한 메뉴의 글자가 툴팁 텍스트와 같을 때 choicemenu 증가
						choicemenu = k + 1;
						break;
					}
				}
				new Meal_menu(choicemenu); //Meal_menu로 이동
				dispose(); //창 닫기
			}
		}
	}
	
	//시간 들어가는 패널
	class SouthP extends JPanel { 
		public SouthP() {
			this.setBackground(Color.DARK_GRAY);
			JLabel timelabel = new JLabel();
			timelabel.setFont(new Font("Sanserif", Font.BOLD, 20));
			timelabel.setForeground(Color.CYAN); //글자 색
			this.add(timelabel);
			
			//Thread 생성 및 실행
			Time th = new Time(timelabel);
			th.start(); //thread를 실행하고 싶으면 start를 사용
		}
	}
	
	class Time extends Thread {
		//생성자
		JLabel timeLabel;
		public Time(JLabel timeLabel) {
			this.timeLabel = timeLabel;
		}
		
		//run 메소드 꼭 오버라이드
		@Override
		public void run() {
			while(true) {
				Calendar cal = Calendar.getInstance(); //현재 년, 월, 일, 시, 분, 초를 다 가져옴
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1; //0부터 시작하기 때문에 +1 해줌
				int day = cal.get(Calendar.DAY_OF_MONTH); //일
				int hour = cal.get(Calendar.HOUR_OF_DAY); //시
				int min = cal.get(Calendar.MINUTE); //분
				int sec = cal.get(Calendar.SECOND); //초
				timeLabel.setText("현재시간 : " + year + "년 " + month + "월 " + day + "일 " 
								  + hour + "시 " + min + "분 " + sec + "초");
				try {
					Thread.sleep(1000); //1000 = 1초 쉬어라
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}
}