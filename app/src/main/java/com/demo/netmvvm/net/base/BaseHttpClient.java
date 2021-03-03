package com.demo.netmvvm.net.base;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author : anzh
 * @date : 2021/3/2
 * @description :
 */
public abstract class BaseHttpClient {
    private final static String TAG = BaseHttpClient.class.getName();
    private final int TIME_OUT_SECONDS = 10;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    protected HashMap<Class, Object> serviceCache;

    protected BaseHttpClient() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
            Interceptor interceptor = getInterceptor();
            if (interceptor != null) {
                builder.addInterceptor(interceptor)
                        .addNetworkInterceptor(new StethoInterceptor());
            }
            okHttpClient = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        addFactory(getConvertFactory(), builder);
        retrofit = builder.build();
        serviceCache = new HashMap<>(2);

    }

    protected Interceptor getInterceptor() {
        return null;
    }

    protected abstract String getBaseUrl();

    private void addFactory(List<Converter.Factory> factories, Retrofit.Builder builder) {
        for (Converter.Factory factory : factories) {
            builder.addConverterFactory(factory);
        }
    }

    protected List<Converter.Factory> getConvertFactory() {
        List<Converter.Factory> factories = new ArrayList<>(2);
        Gson gson = new GsonBuilder().setLenient().create();
        factories.add(ScalarsConverterFactory.create());
        factories.add(GsonConverterFactory.create(gson));
        return factories;
    }

    /**
     * 获得网络通讯接口
     *
     * @param service 网络通讯接口
     */
    public synchronized Object getService(Class service) {
        if (serviceCache.containsKey(service)) {
            return serviceCache.get(service);
        }
        Object api = retrofit.create(service);
        serviceCache.put(service, api);
        return api;
    }
}
