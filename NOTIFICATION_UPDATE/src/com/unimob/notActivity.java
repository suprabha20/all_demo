package com.unimob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class notActivity extends Activity {
	/** Called when the activity is first created. */
	int notification_id = 19172439;
	NotificationManager nm;
	Handler handler = new Handler();
	Notification notification;
	int count = 0;
	public int versionCode = 0;
	public String versionName = "";
	private static final String TAG = "AutoUpdate";
	private String currentFilePath = "";
	private String currentTempFilePath = "";
	private String fileEx = "";
	private String fileNa = "";
	private String strURL = "http://211.154.153.84/swt/qiye/version";
	TextView textview;
	int size = 1;
	int kk;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);

		// ����notification,ǰ����ѧϰ�����������ˣ�������������ǰ������
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.home, "ͼ��ߵ�����",
				System.currentTimeMillis());
		notification.contentView = new RemoteViews(getPackageName(),
				R.layout.notification);
		// ʹ��notification.xml�ļ���VIEW
		notification.contentView.setTextViewText(R.id.down_tv, "��ǰ����=0%");
		notification.contentView.setProgressBar(R.id.pb, 100, 0, false);
		// ���ý����������ֵ Ϊ100,��ǰֵΪ0�����һ������Ϊtrueʱ��ʾ����
		// ��������Android Market���������������ص���û��ȡ��Ŀ���Сʱ��״̬��
		Intent notificationIntent = new Intent(this, notActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.contentIntent = contentIntent;
		getCurrentVersion();
		calculate();
	}

	OnClickListener bt1lis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showNotification();// ��ʾnotification
			handler.post(run);
		}

	};
	Runnable run = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			notification.contentView
					.setProgressBar(R.id.pb, size, count, false);
			notification.contentView.setTextViewText(R.id.down_tv, "��ǰ����="
					+ count * 100 / size + "%");
			// ���õ�ǰֵΪcount
			showNotification();// �����Ǹ���notification,���Ǹ��½�����
			if (count < size)
				handler.postDelayed(run, 200);
			// 200����count��1
		}

	};

	/**
	 * ��ʾnotification��Ϣ
	 */
	public void showNotification() {
		nm.notify(notification_id, notification);
	}

	/**
	 * ��ȡ�汾��
	 */
	private void calculate() { 

		StringBuffer sb = new StringBuffer("");
		URL url = null;
		try {
			// ����һ��URL����
			url = new URL(strURL);
		} catch (MalformedURLException e) {
			Log.e("test", "MalformedURLException");
		}
		if (url != null) {
			try {
				// ʹ��HttpURLConnection������
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				// �õ���ȡ������(��)
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				// Ϊ�������BufferedReader
				BufferedReader buffer = new BufferedReader(in);
				String inputLine = null;
				// ʹ��ѭ������ȡ��õ�����
				while (((inputLine = buffer.readLine()) != null)) {
					// ������ÿһ�к������һ��"\n"������
					sb.append(inputLine + "/n");
				}
				// �ر�InputStreamReader
				in.close();
				// �ر�http����
				urlConn.disconnect();
			} catch (IOException e) {
				Toast.makeText(this, "�������ӳ���", Toast.LENGTH_SHORT).show();
			}
			check(sb.toString());
		}

	}

	/**
	 * ���¼��
	 * 
	 * @param mess
	 */
	public void check(String mess) {
		String version = mess.split("/n")[0];
		String url = mess.split("/n")[1];
		String ver = "" + versionCode;
		if (ver.equals(version)) {
			return;
		} else {
			showUpdateDialog(url);
		}
	}

	public static boolean isNetworkAvailable(Context ctx) {
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �������¶Ի���
	 * 
	 * @param strURL
	 */
	public void showUpdateDialog(String strURL) {
		@SuppressWarnings("unused")
		final String strURLs = strURL;
		AlertDialog alert = new AlertDialog.Builder(this).setTitle("Title")
				.setIcon(R.drawable.icon).setMessage("�Ƿ�Ҫ�������?")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						downloadTheFile(strURLs);
						dialog.cancel();
						// showWaitDialog();

					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	/**
	 * ��ȡ�汾��
	 */
	public void getCurrentVersion() {
		try {
			PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			this.versionCode = info.versionCode;
			this.versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ļ�
	 * 
	 * @param strPath
	 */
	private void downloadTheFile(String strPath) {
		final String strPaths = strPath;
		fileEx = strPaths.substring(strPaths.lastIndexOf(".") + 1,
				strPaths.length()).toLowerCase();
		fileNa = strPaths.substring(strPaths.lastIndexOf("/") + 1,
				strPaths.lastIndexOf("."));
		try {
			if (strPath.equals(currentFilePath)) {
				doDownloadTheFile(strPath);
			}
			currentFilePath = strPath;
			Runnable r = new Runnable() {
				public void run() {
					try {
						doDownloadTheFile(strPaths);
					} catch (Exception e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			};
			new Thread(r).start();
		} catch (Exception e) {
			Toast.makeText(this, "�������ӳ���", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ���ӵ�ַ���ظ���
	 * 
	 * @param strPath
	 * @throws Exception
	 */
	private void doDownloadTheFile(String strPath) throws Exception {
		// File file=null;
		if (!URLUtil.isNetworkUrl(strPath)) {
			Log.i(TAG, "getDataSource() It's a wrong URL!");
		} else {
			URL myURL = new URL(strPath);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}
			File myTempFile = createSDFile(fileNa + "." + fileEx);
			currentTempFilePath = myTempFile.getAbsolutePath();
			// delFile();
			// file = createSDFile(fileNa + "." + fileEx);
			FileOutputStream fos = new FileOutputStream(myTempFile);
			showNotification();// ��ʾnotification
			handler.post(run);
			size = conn.getContentLength();
			byte buf[] = new byte[128];
			do {
				int numread = is.read(buf);
				if (numread <= 0) {
					break;
				}
				count += numread;
				fos.write(buf, 0, numread);
			} while (true);
			openFile(myTempFile);
			try {
				is.close();
			} catch (Exception ex) {
				Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
			}
		}
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(Environment.getExternalStorageDirectory() + "/"
				+ fileName);
		if (file.exists()) {
			file.mkdirs();
		}
		file.createNewFile();
		return file;
	}

	/**
	 * �����غõ��ļ�
	 * 
	 * @param f
	 */
	private void openFile(File f) {
		/* �ر�notification֪ͨ */
		nm.cancel(notification_id);

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		this.startActivity(intent);
		nm.cancel(notification_id);
	}

	/**
	 * ɾ���ļ�
	 */
	public void delFile() {
		Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");
		File myFile = new File(currentTempFilePath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * �ļ���Ӧ����
	 * 
	 * @param f
	 * @return
	 */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}
}