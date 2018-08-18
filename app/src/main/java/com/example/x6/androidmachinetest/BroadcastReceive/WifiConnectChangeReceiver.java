package com.example.x6.androidmachinetest.BroadcastReceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.x6.androidmachinetest.function.Debug;

/**
 * Created by 24179 on 2018/5/7.
 */

public class WifiConnectChangeReceiver extends BroadcastReceiver {
    Debug debug = new Debug("BroadcastReceiver");
    private Handler handler;
    public final static String WIFI_INFO_KEY = "wifi info";
    public WifiConnectChangeReceiver(Handler handler1){
        this.handler = handler1;
    }
    private void sendMsg(String str, int what){
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString(WIFI_INFO_KEY,str);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)){ //监听wifi变化
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            switch (wifiState){
                case WifiManager.WIFI_STATE_DISABLED:
                    debug.logd("无线网络已禁用");
                    sendMsg("无线网络已禁用",0);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    debug.logd("正在禁用无线网络");
                    sendMsg("正在禁用无线网络",0);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    debug.logd("无线网络已使能");
                    sendMsg("无线网络已打开",0);
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    debug.logd("无线网络正在使能");
                    sendMsg("正在打开无线网络",0);
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    debug.logd( "未知的无线网络状态");
                    sendMsg("未知的无线网络状态",0);
                    break;
            }
        }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)){
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(info.getState().equals(NetworkInfo.State.DISCONNECTED)){
                debug.logd("wifi 连接已断开");
                sendMsg("无线网络已断开",0);
            }else if(info.getState().equals(NetworkInfo.State.DISCONNECTING)){
                debug.logd("wifi 连接正在断开");
                sendMsg("正在断开无线网络",0);
            }else if(info.getState().equals(NetworkInfo.State.CONNECTING)){
                debug.logd("wifi 连接正在连接");
                sendMsg("正在连接无线网络",0);
            }else if(info.getState().equals(NetworkInfo.State.CONNECTED)){
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                debug.logd("wifi 连接已连接: "+ wifiInfo.getSSID());
                sendMsg("无线网络已连接到 "+wifiInfo.getSSID(),1);
            }
        }



    }
}
