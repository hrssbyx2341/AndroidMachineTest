package com.example.x6.androidmachinetest.Activity.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.function.Debug;

import java.io.IOException;

/**
 * Created by 24179 on 2018/7/12.
 */

public class Speak extends Common{
    private Debug debug;
    private MediaPlayer mp = new MediaPlayer();
    public Speak(Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("Headhhone");


        getMessage().setText("当前测试外放，请确认喇叭是否有声音播放");
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
    public void startTest(Context context) {
        super.startTest(context);
        if(!startRing(context)){
            getYes().setEnabled(false);
            getNo().setEnabled(false);
            getMessage().setEnabled(false);
            getMessage().setText("系统默认没有打开提示音，请到\n \"设置->声音->默认提示音\"\n选择一个默认的提示音，再进行测试");
            return;
        }//开始响铃
    }

    private boolean startRing(Context context){
        if(RingtoneManager.getActualDefaultRingtoneUri(context , RingtoneManager.TYPE_NOTIFICATION) == null){
            return false;
        }
        try {
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        mp.setLooping(true);
        return true;
    }

    @Override
    public LinearLayout getLineout() {
        return super.getLineout();
    }
    public void finish(){
        if(mp.isPlaying())
            mp.stop();
        debug.logd("关闭铃声");
    }
}
