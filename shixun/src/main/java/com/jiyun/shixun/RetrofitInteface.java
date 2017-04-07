package com.jiyun.shixun;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Administrator on 2017/4/5.
 */

public interface RetrofitInteface {
    @GET("action/api/news_list")
    Call<ResponseBody> getLogin(@Query("catalog") int a,@Query("pageIndex") int b,@Query("pageSize")  int c);

    @GET("action/api/news_detail")
    Call<ResponseBody> getData(@Query("id") int id);

}
