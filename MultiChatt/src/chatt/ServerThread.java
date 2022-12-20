package chatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ServerThread extends Thread {
	String user;
	BufferedWriter bw;
	BufferedReader br;
	ServerMain main;
	Socket socket;
	boolean flag;

	public ServerThread(Socket s, ServerMain m) {
		this.socket = s;
		this.main = m;
		try {
			OutputStream os = s.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);

			InputStream is = s.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	public void run() {
		JSONParser parser = new JSONParser();
		flag = true;
		while (flag) {
			try {
				String msg = br.readLine();

				JSONObject obj = (JSONObject) parser.parse(msg);
				main.getTextArea().append(obj.get("user") + " : ");
				main.getTextArea().append(obj.get("message") + "\n");

				// 스크롤바를 맨하단으로
				main.getTextArea().setCaretPosition(main.getTextArea().getText().length());

				Long o = (Long) obj.get("command");

				switch (o.intValue()) {
				case ServerMain.LOGIN:
					String u = (String) obj.get("user");
					this.user = u;

					// 자기 자신에게 users에 저장된 모든 목록을 전송
					if (main.userListModel.size() > 0) {
						JSONObject usersObj = new JSONObject();
						usersObj.put("command", ServerMain.USERS);
						usersObj.put("user", "server");
						usersObj.put("message", u + " 님이 접속함...");

						Object[] array  = main.userListModel.toArray();
						Vector<String> userVector = new Vector(Arrays.asList(array));
						usersObj.put("data", userVector);

						sendMsg(usersObj);

					}

					// 접속된 모든 유저에게 신규유저의 아이디를 전송
					JSONObject newObj = new JSONObject();
					newObj.put("command", ServerMain.LOGIN);
					newObj.put("user", u);
					newObj.put("message", u + " 님이 접속함...");

					sendMsgAll(newObj);

					// 서버 자신의 JList에 추가
					main.userListModel.addElement(u);

					break;

				case ServerMain.LOGOUT:
					main.clients.remove(this);
					main.userListModel.removeElement(user);
					flag = false;
					sendMsgAll(obj);
					break;

				case ServerMain.WHISPER:	//클라이언트가 귓속말을 보낸 경우
					sendWhisper(obj);
					break;

				default:
					sendMsgAll(obj);
				}

			} catch (Exception e) {
				e.printStackTrace();
				JSONObject obj = new JSONObject();
				obj.put("user", "server");
				obj.put("command", ServerMain.SERVER_STOP);
				obj.put("message", "서버에 예기치 않은 오류가 발생");
				sendMsg(obj);
				flag = false;
			}
		}

		try {
			br.close();
			bw.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendMsg(JSONObject obj) {
		try {
			bw.write(obj.toJSONString() + "\n");
			bw.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public void sendMsgAll(JSONObject obj) {
		for (ServerThread st : main.clients) {
			if(st.isAlive()) {
//				sendMsg(obj);
				try {
					st.bw.write(obj.toJSONString());
					st.bw.write("\n");
					st.bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

	public void sendWhisper(JSONObject obj) {
		List<String> receiveUser = (List) obj.get("users");
		if(receiveUser == null) return;
		for (ServerThread st : main.clients) {	// 서버메인에서 클라이언트들을 하나씩 가져와 서버스레드에 담는다
			if (receiveUser.contains(st.user) && st.isAlive()) {	// isAlive: 살아있는
//				sendMsg(obj);
				try {
					st.bw.write(obj.toJSONString() + "\n");
					st.bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

			
}
	



