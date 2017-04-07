package com.jiyun.opensuorcechina.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;
import com.jiyun.opensuorcechina.R;
import com.jiyun.opensuorcechina.bean.News;

import java.util.List;

public class ZongHeNewsAdapter extends BaseAdapter<News> {

    public ZongHeNewsAdapter(Context context, int layoutId, List<News> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder,News news) {
        holder.setText(R.id.news_title,news.getTitle());
        holder.setText(R.id.news_body,news.getBody());
        holder.setText(R.id.news_author,"@"+news.getAuthor());
        holder.setText(R.id.news_commentCount,"赞 "+news.getCommentCount()+"");

    }


    }
