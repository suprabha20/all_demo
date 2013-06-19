package com.eoeandorid.reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EoeEpubReaderActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button openEpub = (Button)findViewById(R.id.openEpub);
		openEpub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent();
				in.setClass(EoeEpubReaderActivity.this, EpubReaderActivity.class);
				startActivity(in);
			}
		});
    }
}