package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.serialportlib.SerialPort;

import java.util.Arrays;

/**
 * Created by 24179 on 2018/7/19.
 */

public class Serial485Odd extends Common{
    private SerialPort serialPort = null;
    private MachineInfoData machineInfoData;
    private boolean isPooling = true;
    private byte[] bytes = new byte[128];
    private Handler handler;
    private Debug debug;
    public Serial485Odd(Context context, String title1, final Handler handler) {
        super(context, title1);
        machineInfoData = MachineInfoData.getMachineInfoData();
//        getLineout().removeView(getReTest());
        debug = new Debug("Serial485Odd");
        int i = 0;
        for(i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (i+1);
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
        startOdd485Test();
    }
    public void finish(){
        isPooling = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(serialPort != null)
                    serialPort.closeSerial();
            }
        }).start();
    }


    private void startOdd485Test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sendNum = 0;
                int receiveNum = 0;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getMessage().setText("正在准备485测试...");
                        getMessage().setTextColor(Color.BLACK);
                    }
                });
                try {
                    Thread.sleep(4*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(serialPort == null)
                    serialPort = new SerialPort("S3", 115200, 8, 2, 'n');
                while (isPooling) {
                    serialPort.sendData(bytes);
                    sendNum++;
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    byte[] bytes1 = serialPort.receiveData(128);
                    if(Arrays.equals(bytes,bytes1))
                        receiveNum++;
                    syncMessage(sendNum,receiveNum);
                }
            }
        }).start();
    }

    private void syncMessage(final int sendNum, final int receiveNum){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getMessage().setText("485串口发送 "+String.valueOf(sendNum)+" 帧数据 接收 "+String.valueOf(receiveNum)+" 帧数据");
                debug.logw("485串口发送 "+String.valueOf(sendNum)+" 帧数据 接收 "+String.valueOf(receiveNum)+" 帧数据");
            }
        });
    }

}
