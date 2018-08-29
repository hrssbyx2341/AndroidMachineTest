package com.example.x6.androidmachinetest.Activity.UI;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;

/**
 * Created by 24179 on 2018/7/18.
 */

public class BlueTooth extends AutoCommon {
    Debug debug;
    private BluetoothAdapter mBluetoothAdapter;
    public BlueTooth(final Context context, String Title, final Handler handler) {
        super(context, Title);
        debug = new Debug("BlueTooth");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getReTest().setEnabled(false);
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReTest().setEnabled(false);
                startTest(context,handler);
            }
        });

    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        getTextView().setText("正在打开蓝牙...");
        BlueToothCheck(handler);
    }

    private void BlueToothCheck(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
                long startTime = System.currentTimeMillis();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable(); //打开蓝牙
                }
                while (!mBluetoothAdapter.isEnabled()){
                    if(System.currentTimeMillis() - startTime > 5*1000){
                        TestActivity.syncResultList(handler,getPosition(),0); //蓝牙打开超时
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                getTextView().setText("蓝牙测试不通过：\n原因：蓝牙打开超时，请检查蓝牙硬件");
                                getTextView().setTextColor(Color.RED);
                                handler.sendEmptyMessage(0);
                                getReTest().setEnabled(true);
                            }
                        });
                        break;
                    }
                    try {
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(mBluetoothAdapter.isEnabled()){
                    TestActivity.syncResultList(handler,getPosition(),1);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getTextView().setText("蓝牙测试通过");
                            getTextView().setTextColor(Color.BLUE);
                            handler.sendEmptyMessage(0);
                            getReTest().setEnabled(true);
                        }
                    });
                }
            }
        }).start();
    };
}
