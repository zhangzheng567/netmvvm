package com.demo.netmvvm.net.api;

import com.demo.netmvvm.weather.model.WeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * @author : anzh
 * @date : 2021/3/2
 * @description :
 */
public interface NetApi {
    @Headers("Connection:close")
    @GET("data/cityinfo/101210101.html")
    Observable<WeatherData> getWeatherResponse();
}
