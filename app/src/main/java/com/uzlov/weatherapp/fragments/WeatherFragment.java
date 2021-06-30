package com.uzlov.weatherapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.uzlov.weatherapp.R;
import com.uzlov.weatherapp.RemoteRepositoryImpl;

import org.jetbrains.annotations.NotNull;

public class WeatherFragment extends Fragment {
    public WeatherFragment() {

    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RemoteRepositoryImpl.INSTANCE.loadWeatherByLatLng(55.75697, 37.61502).observe(getViewLifecycleOwner(), (responseWeather)-> {
            Log.e("TAG", responseWeather.getName());
            TextView tv = view.findViewById(R.id.tvTest);
            tv.setText(responseWeather.getName());
        });

    }
}