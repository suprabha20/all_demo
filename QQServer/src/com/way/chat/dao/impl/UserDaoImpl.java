package com.way.chat.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.way.chat.common.bean.User;
import com.way.chat.common.util.Constants;
import com.way.chat.common.util.DButil;
import com.way.chat.dao.UserDao;

public class UserDaoImpl implements UserDao {

	@Override
	public int register(User u) {
		int id;
		Connection con = DButil.connect();
		String sql1 = "insert into user(name,password,email) values(?,?,?)";
		String sql2 = "select id from user";

		try {
			PreparedStatement ps = con.prepareStatement(sql1);
			ps.setString(1, u.getName());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			int res = ps.executeUpdate();
			if (res > 0) {
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ResultSet rs = ps2.executeQuery();
				if (rs.last()) {
					id = rs.getInt("id");
					createFriendtable(id);// ע��ɹ��󣬴���һ�����û�idΪ�����ı����ڴ�ź�����Ϣ
					return id;
				}
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return Constants.REGISTER_FAIL;
	}

	@Override
	public ArrayList<User> login(User u) {
		Connection con = DButil.connect();
		String sql = "select * from user where id=? and password=?";
		ArrayList<User> list = new ArrayList<User>();
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, u.getId());
			ps.setString(2, u.getPassword());
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				User friend = new User();
				friend.setId(rs.getInt("id"));
				friend.setEmail(rs.getString("email"));
				friend.setName(rs.getString("name"));
				friend.setImg(rs.getInt("img"));
				list.add(friend);
				setOnline(u.getId());// ���±�״̬Ϊ����
				ArrayList<User> refreshList = refresh(u.getId());
				for (User user : refreshList) {
					list.add(user);
				}
				return list;
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

	/**
	 * ˢ�º����б�
	 */
	public ArrayList<User> refresh(int id) {
		ArrayList<User> list = new ArrayList<User>();
		Connection con = DButil.connect();
		String sql = "select * from _? ";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				do {
					User friend = new User();
					friend.setId(rs.getInt("qq"));
					friend.setName(rs.getString("name"));
					friend.setIsOnline(rs.getInt("isOnline"));
					friend.setImg(rs.getInt("img"));
					list.add(friend);
				} while (rs.next());
			}
			return list;
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

	/**
	 * ����״̬Ϊ����
	 * 
	 * @param id
	 */
	public void setOnline(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update user set isOnline=1 where id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOn(id);// �������б�״̬Ϊ����
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	/**
	 * ע��ɹ��󣬴���һ���û���������û�����
	 * 
	 * @param id
	 */
	public void createFriendtable(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "create table _" + id
					+ " (id int auto_increment not null primary key,"
					+ "name varchar(16) not null,"
					+ "isOnline varchar(5) not null," + "img int)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	@Override
	/**
	 * ���߸���״̬Ϊ����
	 */
	public void logout(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update user set isOnline=0 where id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			updateAllOff(id);
			// System.out.println(res);
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	/**
	 * ���������û���״̬Ϊ����
	 * 
	 * @param id
	 */
	public void updateAllOff(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update _? set isOnline=0 where qq=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for (int offId : getAllId()) {
				ps.setInt(1, offId);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}
	/**
	 * ���������û�״̬Ϊ����
	 * @param id
	 */
	public void updateAllOn(int id) {
		Connection con = DButil.connect();
		try {
			String sql = "update _? set isOnline=1 where qq=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for (int OnId : getAllId()) {
				ps.setInt(1, OnId);
				ps.setInt(2, id);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
	}

	public List<Integer> getAllId() {
		Connection con = DButil.connect();
		List<Integer> list = new ArrayList<Integer>();
		try {
			String sql = "select id from user";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.first()) {
				do {
					int id = rs.getInt("id");
					list.add(id);
				} while (rs.next());
			}
//			System.out.println(list);
			return list;
		} catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			DButil.close(con);
		}
		return null;
	}

//	public static void main(String[] args) {
//		User u = new User();
//		UserDaoImpl dao = new UserDaoImpl();
//		u.setId(2016);
//		u.setName("qq");
//		u.setPassword("123");
//		u.setEmail("158342219@qq.com");
//		// System.out.println(new UserDaoImpl().register(u));
//		// System.out.println(dao.login(u));
//		// dao.logout(2016);
//		dao.setOnline(2016);
//		// dao.getAllId();
//
//	}

}
