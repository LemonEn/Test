/*
package com.jiyun.test;

*/
/**
 * Created by 张萌 on 2017/4/6.
 *//*

import java.net.HttpURLConnection;

import java.util.ArrayList;

 import java.util.HashMap;



       import org.json.JSONObject;



 import android.app.Activity;

 import android.app.ProgressDialog;

 import android.graphics.drawable.Drawable;

 import android.os.Bundle;

 import android.os.Handler;

 import android.os.HandlerThread;

import android.os.Looper;

 import android.os.Message;

 import android.view.LayoutInflater;

 import android.view.View;

 import android.widget.AbsListView;

 import android.widget.AbsListView.OnScrollListener;

 import android.widget.Button;

 import android.widget.LinearLayout;

 import android.widget.ListView;

 import android.widget.RelativeLayout;

 import android.widget.TextView;







         */
/**

  * @author Stay

  * 动态加载listview数据，上拉 刷新，下拉 更多

  *//*


         public class ListViewActivity extends Activity implements OnScrollListener {

     private static final int LOAD = 0;

     private static final int ERROR = 0;

     private static final int MEMBER = 1;

     private static final int LOADED = 2;

     private static final int DIALOG = 3;

     private static final int FULL = 4;

     private NearbyAdapter adapter;

     private ListView nearby_lv;

     private RelativeLayout nearby_lv_header;

     private Button list_bottom_btn;

     private LinearLayout list_bottom_linear;

     private TextView bottom_progress_text;

     private RelativeLayout nearby_lv_footer;

     private Button list_header_btn;

     private LinearLayout list_header_linear;

     private TextView heard_progress_text;

     private ArrayList<JSONObject> nearby_data = new ArrayList<JSONObject>();

     private int lastItem;

     private HashMap<String, Drawable> imageCache;

     private MyHandler myHandler;

     private ProgressDialog dialog;

     private int curPage = 1;

     private boolean isMember = false;

     private int firstItem;

     public int count;



             */
/** Called when the activity is first created. *//*


             @Override

     public void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);

         setContentView(R.layout.main);

         initView();

                 myHandler.sendEmptyMessage(LOAD);

         }



             @Override

     public void onScrollStateChanged(AbsListView view, int scrollState) {

         DebugUtil.debug("onScrollStateChanged");

         //当滚动停止且滚动的总数等于数据的总数，去加载

         if (lastItem == count && scrollState == SCROLL_STATE_IDLE) {

             DebugUtil.debug("onScrollStateChanged--------next");

             if (curPage == 4 && !isMember) {

                 DebugUtil.show(this, "您不是正式会员，请申请正式会员，");

                 list_bottom_linear.setVisibility(View.GONE);

                 } else {

                 //加载数据

                 myHandler.sendEmptyMessage(LOAD);

                 }

             return;

             }

         //当往上拉时更新数据，将data清空然后去重新加载

         if (firstItem == 0 && scrollState == SCROLL_STATE_IDLE) {

             DebugUtil.debug("onScrollStateChanged--------refresh");

             curPage = 0;

             myHandler.sendEmptyMessage(LOAD);

             }

         }



             @Override

     public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

         DebugUtil.debug("firstVisibleItem=" + firstVisibleItem);

         DebugUtil.debug("visibleItemCount=" + visibleItemCount);

         DebugUtil.debug("totalItemCount=" + totalItemCount);

         //这里要减二，因为我加了header footer

         lastItem = firstVisibleItem + visibleItemCount - 2;

         firstItem = firstVisibleItem;

         }



             public int getData() {

         try {

             HttpURLConnection conn = DownloadUtil.download("http://zdevl.mapi.jjdd.com/nearby/lbs?page=" + curPage);

             ArrayList<JSONObject> temp = JSONUtil.streamToJsonList(conn.getInputStream());

             if (curPage == 0 && nearby_data.size() > 0) {

                 nearby_data.clear();

                 count = 0;

                 }

             if (temp != null && temp.size() > 0) {

                 count += temp.size();

                 nearby_data.addAll(temp);

                 DebugUtil.debug("nearby_data.size()="+nearby_data.size());

                 } else {

                 return FULL;

                 }

             return LOADED;

             } catch (Exception e) {

             return ERROR;

             }

         }



             private Handler handler = new Handler() {

         @Override

         public void handleMessage(Message msg) {

             super.handleMessage(msg);

             switch (msg.what) {

                 case DIALOG:

                     list_bottom_linear.setVisibility(View.VISIBLE);

                     list_header_linear.setVisibility(View.VISIBLE);

                     break;

                 case LOADED:

                     list_bottom_linear.setVisibility(View.GONE);

                     list_header_linear.setVisibility(View.GONE);

                     curPage++;

                     adapter.notifyDataSetChanged();

                     break;

                 case ERROR:

                     DebugUtil.debug("error,missing data");

                     break;

                 case MEMBER:

                     DebugUtil.debug("you must regist formal member");

                     break;

                 default:

                     break;

                 }

             }

         };

     //创建子线程加载数据，然后更新

             private class MyHandler extends Handler {

         private int status;



                 public MyHandler(Looper looper) {

             super(looper);

             }



                 @Override

         public void handleMessage(Message msg) {

             synchronized (this) {

                 switch (msg.what) {

                     case LOAD:// get data from server

                         handler.sendEmptyMessage(DIALOG);//显示等待框

                         status = getData();

                         handler.sendEmptyMessageDelayed(status, 1000);

                         break;

                     default:

                         break;

                     }

                 }

             }

         }



             public void initView() {

         imageCache = new HashMap<String, Drawable>();

         HandlerThread handlerThread = new HandlerThread("nearby");

         // 在使用HandlerThread的getLooper()方法之前，必须先调用该类的start();

         handlerThread.start();

         myHandler = new MyHandler(handlerThread.getLooper());

         nearby_lv = (ListView) findViewById(R.id.main_listview);

         nearby_lv_footer = (RelativeLayout) LayoutInflater.from(ListViewActivity.this).inflate(R.layout.nearby_lv_header, null);

         list_bottom_btn = (Button) nearby_lv_footer.findViewById(R.id.list_bottom_btn);

         list_bottom_linear = (LinearLayout) nearby_lv_footer.findViewById(R.id.list_bottom_linear);

         bottom_progress_text = (TextView) nearby_lv_footer.findViewById(R.id.progress_text);

         nearby_lv_header = (RelativeLayout) LayoutInflater.from(ListViewActivity.this).inflate(R.layout.nearby_lv_header, null);

         list_header_btn = (Button) nearby_lv_header.findViewById(R.id.list_bottom_btn);

         list_header_linear = (LinearLayout) nearby_lv_header.findViewById(R.id.list_bottom_linear);

         heard_progress_text = (TextView) nearby_lv_header.findViewById(R.id.progress_text);

        list_header_btn.setText("刷新");

       list_bottom_btn.setText("更多");

         list_header_linear.setVisibility(View.GONE);

         nearby_lv.addHeaderView(nearby_lv_header);

         nearby_lv.addFooterView(nearby_lv_footer);

         // list_header_btn.setOnClickListener(header_click);

         adapter = new NearbyAdapter(ListViewActivity.this, nearby_data);

         nearby_lv.setAdapter(adapter);

         nearby_lv.setOnScrollListener(ListViewActivity.this);

         }

     }
*/
