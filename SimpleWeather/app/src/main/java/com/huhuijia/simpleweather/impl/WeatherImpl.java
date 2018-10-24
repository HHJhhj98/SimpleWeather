package com.huhuijia.simpleweather.impl;

import com.huhuijia.simpleweather.data.WeatherDataBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//天气接口
public interface WeatherImpl {
    //http://v.juhe.cn/ baseurl
    //weather/index?format=2& get地址
    // cityname="长沙"&key=1a2f905a5b215379c797ab7b703ca249
    @GET("weather/index?format=2&")
    Call<WeatherDataBean>getWeather(@Query("cityname") String cityname,@Query("key") String key);

}
