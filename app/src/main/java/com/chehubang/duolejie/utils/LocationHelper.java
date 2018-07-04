package com.chehubang.duolejie.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Beidouht on 2017/10/20.
 */

public class LocationHelper {

    private static LocationManager locationManager;
    private static Activity activity;
    private static String locationProvider;
    private static double latitude;//经度
    private static double longitude;//纬度

    private static final LocationListener locationListener=new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();     //经度
            longitude = location.getLongitude(); //纬度
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public LocationHelper(Activity activity) {
        this.activity = activity;
    }


    @SuppressLint("MissingPermission")
    public static String getLocation() {

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(activity, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return null;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(activity, "请开启定位权限", Toast.LENGTH_SHORT).show();
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            latitude = location.getLatitude();
            latitude = location.getLongitude();
            return latitude+","+latitude;
        }
        return latitude+","+latitude;
    }



}
