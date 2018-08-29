package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.SuCommand;
import com.example.x6.androidmachinetest.function.WifiControl;

/**
 * Created by 24179 on 2018/7/16.
 */

public class Wifi extends AutoCommon {
    private Debug debug;
    private SuCommand suCommand;
    private WifiControl wifiControl;


    Handler handlerP;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    appendWifiState(wifiControl.getMsg(msg), Color.BLACK);
                    break;
                case 1:
                    appendWifiState(wifiControl.getMsg(msg),Color.BLUE);
                    handlerP.sendEmptyMessage(0);
                    TestActivity.syncResultList(handlerP,getPosition(),1);
                    break;
                case 2:
                    TestActivity.syncResultList(handlerP,getPosition(),0); //wifi超过5S打不开，那就识别为WIFI有故障
                    handlerP.sendEmptyMessage(0);
                    getTextView().setText("WIFI 打开超时，请检查WIFI是否有故障");
                    getTextView().setTextColor(Color.RED);
                    break;
            }
        }
    };

    public Wifi(final Context context, String Title, Handler handler1) {
        super(context, Title);
        debug = new Debug("Wifi");
        suCommand = new SuCommand();
        wifiControl = new WifiControl(context,handler);
        wifiControl.registerNetworkConnectChangeReceiver(context,handler);
        this.handlerP = handler1;
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWifiState("",Color.BLACK);
                handlerP.sendEmptyMessage(1);
                startTest(context,handler);
            }
        });
    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        wifiControl.wifiStartTest();
    }

    private void setWifiState(String message, int color){
        getTextView().setText(message);
        getTextView().setTextColor(color);
    }
    private void appendWifiState(String message, int color){
        if(getTextView().getText() == null){
            getTextView().setText(message);
        }else{
            getTextView().setText(getTextView().getText().toString()+"\n"+message);
        }
        getTextView().setTextColor(color);
    }

    public void _finish(Context context){

        wifiControl.unregisterNetworkConnectChangeReceiver(context);
    }


}
