package chatt;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

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

public class ServerMain extends JFrame {
	ServerSocket server;
	Vector<ServerThread> clients = new Vector<ServerThread>();
	boolean flagServer = true;
	DefaultListModel userListModel = new DefaultListModel<String>();
	// JList와 Vector와의 시차떄문에 반영이 안되는 오류가 있어서 DefaltTableModel처럼 DefaultListModel을 사용함
	// JList의 데이터를 컨트롤하기 위한 코드

	final static int SERVtER_START = 1;
	final static int SERVER_STOP = 2;
	final static int LOGIN = 3;
	final static int LOGOUT = 4;
	final static int MESSAGE = 5;
	final static int USERS = 6; // 유저들의 명단
	final static int WHISPER = 7; // 귓속말

	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JTextField tfIP;
	private JButton btnStart;
	private JButton btnStop;
	private JScrollPane scrollPane;
	private JList list;
	private JScrollPane scrollPane_1;
	private JTextArea textArea;
	private JTextField tfMessage;
	private JButton btnWhisper;
	private JButton btnSend;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerMain frame = new ServerMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void start() {
		try {
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						textArea.append("서버가 시작되었습니다.\n");
						btnStart.setEnabled(false);
						btnStop.setEnabled(true);
						btnSend.setEnabled(true);
						btnWhisper.setEnabled(true);

						server = new ServerSocket(5555);
						flagServer = true;

						while (flagServer) {
							Socket socket = server.accept();

							if (flagServer) {
								ServerThread st = new ServerThread(socket, ServerMain.this);
								st.start();
								clients.add(st);
							}
						}

						for (ServerThread st : clients) {
					
							st.br.close();
							st.bw.close();
							st.socket.close();
						}

						clients.clear();
						server.close();
						userListModel.clear();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		try {
			// 1) 접속된 모든 클라이언트들에게 서버 중지 통보
			JSONObject jData = new JSONObject();
			jData.put("user", "server");
			jData.put("command", SERVER_STOP);
			jData.put("message", "서버가 중지되었습니다.");

			for (ServerThread st : clients) {
				st.bw.write(jData.toJSONString());
				st.bw.write("\n");
				st.bw.flush();
			}
			

		} catch (Exception e) {
		}

		// 2) 버튼들을 토클.
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		btnSend.setEnabled(false);
		btnWhisper.setEnabled(false);
		userListModel.removeAllElements();

		// 3)서버를 중지하기 위한 가상의 클라이언트로 접속
		flagServer = false;
		try {
			Socket tempSocket = new Socket("127.0.0.1", 5555);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void send() {
		String msg = tfMessage.getText();

		JSONObject obj = new JSONObject();
		obj.put("user", "server");
		obj.put("command", ServerMain.MESSAGE);
		obj.put("message", msg);

		textArea.append(msg + "\n");
		textArea.setCaretPosition(textArea.getText().length());

		for (ServerThread st : clients) {
			try {
				st.bw.write(obj.toJSONString());
				st.bw.write("\n");
				st.bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendWhisper() {
		String msg = tfMessage.getText();

		JSONObject obj = new JSONObject();
		obj.put("user", "server");
		obj.put("command", ServerMain.WHISPER);
		obj.put("message", msg);

		textArea.append(msg + "\n");
		textArea.setCaretPosition(textArea.getText().length());
		
		// 데이터를 보내는 부분
		
		List<String> receiveUsers = getList().getSelectedValuesList();  
		// JList에서 참여자 aa나 bb등 귓속말을 보낼 유저'들'을 선택 SelectedValue's'
		System.out.println(receiveUsers);
		for (ServerThread st : clients) {
			if( !receiveUsers.contains(st.user) ) continue;
			// 선택한 유저가 st의 user('현재 접속중인' list에 있는 유저리스트)에 포함되어 있지 않으면 다시 for문을 돌린다(continue 때문)
			try {
				st.bw.write(obj.toJSONString());
				st.bw.write("\n");
				st.bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ServerMain() {
		setTitle("멀티 채팅 서버");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 668, 454);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblNewLabel());
		contentPane.add(getTfIP());
		contentPane.add(getBtnStart());
		contentPane.add(getBtnStop());
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
			tfIP.setBounds(65, 7, 116, 21);
			tfIP.setColumns(10);

			try {
				InetAddress ia = InetAddress.getLocalHost();
				tfIP.setText(ia.getHostAddress());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return tfIP;
	}

	public JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton("시작");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 서버를 시작하고 클라이언트의 접속대기
					start();
				}
			});
			btnStart.setBounds(191, 6, 97, 23);
		}
		return btnStart;
	}

	public JButton getBtnStop() {
		if (btnStop == null) {
			btnStop = new JButton("종료");
			btnStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					stop();
				}
			});
			btnStop.setEnabled(false);
			btnStop.setBounds(303, 6, 97, 23);
		}
		return btnStop;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 43, 124, 324);
			scrollPane.setViewportView(getList());
			scrollPane.setColumnHeaderView(getLblNewLabel_1());
		}
		return scrollPane;
	}

	public JList getList() {
		if (list == null) {
			list = new JList(userListModel);
		}

		return list;
	}

	public JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(148, 43, 492, 324);
			scrollPane_1.setViewportView(getTextArea());
			scrollPane_1.setColumnHeaderView(getLblNewLabel_2());
		}
		return scrollPane_1;
	}

	public JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
			textArea.setFont(new Font("Monospaced", Font.BOLD, 22));
		}
		return textArea;
	}

	public JTextField getTfMessage() {
		if (tfMessage == null) {
			tfMessage = new JTextField();
			tfMessage.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(!tfMessage.getText().equals("")) {
							send();
							tfMessage.setText("");
						}
					}
				}
			});
			tfMessage.setBounds(12, 377, 410, 21);
			tfMessage.setColumns(10);
		}
		return tfMessage;
	}

	public JButton getBtnWhisper() {
		if (btnWhisper == null) {
			btnWhisper = new JButton("귓속말");
			btnWhisper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sendWhisper();
				}
			});
			btnWhisper.setEnabled(false);
			btnWhisper.setBounds(543, 377, 97, 23);
		}
		return btnWhisper;
	}

	public JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton("전송");
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!tfMessage.getText().equals("")) {
					send();
					tfMessage.setText("");
					}
				}
			});
			btnSend.setEnabled(false);
			btnSend.setBounds(434, 377, 97, 23);
		}
		return btnSend;
	}

	public JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("접속자");
			lblNewLabel_1.setOpaque(true);
			lblNewLabel_1.setForeground(SystemColor.text);
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_1.setBackground(SystemColor.desktop);
		}
		return lblNewLabel_1;
	}

	public JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("대화 내용");
			lblNewLabel_2.setBackground(SystemColor.desktop);
			lblNewLabel_2.setForeground(SystemColor.text);
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_2.setOpaque(true);
		}
		return lblNewLabel_2;
	}
}
