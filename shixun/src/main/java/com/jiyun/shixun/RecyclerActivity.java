package com.jiyun.shixun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;
import com.androidkun.callback.PullToRefreshListener;
import com.jiyun.shixun.bean.MainData;
import com.jiyun.shixun.adapter.MyRecyAdaptera;
import com.thoughtworks.xstream.XStream;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by 张萌 on 2017/4/6.
 */
public class RecyclerActivity  extends Activity {
    //private RecyclerView mRecycler;
    //  private MyAdapter myAdapter;
    private PullToRefreshRecyclerView mRecyclerView;
    private List<MainData.NewsBean> mList = new ArrayList<>();
    private Button  mSearch;
    private MyAdapter mAdapter;
    int pageIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lsy);
//        getRetrofit(1,0,20);
        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.Mode_PullTorefresh);
        mSearch= (Button) findViewById(R.id.main_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(RecyclerActivity.this, SoftwareActvitity.class);
                startActivity(intent);
            }
        });
        initViews();
        mAdapter=new MyAdapter(RecyclerActivity.this,R.layout.recycler_item,mList);

        mRecyclerView.setAdapter(mAdapter);

    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置PullToRefreshRecyclerView的样式
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        //是否开启下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(true);
        //是否开启上拉加载功能
        mRecyclerView.setLoadingMoreEnabled(true);
        //设置是否显示上次刷新的时间
        mRecyclerView.displayLastRefreshTime(true);
        getRetrofit(1,pageIndex,20);
        pageIndex++;

        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setRefreshComplete();
                        mList.clear();
                        Toast.makeText(RecyclerActivity.this, "下拉", Toast.LENGTH_SHORT).show();
                        getRetrofit(1,0,20);
                    }
                },2000);

            }

            @Override
            public void onLoadMore() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setLoadMoreComplete();
                        Toast.makeText(RecyclerActivity.this, "上啦", Toast.LENGTH_SHORT).show();
                        getRetrofit(1,pageIndex,20);
                        pageIndex++;
                    }
                },2000);

            }
        });
    }

    public void getRetrofit(int a,int b,int c) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.oschina.net/")
                .build();
        RetrofitInteface inteface = retrofit.create(RetrofitInteface.class);
        Call<ResponseBody> call = inteface.getLogin(a, b, c);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(RecyclerActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    InputStream  inputStream=response.body().byteStream();
                    XStream  xStream=new XStream();
                    xStream.alias("oschina", MainData.class);
                    xStream.alias("news",MainData.NewsBean.class);
                    MainData md = (MainData) xStream.fromXML(inputStream);
                    mList=md.getNewslist();
                mAdapter=new MyAdapter(RecyclerActivity.this,R.layout.recycler_item,mList);
                mRecyclerView.setAdapter(mAdapter);
                Log.i("年覅师傅你说的呢",mList.toString());
                    mAdapter.notifyDataSetChanged();

                }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RecyclerActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private class MyAdapter extends BaseAdapter<MainData.NewsBean> {

        public MyAdapter(Context context, int layoutId, List<MainData.NewsBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, MainData.NewsBean newsBean) {
            holder.setText(R.id.Recycler_Item_Title,newsBean.getTitle()+"");
            holder.setText(R.id.Recycler_Item_Body,newsBean.getBody()+"");

        }
    }


}
