package com.jiyun.shixun.adapter;

import android.content.Context;

import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;
import com.jiyun.shixun.R;
import com.jiyun.shixun.bean.NewsList;
import com.jiyun.shixun.bean.SearchResult;

import java.util.List;


/**
 * Created by Administrator on 2017/4/5.
 */

public class MyRecyAdapter extends BaseAdapter<SearchResult.ResultBean> {
//    private Context context;
    public MyRecyAdapter(Context context, List<SearchResult.ResultBean> datas) {
        super(context, R.layout.recycler_item, datas);

    }

    public void convert(ViewHolder holder, final NewsList news) {
        holder.setText(R.id.Recycler_Item_Body,news.getId());
        holder.setText(R.id.Recycler_Item_Title,news.getTitle());
//        holder.setOnclickListener(R.id.layout, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, Main2Activity.class);
////                intent.putExtra("id",news.getId());
////                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public void convert(ViewHolder holder, SearchResult.ResultBean resultBean) {

    }
}
