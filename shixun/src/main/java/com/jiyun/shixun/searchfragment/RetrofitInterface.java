package com.jiyun.shixun.searchfragment;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by 张萌 on 2017/4/7.
 */
public interface RetrofitInterface {

    @GET("action/api/search_list")
    Call<ResponseBody>  getMapdata(@QueryMap Map<String,String>  map);
}
