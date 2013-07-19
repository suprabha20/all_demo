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
 * ryan:�̳�InstrumentationTestCase��ʵ�ֵĹ��������ᱻ�Զ����ã��Զ����ԡ�private�������ᱻ���á�
 * �������ʵ�ֶ�һ����Ĳ���
 * 
 * android�еĲ��Է�����Ҫ��AndroidTextCase��InstrumentationTextCase��
 * 
 * Instrumentation��Activity�е����ƣ�ֻ����Activity����Ҫһ������ģ���Instrumentation�����������ģ�
 * ���ǿ��Խ������Ϊһ��û��ͼ�ν���ģ��������������ģ����ڼ��������(��Target Package����)�Ĺ����ࡣ
 * 
 * @author ryanlee
 * 
 */
public class HelloTest extends InstrumentationTestCase {

	Hello mActivityTested;

	public HelloTest() {
	}

	/*
	 * ��ʼ����
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
	 * ������������Դ����
	 * 
	 * @see android.test.InstrumentationTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		mActivityTested.finish();
		super.tearDown();
	}

	/**
	 * ����ܲ���
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
	 * ģ�ⰴť����Ľӿ�
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
			// ģ�ⰴť���
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
