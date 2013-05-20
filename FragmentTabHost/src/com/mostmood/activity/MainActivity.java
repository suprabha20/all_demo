package com.mostmood.activity;

import com.mostmood.R;
import com.mostmood.fragment.Home_Fragment;
import com.mostmood.fragment.Me_Fragment;
import com.mostmood.fragment.Message_Fragment;
import com.mostmood.fragment.Wall_Fragment;
import com.mostmood.util.DummyTabContent;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;


/**
 * @Description: 主Activity，底部TabHost选项卡
 *
 * @Author Hades
 *
 * @date 2012-10-6
 *
 * @version V1.0  
 */

public class MainActivity extends FragmentActivity {

	TabHost tabHost;
	TabWidget tabWidget; 
	LinearLayout bottom_layout;
	int CURRENT_TAB = 0;	//设置常量
	Home_Fragment homeFragment;
	Wall_Fragment wallFragment;
	Message_Fragment messageFragment;
	Me_Fragment meFragment;
	android.support.v4.app.FragmentTransaction ft;
	RelativeLayout tabIndicator1,tabIndicator2,tabIndicator3,tabIndicator4,tabIndicator5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findTabView();
        tabHost.setup();
        
        /** 监听*/
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
				
				/**碎片管理*/
				android.support.v4.app.FragmentManager fm =  getSupportFragmentManager();
				homeFragment = (Home_Fragment) fm.findFragmentByTag("home");
				wallFragment = (Wall_Fragment) fm.findFragmentByTag("wall");
				messageFragment = (Message_Fragment) fm.findFragmentByTag("message");
				meFragment = (Me_Fragment) fm.findFragmentByTag("me");
				ft = fm.beginTransaction();
				
				/** 如果存在Detaches掉 */
				if(homeFragment!=null)
					ft.detach(homeFragment);
				
				/** 如果存在Detaches掉 */
				if(wallFragment!=null)
					ft.detach(wallFragment);
				
				/** 如果存在Detaches掉 */
				if(messageFragment!=null)
					ft.detach(messageFragment);
				
				/** 如果存在Detaches掉 */
				if(meFragment!=null)
					ft.detach(meFragment);
				
