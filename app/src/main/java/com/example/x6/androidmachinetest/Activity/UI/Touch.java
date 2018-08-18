package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.SuCommand;

/**
 * Created by 24179 on 2018/7/19.
 */

public class Touch extends Common {
    private Debug debug;
    private SuCommand suCommand;
    private Handler handler;
    public Touch(Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("Touch");
        suCommand = new SuCommand();
        this.handler = handler;
        getMessage().setText("请测试触摸是否有盲区，多点触摸是否支持");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                debug.logd("打开触摸调试");
                suCommand.execRootCmdSilent("settings put system pointer_location 1");
            }
        }).start();
    }
    public void finish(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                debug.logd("关闭触摸调试");
                suCommand.execRootCmdSilent("settings put system pointer_location 0");
            }
        }).start();
    }
}
