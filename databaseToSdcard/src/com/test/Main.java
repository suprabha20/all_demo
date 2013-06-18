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
 * 几个常用的系统内置的 ContentProvider 如下：  
 * content://media/internal/images  这个URI将返回设备上存储的所有图片 
 * content://contacts/people/ 这个URI将返回设备上的所有联系人信息 
 * content://contacts/people/45 这个URI返回单个结果（联系人信息中ID为45的联系人记录） 
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
		btn1.setText("创建表");
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				CreateTable();
			}
		});

		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setText("插入 3 条记录");
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				insertItem();
			}
		});

		Button btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setText("删除全部记录");
		btn3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				deleteItem();
			}
		});

		Button btn4 = (Button) this.findViewById(R.id.btn4);
		btn4.setText("更新指定数据");
		btn4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				updateItem();
			}
		});

		Button btn5 = (Button) this.findViewById(R.id.btn5);
		btn5.setText("显示全部数据");
		btn5.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showItems();
			}
		});

		Button btn6 = (Button) this.findViewById(R.id.btn6);
		btn6.setText("删除表");
		btn6.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				dropTable();
			}
		});
	}

	// 创建数据表
	private void CreateTable() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
				+ " (ID INTEGER PRIMARY KEY, Name VARCHAR, Age INTEGER);";
		try {
			db.execSQL(sql);
			txtMsg.append("数据表成功创建\n");
		} catch (SQLException ex) {
			txtMsg.append("数据表创建错误\n" + ex.toString() + "\n");
		}
	}

	// 插入数据
	private void insertItem() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			Random random = new Random();
			for (int i = 0; i < 3; i++) {
				String sql = "insert into " + TABLE_NAME
						+ " (name, age) values ('name" + String.valueOf(i)
						+ "', " + random.nextInt() + ")";
				// execSQL() - 执行指定的 sql
				db.execSQL(sql);
			}
			txtMsg.append("成功插入 3 条数据\n");
		} catch (SQLException ex) {
			txtMsg.append("插入数据失败\n" + ex.toString() + "\n");
		}
	}

	// 删除数据
	private void deleteItem() {
		try {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.delete(TABLE_NAME, " id < 999999", null);
			txtMsg.append("成功删除数据\n");
		} catch (SQLException e) {
			txtMsg.append("删除数据失败\n");
		}
	}

	// 更新数据
	private void updateItem() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put("name", "批量更新后的名字");

			db.update(TABLE_NAME, values, "id<?", new String[] { "3" });
			txtMsg.append("成功更新数据\n");
		} catch (SQLException e) {
			txtMsg.append("更新数据失败\n");
		}
	}

	// 查询数据
	private void showItems() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		try {
			String[] column = { "id", "name", "age" };
			Cursor cursor = db.query(TABLE_NAME, column, null, null, null,
					null, null);
			Integer num = cursor.getCount();
			txtMsg.append("共 " + Integer.toString(num) + " 条记录\n");
			cursor.moveToFirst();

			while (cursor.getPosition() != cursor.getCount()) {
				txtMsg.append(Integer.toString(cursor.getPosition()) + ","
						+ String.valueOf(cursor.getString(0)) + ","
						+ cursor.getString(1) + ","
						+ String.valueOf(cursor.getString(2)) + "\n");
				cursor.moveToNext();
			}
		} catch (SQLException ex) {
			txtMsg.append("读取数据失败\n" + ex.toString() + "\n");
		}
	}

	// 删除数据表
	private void dropTable() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		try {
			db.execSQL(sql);
			txtMsg.append("数据表删除成功\n");
		} catch (SQLException ex) {
			txtMsg.append("数据表删除错误\n" + ex.toString() + "\n");
		}
	}
	

}