package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.SuCommand;

import java.util.Vector;

public class AD extends Common {
    private Debug debug;
    private boolean isPooling = true;
    private SuCommand suCommand;
    private Handler handler;
    public AD(Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("AD");
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
        CheckAD();
    }

    private void CheckAD(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cmd = "cat /sys/bus/iio/devices/iio\\:device0/in_voltage2_raw";
                while (isPooling){
                    Vector<String> vector = suCommand.execRootCmd(cmd);
                    String string = "AD为B701的感光模块\n" +
                            "请调节感光模块附件亮度判断光线亮度是否变化\n" +
                            "用手捂住和放开手，数值差异会在几百左右此时模块才算测试通过\n" +
                            "不插入光感模块，数值也会出现细微的变化，这样的变化不足以说明光感模块测试通过\n" +
                            "光线越亮数值越低\n" +
                            "光线越暗数值越高\n" +
                            "当前光线亮度为[";
                    if(!vector.isEmpty()){
                        string+=vector.get(0)+"]";
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
