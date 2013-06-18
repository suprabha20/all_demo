package com.test;

import android.net.Uri;
import android.provider.BaseColumns;

// 自定义 ContentProvider 所需的实体类  
public class MyUser {
	// 必须要有 _id 字段。本例中 BaseColumn 类中已经包含了 _id 字段
	public static final class User implements BaseColumns {

		// 定义 CONTENT_URI
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.webabcd.MyContentProvider");

		// 表数据列
		public static final String USER_NAME = "USER_NAME";
	}
}
