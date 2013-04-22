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
	 * ����popupwindow
	 */	
	private PopupWindow popup;
	/**
	 * ����������
	 */
	private MenuAdapter menuAdapter;
	//�˵����б�
	private List<MenuInfo> menulists;
	//����gridview
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
	 * ����PopupWindows
	 */
    private void initPopuWindows() {
    	//��ʼ��gridview
		menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu, null);
		//��ʼ��PopupWindow,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT������ʾ
		popup = new PopupWindow(menuGridView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// ����menu�˵�����
		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
		// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ
		popup.setFocusable(true);
		//������ʾ�����صĶ���
		popup.setAnimationStyle(R.style.menushow);
		popup.update();
		//���ô�����ȡ����
		menuGridView.setFocusableInTouchMode(true);
		//���ü����¼�,������²˵��������ز˵�
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
		//��Ӳ˵���ť�¼�
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
					Toast.makeText(MenuLikeQQActivity.this, "��Ӻ���", 1).show();
					break;
				case MenuUtils.MENU_ADD_GROUP:
					Toast.makeText(MenuLikeQQActivity.this, "��ӷ���", 1).show();
					break;
				case MenuUtils.MENU_EXIT:
					Toast.makeText(MenuLikeQQActivity.this, "�˳�Ӧ��", 1).show();
					break;
				case MenuUtils.MENU_GROUP_ACCURATE:

					break;
				case MenuUtils.MENU_GROUP_CATEGORY:

					break;
				case MenuUtils.MENU_HELP:
					Toast.makeText(MenuLikeQQActivity.this, "������", 1).show();

					break;
				case MenuUtils.MENU_LOGOUT:
					Toast.makeText(MenuLikeQQActivity.this, "�л��û�", 1).show();
					break;
				case MenuUtils.MENU_SERCH_FRIEND:
					Toast.makeText(MenuLikeQQActivity.this, "��������", 1).show();
					break;
				case MenuUtils.MENU_SETTING:
					Toast.makeText(MenuLikeQQActivity.this, "����", 1).show();
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
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}
}