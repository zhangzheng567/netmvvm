package com.demo.netmvvm.weather.viewmodel;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.demo.netmvvm.net.request.weather.WeatherNetRequestManager;
import com.demo.netmvvm.weather.model.WeatherData;
import com.demo.netmvvm.weather.model.WeatherInfo;

/**
 * @author : anzh
 * @date : 2021/3/2
 * @description :
 */
public class QueryWeatherViewModel implements WeatherNetRequestManager.WeatherNetListener {
    private final static String TAG = QueryWeatherViewModel.class.getName();
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableBoolean loadingSuccess = new ObservableBoolean(false);
    public final ObservableBoolean loadingFailed = new ObservableBoolean(false);
    public final ObservableField<String> city = new ObservableField<>();
    public final ObservableField<String> cityId = new ObservableField<>();
    public final ObservableField<String> temp1 = new ObservableField<>();
    public final ObservableField<String> temp2 = new ObservableField<>();
    public final ObservableField<String> weather = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();

    public QueryWeatherViewModel() {

    }

    public void queryWeather() {
        loading.set(true);
        loadingSuccess.set(false);
        loadingFailed.set(false);
        WeatherNetRequestManager.getInstance().setWeatherListener(this);
        WeatherNetRequestManager.getInstance().getWeatherInfo();
    }


    @Override
    public void onSuccess(WeatherData weatherData) {
        WeatherInfo weatherInfo = weatherData.getWeatherinfo();
        city.set(weatherInfo.getCity());
        cityId.set(weatherInfo.getCityid());
        temp1.set(weatherInfo.getTemp1());
        temp2.set(weatherInfo.getTemp2());
        weather.set(weatherInfo.getWeather());
        time.set(weatherInfo.getPtime());
        loading.set(false);
        loadingSuccess.set(true);
    }

    @Override
    public void onFailed(String error) {
        loading.set(false);
        loadingFailed.set(true);
        Log.d(TAG, "onFailed: " + error);
    }

    public void destroy() {
        WeatherNetRequestManager.getInstance().destroy();
    }
}
