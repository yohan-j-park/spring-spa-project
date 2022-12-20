package chatt;

import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

public class ClientMain extends JFrame {
	ClientThread ct;
	DefaultListModel userListModel = new DefaultListModel();
	// 기존에 사용하던 Vector<String> users를 없애고 DefaultListModel을 사용함(접속한 유저가 바로바로 최신화가 안되는 오류 때문에..)
	// JList에 데이터가 저장되는 공간을 Vector에서 DefaultListModel로 바꾸었다.
	
	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JTextField tfIP;
	private JLabel lblNewLabel_1;
	private JTextField tfUser;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JScrollPane scrollPane;
	private JList list;
	private JScrollPane scrollPane_1;
	private JTextArea textArea;
	private JTextField tfMessage;
	private JButton btnWhisper;
	private JButton btnSend;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMain frame = new ClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void connect() {
		try {
			String ip = tfIP.getText();
			int port=5555;
			Socket s = new Socket(ip, port);
			ct = new ClientThread(s, this);// ClientMain.this
			ct.start();
			
			btnConnect.setEnabled(false);
			btnDisconnect.setEnabled(true);
			btnSend.setEnabled(true);
			btnWhisper.setEnabled(true);
			userListModel.clear();
		}catch(Exception ex) {
//			System.out.println("clientMain connect()");
			ex.printStackTrace();
		}
	}
	
	public void disconnect() {
		//1. 서버에게 자신의 종료 사실 통보
		JSONObject obj = new JSONObject();
		obj.put("user", tfUser.getText());
		obj.put("command", ServerMain.LOGOUT);
		obj.put("message", "난 이제 그만...");
		
		try {
			ct.bw.write(obj.toJSONString());
			ct.bw.write("\n");
			ct.bw.flush();
			ct.flag=false;
			ct.br.close();
			ct.bw.close();
			ct.socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}

		ct=null;
		//3. 버튼 상태 변경
		btnConnect.setEnabled(true);
		btnDisconnect.setEnabled(false);
		btnSend.setEnabled(false);
		btnWhisper.setEnabled(false);
		userListModel.clear();
		
	}
	
	
	
	public ClientMain() {
		setTitle("멀티 채팅 클라이언트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 798, 484);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblNewLabel());
		contentPane.add(getTfIP());
		contentPane.add(getLblNewLabel_1());
		contentPane.add(getTfUser());
		contentPane.add(getBtnConnect());
		contentPane.add(getBtnDisconnect());
		contentPane.add(getScrollPane());
		contentPane.add(getScrollPane_1());
		contentPane.add(getTfMessage());
		contentPane.add(getBtnWhisper());
		contentPane.add(getBtnSend());
	}

	public JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("서버IP");
			lblNewLabel.setBounds(12, 10, 57, 15);
		}
		return lblNewLabel;
	}
	public JTextField getTfIP() {
		if (tfIP == null) {
			tfIP = new JTextField();
			tfIP.setBounds(77, 7, 116, 21);
			tfIP.setColumns(10);
			
			try {
				InetAddress ia = InetAddress.getLocalHost();
				tfIP.setText(ia.getHostAddress());
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		return tfIP;
	}
	public JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("접속자");
			lblNewLabel_1.setBounds(205, 10, 57, 15);
		}
		return lblNewLabel_1;
	}
	public JTextField getTfUser() {
		if (tfUser == null) {
			tfUser = new JTextField();
			tfUser.setBounds(275, 7, 116, 21);
			tfUser.setColumns(10);
		}
		return tfUser;
	}
	public JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton("접속");
			btnConnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					connect();
				}
			});
			btnConnect.setBounds(407, 6, 97, 23);
		}
		return btnConnect;
	}
	public JButton getBtnDisconnect() {
		if (btnDisconnect == null) {
			btnDisconnect = new JButton("종료");
			btnDisconnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					disconnect();
				}
			});
			btnDisconnect.setEnabled(false);
			btnDisconnect.setBounds(514, 6, 97, 23);
		}
		return btnDisconnect;
	}
	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 45, 154, 354);
			scrollPane.setViewportView(getList());
			scrollPane.setColumnHeaderView(getLblNewLabel_2());
		}
		return scrollPane;
	}
	public JList getList() {	//List의 UI만 다루는 메소드
		if (list == null) {
			list = new JList(userListModel);	
		}
		return list;
	}
	public JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(178, 45, 592, 354);
			scrollPane_1.setViewportView(getTextArea());
			scrollPane_1.setColumnHeaderView(getLblNewLabel_3());
		}
		return scrollPane_1;
	}
	public JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
		}
		return textArea;
	}
	public JTextField getTfMessage() {
		if (tfMessage == null) {
			tfMessage = new JTextField();
			tfMessage.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER) {
						String msg = tfMessage.getText();
						if(!msg.equals("")) {
						ct.sendMsg(msg);
						tfMessage.setText("");
						}
					}
				}
			});
			tfMessage.setBounds(12, 411, 540, 21);
			tfMessage.setColumns(10);
		}
		return tfMessage;
	}
	public JButton getBtnWhisper() {
		if (btnWhisper == null) {
			btnWhisper = new JButton("귓속말");
			btnWhisper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg = tfMessage.getText();
					List<String> users = getList().getSelectedValuesList();
					ct.sendWhisper(users, msg);
				}
			});
			btnWhisper.setEnabled(false);
			btnWhisper.setBounds(673, 409, 97, 23);
		}
		return btnWhisper;
	}
	public JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton("전송");
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg = tfMessage.getText();
					if(!tfMessage.getText().equals("")) {
					ct.sendMsg(msg);
					tfMessage.setText("");
					}
				}
			});
			btnSend.setEnabled(false);
			btnSend.setBounds(564, 409, 97, 23);
		}
		return btnSend;
	}
	public JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("접속자");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_2.setOpaque(true);
			lblNewLabel_2.setForeground(SystemColor.text);
			lblNewLabel_2.setBackground(SystemColor.desktop);
		}
		return lblNewLabel_2;
	}
	public JLabel getLblNewLabel_3() {
		if (lblNewLabel_3 == null) {
			lblNewLabel_3 = new JLabel("대화 내용");
			lblNewLabel_3.setOpaque(true);
			lblNewLabel_3.setForeground(SystemColor.text);
			lblNewLabel_3.setBackground(SystemColor.desktop);
			lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblNewLabel_3;
	}
}
