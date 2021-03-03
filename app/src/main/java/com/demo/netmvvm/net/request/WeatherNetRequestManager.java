package com.demo.netmvvm.net.request;

import com.demo.netmvvm.net.api.NetApi;
import com.demo.netmvvm.weather.model.WeatherData;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : anzh
 * @date : 2021/3/2
 * @description :
 */
public class WeatherNetRequestManager {
    private static WeatherNetRequestManager sInstance = new WeatherNetRequestManager();
    private NetApi mNetService;
    private CompositeDisposable mDisposables;
    private WeatherNetListener mListener;

    private WeatherNetRequestManager() {
        mDisposables = new CompositeDisposable();
    }

    public static WeatherNetRequestManager getInstance() {
        if (sInstance == null) {
            throw new RuntimeException("WeatherNetRequestManager sInstance is null");
        }
        return sInstance;
    }

    private synchronized void initNetService() {
        if (mNetService != null) {
            return;
        }
        mNetService = (NetApi) WeatherHttpClient.getInstance().getService(NetApi.class);
    }

    /**
     * 天气接口
     */
    public void getWeatherInfo() {
        initNetService();
        Disposable d = mNetService.getWeatherResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherData>() {
                    @Override
                    public void accept(WeatherData weatherData) throws Exception {
                        if (mListener != null) {
                            mListener.onSuccess(weatherData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mListener != null) {
                            mListener.onFailed(throwable.getMessage());
                        }
                    }
                });
        mDisposables.add(d);

    }

    public void setWeatherListener(WeatherNetListener listener) {
        mListener = listener;
    }

    public void destroy() {
        mListener = null;
        mDisposables.dispose();
    }

    public interface WeatherNetListener {
        void onSuccess(WeatherData weatherData);

        void onFailed(String error);
    }

}
