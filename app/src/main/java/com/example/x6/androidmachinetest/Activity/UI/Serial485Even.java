package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.Core.Result;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.serialportlib.SerialPort;

import java.util.Arrays;

/**
 * Created by 24179 on 2018/7/19.
 */

public class Serial485Even extends AutoCommon{

    private TextView[] serialTextViews;
    private SerialPort[] serialPorts;
    private MachineInfoData machineInfoData;
    private Debug debug;
    private Handler handler;
    private byte[] bytes = new byte[128];
    public Serial485Even(final Context context, String Title, final Handler handler) {
        super(context, Title);
        machineInfoData = MachineInfoData.getMachineInfoData();
        this.handler = handler;
        debug = new Debug("Serial485Even");
        getLineout().removeView(getTextView());
        getTitle().setText("485测试");
        serialTextViews = new TextView[2];
        serialTextViews[0] = new TextView(context);
        serialTextViews[1] = new TextView(context);
        getLineout().addView(serialTextViews[0], LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getLineout().addView(serialTextViews[1], LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        serialPorts = new SerialPort[2];
        serialPorts[0] = null;
        serialPorts[1] = null;
        int i = 0;
        for(i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (i+1);

        getReTest().setEnabled(false);
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReTest().setEnabled(false);
                startTest(context,handler);
                serialTextViews[0].setText("");
                serialTextViews[1].setText("");
                serialTextViews[0].setTextColor(Color.BLACK);
                serialTextViews[1].setTextColor(Color.BLACK);
            }
        });
    }


    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        if(serialPorts[0] == null && serialPorts[1] == null)
            serialPortInit();
        serial485EvenStart();
    }

    @Override
    public void finish() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(serialPorts[0] != null)
                    serialPorts[0].closeSerial();
                if(serialPorts[1] != null)
                    serialPorts[1].closeSerial();
            }
        }).start();
    }

    private void serial485Remark(String reMark){
        Result result = Result.getResult();
        result.RS485ReMark = "";
        result.RS485ReMark += reMark+"\n";
    }

    private void serial485EvenStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSerial485Ok = true;
                handler.sendEmptyMessage(1);
                syncView(handler,"串口2 正在发送数据到 串口3...",0, Color.BLACK);
                serialPorts[0].sendData(bytes);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(Arrays.equals(bytes, serialPorts[1].receiveData(128))){
                    syncView(handler,"串口2 -> 串口3 成功",0,Color.BLUE);
                }else{
                    syncView(handler,"串口2 -> 串口3 失败",0,Color.RED);
                    serial485Remark("串口2 -> 串口3 失败");
                    isSerial485Ok = false;
                }

                syncView(handler,"串口3 正在发送数据到 串口2...",1,Color.BLACK);
                serialPorts[1].sendData(bytes);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(Arrays.equals(bytes,serialPorts[0].receiveData(128))){
                    syncView(handler,"串口3 -> 串口2 成功",1,Color.BLUE);
                }else{
                    syncView(handler,"串口2 -> 串口3 失败",1,Color.RED);
                    serial485Remark("串口3 -> 串口2 失败");
                    isSerial485Ok = false;
                }
                TestActivity.syncResultList(handler,getPosition(),isSerial485Ok);
                handler.sendEmptyMessage(0);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getReTest().setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void serialPortInit(){
        if(machineInfoData.cpuName != null) {
            if (machineInfoData.cpuName.equals(PreMachineInfo.RK3288)
                    || machineInfoData.cpuName.equals(PreMachineInfo.RK3368)) {
                serialPorts[0] = new SerialPort("S3",115200,8,2,'n');
                serialPorts[1] = new SerialPort("S4",115200,8,2,'n');
            } else if (machineInfoData.cpuName.equals(PreMachineInfo.A33)
                    || machineInfoData.cpuName.equals(PreMachineInfo.H3)) {
                serialPorts[0] = new SerialPort("S2",115200,8,2,'n');
                serialPorts[1] = new SerialPort("S3",115200,8,2,'n');
            }
        }
    }
    private void syncView(Handler handler, final String message, final int pos, final int color){
        if(pos >= 2) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                serialTextViews[pos].setText(message);
                serialTextViews[pos].setTextColor(color);
            }
        });
    }

}
