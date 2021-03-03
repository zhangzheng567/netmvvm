package com.demo.netmvvm.weather;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.demo.netmvvm.BR;
import com.demo.netmvvm.R;
import com.demo.netmvvm.databinding.ActivityMainBinding;
import com.demo.netmvvm.weather.viewmodel.QueryWeatherViewModel;

/**
 * @author : anzh
 * @date : 2021/3/2
 * @description :
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private QueryWeatherViewModel mViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mViewModel = new QueryWeatherViewModel();
        mainBinding.setViewModel(mViewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null){
            mViewModel.destroy();
        }
    }
}
