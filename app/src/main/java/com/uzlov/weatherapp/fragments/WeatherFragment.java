package com.uzlov.weatherapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationSettingsRequest;
import com.uzlov.weatherapp.BuildConfig;
import com.uzlov.weatherapp.databinding.FragmentWeatherBinding;
import com.uzlov.weatherapp.model.ResponseWeather;
import com.uzlov.weatherapp.repository.LocalRepositoryImpl;
import com.uzlov.weatherapp.server.Constants;
import com.uzlov.weatherapp.services.LocationService;
import com.uzlov.weatherapp.viewmodel.WeatherViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.Lazy;
import static org.koin.java.KoinJavaComponent.inject;


public class WeatherFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 100;
    private LocationService locationService;
    private final Lazy<WeatherViewModel> weatherViewModel = inject(WeatherViewModel.class);


    private FragmentWeatherBinding viewBinding;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // запрашиваем разрешение на ГЕО если необходимо
        if (!checkPermissions()) requestLocationPermission();
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
                locationService.startUpdatesButtonHandler();
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
        viewBinding = FragmentWeatherBinding.inflate(getLayoutInflater(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // создание сервиса для получения координат, TODO() применить koin в будущем
        locationService = new LocationService(requireContext(), location -> {
            // callback  получения погоды
            weatherViewModel.getValue().getWeatherByLatLng(location.getLatitude(), location.getLongitude()).observe(getViewLifecycleOwner(), response -> {
                LocalRepositoryImpl.INSTANCE.insertWeather(response);
                updateUI(response);
            });
        });

        locationService.startUpdatesButtonHandler();
        LocationSettingsRequest settingRequest = locationService.buildLocationSettingsRequest();
        locationService.getMSettings().checkLocationSettings(settingRequest).addOnSuccessListener(locationSettingsResponse -> {
            locationService.requestLocationUpdates();
        })
                .addOnFailureListener(e -> requestLocationPermission());
    }

    private void updateUI(ResponseWeather responseWeather) {
        if (responseWeather.getWeather().isEmpty()) return;
        viewBinding.tvStateWeather.setText(responseWeather.getWeather().get(0).getDescription());
        viewBinding.tvTemperature.setText(responseWeather.getMain().getTemp() + " \u2103");
        viewBinding.tvCityName.setText(responseWeather.getName() + ", " + responseWeather.getSys().getCountry());
        viewBinding.tvTempMinLike.setText(responseWeather.getMain().getTemp_min() + " \u2103");
        viewBinding.tvTempMaxLike.setText(responseWeather.getMain().getTemp_max() + " \u2103");
        viewBinding.tvFeelLike.setText(responseWeather.getMain().getFeels_like() + " \u2103");
        viewBinding.tvWindSpeed.setText(responseWeather.getWind().getSpeed() + " m/s");
        viewBinding.tvCloud.setText(responseWeather.getClouds().getAll() + "%");

        Date dateSunrise = new Date(responseWeather.getSys().getSunrise());
        SimpleDateFormat formmater = new SimpleDateFormat("hh:mm", Locale.getDefault());
        viewBinding.tvSunrise.setText(formmater.format(dateSunrise));

        Date dateSunset = new Date(responseWeather.getSys().getSunset());
        viewBinding.tvSunset.setText(formmater.format(dateSunset));
//        viewBinding.tvWindTemp.setText(responseWeather.getWind().getDeg() + " \u2103");

        Glide.with(requireContext())
                .load(Constants.INSTANCE.getBASE_URL_IMAGE() + responseWeather.getWeather().get(0).getIcon() + "@4x.png")
                .into(viewBinding.iconWeather);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationService != null) locationService.stopLocationUpdates();
    }
}