package com.xiaoma.betweenactivityanimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**  
* @Title: BetweenActivityAnimationDemoActivity.java
* @Package com.xiaoma.betweenactivityanimation
* @Description: Activity֮����ת����ѧϰ  ����¶���̫�̿�������ģ�
* ���԰Ѷ���XML�ļ������ǩ���� duringֵ���õĳ�Щ�����
* @author XiaoMa
*/
public class BetweenActivityAnimationDemoActivity extends Activity implements
		OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			Intent intent = new Intent(getApplicationContext(),
					BetweenActivityAnimationDemoActivity2.class);
			startActivity(intent);
			
			/**
			 * ��������������Ĺٷ��������£��汾��2.0��ʼŶ
			 * Call immediately after one of the flavors of startActivity(Intent) or finish() 
			 * to specify an explicit transition animation to perform next.
			 * �ù��߲鵽����Ϊ��
			 *   ��startActivity(Intent)��finish()֮��֮����ú󣬻�������һ��ָ��������������XML�ļ���ִ��
			 *   ��һ��Activity 
			 * 
			 * ���������Ƕ�����������������Ľ���,�ڴ�֮ǰС��Ҳ�����±��˽��ģ�
			 * ��ʵ�Ǵ�ģ����ٷ��Ľ��Ϳ϶�û������Ӣ����ù��߲���
			 * С��һֱ��˵�ģ���Ӣ����ã����ܲ����һ��Ҳ�ܲ�õ�
			 * 1.enterAnim	A resource ID of the animation resource 
			 *              to use for the incoming activity. Use 0 for no animation.
			 * 2.exitAnim	A resource ID of the animation resource 
			 *              to use for the outgoing activity. Use 0 for no animation.
			 * һ�����붯��  һ��������Դ������Ŀ��Activity ������Ļʱ�Ķ������˴�д0�����޶���
			 * �����˳�����  һ��������Դ�����ڵ�ǰActivity �˳���Ļʱ�Ķ������˴�д0�����޶���
			 * 
			 * ���Ŀ�ꡢ��ǰ��ô��⣿���磺startActivity( A����ǰ��--> B��Ŀ�꣩) ��finish()һ����
			 * �����������һ��Ϊ0���ͱ�ʾA�˳�ʱ�޶���...һ���Ѳ������������Ȼ�����͸�����
			 * overridePendingTransition(R.anim.zoom_enter, 0);  
			 * ��������������Ŀ�ꡢ��ǰActivity��Ӧ��ϵ��Ч���·���ɫͼ��ʾ
			 */
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
	}
	
	/** �м�����׿�Դ��Ķ���Ч������ҿ��԰����� overridePendingTransition�������¿���Ч��
	 *  ʵ�ֵ��뵭����Ч��
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);    
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		 
		�������һ����Ч��
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);    
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	 */
}