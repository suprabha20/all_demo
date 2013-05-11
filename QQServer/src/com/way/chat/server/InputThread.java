package com.way.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.way.chat.common.bean.TextMessage;
import com.way.chat.common.bean.User;
import com.way.chat.common.tran.bean.TranObject;
import com.way.chat.common.tran.bean.TranObjectType;
import com.way.chat.common.util.MyDate;
import com.way.chat.dao.UserDao;
import com.way.chat.dao.impl.UserDaoFactory;

public class InputThread extends Thread {
	private Socket socket;
	private OutputThread out;
	private OutputThreadMap map;
	private ObjectInputStream ois;
	private boolean isStart = true;

	public InputThread(Socket socket, OutputThread out, OutputThreadMap map) {
		this.socket = socket;
		this.out = out;
		this.map = map;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				// ��ȡ��Ϣ
				readMessage();
			}
			if (ois != null)
				ois.close();
			if (socket != null)
				socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
			// isStart = false;
		}

	}

	public void readMessage() throws IOException, ClassNotFoundException {
		Object readObject = ois.readObject();// �����ж�ȡ����
		UserDao dao = UserDaoFactory.getInstance();// ͨ��daoģʽ�����̨
		if (readObject != null && readObject instanceof TranObject) {
			TranObject read_tranObject = (TranObject) readObject;// ת���ɴ������
			switch (read_tranObject.getType()) {
			case REGISTER:// ����û���ע��
				User registerUser = (User) read_tranObject.getObject();
				int registerResult = dao.register(registerUser);
				System.out.println("ע��:" + registerResult);
				// ���û��ظ���Ϣ
				TranObject<User> register2TranObject = new TranObject<User>(
						TranObjectType.REGISTER);
				User register2user = new User();
				register2user.setId(registerResult);
				register2TranObject.setObject(register2user);
				out.setMessage(register2TranObject);
				break;
			case LOGIN:
				User loginUser = (User) read_tranObject.getObject();
				ArrayList<User> list = dao.login(loginUser);
				TranObject<ArrayList<User>> login2Object = new TranObject<ArrayList<User>>(
						TranObjectType.LOGIN);
				if (list != null) {// �����¼�ɹ�
					TranObject<User> onObject = new TranObject<User>(
							TranObjectType.LOGIN);
					User login2User = new User();
					login2User.setId(loginUser.getId());
					onObject.setObject(login2User);
					for (OutputThread onOut : map.getAll()) {
						onOut.setMessage(onObject);// �㲥һ���û�����
					}
					map.add(loginUser.getId(), out);// �ȹ㲥���ٰѶ�Ӧ�û�id��д�̴߳���map�У��Ա�ת����Ϣʱ����
					login2Object.setObject(list);// �Ѻ����б����ظ��Ķ�����
				} else {
					login2Object.setObject(null);
				}
				out.setMessage(login2Object);// ͬʱ�ѵ�¼��Ϣ�ظ����û�
				
				System.out.println(MyDate.getDateCN() + " �û���" + loginUser.getId() + " ������");
				break;
			case LOGOUT:// ������˳����������ݿ�����״̬��ͬʱȺ���������������û�
				User logoutUser = (User) read_tranObject.getObject();
				int offId = logoutUser.getId();
				System.out.println(MyDate.getDateCN() + " �û���" + offId + " ������");
				dao.logout(offId);
				isStart = false;// �����Լ��Ķ�ѭ��
				out.removeThread();// �ӻ�����߳����Ƴ�
				out.setMessage(null);// ��Ҫ����һ������Ϣȥ����д�߳�
				out.setStart(false);// �ٽ���д�߳�ѭ��

				TranObject<User> offObject = new TranObject<User>(
						TranObjectType.LOGOUT);
				User logout2User = new User();
				logout2User.setId(logoutUser.getId());
				offObject.setObject(logout2User);
				for (OutputThread offOut : map.getAll()) {// �㲥�û�������Ϣ
					offOut.setMessage(offObject);
				}
				break;
			case MESSAGE:// �����ת����Ϣ�������Ⱥ����
				// ��ȡ��Ϣ��Ҫת���Ķ���id��Ȼ���ȡ����ĸö����д�߳�
				int id2 = read_tranObject.getToUser();
				OutputThread toOut = map.getById(id2);
				if (toOut != null) {// ����û�����
					toOut.setMessage(read_tranObject);
				} else {// ���Ϊ�գ�˵���û��Ѿ�����,�ظ��û�
					TextMessage text = new TextMessage();
					text.setMessage("������Ϣʧ�ܣ��û������Ѿ����ߣ�");
					TranObject<TextMessage> offText = new TranObject<TextMessage>(
							TranObjectType.MESSAGE);
					offText.setObject(text);
					offText.setFromUser(0);
					out.setMessage(offText);
				}
				break;
			case FILE:

				break;
			default:
				break;
			}
		}
	}
}