				/** 如果当前选项卡是home */
				if(tabId.equalsIgnoreCase("home")){
					isTabHome();
					CURRENT_TAB = 1;
					
				/** 如果当前选项卡是wall */
				}else if(tabId.equalsIgnoreCase("wall")){	
					isTabWall();
					CURRENT_TAB = 2;
					
				/** 如果当前选项卡是message */
				}else if(tabId.equalsIgnoreCase("message")){	
					isTabMessage();
					CURRENT_TAB = 3;
					
				/** 如果当前选项卡是me */
				}else if(tabId.equalsIgnoreCase("me")){	
					isTabMe();
					CURRENT_TAB = 4;
				}else{
					switch (CURRENT_TAB) {
					case 1:
						isTabHome();
						break;
					case 2:
						isTabWall();
						break;
					case 3:
						isTabMessage();
						break;
					case 4:
						isTabMe();
						break;
					default:
						isTabHome();
						break;
					}		
					
				}
					ft.commit();	
			}
        	
        };
        //设置初始选项卡  
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(tabChangeListener);
        initTab();
         /**  设置初始化界面  */
        tabHost.setCurrentTab(0);

    }
    
    //判断当前
    public void isTabHome(){
    	
    	if(homeFragment==null){		
			ft.add(R.id.realtabcontent,new Home_Fragment(), "home");						
		}else{
			ft.attach(homeFragment);						
		}
    }
    
    public void isTabWall(){
    	
    	if(wallFragment==null){
			ft.add(R.id.realtabcontent,new Wall_Fragment(), "wall");						
		}else{
			ft.attach(wallFragment);						
		}
    }
    
    public void isTabMessage(){
    	
    	if(messageFragment==null){
			ft.add(R.id.realtabcontent,new Message_Fragment(), "message");						
		}else{
			ft.attach(messageFragment);						
		}
    }
    
    public void isTabMe(){
    	
    	if(meFragment==null){
			ft.add(R.id.realtabcontent,new Me_Fragment(), "me");						
		}else{
			ft.attach(meFragment);	
		}
    }
    /**
     * 找到Tabhost布局
     */
    public void findTabView(){
    	
    	 tabHost = (TabHost) findViewById(android.R.id.tabhost);
         tabWidget = (TabWidget) findViewById(android.R.id.tabs);
         LinearLayout layout = (LinearLayout)tabHost.getChildAt(0);
         TabWidget tw = (TabWidget)layout.getChildAt(1);
         
         tabIndicator1 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab_indicator, tw, false);
         TextView tvTab1 = (TextView)tabIndicator1.getChildAt(1);
         ImageView ivTab1 = (ImageView)tabIndicator1.getChildAt(0);
         ivTab1.setBackgroundResource(R.drawable.selector_mood_home);
         tvTab1.setText(R.string.buttom_home);
         
         tabIndicator2 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab_indicator, tw, false);
         TextView tvTab2 = (TextView)tabIndicator2.getChildAt(1);
         ImageView ivTab2 = (ImageView)tabIndicator2.getChildAt(0);
         ivTab2.setBackgroundResource(R.drawable.selector_mood_wall);
         tvTab2.setText(R.string.buttom_wall);
         
         tabIndicator3 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab_indicator_camera, tw, false);
         TextView tvTab3 = (TextView)tabIndicator3.getChildAt(1);
         ImageView ivTab3 = (ImageView)tabIndicator3.getChildAt(0);
         ivTab3.setBackgroundResource(R.drawable.selector_mood_photograph);
         tvTab3.setText(R.string.buttom_camera);
          
         tabIndicator4 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab_indicator, tw, false);
         TextView tvTab4 = (TextView)tabIndicator4.getChildAt(1);
         ImageView ivTab4 = (ImageView)tabIndicator4.getChildAt(0);
         ivTab4.setBackgroundResource(R.drawable.selector_mood_message);
         tvTab4.setText(R.string.buttom_message);
         
         tabIndicator5 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab_indicator, tw, false);
         TextView tvTab5 = (TextView)tabIndicator5.getChildAt(1);
         ImageView ivTab5 = (ImageView)tabIndicator5.getChildAt(0);
         ivTab5.setBackgroundResource(R.drawable.selector_mood_my_wall);
         tvTab5.setText(R.string.buttom_me);
    }
    
    /** 
     * 初始化选项卡
     * 
     * */
    public void initTab(){
    	
        TabHost.TabSpec tSpecHome = tabHost.newTabSpec("home");
        tSpecHome.setIndicator(tabIndicator1);        
        tSpecHome.setContent(new DummyTabContent(getBaseContext()));
        tabHost.addTab(tSpecHome);
        
        TabHost.TabSpec tSpecWall = tabHost.newTabSpec("wall");
        tSpecWall.setIndicator(tabIndicator2);        
        tSpecWall.setContent(new DummyTabContent(getBaseContext()));
        tabHost.addTab(tSpecWall);
        
        TabHost.TabSpec tSpecCamera = tabHost.newTabSpec("camera");
        tSpecCamera.setIndicator(tabIndicator3);        
        tSpecCamera.setContent(new DummyTabContent(getBaseContext()));
        tabHost.addTab(tSpecCamera);
        
        //拍照按钮监听事件，弹出dialog
        tabIndicator3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Dialog choose = new Dialog(MainActivity.this,R.style.draw_dialog);
		    	choose.setContentView(R.layout.camera_dialog);
		    	// 设置背景模糊参数
				WindowManager.LayoutParams winlp = choose.getWindow()
						.getAttributes();
				winlp.alpha = 0.9f; // 0.0-1.0
				choose.getWindow().setAttributes(winlp);
				choose.show();// 显示弹出框
			}
		});
        
        TabHost.TabSpec tSpecMessage = tabHost.newTabSpec("message");
        tSpecMessage.setIndicator(tabIndicator4);      
        tSpecMessage.setContent(new DummyTabContent(getBaseContext()));
        tabHost.addTab(tSpecMessage);
        
        TabHost.TabSpec tSpecMe = tabHost.newTabSpec("me");
        tSpecMe.setIndicator(tabIndicator5);        
        tSpecMe.setContent(new DummyTabContent(getBaseContext()));
        tabHost.addTab(tSpecMe);
        
    }
    
}
