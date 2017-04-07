package com.jiyun.shixun;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.jiyun.shixun.adapter.MyRecyAdapter;
import com.jiyun.shixun.searchfragment.RetrofitInterface;
import com.jiyun.shixun.bean.SearchResult;
import com.thoughtworks.xstream.XStream;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SoftwareActvitity extends Activity {
   private PullToRefreshRecyclerView  mpullview;
    private List<SearchResult.ResultBean> mlist =new ArrayList<>();
    private EditText  mEdit;
    private Button  mBtn;
    private String  s;
    private MyRecyAdapter myRecyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_actvitity);
        init();
    }

    private void init() {
        mpullview= (PullToRefreshRecyclerView) findViewById(R.id.software_view);
        LinearLayoutManager manger=new LinearLayoutManager(SoftwareActvitity.this);
        manger.setOrientation(LinearLayoutManager.VERTICAL);
        mpullview.setLayoutManager(manger);
        mEdit= (EditText) findViewById(R.id.software_edit);
        mBtn= (Button) findViewById(R.id.software_search);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEdit.getText().toString().isEmpty()){
                    s=mEdit.getText().toString();
                    getdata(s);
                }else {
                    Toast.makeText(SoftwareActvitity.this, "参数不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getdata(String name) {

        Retrofit  retr=new Retrofit.Builder().baseUrl("http://www.oschina.net/").build();
        RetrofitInterface intf=retr.create(RetrofitInterface.class);
        Map<String,String>  map=new HashMap<>();
        map.put("catalog","software");
        map.put("content",name);
        Log.i("___",name);
        map.put("pageIndex","1");
        map.put("pageSize","5");

        intf.getMapdata(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              if(response.isSuccessful()){
                  InputStream in=response.body().byteStream();
                  XStream  xStream=new XStream();
                  xStream.alias("oschina", SearchResult.class);
                  xStream.alias("results",SearchResult.ResultBean.class);
                  xStream.alias("notice",SearchResult.NoticeBean.class);
                  SearchResult sr = (SearchResult) xStream.fromXML(in);

                  mlist.addAll(sr.getResults());
                  myRecyAdapter.notifyDataSetChanged();

                  myRecyAdapter = new MyRecyAdapter(SoftwareActvitity.this,mlist);
                  mpullview.setAdapter(myRecyAdapter);
                  //是否开启下拉刷新功能
                  mpullview.setPullRefreshEnabled(true);
                  mpullview.setRefreshComplete();

                  //是否开启上拉加载功能
                  mpullview.setLoadingMoreEnabled(true);
                  mpullview.setPullToRefreshListener(new PullToRefreshListener() {
                      @Override
                      public void onRefresh() {
                          mpullview.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  mpullview.setRefreshComplete();
                                  myRecyAdapter.notifyDataSetChanged();
                              }
                          }, 2000);
                      }

                      @Override
                      public void onLoadMore() {
                          mpullview.setLoadMoreComplete();
                          myRecyAdapter.notifyDataSetChanged();
                      }
                  });
              }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("失败","--------------------------------------");
            }
        });
    }
}
