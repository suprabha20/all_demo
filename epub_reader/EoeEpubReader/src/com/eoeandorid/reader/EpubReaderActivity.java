package com.eoeandorid.reader;

import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EpubReaderActivity extends Activity implements OnGestureListener {

	public static EpubReaderActivity activity;
	private EpubKernel epubKernel;
	private GestureDetector gd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gd = new GestureDetector((OnGestureListener)this);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		// 设置全屏  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.epub_reader);

		activity = this;

		epubKernel = new EpubKernel();
		try {
			epubKernel.openEpubFile(Environment.getExternalStorageDirectory().getPath()+"/EoeReader/55369.epub", Environment.getExternalStorageDirectory().getPath()+"/EoeReader/epub/55369");
		} catch (Exception e) {
		}

		initUiView();
		initData();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private LinearLayout mEpubLinearLayout;
	private EpubWebView mCurEpubWebView = null;
	private EpubWebView mPreEpubWebview = null;
	private EpubWebView mNextEpubWebview = null;
	private int scrollWidth = 0;
	private int mTotalPage = 0;
	private int curHtmlPage = 0;
	private int htmlSize = 0;
	private static final int NEXT_PAGE = 0;
	private static final int PRE_PAGE = 1;

	private void initUiView(){
		mEpubLinearLayout = (LinearLayout)findViewById(R.id.epubLinearLayout);
	}

	private void initData(){

		Map spainMap = epubKernel.getSpineMap();
		htmlSize = spainMap.size();

		String curHtml = epubKernel.getHtmlUrlByIndex(curHtmlPage);
		mCurEpubWebView = new EpubWebView(this);

		mEpubLinearLayout.addView(mCurEpubWebView,
				new LinearLayout.LayoutParams(UIHelper.getScreenWidth(activity),UIHelper.getScreenHeight(activity)));
		mCurEpubWebView.setAct(activity);
		mCurEpubWebView.loadUrl(curHtml);

		mPreEpubWebview = new EpubWebView(this);
		mPreEpubWebview.setVisibility(View.GONE);
		mEpubLinearLayout.addView(mPreEpubWebview,
				new LinearLayout.LayoutParams(UIHelper.getScreenWidth(activity),UIHelper.getScreenHeight(activity)));
		mCurEpubWebView.setAct(activity);

		curHtmlPage++;

		String nextHtml = epubKernel.getHtmlUrlByIndex(curHtmlPage);
		mNextEpubWebview = new EpubWebView(this);
		mNextEpubWebview.setAct(activity);
		mEpubLinearLayout.addView(mNextEpubWebview,
				new LinearLayout.LayoutParams(UIHelper.getScreenWidth(activity),UIHelper.getScreenHeight(activity)));
		LoadHtmlTask htt = new LoadHtmlTask(mNextEpubWebview, nextHtml);
		htt.execute();
	}

	/*override dispatchTouchEvent method and dispatch the touch event to GestureDetector*/
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gd.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	private int verticalMinDistance = 20;  
	private int minVelocity         = 0;
	private double mCurX = 0;
	private double curPage = 1;
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		this.mTotalPage = mCurEpubWebView.getTotalPage();
		if (e1.getX() - e2.getX() > verticalMinDistance 
				&& Math.abs(velocityX) > minVelocity) {
			if(curPage >= mTotalPage && curHtmlPage == htmlSize){
				Toast.makeText(activity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
				return true;
			}
			curPage++;
			if(curPage > mTotalPage && curHtmlPage < htmlSize ){
				curPage = 1;
				changePage(NEXT_PAGE);
			}
			mCurX = (curPage-1) * UIHelper.getScreenWidth(activity);
			mCurEpubWebView.scrollTo((int)mCurX, 0);
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			if(curPage <= 1 && curHtmlPage <= 1){
				Toast.makeText(activity, "已经是第一页", Toast.LENGTH_SHORT).show();
				return true;
			}
			curPage--;
			if(curPage <= 0 && curHtmlPage <= htmlSize ){
				changePage(PRE_PAGE);
				curPage = mCurEpubWebView.getTotalPage();
			}
			mCurX = (curPage-1) * UIHelper.getScreenWidth(activity);
			mCurEpubWebView.scrollTo((int)mCurX, 0);
		}  
		return true;
	}

	private void changePage(int num){

		EpubWebView tmpCurWebView = mCurEpubWebView;
		EpubWebView tmpNextWebView = mNextEpubWebview;
		EpubWebView tmpPreWebView = mPreEpubWebview;

		if(num == NEXT_PAGE){

			mCurEpubWebView = tmpNextWebView;
			mPreEpubWebview = tmpCurWebView;
			mNextEpubWebview = tmpPreWebView;

			tmpNextWebView.setVisibility(View.VISIBLE);
			tmpCurWebView.setVisibility(View.GONE);
			tmpPreWebView.setVisibility(View.GONE);
			curHtmlPage++;
			if(curHtmlPage < htmlSize){
				preLoadNextHtml(mNextEpubWebview,curHtmlPage);
			}
		}else if(num == PRE_PAGE){

			mCurEpubWebView = tmpPreWebView;
			mPreEpubWebview = tmpNextWebView;
			mNextEpubWebview = tmpCurWebView;

			tmpPreWebView.setVisibility(View.VISIBLE);
			tmpCurWebView.setVisibility(View.GONE);
			tmpNextWebView.setVisibility(View.GONE);

			curHtmlPage--;
			int preHtmlPage = curHtmlPage-2;
			if(preHtmlPage >= 0){
				preLoadNextHtml(mPreEpubWebview,preHtmlPage);
			}
		}
	}

	private void preLoadNextHtml(EpubWebView epubView,int page){
		String nextHtml = epubKernel.getHtmlUrlByIndex(page);
		epubView.setAct(activity);
		LoadHtmlTask htt = new LoadHtmlTask(epubView, nextHtml);
		htt.execute();
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	//use AsyncTask loading previous or next HTML file. 
	private class LoadHtmlTask extends AsyncTask<Void,Void,Void>{

		private EpubWebView epubWebview;
		private String loadUrl;

		public LoadHtmlTask(EpubWebView epubWebview,String loadUrl){
			this.epubWebview = epubWebview;
			this.loadUrl = loadUrl;
		}

		@Override
		protected Void doInBackground(Void... params) {
			epubWebview.loadUrl(loadUrl);
			return null;
		}
	}
}
