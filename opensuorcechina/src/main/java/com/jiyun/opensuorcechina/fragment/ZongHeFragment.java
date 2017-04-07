package com.jiyun.opensuorcechina.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.jiyun.opensuorcechina.R;
import com.jiyun.opensuorcechina.adapter.ZongHeNewsAdapter;
import com.jiyun.opensuorcechina.bean.News;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ZongHeFragment extends Fragment {

    private PullToRefreshRecyclerView news_recyclerView;
    private ZongHeNewsAdapter news_adapter;
    private List<News> newlist;
   // private ListView mlistView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.from(getContext()).inflate(R.layout.zonghe_view, null);

        news_recyclerView= (PullToRefreshRecyclerView) inflate.findViewById(R.id.myRecyclerView_news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_recyclerView.setLayoutManager(layoutManager);

        news_recyclerView.setLoadingMoreEnabled(true);
        news_recyclerView.setPullRefreshEnabled(true);


        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("http://www.oschina.net/action/api/news_list/?pageSize=7");

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        news_recyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                news_recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        news_recyclerView.setRefreshComplete();
                        //模拟没有数据的情况
                        //newlist.clear();
                        //news_adapter.notifyDataSetChanged();
                    }
                }, 3000);

            }

            @Override
            public void onLoadMore() {
                news_recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        news_recyclerView.setLoadMoreComplete();
                        //模拟加载数据的情况
                        int size = newlist.size()+4;
                        MyAsyncTask myAsyncTask = new MyAsyncTask();
                        String loadingNum="pageSize="+size;
                        myAsyncTask.execute("http://www.oschina.net/action/api/news_list/?"+loadingNum);
                        //news_adapter.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    public String getString(String string) {

        try {

            /*
            * httpUrlConnection请求网络
            *
            * */
            URL url=new URL(string);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            if (responseCode==200){

                //得到流
                InputStream inputStream = connection.getInputStream();

                int len;
                byte[] b = new byte[1024];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


                while ((len = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }

                Log.i("abc-------------",outputStream.toString());
                return outputStream.toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    * 异步
    *
    * */
    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        private News news;
        private XmlPullParser pullParser;

        @Override
        protected String doInBackground(String... params) {
            //得到数据
            Log.i("abc----",params[0]);
            String info = getString(params[0]);
            return info;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            pullParser = Xml.newPullParser();

            try {
                //将string转换成字节流
                pullParser.setInput(
                        new ByteArrayInputStream(result.getBytes("UTF-8")),
                        "utf-8");

                //得到事件类型
                int eventType = pullParser.getEventType();

                while (eventType != pullParser.END_DOCUMENT) {

                    String name = pullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("newslist".equals(name)) {
                                // 创建集合
                                newlist = new ArrayList<>();
                            } else if ("news".equals(name)) {
                                // 创建对象
                                news = new News();
                            } else if ("id".equals(name)) {
                                // 属性
                                //news.id = pullParser.nextText();
                                int i = Integer.parseInt(pullParser.nextText());
                                news.setId(i);
                            } else if ("title".equals(name)) {
                                // 属性
                                //news.title = pullParser.nextText();
                                news.setTitle(pullParser.nextText());
                            } else if ("body".equals(name)) {
                                // 属性
                                //news.body = pullParser.nextText();
                                news.setBody(pullParser.nextText());
                            }else if("author".equals(name)){
                                news.setAuthor(pullParser.nextText());
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if ("news".equals(name)) {
                                newlist.add(news);
                            }
                            break;

                        default:
                            break;
                    }
                    eventType = pullParser.next();
                    ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            news_adapter=new ZongHeNewsAdapter(getContext(),R.layout.newsbody_adapter_view,newlist);
            news_recyclerView.setAdapter(news_adapter);

            super.onPostExecute(result);
        }
    }
}
