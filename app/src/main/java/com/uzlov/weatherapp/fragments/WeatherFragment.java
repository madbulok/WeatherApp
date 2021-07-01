package com.uzlov.weatherapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationSettingsRequest;
import com.uzlov.weatherapp.BuildConfig;
import com.uzlov.weatherapp.R;
import com.uzlov.weatherapp.RemoteRepositoryImpl;
import com.uzlov.weatherapp.model.ResponseWeather;
import com.uzlov.weatherapp.services.LocationService;
import com.uzlov.weatherapp.viewmodel.WeatherViewModel;

import org.jetbrains.annotations.NotNull;

public class WeatherFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 100;
    private LocationService locationService;
    private WeatherViewModel weatherViewModel;

    public WeatherFragment() {}

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherViewModel =  new ViewModelProvider(this).get(WeatherViewModel.class);

        // запрашиваем разрешение на ГЕО если необходимо
        if (!checkPermissions()) requestLocationPermission();

        // создание сервиса для получения координат, TODO() применить koin в будущем
        locationService = new LocationService(requireContext(), location -> {
            // callback  получения погоды
            weatherViewModel.getWeatherByLatLng(location.getLatitude(), location.getLongitude()).observe(getViewLifecycleOwner(), this::updateUI);

        });

        locationService.startUpdatesButtonHandler();
        LocationSettingsRequest settingRequest = locationService.buildLocationSettingsRequest();
        locationService.getMSettings().checkLocationSettings(settingRequest).addOnSuccessListener(locationSettingsResponse -> {
            locationService.requestLocationUpdates();
        })
                .addOnFailureListener(e -> requestLocationPermission());

    }

    private void updateUI(ResponseWeather responseWeather) {

    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    private void requestLocationPermission() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
        );

        if (!shouldProvideRationale) {
            // запрашиваем разрешение
            String[] arr = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(
                    requireActivity(), arr,
                    REQUEST_PERMISSIONS_REQUEST_CODE
            );
        } else {
            locationService.startUpdatesButtonHandler();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationService.requestLocationUpdates();
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
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
        RemoteRepositoryImpl.INSTANCE.loadWeatherByLatLng(55.75697, 37.61502).observe(getViewLifecycleOwner(), (responseWeather) -> {
            Log.e("TAG", responseWeather.getName());
            TextView tv = view.findViewById(R.id.tvTest);
            tv.setText(responseWeather.getName());
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationService != null) locationService.stopLocationUpdates();
    }
}