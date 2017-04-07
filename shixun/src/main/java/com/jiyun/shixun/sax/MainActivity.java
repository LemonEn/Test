package com.jiyun.shixun.sax;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jiyun.shixun.R;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * ListView 分页加载
 * 1：首先ListView
 * 2：两种  1：自动加载 2：通过底部按钮加载
 * 3：需要实现OnScrollClickListener重写两个方法
 * 1：onScroll(实现底部按钮的)
 * 2：onScrollStateChanged(自动加载的)需要底部模板
 * 4：三个页面，listView_moban,底部模板，主页面
 *
 * 注意事项：只需要考虑 没有底部bottom ，有数据+1；
 *
 */

public class MainActivity extends AppCompatActivity /*implements AbsListView.OnScrollListener*/{
  private String  str;
    private ListView  mlistview;
    private MyAdapter  myadapter;
    private ArrayList<Person>  mlist=new ArrayList<>();

    //分页加载
//    private final int DATA_SIZE ;
//    private View mBottemView;
//    private int mJiZaiKeJianINdex = 0;   //最后的可视项索引
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
//        initMoreView();

    }

//    private void initMoreView() {
//
//        //这是引用我底部视图
//        mBottemView = getLayoutInflater().inflate(R.layout.load_bottem_view, null);
//    }

    private void initData() {
         mlistview= (ListView) findViewById(R.id.main_listview);
        //如果用自动，OnScroll获取参数即可，然后操作state
//        mlistview.addFooterView(mBottemView);
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://www.oschina.net/").build();
        ReInterface  intf=retrofit.create(ReInterface.class);
        Call<ResponseBody> call=intf.getData(1,0,20);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
//                    try {
//                          str=response.body().string();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i("aaa",str);
                   InputStream  inp=response.body().byteStream();
                    InputSource  ins=new InputSource(inp);
                    SAXParserFactory sax=SAXParserFactory.newInstance();
                    try {
                        SAXParser parse=sax.newSAXParser();
                        XMLReader reader=parse.getXMLReader();
                        MyHandler myHandler=new MyHandler();
                        reader.setContentHandler(myHandler);
                        reader.parse(ins);

                        ArrayList<Person> list=myHandler.getList();
                        for(Person  p:list){
//                            Log.i("aaa",p.toString());
                            Person  pp=new Person();
                            pp.setId(p.getId());
                            pp.setTitle(p.getTitle());
                            pp.setBody(p.getBody());
                            mlist.add(pp);
                        }
                        myadapter=new MyAdapter(mlist,MainActivity.this);
                        myadapter.notifyDataSetChanged();
                        mlistview.setAdapter(myadapter);

                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                  Log.e("bbb","失败");
            }
        });

    }


    /*
    * 分页加载
    * */

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        int lastIndex = myadapter.getCount();
//        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mJiZaiKeJianINdex == lastIndex){
//            // 如果是自动加载,可以在这里放置加载数据的代码
//            LoadMoreData();
//        }
//    }

//    private void LoadMoreData() {
//        int count = myadapter.getCount() - 1;
//        if (count + 10 <= mlist.size()) {
//            for (int i = count + 1; i <= count + 10; i++) {
//                Person  per=mlist.get(10);
//                mlist.add(per);
//            }
//            myadapter.getList(mlist);
//            myadapter.notifyDataSetChanged();
//        } else {
//            for (int i = count + 1; i <= mlist.size(); i++) {
//                Person  per=mlist.get(10);
//                mlist.add(per);
//            }
//            myadapter.getList(mlist);
//            myadapter.notifyDataSetChanged();
//        }

//    }

//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        mJiZaiKeJianINdex = firstVisibleItem + visibleItemCount;
//    }


    class  MyAdapter  extends BaseAdapter{
        private ArrayList<Person>  alist;
        private Context  mContext;


        public void getList(ArrayList<Person> list) {
            this.alist = list;
        }



        public MyAdapter(ArrayList<Person> alist, Context mContext) {
            this.alist = alist;
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            return alist.isEmpty()?0:alist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           Holer  holder=null;
            if(convertView==null){
                holder=new Holer();
                convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_item,null);
                holder.mId= (TextView) convertView.findViewById(R.id.item_id);
                holder.mTitle= (TextView) convertView.findViewById(R.id.item_title);
                holder.mBody= (TextView) convertView.findViewById(R.id.item_body);
                convertView.setTag(holder);
            }else{
                holder= (Holer) convertView.getTag();
            }
            Person  per=alist.get(position);
            holder.mId.setText(per.getId()+"");
            holder.mTitle.setText(per.getTitle()+"");
            holder.mBody.setText(per.getBody()+"");


            return convertView;
        }
        class Holer{
            private TextView  mId,mTitle,mBody;
        }
    }


}