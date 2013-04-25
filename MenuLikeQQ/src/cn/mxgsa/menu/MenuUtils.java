package cn.mxgsa.menu;

import java.util.ArrayList;
import java.util.List;
import cn.mxgsa.menu.MenuInfo;

public class MenuUtils {
	public static final int MENU_SETTING=1;
	public static final int MENU_LOGOUT=2;
	public static final int MENU_HELP=3;
	public static final int MENU_EXIT=4;
	public static final int MENU_LOGIN=5;
	public static final int MENU_SERCH_FRIEND=6;
	public static final int MENU_GROUP_ACCURATE=7;
	public static final int MENU_GROUP_CATEGORY=8;
	public static final int MENU_CLEAR_LIST=9;
	public static final int MENU_ADD_FRIEND=10;
	public static final int MENU_ADD_GROUP=11;	
	public static final int MENU_CHAT_HISTORY=12;
	public static final int MENU_ONE_CLOSE=13;
	public static final int MENU_MULITE_CLOSE=14;
	
	private static List<MenuInfo> initMenu(){
		List<MenuInfo> list=new ArrayList<MenuInfo>();
		list.add(new MenuInfo(MENU_SETTING,"设置",R.drawable.menu_ic_setting,false));
		list.add(new MenuInfo(MENU_LOGOUT,"切换用户",R.drawable.menu_ic_logout,false));
		list.add(new MenuInfo(MENU_HELP,"检查更新",R.drawable.menu_ic_help,false));
		list.add(new MenuInfo(MENU_EXIT,"退出应用",R.drawable.menu_ic_exit,false));
		return list;
	}
	
	/**
	 * 获取当前菜单列表
	 * @return
	 */
	public static List<MenuInfo> getMenuList(){
		List<MenuInfo> list=initMenu();		
			list.add(0,new MenuInfo(MENU_SERCH_FRIEND,"搜索好友",R.drawable.menu_ic_search_friend,false));
			list.add(0,new MenuInfo(MENU_ADD_GROUP,"添加分组",R.drawable.menu_ic_addgroup,false));
			list.add(0,new MenuInfo(MENU_ADD_FRIEND,"添加好友",R.drawable.menu_ic_addfriend,false));
			list.add(0,new MenuInfo(MENU_SERCH_FRIEND,"搜索好友",R.drawable.menu_ic_search_friend,false));
		
			
		return list;
	}
	
}
