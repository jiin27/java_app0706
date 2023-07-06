package org.sp.app0706.io;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Editor extends JFrame implements ActionListener{
	JMenuBar bar;
	JMenu[] menu;
	JMenuItem[] item;
	JTextArea area;
	JScrollPane scroll;
	JFileChooser chooser;
	
	public Editor() {
		bar = new JMenuBar();
		menu = new JMenu[5];
		item = new JMenuItem[8];
		area = new JTextArea();
		scroll = new JScrollPane(area); // scroll이 area를 잡아먹어야 하므로 꼭 area 변수를 넣어준다.
		chooser = new JFileChooser("D:/morning/javase_workspace"); //FileChooser는 호출하면서 경로를 지정할 수 있음
		
		//메뉴 5개 생성
		String[] menuName= {"파일", "편집", "서식", "보기", "도움말"}; //선언과 동시에 초기화 할 때! js에서 Array["", ""];
		for(int i=0; i<menu.length; i++) {
			menu[i] = new JMenu(menuName[i]);
			
			//생성된 메뉴를 바에 부착
			bar.add(menu[i]);

			
		}
		
		//메뉴 아이템 생성
		String[] itemName = {"새로 만들기", "새창", "열기", "저장", "다른 이름으로 저장", "페이지 인쇄", "설정", "끝내기"};
		
		//java 5(jdk1.5)부터는 개선된(improved) for문을 지원
		//반복문의 대상이 컬렉션, 배열 등의 집합인 경우 유용
		for(int i=0; i<itemName.length; i++) { //배열 명을 적으면 배열 length만큼 for문이 돌고 어떤 자료형으로 받을 건지 동일한 자료혐을 명시해주면 됨.
			item[i] = new JMenuItem(itemName[i]); //
			menu[0].add(item[i]);
			
			//메뉴 아이템들과 리스너 연결
			item[i].addActionListener(this); 
		}
		
		//속성 지정
		area.setBackground(Color.BLACK);
		area.setForeground(Color.WHITE);
		
		//조립
		setJMenuBar(bar); //프레임에 bar 부착
		add(scroll); //scroll 이 area를 잡고 있기 때문에 scroll을 붙이면 된다.
		setSize(800, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); //= margin: auto
		
		setFont();
	}
	
	public void setFont() {
		Font font = new Font("SansSerif", Font.PLAIN, 16);
		area.setFont(font);
	}
	
	public static void main(String[] args) {
		new Editor();
	}

	//byte 기반 스트림으로 파일 열기
	public void openFile() {
		//System.out.println("열거야?");
		int result= chooser.showOpenDialog(this); //디자인적으로 parent 요소(해당 객체를 담고있는 컨테이너)를 명시해야한다.
		
		//스트림은 기본적으로 1byte 씩 처리하기 때문에, 영문을 제외한 문자를 해석할 수 없다(2byte로 표현되는 문자)
		FileInputStream fis=null;
		
		if(result == JFileChooser.APPROVE_OPTION) {
			File file=chooser.getSelectedFile();
			
			//FileInputStream 생성시 경로도 가능하지만, 파일 자체도 받을 수 있다.
			try {
				fis = new FileInputStream(file); //파일 데이터를 읽어들일 통로 삽입
				try {
					int data = -1;
					
					byte[] b=new byte[1024];
					
					while (true) {
						//실행중인 프로그램이 스트림으로부터 1 byte를 읽는 것이 read()
						data=fis.read(b);
						if(data == -1)break;
						//읽어들인 데이터는 b에 들어있음
						String str = new String(b);
						
						//자바의 모든 기본자료형마다 1:1 대응하는 Wrapper 클래스가 지원된다.
						//char:Character
						//Character --> String
						area.append(str+"\n");
					}
					 
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	//문자 기반 스트림으로 파일 열기
	public void openFileByReader() {

		int result=chooser.showOpenDialog(this); //파일 탐색기 열기
		if(result == JFileChooser.APPROVE_OPTION) {
			File file=chooser.getSelectedFile();
			
			//영문 뿐 아니라 전세계 모든 문자를 해석할 수 있는 능력이 있는 스트림을 이용하자
			FileReader fr = null; 
			
			try {
				fr = new FileReader(file);
				try {
					int data = -1;
					//영문도 1자, 한글도 1자로 인식
					//apple 맛있다 --> 9회 읽어들임
					while(true) {
						data = fr.read();// 한 문자씩 읽기(한 문자씩 read() 작동)
						if(data == -1)break;
						System.out.println((char)data);
						area.append(Character.toString((char)data));
						
					}					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	//버퍼까지 처리된 문자 기반 스트림으로 파일 열기(버퍼 단위로 데이터 읽어들임 : 입력되는 시점이 버퍼의 단위 지정 시점)
	//버퍼가 처리된 스트림은 접두어에 Buffered~
	public void openFileByBuffer() {
		int result = chooser.showOpenDialog(this);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			FileReader reader = null;
			BufferedReader buffr = null;
			File file = chooser.getSelectedFile( );
			try {
				reader = new FileReader(file);
				buffr = new BufferedReader(reader);
				String msg=null;
				try {
					while (true) {
						msg=buffr.readLine();
						if(msg == null)break;
						area.append(msg+"\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} //한 줄을 읽어들임(맨끝에 \n 줄바꿈 만나면)
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally {
				if(buffr != null) { //인스턴스가 존재한다면
					try {
						buffr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		JMenuItem obj = (JMenuItem)e.getSource();
		
		//열기 눌렀을때
		if(obj == item[2]) {
			openFileByReader();
		}
		
	}
}
