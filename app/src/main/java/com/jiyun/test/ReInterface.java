package com.jiyun.test;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 张萌 on 2017/4/6.
 */
public interface ReInterface {

    @GET("action/api/news_list/")
    Call<ResponseBody>  getData(@Query("catalog") int a,@Query("pageIndex") int b,@Query("pageSize") int c);
}
