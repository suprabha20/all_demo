package com.andorid.shu.love;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.FileBrowser.FileActivityHelper;
import com.android.FileBrowser.FileAdapter;
import com.android.FileBrowser.FileInfo;
import com.android.FileBrowser.FileUtil;
import com.android.FileBrowser.Main;
import com.sqlite.DbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.ListActivity;

public class LoveReaderActivity extends Activity {

	private static Boolean isExit = false;// �����ж��Ƿ��Ƴ�
	private static Boolean hasTask = false;
	private Context myContext;
	private ShelfAdapter mAdapter;
	private Button shelf_image_button;
	private ListView shelf_list;
	private Button buttontt;
	int[] size = null;// ��������
	private final int SPLASH_DISPLAY_LENGHT = 5000; // �ӳ�����
	private String txtPath = "/sdcard/lovereader/���°ٿ�.txt";
	private final int MENU_RENAME = Menu.FIRST;
	DbHelper db;
	List<BookInfo> books;
	int realTotalRow;
	int bookNumber; // ͼ�������
	final String[] font = new String[] { "20", "24", "26", "30", "32", "36",
			"40", "46", "50", "56", "60", "66", "70" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// AdManager.init(this,"893693f61b171f26", "fa396d910a218fa7", 30,
		// false);
		// YoumiOffersManager.init(this, "893693f61b171f26",
		// "fa396d910a218fa7");
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.shelf);
		// addYoumi();
		// ���׹��
		// ��AdView.setVisibility(GONE)�������ع��������AdView.setVisibility(VISIBLE)������ʾ���
		// new Handler().postDelayed(new Runnable() {
		// // Ϊ�˼��ٴ���ʹ������Handler����һ����ʱ�ĵ���
		// public void run() {
		// Intent i = new Intent(ReaderActivity.this, ReaderActivity.class);
		// // ͨ��Intent������������������Main���Activity
		// ReaderActivity.this.startActivity(i); // ����Main����
		// ReaderActivity.this.finish(); // �ر��Լ����������
		// }
		// }, SPLASH_DISPLAY_LENGHT);
		db = new DbHelper(this);
		if (!copyFile()) {
			// Toast.makeText(this, "�����鲻���ڣ�", Toast.LENGTH_SHORT).show();
		}
		myContext = this;
		init();
		/************** ��ʼ�����ͼ�� *********************/
		books = db.getAllBookInfo();// ȡ�����е�ͼ��
		bookNumber = books.size();
		int count = books.size();
		int totalRow = count / 3;
		if (count % 3 > 0) {
			totalRow = count / 3 + 1;
		}
		realTotalRow = totalRow;
		if (totalRow < 4) {
			totalRow = 4;
		}
		size = new int[totalRow];
		/***********************************/
		mAdapter = new ShelfAdapter();// new adapter���������
		shelf_list.setAdapter(mAdapter);
		// ע��ContextView��view��
	}

	private void init() {
		shelf_image_button = (Button) findViewById(R.id.shelf_image_button);
		shelf_list = (ListView) findViewById(R.id.shelf_list);
	}

	public class ShelfAdapter extends BaseAdapter {

		public ShelfAdapter() {
		}

		@Override
		public int getCount() {
			if (size.length > 3) {
				return size.length;
			} else {
				return 3;
			}
		}

		@Override
		public Object getItem(int position) {
			return size[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layout_inflater = (LayoutInflater) LoveReaderActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layout_inflater.inflate(R.layout.shelf_list_item,
					null);
			if (position < realTotalRow) {
				int buttonNum = (position + 1) * 3;
				if (bookNumber <= 3) {
					buttonNum = bookNumber;
				}
				for (int i = 0; i < buttonNum; i++) {
					if (i == 0) {
						BookInfo book = books.get(position * 3);
						String buttonName = book.bookname;
						buttonName = buttonName.substring(0,
								buttonName.indexOf("."));
						Button button = (Button) layout
								.findViewById(R.id.button_1);
						button.setVisibility(View.VISIBLE);
						button.setText(buttonName);
						button.setId(book.id);
						button.setOnClickListener(new ButtonOnClick());
						button.setOnCreateContextMenuListener(listener);
					} else if (i == 1) {
						BookInfo book = books.get(position * 3 + 1);
						String buttonName = book.bookname;
						buttonName = buttonName.substring(0,
								buttonName.indexOf("."));
						Button button = (Button) layout
								.findViewById(R.id.button_2);
						button.setVisibility(View.VISIBLE);
						button.setText(buttonName);
						button.setId(book.id);
						button.setOnClickListener(new ButtonOnClick());
						button.setOnCreateContextMenuListener(listener);
					} else if (i == 2) {
						BookInfo book = books.get(position * 3 + 2);
						String buttonName = book.bookname;
						buttonName = buttonName.substring(0,
								buttonName.indexOf("."));
						Button button = (Button) layout
								.findViewById(R.id.button_3);
						button.setVisibility(View.VISIBLE);
						button.setText(buttonName);
						button.setId(book.id);
						button.setOnClickListener(new ButtonOnClick());
						button.setOnCreateContextMenuListener(listener);
					}
				}
				bookNumber -= 3;
			}
			return layout;
		}
	};

	// ��ӳ������
	OnCreateContextMenuListener listener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// menu.setHeaderTitle(String.valueOf(v.getId()));
			menu.add(0, 0, v.getId(), "��ϸ��Ϣ");
			menu.add(0, 1, v.getId(), "ɾ������");
		}
	};

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case 0:

			break;
		case 1:
			Dialog dialog = new AlertDialog.Builder(LoveReaderActivity.this)
					.setTitle("��ʾ")
					.setMessage("ȷ��Ҫɾ����")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									BookInfo book = db.getBookInfo(item
											.getOrder());
									File dest = new File("/sdcard/lovereader/"
											+ book.bookname);
									db.delete(item.getOrder());
									if (dest.exists()) {
										dest.delete();
										Toast.makeText(myContext, "ɾ���ɹ�",
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(myContext, "�����ļ�ɾ��ʧ��",
												Toast.LENGTH_SHORT).show();
									}
									refreshShelf();
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();// ������ť
			dialog.show();
			break;
		default:
			break;
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 222) {
			String isImport = data.getStringExtra("isImport");
			if ("1".equals(isImport)) {
				refreshShelf();
			}
		}
	}

	// ���¼������
	public void refreshShelf() {
		/************** ��ʼ�����ͼ�� *********************/
		books = db.getAllBookInfo();// ȡ�����е�ͼ��
		bookNumber = books.size();
		int count = books.size();
		int totalRow = count / 3;
		if (count % 3 > 0) {
			totalRow = count / 3 + 1;
		}
		realTotalRow = totalRow;
		if (totalRow < 4) {
			totalRow = 4;
		}
		size = new int[totalRow];
		/***********************************/
		mAdapter = new ShelfAdapter();// new adapter���������
		shelf_list.setAdapter(mAdapter);
	}

	public class ButtonOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// switch ( v.getId () ) {
			// case 1 :
			Intent intent = new Intent();
			intent.setClass(LoveReaderActivity.this, BookActivity.class);
			intent.putExtra("bookid", String.valueOf(v.getId()));
			startActivity(intent);
		}
	}

	public class ButtonOnLongClick implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			// Toast.makeText(myContext, "�ٰ�һ�κ��˼��˳�Ӧ�ó���",
			// Toast.LENGTH_SHORT).show();

			return true;
		}
	}

	protected boolean copyFile() {
		try {
			String dst = txtPath;
			File outFile = new File(dst);
			if (!outFile.exists()) {
				File destDir = new File("/sdcard/lovereader");
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				InputStream inStream = getResources().openRawResource(
						R.raw.text);
				outFile.createNewFile();
				FileOutputStream fs = new FileOutputStream(outFile);
				byte[] buffer = new byte[1024 * 1024];// 1MB
				int byteread = 0;
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
				// db.insert("test.txt", "0","40");
				// db.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// ������׹��
	private void addYoumi() {
		/*
		 * //��ʼ�������ͼ AdView adView = new AdView(this, Color.GRAY,
		 * Color.WHITE,200); FrameLayout.LayoutParams params = new
		 * FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
		 * FrameLayout.LayoutParams.WRAP_CONTENT); //���ù����ֵ�λ��(��������Ļ���½�)
		 * params.gravity=Gravity.BOTTOM|Gravity.RIGHT; //�������ͼ����Activity��
		 * addContentView(adView, params);
		 */
	}

	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// pagefactory.createLog();
		// System.out.println("TabHost_Index.java onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "�ٰ�һ�κ��˼��˳�Ӧ�ó���", Toast.LENGTH_SHORT)
						.show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {// �����˵�
		super.onCreateOptionsMenu(menu);
		// ͨ��MenuInflater��XML ʵ����Ϊ Menu Object
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public boolean onOptionsItemSelected(MenuItem item) {// �����˵�
		int ID = item.getItemId();
		switch (ID) {
		case R.id.mainexit:
			creatIsExit();
			break;
		case R.id.addbook:
			Intent i = new Intent();
			i.setClass(LoveReaderActivity.this, Main.class);
			startActivityForResult(i, 222);
			// startActivity(new Intent(LoveReaderActivity.this, Main.class));
			// finish();
			break;
		default:
			break;

		}
		return true;
	}

	private void creatIsExit() {
		Dialog dialog = new AlertDialog.Builder(LoveReaderActivity.this)
				.setTitle("��ʾ")
				.setMessage("�Ƿ�Ҫȷ��LoverReader��")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// dialog.cancel();
						// finish();
						LoveReaderActivity.this.finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
						System.exit(0);
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();// ������ť
		dialog.show();
	}
}