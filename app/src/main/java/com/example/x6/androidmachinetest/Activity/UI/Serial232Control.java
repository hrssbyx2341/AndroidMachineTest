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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/7/18.
 */

public class Serial232Control extends Serial232 {
    private MachineInfoData machineInfoData;
    private SerialPort[] serialPorts = null;
    private byte[] bytes = new byte[128];
    private Handler handler;
    private TextView[] textView;
    private Debug debug;


    public Serial232Control(final Context context, int num, Handler handler) {
        super(context, num, handler);
        machineInfoData = MachineInfoData.getMachineInfoData();
        debug = new Debug("Serial232Control");
        int i = 0;
        for(i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (i+1);
        this.handler = handler;
        getReTest().setEnabled(false);
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReTest().setEnabled(false);
                int i = 0;
                for (i = 0 ; i < getSerialNum(); i++){
                    getSerialTextView(i).setText("");
                    getSerialTextView(i).setTextColor(Color.BLACK);
                }
                startTest(context);
            }
        });
    }

    @Override
    public void startTest(Context context) {
        if(serialPorts == null)
            Serial232Init(context);
        serial232Test();
    }

    public void Serial232Init(Context context){
        debug.logd("232串口初始化");
        if(machineInfoData.typeName.equals(PreMachineInfo.Y7)
                || machineInfoData.typeName.equals(PreMachineInfo.Y8)
                || machineInfoData.typeName.equals(PreMachineInfo.Y10)
                || (machineInfoData.typeName.equals(PreMachineInfo.K7) && !machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.K10) && !machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B301) && !machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B301_LVDS) && !machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B701) && !machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B601) && !machineInfoData.ISRS485)){
            setSerialNum(4);
            textView = new TextView[getSerialNum()];
            int i = 0;
            for(i = 0; i < getSerialNum(); i++){
                textView[i] = new TextView(context);
                getLineout().addView(textView[i], LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            serialPorts = new SerialPort[getSerialNum()];
            if(machineInfoData.cpuName.equals(PreMachineInfo.RK3288)|| machineInfoData.cpuName.equals(PreMachineInfo.RK3368)){
                debug.logd("rockchip 232串口 初始化");
                serialPorts[0] = new SerialPort(PreMachineInfo.ttyS1,115200,8,2,'n');
                serialPorts[1] = new SerialPort(PreMachineInfo.ttyS2,115200,8,2,'n');
                serialPorts[2] = new SerialPort(PreMachineInfo.ttyS3,115200,8,2,'n');
                serialPorts[3] = new SerialPort(PreMachineInfo.ttyS4,115200,8,2,'n');
            }else{
                debug.logd("allwinner 232串口 初始化");
                serialPorts[0] = new SerialPort(PreMachineInfo.ttyS0,115200,8,2,'n');
                serialPorts[1] = new SerialPort(PreMachineInfo.ttyS1,115200,8,2,'n');
                serialPorts[2] = new SerialPort(PreMachineInfo.ttyS2,115200,8,2,'n');
                serialPorts[3] = new SerialPort(PreMachineInfo.ttyS3,115200,8,2,'n');
            }
        }else if(machineInfoData.typeName.equals(PreMachineInfo.Y5)){
            setSerialNum(2);
            textView = new TextView[getSerialNum()];
            int i = 0;
            for(i = 0; i < getSerialNum(); i++){
                textView[i] = new TextView(context);
                getLineout().addView(textView[i], LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            serialPorts = new SerialPort[getSerialNum()];
            serialPorts[0] = new SerialPort(PreMachineInfo.ttyS1,115200,8,2,'n');
            serialPorts[1] = new SerialPort(PreMachineInfo.ttyS2,115200,8,2,'n');
        }else if( (machineInfoData.typeName.equals(PreMachineInfo.K7) && machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.K10) && machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B301) && machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B301_LVDS) && machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B701) && machineInfoData.ISRS485)
                || (machineInfoData.typeName.equals(PreMachineInfo.B601) && machineInfoData.ISRS485)) {
            setSerialNum(2);
            textView = new TextView[getSerialNum()];
            int i = 0;
            for(i = 0; i < getSerialNum(); i++){
                textView[i] = new TextView(context);
                getLineout().addView(textView[i], LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            serialPorts = new SerialPort[2];
            if(machineInfoData.cpuName.equals(PreMachineInfo.RK3368) || machineInfoData.cpuName.equals(PreMachineInfo.RK3288)){
                serialPorts[0] = new SerialPort(PreMachineInfo.ttyS1,115200,8,2,'n');
                serialPorts[1] = new SerialPort(PreMachineInfo.ttyS2,115200,8,2,'n');
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.A33) || machineInfoData.cpuName.equals(PreMachineInfo.H3)) {
                serialPorts[0] = new SerialPort(PreMachineInfo.ttyS0,115200,8,2,'n');
                serialPorts[1] = new SerialPort(PreMachineInfo.ttyS1,115200,8,2,'n');
            }
        }
    }

    private void serial232ReMark(String reMark){
        Result result = Result.getResult();
        result.RS232ReMark = "";
        result.RS232ReMark += reMark +"\n";
    }

    private void serial232Test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isOk = true;

                syncView(handler,0,"232串口 0 正在测试...",Color.BLACK);
                serialPorts[0].sendData(bytes);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(Arrays.equals(bytes,serialPorts[0].receiveData(128))){
                    syncView(handler,0,"232 串口 0 测试通过",Color.BLUE);
                }else{
                    syncView(handler,0,"232 串口 0 测试不通过",Color.RED);
                    serial232ReMark("232串口 0 测试不通过");
                    isOk = false;
                };

                syncView(handler,1,"232串口 1 正在测试...",Color.BLACK);
                serialPorts[1].sendData(bytes);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(Arrays.equals(bytes,serialPorts[1].receiveData(128))){
                    syncView(handler,1,"232 串口 1 测试通过",Color.BLUE);
                }else{
                    syncView(handler,1,"232 串口 1 测试不通过",Color.RED);
                    serial232ReMark("232串口 1 测试不通过");
                    isOk = false;
                };
                if((machineInfoData.typeName.equals(PreMachineInfo.Y5) || machineInfoData.ISRS485) && (
                        !machineInfoData.typeName.equals(PreMachineInfo.Y7)
                        && !machineInfoData.typeName.equals(PreMachineInfo.Y8)
                        && !machineInfoData.typeName.equals(PreMachineInfo.Y10)
                        )){}else{
                    syncView(handler, 2, "232串口 2 正在测试...", Color.BLACK);
                    serialPorts[2].sendData(bytes);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Arrays.equals(bytes, serialPorts[2].receiveData(128))) {
                        syncView(handler, 2, "232 串口 2 测试通过", Color.BLUE);
                    } else {
                        syncView(handler, 2, "232 串口 2 测试不通过", Color.RED);
                        serial232ReMark("232串口 2 测试不通过");
                        isOk = false;
                    }
                    ;

                    syncView(handler, 3, "232串口 3 正在测试...", Color.BLACK);
                    serialPorts[3].sendData(bytes);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Arrays.equals(bytes, serialPorts[3].receiveData(128))) {
                        syncView(handler, 3, "232 串口 3 测试通过", Color.BLUE);
                    } else {
                        syncView(handler, 3, "232 串口 3 测试不通过", Color.RED);
                        serial232ReMark("232串口 3 测试不通过");
                        isOk = false;
                    }
                }
                TestActivity.syncResultList(handler,getPosition(),isOk);
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

    @Override
    public TextView getSerialTextView(int num) {
        if (num >= getSerialNum()){
            return  null;
        }else{
            return textView[num];
        }
    }

    private void syncView(Handler handler, final int i, final String message, final int color ){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getSerialTextView(i).setText(message);
                getSerialTextView(i).setTextColor(color);
            }
        });
    }
    public void finish(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                if(serialPorts != null) {
                    for (i = 0; i < serialPorts.length; i++) {
                        debug.logw("当前关闭串口节点：/dev/ttyS"+String.valueOf(i));
                        try {
                            Thread.sleep(1*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        serialPorts[i].closeSerial();
                    }
                }
            }
        }).start();
    }
}
