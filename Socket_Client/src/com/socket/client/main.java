package com.socket.client;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class main extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	//定义声明需要用到的UI元素
	private EditText edtmsgcontent;
	private Button btnSend;
	private String ip="169.254.191.14";
	private int port=1818;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        InitView();
    }
    private void InitView()
    {
    	//显示主界面
    	setContentView(R.layout.main);
    	
    	//通过id获取ui元素对象
    	edtmsgcontent=(EditText)findViewById(R.id.msgcontent);
    	btnSend=(Button)findViewById(R.id.btnsend);
    	
    	//为btnsend设置点击事件
    	btnSend.setOnClickListener(this);
    }
    
    public void onClick(View bt)
    {
    	try 
		{
			String msg=edtmsgcontent.getText().toString();
			if(!TextUtils.isEmpty(msg))
				SendMsg(ip,port,msg);
			else
			{
				Toast.makeText(this,"请先输入要发送的内容", Toast.LENGTH_LONG);
				edtmsgcontent.requestFocus();
			}
		}
	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 	}
	 }
    public void SendMsg(String ip,int port,String msg) throws UnknownHostException, IOException
    {
    	try
    	{
    	Socket socket=null;
    	socket=new Socket(ip,port);
    	BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    	writer.write(msg);
    	writer.flush();
    	writer.close();
    	socket.close();
    	}
    	catch(UnknownHostException e)
    	{
    		e.printStackTrace();
    	} catch (IOException e) 
    	{
    	    e.printStackTrace();
        }
    }
}