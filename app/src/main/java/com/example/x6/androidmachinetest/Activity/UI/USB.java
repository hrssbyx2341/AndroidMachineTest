package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.R;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.SuCommand;

import java.util.Vector;

/**
 * Created by 24179 on 2018/7/19.
 */

public class USB extends Common{
    private Debug debug;
    private boolean isPooling = true;
    private SuCommand suCommand;
    private Handler handler;
    public USB(Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("USB");
        suCommand = new SuCommand();
        this.handler = handler;
        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),1);
            }
        });
        getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getYes().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),0);
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
        CheckUSB();
    }

    private void CheckUSB(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPooling){
                    Vector<String> vector = suCommand.execRootCmd("busybox lsusb");
                    String string = "请依次用USB设备测试各个USB口，当前设备上USB设备如下：\n";
                    int i = 0;
                    for(i = 0; i < vector.size(); i++){
                        string += vector.get(i)+"\n";
                    }
                    syncMessage(string);
                    try {
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void finish(){
        isPooling = false;
    }

    private void syncMessage(final String message){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getMessage().setTextColor(Color.BLACK);
                getMessage().setText(message);
                getMessage().setTextSize(18);
            }
        });
    }
}
