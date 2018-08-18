package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.EthControl;
import com.example.x6.androidmachinetest.function.MobileNetControl;
import com.example.x6.androidmachinetest.function.WifiControl;

/**
 * Created by 24179 on 2018/7/20.
 */

public class MobileNet extends Common {
    private Debug debug;
    private MobileNetControl mobileNetControl;
    private WifiControl wifiControl;
    private EthControl ethControl;
    private Boolean isPooling = true;
    private Handler handler;
    private Context context;

    public MobileNet(Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("MobileNet");
        this.handler = handler;
        this.context = context;
        mobileNetControl = new MobileNetControl();
        wifiControl = new WifiControl(context,handler);
        ethControl = new EthControl();

        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),true);
            }
        });
        getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getYes().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),false);
            }
        });
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                handler.sendEmptyMessage(1);
            }
        });

    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        testMobileNet();
    }

    public void finish(){
        isPooling = false;
    }

    private void testMobileNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*先关闭其他网络*/
                mobileNetControl.CloseFlaMode();
                wifiControl.WifiClose();
                ethControl.ethClose();
                mobileNetControl.enableData();

                while (isPooling){
                    syncView(mobileNetControl.isNetworkAvailable(context));
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
    private void syncView(final int result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (result){
                    case 8888:
                        getMessage().setText("移动网络 测试正常");
                        getMessage().setTextColor(Color.BLUE);
                        break;
                    case 6666:
                        getMessage().setText("当前测试移动网络，请关闭Wifi");
                        break;
                    case 6688:
                        getMessage().setText("当前测试移动网络，请关闭以太网");
                        break;
                    case 8866:
                        getMessage().setText("当前无网络连接");
                        getMessage().setTextColor(Color.RED);
                        break;
                }
            }
        });
    }

}
