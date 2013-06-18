package com.test;

import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/* 
 * �������õ�ϵͳ���õ� ContentProvider ���£�  
 * content://media/internal/images  ���URI�������豸�ϴ洢������ͼƬ 
 * content://contacts/people/ ���URI�������豸�ϵ�������ϵ����Ϣ 
 * content://contacts/people/45 ���URI���ص����������ϵ����Ϣ��IDΪ45����ϵ�˼�¼�� 
 */

public class Main extends Activity {
	private DatabaseHelper dbHelper;

	private static final String DATABASE_NAME = "db.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "employee";
	TextView txtMsg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dbHelper = new DatabaseHelper(this, DATABASE_NAME, null,
				DATABASE_VERSION);
		txtMsg = (TextView) this.findViewById(R.id.txtMsg);

		Button btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setText("������");
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				CreateTable();
			}
		});

		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setText("���� 3 ����¼");
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				insertItem();
			}
		});

		Button btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setText("ɾ��ȫ����¼");
		btn3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				deleteItem();
			}
		});

		Button btn4 = (Button) this.findViewById(R.id.btn4);
		btn4.setText("����ָ������");
		btn4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				updateItem();
			}
		});

		Button btn5 = (Button) this.findViewById(R.id.btn5);
		btn5.setText("��ʾȫ������");
		btn5.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showItems();
			}
		});

		Button btn6 = (Button) this.findViewById(R.id.btn6);
		btn6.setText("ɾ����");
		btn6.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				dropTable();
			}
		});
	}

	// �������ݱ�
	private void CreateTable() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
				+ " (ID INTEGER PRIMARY KEY, Name VARCHAR, Age INTEGER);";
		try {
			db.execSQL(sql);
			txtMsg.append("���ݱ�ɹ�����\n");
		} catch (SQLException ex) {
			txtMsg.append("���ݱ�������\n" + ex.toString() + "\n");
		}
	}

	// ��������
	private void insertItem() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			Random random = new Random();
			for (int i = 0; i < 3; i++) {
				String sql = "insert into " + TABLE_NAME
						+ " (name, age) values ('name" + String.valueOf(i)
						+ "', " + random.nextInt() + ")";
				// execSQL() - ִ��ָ���� sql
				db.execSQL(sql);
			}
			txtMsg.append("�ɹ����� 3 ������\n");
		} catch (SQLException ex) {
			txtMsg.append("��������ʧ��\n" + ex.toString() + "\n");
		}
	}

	// ɾ������
	private void deleteItem() {
		try {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.delete(TABLE_NAME, " id < 999999", null);
			txtMsg.append("�ɹ�ɾ������\n");
		} catch (SQLException e) {
			txtMsg.append("ɾ������ʧ��\n");
		}
	}

	// ��������
	private void updateItem() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put("name", "�������º������");

			db.update(TABLE_NAME, values, "id<?", new String[] { "3" });
			txtMsg.append("�ɹ���������\n");
		} catch (SQLException e) {
			txtMsg.append("��������ʧ��\n");
		}
	}

	// ��ѯ����
	private void showItems() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		try {
			String[] column = { "id", "name", "age" };
			Cursor cursor = db.query(TABLE_NAME, column, null, null, null,
					null, null);
			Integer num = cursor.getCount();
			txtMsg.append("�� " + Integer.toString(num) + " ����¼\n");
			cursor.moveToFirst();

			while (cursor.getPosition() != cursor.getCount()) {
				txtMsg.append(Integer.toString(cursor.getPosition()) + ","
						+ String.valueOf(cursor.getString(0)) + ","
						+ cursor.getString(1) + ","
						+ String.valueOf(cursor.getString(2)) + "\n");
				cursor.moveToNext();
			}
		} catch (SQLException ex) {
			txtMsg.append("��ȡ����ʧ��\n" + ex.toString() + "\n");
		}
	}

	// ɾ�����ݱ�
	private void dropTable() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		try {
			db.execSQL(sql);
			txtMsg.append("���ݱ�ɾ���ɹ�\n");
		} catch (SQLException ex) {
			txtMsg.append("���ݱ�ɾ������\n" + ex.toString() + "\n");
		}
	}
	

}