package com.example.x6.androidmachinetest.Activity.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;

import java.util.List;


public class GPS extends Common {
    private Button notSupport;
    private TextView gpsLocationShow;
    private Debug debug;
    private Handler handler;

    private String provider;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private long startTime;

    public GPS(final Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("GPS测试");
        this.handler = handler;
        notSupport = new Button(context);
        gpsLocationShow = new TextView(context);
        notSupport.setText("不支持");
        gpsLocationShow.setText("正在定位(请耐心等待)...");

        LinearLayout.LayoutParams notSupportLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notSupport.setLayoutParams(notSupportLayout);
        gpsLocationShow.setLayoutParams(notSupportLayout);
        getLineout().addView(notSupport);
        getLineout().addView(gpsLocationShow);


        debug = new Debug("Headhhone");


        getMessage().setText("当前测试GPS，请确保GPS天线接入，等待GPS定位数据。如果设备不支持GPS，请点击不支持GPS，并且进入下一项测试。");
        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                notSupport.setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                notSupport.setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler, getPosition(), true);
            }
        });
        getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                notSupport.setEnabled(false);
                getYes().setTextColor(Color.WHITE);
                notSupport.setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler, getPosition(), false);
            }
        });
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                notSupport.setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                notSupport.setTextColor(Color.BLACK);
                handler.sendEmptyMessage(1);
                finish();
                startTest(context,handler);
            }
        });
        notSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                notSupport.setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                getYes().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler, getPosition(), true);
            }
        });


    }


    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);

        /*开始GPS定位*/
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null)//判断GPS是否返回非空
            return;
        List<String> list = locationManager.getProviders(true);
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            debug.loge("请检查网络或者GPS是否打开");
            gpsLocationShow.setText("当前GPS定位没有打开,正在打开GPS功能");
            setLocationMode(context, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gpsLocationShow.setText("正在定位(请耐心等待)...");
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            String string = "纬度:" + location.getLatitude() + ",经度:" + location.getLongitude();
            debug.loge("获取上次位置：定位工具：" + provider + " 定位结果：" + string);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String string = "纬度:" + location.getLatitude() + ",经度:" + location.getLongitude();
                long endTime = System.currentTimeMillis();
                String timeSpend = "耗时[" + String.valueOf((endTime - startTime) / 1000) + "s]";
                debug.loge(timeSpend + ";位置更改：定位工具：" + provider + " 定位结果：" + string);
                gpsLocationShow.setText(timeSpend + ";位置更改：定位工具：" + provider + " 定位结果：" + string);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                debug.loge("状态变化" + s);
            }

            @Override
            public void onProviderEnabled(String s) {
                debug.loge("供应者使能" + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                debug.loge("供应者禁止" + s);
            }
        };

        startTime = System.currentTimeMillis();
        locationManager.requestLocationUpdates(provider, 1000, 2, locationListener);//绑定监听者，获取坐标
    }

    public void finish(){
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }


    /**
     * mode can be one of:
     * android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY
     * android.provider.Settings.Secure.LOCATION_MODE_OFF
     * android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
     * android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING
     * @param context
     * @param mode
     */
    public static void setLocationMode(Context context,int mode) {
        Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
        int currentMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF);
        intent.putExtra("CURRENT_MODE", currentMode);
        intent.putExtra("NEW_MODE", mode);

        Log.e("jerry", "currentMode="+currentMode + " newmode="+mode);
        context.sendBroadcast(intent, android.Manifest.permission.WRITE_SECURE_SETTINGS);
        Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, mode);
    }

}
