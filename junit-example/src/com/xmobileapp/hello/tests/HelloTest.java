package com.xmobileapp.hello.tests;

import android.content.Intent;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmobileapp.hello.Hello;
import com.xmobileapp.hello.R;

/**
 * 
 * ryan:继承InstrumentationTestCase中实现的公开方法会被自动调用，自动测试。private方法不会被调用。
 * 这里可以实现对一个类的测试
 * 
 * android中的测试方法主要有AndroidTextCase和InstrumentationTextCase。
 * 
 * Instrumentation和Activity有点类似，只不过Activity是需要一个界面的，而Instrumentation并不是这样的，
 * 我们可以将它理解为一种没有图形界面的，具有启动能力的，用于监控其他类(用Target Package声明)的工具类。
 * 
 * @author ryanlee
 * 
 */
public class HelloTest extends InstrumentationTestCase {

	Hello mActivityTested;

	public HelloTest() {
	}

	/*
	 * 初始设置
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Intent intent = new Intent();
		intent.setClassName("com.xmobileapp.hello", Hello.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mActivityTested = (Hello) getInstrumentation()
				.startActivitySync(intent);
	}

	/*
	 * 垃圾清理与资源回收
	 * 
	 * @see android.test.InstrumentationTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		mActivityTested.finish();
		super.tearDown();
	}

	/**
	 * 活动功能测试
	 * 
	 * @throws Exception
	 */
	public void testClickButtonToShowText() throws Exception {
		TextView tv = (TextView) mActivityTested.findViewById(R.id.text);
		SystemClock.sleep(2000);
		assertEquals("TextView should be Gone before Button Clicking",
				View.GONE, tv.getVisibility());

		Button btn = (Button) mActivityTested.findViewById(R.id.button);
		getInstrumentation().runOnMainSync(new PerformClick(btn));
		SystemClock.sleep(2000);
		assertEquals("TextView should be Visible after Button Clicking",
				View.VISIBLE, tv.getVisibility());
	}

	/**
	 * 模拟按钮点击的接口
	 * 
	 * @author ryanlee
	 * 
	 */
	private class PerformClick implements Runnable {

		Button mBtnClicked;

		public PerformClick(Button button) {
			mBtnClicked = button;
		}

		public void run() {
			// 模拟按钮点击
			mBtnClicked.performClick();
		}
	}

	public void testAdd() throws Exception {

		String tag = "testAdd";
		Log.v(tag, "test the method.");
		int test = mActivityTested.add(1, 1);
		assertEquals(2, test);
	}

}
