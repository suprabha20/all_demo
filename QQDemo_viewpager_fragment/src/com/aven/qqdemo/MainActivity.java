package com.aven.qqdemo;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.R;

/**
 * 参考原作者D.Winter基础，
 * 
 * @author avenwu iamavenwu@gmail.com
 * 
 */
public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private ViewPager mPager;// 页卡内容
	private ArrayList<Fragment> fragmentsList;// Tab页面列表
	private ImageView ivBottomLine;// 下划线
	private TextView tvTabActivity, tvTabGroups, tvTabFriends, tvTabChat;

	private int currIndex = 0;// 当前页卡编号
	private int bottomLineWidth;// 动画图片宽度
	private int offset = 0;// 动画图片偏移量
	private int position_one;
	private int position_two;
	private int position_three;
	private Resources resources;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		resources = getResources();
		InitWidth();
		InitTextView();
		InitViewPager();
	}

	private void InitTextView() {
		tvTabActivity = (TextView) findViewById(R.id.tv_tab_activity);
		tvTabGroups = (TextView) findViewById(R.id.tv_tab_groups);
		tvTabFriends = (TextView) findViewById(R.id.tv_tab_friends);
		tvTabChat = (TextView) findViewById(R.id.tv_tab_chat);

		tvTabActivity.setOnClickListener(new MyOnClickListener(0));
		tvTabGroups.setOnClickListener(new MyOnClickListener(1));
		tvTabFriends.setOnClickListener(new MyOnClickListener(2));
		tvTabChat.setOnClickListener(new MyOnClickListener(3));
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();
		LayoutInflater mInflater = getLayoutInflater();
		View activityView = mInflater.inflate(R.layout.lay1, null);

		Fragment activityfragment = TestFragment.newInstance("Hello Activity.");
		Fragment groupFragment = TestFragment.newInstance("Hello Group.");
		Fragment friendsFragment = TestFragment.newInstance("Hello Friends.");
		Fragment chatFragment = TestFragment.newInstance("Hello Chat.");

		fragmentsList.add(activityfragment);
		fragmentsList.add(groupFragment);
		fragmentsList.add(friendsFragment);
		fragmentsList.add(chatFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitWidth() {
		ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
		// 滚动条的长度,已经确定.
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		Log.d(TAG, "cursor imageview width=" + bottomLineWidth);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		// offset滚动条间隔长度,根据全部长度和滚动条的长度,项目个数计算出来
		offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
		Log.i("MainActivity", "offset=" + offset);

		// 4个项目共有3条分隔线,计算出它们的位置
		position_one = (int) (screenW / 4.0);
		position_two = position_one * 2;
		position_three = position_one * 3;
	}

	// 选项卡按钮事件
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	// 页面切换监听
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				// currIndex当前页面所在索引
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					tvTabGroups.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					tvTabFriends.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three, 0, 0, 0);
					tvTabChat.setTextColor(resources
							.getColor(R.color.lightwhite));
				}
				tvTabActivity.setTextColor(resources.getColor(R.color.white));
				break;
			case 1:
				if (currIndex == 0) {
					// 从索引0滑到索引1
					animation = new TranslateAnimation(offset, position_one, 0,
							0);
					tvTabActivity.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_one, 0, 0);
					tvTabFriends.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three,
							position_one, 0, 0);
					tvTabChat.setTextColor(resources
							.getColor(R.color.lightwhite));
				}
				// 设回原来的颜色
				tvTabGroups.setTextColor(resources.getColor(R.color.white));
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_two, 0,
							0);
					tvTabActivity.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_two, 0, 0);
					tvTabGroups.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three,
							position_two, 0, 0);
					tvTabChat.setTextColor(resources
							.getColor(R.color.lightwhite));
				}
				tvTabFriends.setTextColor(resources.getColor(R.color.white));
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_three,
							0, 0);
					tvTabActivity.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_three, 0, 0);
					tvTabGroups.setTextColor(resources
							.getColor(R.color.lightwhite));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_three, 0, 0);
					tvTabFriends.setTextColor(resources
							.getColor(R.color.lightwhite));
				}
				tvTabChat.setTextColor(resources.getColor(R.color.white));
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}