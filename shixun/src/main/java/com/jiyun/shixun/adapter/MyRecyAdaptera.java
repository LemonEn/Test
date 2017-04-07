package com.jiyun.shixun.adapter;

import android.content.Context;
import android.view.View;

import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;
import com.jiyun.shixun.R;
import com.jiyun.shixun.bean.MainData;
import com.jiyun.shixun.bean.NewsList;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/5.
 */

public class MyRecyAdaptera extends BaseAdapter<MainData.NewsBean> {
    private Context context;
    public MyRecyAdaptera(Context context, ArrayList<MainData.NewsBean> datas) {
        super(context, R.layout.recycler_item, datas);
        this.context = context;
    }

//    @Override
//    public void convert(ViewHolder holder, final NewsList news) {
//        holder.setText(R.id.Recycler_Item_Title,news.getTitle());
//        holder.setOnclickListener(R.id.layout, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, Main2Activity.class);
////                intent.putExtra("id",news.getId());
////                context.startActivity(intent);
//            }
//        });
//    }

    @Override
    public void convert(ViewHolder holder, MainData.NewsBean newsBean) {
        holder.setText(R.id.Recycler_Item_Title,newsBean.getTitle());
    }
}
