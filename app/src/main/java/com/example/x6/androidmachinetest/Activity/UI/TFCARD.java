package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.SuCommand;

import java.util.Vector;

/**
 * Created by 24179 on 2018/7/26.
 */

public class TFCARD extends Common {
    private Debug debug;
    private boolean isPooling = true;
    private SuCommand suCommand;
    private Handler handler;
    public TFCARD(Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("USB");
        suCommand = new SuCommand();
        this.handler = handler;

        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debug.loge("通过按键按下了");
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
                debug.loge("不通过按键按下了");
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
                debug.loge("重测按键按下了");
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                handler.sendEmptyMessage(1);
            }
        });
        getReTest().setEnabled(false);
    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        CheckTFCARD();
    }


    private boolean isYesPress = false;
    private boolean isNoPress = false;

    private void CheckTFCARD(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cmd = "ls /dev/block/mmcblk1p1";
                while (isPooling){
                    suCommand.execRootCmd(cmd);
                    String string = "请插拔TF卡进行TF卡测试：\n";

                    if(0 == suCommand.execRootCmdSilent(cmd)){
                        string += "当前检测到TF卡插入";
                        if(!isYesPress){
                            debug.logd("检测到TF卡");
                            pressButton(true);
                            isYesPress = true;
                            isNoPress = false;
                        }
                    }else{
                        string += "当前没有检测到TF卡";
                        if(!isNoPress){
                            debug.loge("没有检测到TF卡");
                            pressButton(false);
                            isYesPress = false;
                            isNoPress = true;
                        }
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

    private void pressButton(final boolean isPress){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(isPress){
                    getYes().setEnabled(false);
                    getNo().setEnabled(false);
                    getNo().setTextColor(Color.WHITE);
                    getYes().setTextColor(Color.BLACK);
                    handler.sendEmptyMessage(0);
                    TestActivity.syncResultList(handler,getPosition(),1);
                }else{
                    getYes().setEnabled(false);
                    getNo().setEnabled(false);
                    getYes().setTextColor(Color.WHITE);
                    getNo().setTextColor(Color.BLACK);
                    handler.sendEmptyMessage(0);
                    TestActivity.syncResultList(handler,getPosition(),0);
                }
            }
        });
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
