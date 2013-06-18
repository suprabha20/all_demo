package com.test;

import android.net.Uri;
import android.provider.BaseColumns;

// �Զ��� ContentProvider �����ʵ����  
public class MyUser {
	// ����Ҫ�� _id �ֶΡ������� BaseColumn �����Ѿ������� _id �ֶ�
	public static final class User implements BaseColumns {

		// ���� CONTENT_URI
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.webabcd.MyContentProvider");

		// ��������
		public static final String USER_NAME = "USER_NAME";
	}
}
