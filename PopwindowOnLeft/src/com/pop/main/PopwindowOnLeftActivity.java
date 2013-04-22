package com.pop.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopwindowOnLeftActivity extends Activity {
	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 点击按钮弹出菜单
		Button pop = (Button) findViewById(R.id.popBtn);
		pop.setOnClickListener(popClick);
	}
    //点击弹出左侧菜单的显示方式
	OnClickListener popClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getPopupWindow();
			// 这里是位置显示方式,在按钮的左下角
			popupWindow.showAsDropDown(v);
			// 这里可以尝试其它效果方式,如popupWindow.showAsDropDown(v,
			// (screenWidth-dialgoWidth)/2, 0);
			// popupWindow.showAtLocation(findViewById(R.id.layout),
			// Gravity.CENTER, 0, 0);
		}
	};

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub

		// 获取自定义布局文件pop.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.pop, null,
				false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view, 200, 150, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		//点击其他地方消失		
		popupWindow_view.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
					}				
				return false;
			}
		});		
		// pop.xml视图里面的控件
		Button open = (Button) popupWindow_view.findViewById(R.id.open);
		Button save = (Button) popupWindow_view.findViewById(R.id.save);
		Button close = (Button) popupWindow_view.findViewById(R.id.close);
		// pop.xml视图里面的控件触发的事件
		// 打开
		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 这里可以执行相关操作
				System.out.println("打开操作");
				// 对话框消失
				popupWindow.dismiss();
			}
		});
		// 保存
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 这里可以执行相关操作
				System.out.println("保存操作");
				popupWindow.dismiss();
			}
		});
		// 关闭
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 这里可以执行相关操作
				System.out.println("关闭操作");
				popupWindow.dismiss();
			}
		});

	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow() {

		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}
}