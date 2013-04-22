package com.xiaoma.betweenactivityanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BetweenActivityAnimationDemoActivity3 extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        init();
    }
    
    private void init(){
    	findViewById(R.id.button3).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button3)
		{
			finish();
			//�������ط��������Լ��ģ�����ֱ�ӵ���׿�ṩ�Ķ��������£�
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			
			//��׿�Դ��Ķ���Ŷ����Ч����֪����
			//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			
		}
		
	}
    
}