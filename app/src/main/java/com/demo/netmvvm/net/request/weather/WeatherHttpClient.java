package com.demo.netmvvm.net.request.weather;

import com.demo.netmvvm.net.Config;
import com.demo.netmvvm.net.base.BaseHttpClient;

import okhttp3.Interceptor;

/**
 * @author : anzh
 * @date : 2021/3/2
 * @description :
 */
public class WeatherHttpClient extends BaseHttpClient {
    private static WeatherHttpClient sInstance = new WeatherHttpClient();

    private WeatherHttpClient() {

    }

    public static WeatherHttpClient getInstance() {
        return sInstance;
    }

    @Override
    protected Interceptor getInterceptor() {
        return super.getInterceptor();
    }

    @Override
    protected String getBaseUrl() {
        return Config.BASE_URL_WEATHER;
    }
}
