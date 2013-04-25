package com.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProgressDia extends Activity implements OnClickListener
{
    
    Button b1;
    
    ProgressDialog1 mProgressDialog;
    
    boolean flag = false;
    
    int index = 0;
    
    int max = 100;
    
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    if (index > 100)
                    {
                        mProgressDialog.dismiss();
                        flag = false;
                        index = 0;
                        mProgressDialog = null;
                        break;
                    }
                    index++;
                    if (null != mProgressDialog)
                    {
                    	mProgressDialog.setMessage("已经下载");
                        if ((index >= 30)&&(index <= 69))
                        {
                        	
                            mProgressDialog.setTitle("已过三分之一");
                            mProgressDialog.setDynamicStyle(ProgressDialog1.STYLE_SPINNER,"正在上传1。。。");
                        }
                        if (index == 70)
                        {
                        	
                            mProgressDialog.setTitle("还差三分之一");
                            mProgressDialog.setDynamicStyle(ProgressDialog1.STYLE_HORIZONTAL,"正在上传2。。。");
                        }
                        
                        mProgressDialog.setProgress(index);
                    }
                    
                    break;
                
                default:
                    break;
            }
        };
        
    };
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b1 = (Button)findViewById(R.id.Button01);
        b1.setOnClickListener(ProgressDia.this);
    }
    
    public void onClick(View v)
    {
        if (v.equals(b1))
        {
            flag = true;
            mProgressDialog = new ProgressDialog1(ProgressDia.this);
            
            mProgressDialog.setTitle("开始");
            mProgressDialog.setProgressStyle(ProgressDialog1.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setButton2("取消", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    flag = false;
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    index = 0;
                }
            });
            mProgressDialog.show();
            new Thread()
            {
                public void run()
                {
                    while (flag)
                    {
                        try
                        {
                            Thread.sleep(50);
                            handler.obtainMessage(0).sendToTarget();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
            }.start();
        }
    }
}
/*

http://www.eoeandroid.com/thread-70136-1-1.html

1.简介系统ProgressDialog的主要特征
1.在ProgressDialog的源码里可以明显的看到，在STYLE_HORIZONTAL和STYLE_SPINNER分别显示的是不同的XML，这就意味着你的进度条要么是转圈，要么是条形的。
2.不管是上述的任何情况下，系统对各部分文字显示都已经完全格式化。

2.实际情况
但是实际的应用中，我们或者需要改变文字的位置，或者需要转圈和条形共存，甚至是做出完全颠覆系统进度条的个性进度条，这个时候我们必须去重新设计属于你自己的进度条。（个人一直认为应用中的组件尽量不用系统的，而是重写系统的，这样做出来的应用才是百家争鸣）。
下面就实现我自己的进度条中碰到的几个可能需要注意的地方给大家交待下：
1.在系统ProgressDialog的构造函数

public ProgressDialog(Context 
context) 
<P p 
{

this(context, 
com.android.internal.R.style.Theme_Dialog_Alert);

}
复制代码
中涉及了一个theme:com.android.internal.R.style.Theme_Dialog_Alert,这是我当时遇到的第一个问题，开始的时候翻遍源码，终于在data/res/values/themes.xml里找到，


<style name="Theme.Dialog.Alert"><item name="windowBackground">@android:color/transparent</item> 

<item name="windowTitleStyle">@android:style/DialogWindowTitle</item> 

<item name="windowIsFloating">true</item> 

<item name="windowContentOverlay">@null</item> 

</style>
复制代码
但是发现他还关联其他style，继续找下去，结果写到自己的XML里还是错误一大堆，最后仔细看了下，发现不就是个theme吗，这就简单了，有2种方向：1.自己写theme.2.使用系统的theme。我写的时候是
public ProgressDialog1(Context context) {
       this(context, android.R.style.Theme_Panel);
        mContext = context;
        }
复制代码
调用系统的android.R.style.Theme_Panel.
注意：找个地方就是你个性释放的开始。
2.我要实现的是转圈和条形并存。那么肯定得在布局文件上下手了。
找个地方分2块说.第1，布局是XML文件；2，布局是代码生成。
您可能会问，这有区别吗？事实上，区别还是蛮大的，不知道你注意到没有如下属性
style="?android:attr/progressBarStyleHorizontal"
试问，如何代码实现？
先说第1种，XML的话比较简单，因为只需要写2个ProgressBar,然后再在代码里控制visible属性就ok，在此不赘述。
第2种，style的实现，这是我碰到的第2个难点
最后我在网上找到1篇文章，关于获取父类私有属性的文章，利用反射机制实现了style的设置。
以下工具类是转载网上那位朋友的工具类，大家可以借鉴下！

public class BeanUtils {
private BeanUtils() {
}
public static void setFieldValue(final Object object,
   final String fieldName, final Object value) {
  Field field = getDeclaredField(object, fieldName);
  if (field == null)
   throw new IllegalArgumentException("Could not find field ["
     + fieldName + "] on target [" + object + "]");
  makeAccessible(field);
  try {
   field.set(object, value);
  } catch (IllegalAccessException e) {
  }
}
protected static Field getDeclaredField(final Object object,
   final String fieldName) {
  return getDeclaredField(object.getClass(), fieldName);
}
protected static Field getDeclaredField(final Class clazz,
   final String fieldName) {
  for (Class superClass = clazz; superClass != Object.class; superClass = superClass
    .getSuperclass()) {
   try {
    return superClass.getDeclaredField(fieldName);
   } catch (NoSuchFieldException e) {
   }
  }
  return null;
}
protected static void makeAccessible(Field field) {
  if (!Modifier.isPublic(field.getModifiers())
    || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
   field.setAccessible(true);
  }
}
}
复制代码


有了上面的工具类，就可以简单的设置那些私有属性
比如：        
BeanUtils.setFieldValue(progress_h, "mOnlyIndeterminate", new Boolean(false));
        BeanUtils.setFieldValue(progress_h, "mMinHeight", new Integer(15));


以上就是我重写进度条的全部心得，希望能对阅读完得朋友有些许帮助!

最后附上我的demo，里面我的调用的布局是代码实现的，当然也有XML的。

demo说明：功能是前30条形，30-70转圈，70-100条形  文字跟着变
*/