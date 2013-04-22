package cn.mxgsa.menu;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MenuLikeQQActivity extends Activity {
	
	/**
	 * 定义popupwindow
	 */	
	private PopupWindow popup;
	/**
	 * 定义适配器
	 */
	private MenuAdapter menuAdapter;
	//菜单项列表
	private List<MenuInfo> menulists;
	//定义gridview
	private GridView menuGridView;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initPopuWindows();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}
    
	/**
	 * 设置PopupWindows
	 */
    private void initPopuWindows() {
    	//初始化gridview
		menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu, null);
		//初始化PopupWindow,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT控制显示
		popup = new PopupWindow(menuGridView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// 设置menu菜单背景
		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
		// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		popup.setFocusable(true);
		//设置显示和隐藏的动画
		popup.setAnimationStyle(R.style.menushow);
		popup.update();
		//设置触摸获取焦点
		menuGridView.setFocusableInTouchMode(true);
		//设置键盘事件,如果按下菜单键则隐藏菜单
		menuGridView.setOnKeyListener(new android.view.View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (popup.isShowing())) {
					popup.dismiss();  
					return true;
					
				}
				return false;
			}

		});
		//添加菜单按钮事件
		menuGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				MenuInfo mInfo = menulists.get(arg2);
				popup.dismiss();
				if (mInfo.ishide) {
					return;
				}
				switch (mInfo.menuId) {
				case MenuUtils.MENU_ADD_FRIEND:
					Toast.makeText(MenuLikeQQActivity.this, "添加好友", 1).show();
					break;
				case MenuUtils.MENU_ADD_GROUP:
					Toast.makeText(MenuLikeQQActivity.this, "添加分组", 1).show();
					break;
				case MenuUtils.MENU_EXIT:
					Toast.makeText(MenuLikeQQActivity.this, "退出应用", 1).show();
					break;
				case MenuUtils.MENU_GROUP_ACCURATE:

					break;
				case MenuUtils.MENU_GROUP_CATEGORY:

					break;
				case MenuUtils.MENU_HELP:
					Toast.makeText(MenuLikeQQActivity.this, "检查更新", 1).show();

					break;
				case MenuUtils.MENU_LOGOUT:
					Toast.makeText(MenuLikeQQActivity.this, "切换用户", 1).show();
					break;
				case MenuUtils.MENU_SERCH_FRIEND:
					Toast.makeText(MenuLikeQQActivity.this, "搜索好友", 1).show();
					break;
				case MenuUtils.MENU_SETTING:
					Toast.makeText(MenuLikeQQActivity.this, "设置", 1).show();
					break;
				}
			}
		});
	}
    
    @Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (popup != null) {
			menulists = MenuUtils.getMenuList();
			menuAdapter = new MenuAdapter(this, menulists);
			menuGridView.setAdapter(menuAdapter);
			popup.showAtLocation(this.findViewById(R.id.linearlayout), Gravity.BOTTOM, 0, 0);
		}
		return false;// 返回为true 则显示系统menu
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}
}