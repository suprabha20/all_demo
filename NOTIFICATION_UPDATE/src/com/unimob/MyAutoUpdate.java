package com.unimob;   
import java.io.BufferedReader;
import java.io.File;   
import java.io.FileOutputStream;   
import java.io.FileReader;
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
import android.app.ProgressDialog;   
import android.content.Context;   
import android.content.DialogInterface;   
import android.content.Intent;   
import android.content.pm.PackageInfo;   
import android.content.pm.PackageManager.NameNotFoundException;   
import android.net.ConnectivityManager;   
import android.net.NetworkInfo;   
import android.net.Uri;   
import android.os.Handler;
import android.util.Log;   
import android.webkit.URLUtil;   
import android.widget.Toast;
/**  
 * Android AutoUpdate.  
 *   
 * lazybone/2010.08.20  
 *   
 * 1.Set apkUrl.  
 *   
 * 2.check().  
 *   
 * 3.add delFile() method in resume()\onPause().  
 */  
public class MyAutoUpdate {   
    public Activity activity = null;   
    public int versionCode = 0;   
    public String versionName = "";   
    private static final String TAG = "AutoUpdate";   
    private String currentFilePath = "";   
    private String currentTempFilePath = "";   
    private String fileEx = "";   
    private String fileNa = "";   
    private String strURL = "http://211.154.153.84/swt/qiye/version";   
    private ProgressDialog dialog;   
	int notification_id=19172439;
	NotificationManager nm;
	Handler handler=new Handler();
	Notification notification;
	int count=0;
    public MyAutoUpdate(Activity activity) {   
        this.activity = activity;   
        getCurrentVersion();   
        calculate();
    }   
    
    private void calculate()  
    {  //http://211.154.153.84/swt/qiye/version
    	
    	StringBuffer sb=new StringBuffer("");  
    	URL url = null;
		try
		{
			//构造一个URL对象
			url = new URL("http://211.154.153.84/swt/qiye/version"); 
		}
		catch (MalformedURLException e)
		{
			Log.e("test", "MalformedURLException");
		}
		if (url != null)
		{
			try
			{
				//使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				//得到读取的内容(流)
				InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
				// 为输出创建BufferedReader
				BufferedReader buffer = new BufferedReader(in);
				String inputLine = null;
				//使用循环来读取获得的数据
				while (((inputLine = buffer.readLine()) != null))
				{
					//我们在每一行后面加上一个"\n"来换行
					sb.append(inputLine+"/n");
				}		  
				//关闭InputStreamReader
				in.close();
				//关闭http连接
				urlConn.disconnect();
			}catch(IOException e)
			{
				Toast.makeText(activity, "网络连接出错", Toast.LENGTH_SHORT).show();
			}
			check(sb.toString());
		}
		
    } 
    
    public void check(String mess) { 
    	String version= mess.split("/n")[0]; 
    	String url = mess.split("/n")[1];
    	String ver = ""+versionCode;
    	if(ver.equals(version)){
    	    return;	
    	}else{
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
    public void showUpdateDialog(String strURL) {   
        @SuppressWarnings("unused")  
       final String strURLs= strURL;
        AlertDialog alert = new AlertDialog.Builder(this.activity)   
                .setTitle("Title")   
                .setIcon(R.drawable.icon)   
                .setMessage("是否要升级软件?")   
                .setPositiveButton("确定",   
                        new DialogInterface.OnClickListener() {   
                            public void onClick(DialogInterface dialog,   
                                    int which) {   
                                downloadTheFile(strURLs); 
                                dialog.cancel();  
                                showWaitDialog();   
                                
                            }   
                        })   
                .setNegativeButton("取消",   
                        new DialogInterface.OnClickListener() {   
                            public void onClick(DialogInterface dialog,   
                                    int which) {   
                                dialog.cancel();   
                            }   
                        }).show();   
    }   
    public void showWaitDialog() {   
        dialog = new ProgressDialog(activity);   
        dialog.setMessage("软件升级中...");   
        dialog.setIndeterminate(true);   
        dialog.setCancelable(true);   
        dialog.show();   
    }   
    public void getCurrentVersion() {   
        try {   
            PackageInfo info = activity.getPackageManager().getPackageInfo(   
                    activity.getPackageName(), 0);   
            this.versionCode = info.versionCode;   
            this.versionName = info.versionName;   
        } catch (NameNotFoundException e) {   
            e.printStackTrace();   
        }   
    }   
    private void downloadTheFile(String strPath) {   
    	final String strPaths = strPath;
        fileEx = strPaths.substring(strPaths.lastIndexOf(".") + 1, strPaths.length())   
                .toLowerCase();   
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
        	Toast.makeText(activity, "网络连接出错", Toast.LENGTH_SHORT).show();
        }   
    }   
    private void doDownloadTheFile(String strPath) throws Exception {   
        Log.i(TAG, "getDataSource()");   
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
            File myTempFile = File.createTempFile(fileNa, "." + fileEx);   
            currentTempFilePath = myTempFile.getAbsolutePath();   
            FileOutputStream fos = new FileOutputStream(myTempFile);   
            byte buf[] = new byte[128];   
            do {   
                int numread = is.read(buf);   
                if (numread <= 0) {   
                    break;   
                }   
                fos.write(buf, 0, numread);   
            } while (true);   
            Log.i(TAG, "getDataSource() Download  ok...");   
            dialog.cancel();   
            dialog.dismiss();   
            openFile(myTempFile);   
            try {   
                is.close();   
            } catch (Exception ex) {   
                Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);   
            }   
        }   
    }   
    private void openFile(File f) {   
        Intent intent = new Intent();   
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        intent.setAction(android.content.Intent.ACTION_VIEW);   
        String type = getMIMEType(f);   
        intent.setDataAndType(Uri.fromFile(f), type);   
        activity.startActivity(intent);   
    }   
    public void delFile() {   
        Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");   
        File myFile = new File(currentTempFilePath);   
        if (myFile.exists()) {   
            myFile.delete();   
        }   
    }   
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