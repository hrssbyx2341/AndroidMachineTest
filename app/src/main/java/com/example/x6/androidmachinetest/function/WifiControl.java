package com.example.x6.androidmachinetest.function;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.Activity.UI.Eth;
import com.example.x6.androidmachinetest.BroadcastReceive.WifiConnectChangeReceiver;
import com.example.x6.androidmachinetest.Data.PreData;


/**
 * Created by 24179 on 2018/5/7.
 */

public class WifiControl {

    private final String TAG = "WifiControl";
    private WifiManager wifiManager;
    private Debug debug;
    private Handler handler;
    private MobileNetControl mobileNetControl = new MobileNetControl();
    private EthControl ethControl = new EthControl();
    private WifiConnectChangeReceiver wifiConnectChangeReceiver;

    public String getMsg(Message message){
        Bundle bundle = message.getData();
        return bundle.getString(WifiConnectChangeReceiver.WIFI_INFO_KEY);
    }

    public void registerNetworkConnectChangeReceiver(Context context,Handler handler) {
        debug.logw("注册WIFI广播");
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        wifiConnectChangeReceiver = new WifiConnectChangeReceiver(handler);
        context.getApplicationContext().registerReceiver(wifiConnectChangeReceiver, filter);
    }
    public void unregisterNetworkConnectChangeReceiver(Context context){
        debug.logw("注销WIFI广播");
        context.unregisterReceiver(wifiConnectChangeReceiver);
    }

    public enum WifiCipherType{
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID
    }

    public WifiControl(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        debug = new Debug("WifiControl");
    }

    public WifiControl(Context context, Handler handler){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        debug = new Debug("WifiControl");
        this.handler = handler;
    }
    public boolean isEnable() {
        return wifiManager.isWifiEnabled();
    }
    // 打开wifi功能
    public boolean WifiOpen() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    public void WifiConnect(String SSID, String Password,WifiCipherType type){
        debug.logw("SSID:"+SSID+"Password:"+Password);
        WifiOpen();
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\""+SSID+"\"";
        if(Password.equals(""))
            config.preSharedKey = null;
        else
            config.preSharedKey = "\""+Password+"\"";
        config.hiddenSSID = true;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        if(Password.equals(""))
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        else
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        int netid = wifiManager.addNetwork(config);
        boolean b = wifiManager.enableNetwork(netid,true);
    }

    public void WifiClose(){
        if(isEnable())
            wifiManager.setWifiEnabled(false);
    }


    public void wifiStartTest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* 先关闭其他网络 */
                ethControl.ethClose();
                mobileNetControl.disableData();
                WifiClose();
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WifiOpen();
                long startTime = System.currentTimeMillis();
                while (!isEnable()){
                    debug.loge("WIFI 正在打开");
                    if(isEnable())
                        break;
                    if(System.currentTimeMillis() - startTime >= 5*1000){
                        handler.sendEmptyMessage(2);
                        return;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                WifiConnect(PreData.wifiSSid,PreData.wifiPasswork,WifiCipherType.WIFICIPHER_WPA);
            }
        }).start();
    }

}
