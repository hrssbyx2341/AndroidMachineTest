package com.example.x6.androidmachinetest.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.x6.androidmachinetest.Activity.UI.BlueTooth;
import com.example.x6.androidmachinetest.Activity.UI.CameraOne;
import com.example.x6.androidmachinetest.Activity.UI.Eth;
import com.example.x6.androidmachinetest.Activity.UI.EthR;
import com.example.x6.androidmachinetest.Activity.UI.GPIO;
import com.example.x6.androidmachinetest.Activity.UI.GPS;
import com.example.x6.androidmachinetest.Activity.UI.Headhhone;
import com.example.x6.androidmachinetest.Activity.UI.MobileNet;
import com.example.x6.androidmachinetest.Activity.UI.RTC;
import com.example.x6.androidmachinetest.Activity.UI.Record;
import com.example.x6.androidmachinetest.Activity.UI.Screen;
import com.example.x6.androidmachinetest.Activity.UI.Serial232Control;
import com.example.x6.androidmachinetest.Activity.UI.Serial485Even;
import com.example.x6.androidmachinetest.Activity.UI.Serial485Odd;
import com.example.x6.androidmachinetest.Activity.UI.Speak;
import com.example.x6.androidmachinetest.Activity.UI.TFCARD;
import com.example.x6.androidmachinetest.Activity.UI.Touch;
import com.example.x6.androidmachinetest.Activity.UI.USB;
import com.example.x6.androidmachinetest.Activity.UI.Wifi;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.PlanTest;
import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.Core.Result;
import com.example.x6.androidmachinetest.DataBase.TestDataBaseUtils;
import com.example.x6.androidmachinetest.R;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.GetUUID;

/**
 * Created by 24179 on 2018/7/11.
 */

public class TestActivity extends Activity {
    private PlanTest planTest = PlanTest.getPlanTest();
    private Debug debug;
    private LinearLayout linearLayout;
    private LinearLayout stepShowLineLayout;
    private TextView[] testProject;
    private Button nextTest;
    private int waitSize = 10;
    private int padding = 10;
    private Result result;

    private int currentPosition = 0; //当前测试到哪一项


    /* 所有测试项目 */
    private Speak speakTest = null;
    private Headhhone headhhoneTest = null;
    private CameraOne cameraOne = null;
    private Record recordTest = null;
    private EthR ethTest = null;
    private Wifi wifiTest = null;
    private BlueTooth blueToothTest = null;
    private Serial232Control serial232Test = null;
    private Serial485Odd serial485Odd = null;
    private Serial485Even serial485Even = null;
    private USB usbTest = null;
    private GPIO gpioTest = null;
    private Screen screenTets = null;
    private Touch touchTest = null;
    private MobileNet mobileNet = null;
    private RTC rtcTest = null;
    private TFCARD tfcardTest = null;
    private GPS gpsTest = null;

    /*************************************************/
    /*      测试数据在每次按下下一项后写入数据库     */
    /*************************************************/
    private void updateDataBase(){
        int temp = currentPosition - 1;
        String Pass = "测试通过";
        String NotPass = "测试不通过";
        String NotSupport = "设备不支持";
        TestDataBaseUtils testDataBaseUtils = TestDataBaseUtils.getTestDataBaseUtils(TestActivity.this.getApplicationContext());
        if(speakTest!=null){
            if(temp == speakTest.getPosition()){
                debug.loge("spk");
                if(result.speakTestResult.equals(Pass)) {
                    testDataBaseUtils.updateSpk(1);
                }else if(result.speakTestResult.equals(NotPass)){
                    testDataBaseUtils.updateSpk(0);
                }else if(result.speakTestResult.equals(NotSupport)){
                    testDataBaseUtils.updateSpk(-1);
                }
            }
        }
        if(headhhoneTest!=null){
            if(temp == headhhoneTest.getPosition()){
                debug.loge("hp");
                if(result.headPhoneTestResult.equals(Pass)) {
                    testDataBaseUtils.updateHp(1);
                }else if(result.headPhoneTestResult.equals(NotPass)){
                    testDataBaseUtils.updateHp(0);
                }else if(result.headPhoneTestResult.equals(NotSupport)){
                    testDataBaseUtils.updateHp(-1);
                }
            }
        }
        if(cameraOne != null){
            if(temp == cameraOne.getPosition()){
                debug.loge("CameraOne");
                if(result.CameraTestResult.equals(Pass)){
                    testDataBaseUtils.updateCamera(1);
                }else if(result.CameraTestResult.equals(NotPass)){
                    testDataBaseUtils.updateCamera(0);
                }else if(result.CameraTestResult.equals(NotSupport)){
                    testDataBaseUtils.updateCamera(-1);
                }
            }
        }
        if(recordTest!=null){
            if(temp == recordTest.getPosition()){
                debug.loge("rec");
                if(result.recordTest.equals(Pass)) {
                    testDataBaseUtils.updateRecord(1);
                }else if(result.recordTest.equals(NotPass)){
                    testDataBaseUtils.updateRecord(0);
                }else if(result.recordTest.equals(NotSupport)){
                    testDataBaseUtils.updateRecord(-1);
                }
            }
        }
        if(ethTest!=null){
            if(temp == ethTest.getPosition()){
                debug.loge("eth");
                if(result.ethTest.equals(Pass)) {
                    testDataBaseUtils.updateEth(1);
                }else if(result.ethTest.equals(NotPass)){
                    testDataBaseUtils.updateEth(0);
                }else if(result.ethTest.equals(NotSupport)){
                    testDataBaseUtils.updateEth(-1);
                }
            }
        }
        if(wifiTest!=null){
            if(temp == wifiTest.getPosition()){
                debug.loge("wifi");
                if(result.wifiTest.equals(Pass)) {
                    testDataBaseUtils.updateWifi(1);
                }else if(result.wifiTest.equals(NotPass)){
                    testDataBaseUtils.updateWifi(0);
                }else if(result.wifiTest.equals(NotSupport)){
                    testDataBaseUtils.updateWifi(-1);
                }
            }
        }
        if(blueToothTest!=null){
            if(temp == blueToothTest.getPosition()){
                debug.loge("bt");
                if(result.blueToothTest.equals(Pass)) {
                    testDataBaseUtils.updateBT(1);
                }else if(result.blueToothTest.equals(NotPass)){
                    testDataBaseUtils.updateBT(0);
                }else if(result.blueToothTest.equals(NotSupport)){
                    testDataBaseUtils.updateBT(-1);
                }
            }
        }
        if(serial232Test!=null){
            if(temp == serial232Test.getPosition()){
                debug.loge("232");
                if(result.RS232Test.equals(Pass)) {
                    testDataBaseUtils.updateRS232(1);
                }else if(result.RS232Test.equals(NotPass)){
                    testDataBaseUtils.updateRS232(0);
                }else if(result.RS232Test.equals(NotSupport)){
                    testDataBaseUtils.updateRS232(-1);
                }
            }
        }
        if(serial485Odd!=null && serial485Even==null){
            if(temp == serial485Odd.getPosition()){
                debug.loge("485o");
                if(result.RS485Test.equals(Pass)) {
                    testDataBaseUtils.updateRS485(1);
                }else if(result.RS485Test.equals(NotPass)){
                    testDataBaseUtils.updateRS485(0);
                }else if(result.RS485Test.equals(NotSupport)){
                    testDataBaseUtils.updateRS485(-1);
                }
            }
        }
        if(serial485Odd == null && serial485Even!=null){
            if(temp == serial485Even.getPosition()){
                debug.loge("485e");
                if(result.RS485Test.equals(Pass)) {
                    testDataBaseUtils.updateRS485(1);
                }else if(result.RS485Test.equals(NotPass)){
                    testDataBaseUtils.updateRS485(0);
                }else if(result.RS485Test.equals(NotSupport)){
                    testDataBaseUtils.updateRS485(-1);
                }
            }
        }
        if(usbTest != null){
            if(temp == usbTest.getPosition()){
                debug.loge("usb");
                if(result.USBTest.equals(Pass)) {
                    testDataBaseUtils.updateUSB(1);
                }else if(result.USBTest.equals(NotPass)){
                    testDataBaseUtils.updateUSB(0);
                }else if(result.USBTest.equals(NotSupport)){
                    testDataBaseUtils.updateUSB(-1);
                }
            }
        }
        if(gpioTest != null){
            if(temp == gpioTest.getPosition()){
                debug.loge("gpio");
                if(result.GpioTest.equals(Pass)) {
                    testDataBaseUtils.updateGPIO(1);
                }else if(result.GpioTest.equals(NotPass)){
                    testDataBaseUtils.updateGPIO(0);
                }else if(result.GpioTest.equals(NotSupport)){
                    testDataBaseUtils.updateGPIO(-1);
                }
            }
        }
        if(screenTets != null){
            if(temp == screenTets.getPosition()){
                debug.loge("screen");
                if(result.ScreenTest.equals(Pass)) {
                    testDataBaseUtils.updateScreen(1);
                }else if(result.ScreenTest.equals(NotPass)){
                    testDataBaseUtils.updateScreen(0);
                }else if(result.ScreenTest.equals(NotSupport)){
                    testDataBaseUtils.updateScreen(-1);
                }
            }
        }
        if(touchTest != null){
            if(temp == touchTest.getPosition()){
                debug.loge("touch");
                if(result.TouchScreenTest.equals(Pass)) {
                    testDataBaseUtils.updateTS(1);
                }else if(result.TouchScreenTest.equals(NotPass)){
                    testDataBaseUtils.updateTS(0);
                }else if(result.TouchScreenTest.equals(NotSupport)){
                    testDataBaseUtils.updateTS(-1);
                }
            }
        }
        if(mobileNet != null){
            if(temp == mobileNet.getPosition()){
                debug.loge("mobilenet");
                if(result.mobileNetTest.equals(Pass)) {
                    testDataBaseUtils.updateMobileNet(1);
                }else if(result.mobileNetTest.equals(NotPass)){
                    testDataBaseUtils.updateMobileNet(0);
                }else if(result.mobileNetTest.equals(NotSupport)){
                    testDataBaseUtils.updateMobileNet(-1);
                }
            }
        }
        if(tfcardTest != null){
            if(temp == tfcardTest.getPosition()){
                debug.loge("tfcard");
                if(result.TFCARDTest.equals(Pass)) {
                    testDataBaseUtils.updateTF(1);
                }else if(result.TFCARDTest.equals(NotPass)){
                    testDataBaseUtils.updateTF(0);
                }else if(result.TFCARDTest.equals(NotSupport)){
                    testDataBaseUtils.updateTF(-1);
                }
            }
        }
        if(gpsTest != null){
            if(temp == gpsTest.getPosition()){
                debug.loge("gps");
                if(result.gpsTest.equals(Pass)) {
                    testDataBaseUtils.updateGPS(1);
                }else if(result.gpsTest.equals(NotPass)){
                    testDataBaseUtils.updateGPS(0);
                }else if(result.gpsTest.equals(NotSupport)){
                    testDataBaseUtils.updateGPS(-1);
                }
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;int pos;
            switch (msg.what){
                case 0:
                    nextTest.setEnabled(true);
                    break;
                case 1:
                    nextTest.setEnabled(false);
                    break;
                case 3:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos");
                    testProject[pos].setTextColor(Color.GREEN);
                    setTestResult(1);
                    break;
                case -3:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos");
                    testProject[pos].setTextColor(Color.RED);
                    setTestResult(0);
                    break;
                case -1:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos");
                    testProject[pos].setTextColor(Color.argb(0,0xf1,0x9e,0x9e));
                    setTestResult(-1);
                    break;
            }
        }
    };






    public static void syncResultList(Handler handler,int i,int isPass){
        Bundle bundle = new Bundle();
        Message message = new Message();
        bundle.putInt("pos",i);
        switch (isPass){
            case 1:
                message.what = 3;
                break;
            case -1:
                message.what = -1;
                break;
            case 0:
                message.what = -3;
                break;
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }
    private void setTestResult(int isPass){
        if(speakTest != null){
            if(currentPosition == speakTest.getPosition()) {
                switch (isPass){
                    case 1:
                        result.speakTestResult = "测试通过";
                        break;
                    case 0:
                        result.speakTestResult = "测试不通过";
                        break;
                    case -1:
                        result.speakTestResult = "设备不支持";
                        break;
                }
            }
        }

        if(headhhoneTest != null){
            if(currentPosition == headhhoneTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.headPhoneTestResult = "测试通过";
                        break;
                    case 0:
                        result.headPhoneTestResult = "测试不通过";
                        break;
                    case -1:
                        result.headPhoneTestResult = "设备不支持";
                        break;
                }
            }
        }

        if(cameraOne != null){
            if(currentPosition == cameraOne.getPosition()){
                switch (isPass){
                    case 1:
                        result.CameraTestResult = "测试通过";
                        break;
                    case 0:
                        result.CameraTestResult = "测试不通过";
                        break;
                    case -1:
                        result.CameraTestResult = "设备不支持";
                        break;
                }
            }
        }

        if(recordTest != null){
            switch (isPass){
                case 1:
                    result.recordTest = "测试通过";
                    break;
                case 0:
                    result.recordTest = "测试不通过";
                    break;
                case -1:
                    result.recordTest = "设备不支持";
                    break;
            }
        }

        if(ethTest != null){
            if(currentPosition == ethTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.ethTest = "测试通过";
                        break;
                    case 0:
                        result.ethTest = "测试不通过";
                        break;
                    case -1:
                        result.ethTest = "设备不支持";
                        break;
                }
            }
        }

        if(wifiTest != null){
            if(currentPosition == wifiTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.wifiTest = "测试通过";
                        break;
                    case 0:
                        result.wifiTest = "测试不通过";
                        break;
                    case -1:
                        result.wifiTest = "设备不支持";
                        break;
                }
            }
        }

        if(blueToothTest != null){
            if(currentPosition == blueToothTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.blueToothTest = "测试通过";
                        break;
                    case 0:
                        result.blueToothTest = "测试不通过";
                        break;
                    case -1:
                        result.blueToothTest = "设备不支持";
                        break;
                }
            }
        }

        if(serial232Test != null){
            if(currentPosition == serial232Test.getPosition()){
                switch (isPass){
                    case 1:
                        result.RS232Test = "测试通过";
                        break;
                    case 0:
                        result.RS232Test = "测试不通过";
                        break;
                    case -1:
                        result.RS232Test = "设备不支持";
                        break;
                }
            }
        }

        if(serial485Odd != null){
            if(currentPosition == serial485Odd.getPosition()){
                switch (isPass){
                    case 1:
                        result.RS485Test = "测试通过";
                        break;
                    case 0:
                        result.RS485Test = "测试不通过";
                        break;
                    case -1:
                        result.RS485Test = "设备不支持";
                        break;
                }
            }
        }

        if(serial485Even != null){
            if(currentPosition == serial485Even.getPosition()){
                switch (isPass){
                    case 1:
                        result.RS485Test = "测试通过";
                        break;
                    case 0:
                        result.RS485Test = "测试不通过";
                        break;
                    case -1:
                        result.RS485Test = "设备不支持";
                        break;
                }
            }
        }

        if(usbTest != null){
            if(currentPosition == usbTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.USBTest = "测试通过";
                        break;
                    case 0:
                        result.USBTest = "测试不通过";
                        break;
                    case -1:
                        result.USBTest = "设备不支持";
                        break;
                }
            }
        }

        if(tfcardTest != null){
            if(currentPosition == tfcardTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.TFCARDTest = "测试通过";
                        break;
                    case 0:
                        result.TFCARDTest = "测试不通过";
                        break;
                    case -1:
                        result.TFCARDTest = "设备不支持";
                        break;
                }
            }
        }

        if(gpioTest != null){
            if(currentPosition == gpioTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.GpioTest = "测试通过";
                        break;
                    case 0:
                        result.GpioTest = "测试不通过";
                        break;
                    case -1:
                        result.GpioTest = "设备不支持";
                        break;
                }
            }
        }

        if(screenTets != null){
            if(currentPosition == screenTets.getPosition()){
                switch (isPass){
                    case 1:
                        result.ScreenTest = "测试通过";
                        break;
                    case 0:
                        result.ScreenTest = "测试不通过";
                        break;
                    case -1:
                        result.ScreenTest = "设备不支持";
                        break;
                }
            }
        }

        if(touchTest != null){
            if(currentPosition == touchTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.TouchScreenTest = "测试通过";
                        break;
                    case 0:
                        result.TouchScreenTest = "测试不通过";
                        break;
                    case -1:
                        result.TouchScreenTest = "设备不支持";
                        break;
                }
            }
        }

        if(mobileNet != null){
            if(currentPosition == mobileNet.getPosition()){
                switch (isPass){
                    case 1:
                        result.mobileNetTest = "测试通过";
                        break;
                    case 0:
                        result.mobileNetTest = "测试不通过";
                        break;
                    case -1:
                        result.mobileNetTest = "设备不支持";
                        break;
                }
            }
        }
        if(gpsTest != null){
            if(currentPosition == gpsTest.getPosition()){
                switch (isPass){
                    case 1:
                        result.gpsTest = "测试通过";
                        break;
                    case 0:
                        result.gpsTest = "测试不通过";
                        break;
                    case -1:
                        result.gpsTest = "设备不支持";
                        break;
                }
            }
        }

        debug.logw(result.showResult());
    }

    /**
     * 设备信息录入数据库
     */
    private void updateBaseDate(){
        TestDataBaseUtils testDataBaseUtils = TestDataBaseUtils.getTestDataBaseUtils(TestActivity.this);
        testDataBaseUtils.updateType(MachineInfoData.getMachineInfoData().typeName);
        testDataBaseUtils.updateName(MachineInfoData.getMachineInfoData().typeName+"_"+MachineInfoData.getMachineInfoData().UUID);
        testDataBaseUtils.updateCpuType(MachineInfoData.getMachineInfoData().cpuName);
        testDataBaseUtils.updateCpuSerial(GetUUID.getUUID());
        testDataBaseUtils.updateLastUpdateDate(android.os.Build.VERSION.INCREMENTAL);
        testDataBaseUtils.updateUuid(MachineInfoData.getMachineInfoData().UUID);
        debug.logw("写入数据库设备相关数据");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug = new Debug("TestActivity");
        result = Result.getResult();
        /*
        初始化
         */
        linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_test,null);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        stepShowLineLayout = new LinearLayout(this);
        stepShowLineLayout.setOrientation(LinearLayout.HORIZONTAL);
        addStepView(stepShowLineLayout,this);
        linearLayout.addView(stepShowLineLayout);
        nextTest = new Button(this);
        nextTest.setText("下一个");
        nextTest.setEnabled(false);

        initAllTestProject(this); //把所有要测试的项目都初始化一下。
        updateBaseDate();//相关设备信息
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPosition >= planTest.planStep.length){
                    return;
                }
                /*
                这里进行一次布局的替换
                 */
                debug.logd("当前位置："+String.valueOf(currentPosition));
                removeCurrentLayout(currentPosition);
                addNextLayout(currentPosition+1);
                syncStepView(currentPosition+1);
                currentPosition ++;
                setContentView(linearLayout);
                updateDataBase();
                nextTest.setEnabled(false);//disable nextTest Button
            }
        });


        addNextLayout(currentPosition); //这里添加第一次布局
        setContentView(linearLayout);


    }


    private void addNextLayout(int nextTestNum){
        if(speakTest != null) {
            if (speakTest.getPosition() == nextTestNum) {
                linearLayout.addView(speakTest.getLineout());
                speakTest.startTest(this);
                debug.logd("添加外放布局");
            }
        }
        if(headhhoneTest != null) {
            if (headhhoneTest.getPosition() == nextTestNum) {
                linearLayout.addView(headhhoneTest.getLineout());
                headhhoneTest.startTest(this);
                debug.logd("添加耳机布局");
            }
        }
        if(cameraOne != null){
            if(cameraOne.getPosition() == nextTestNum){
                linearLayout.addView(cameraOne.getLineout());
                cameraOne.startTest(this,handler);
                debug.logd("添加摄像头布局");
            }
        }
        if(recordTest != null){
            if(recordTest.getPosition() == nextTestNum){
                linearLayout.addView(recordTest.getLineout());
                recordTest.startTest(this);
                debug.logd("添加录音布局");
            }
        }
        if(ethTest != null){
            if(ethTest.getPosition() == nextTestNum){
                linearLayout.addView(ethTest.getLineout());
                ethTest.startTest(this);
                debug.logd("添加以太网测试布局");
            }
        }
        if(wifiTest != null){
            if(wifiTest.getPosition() == nextTestNum){
                linearLayout.addView(wifiTest.getScrollView());
                wifiTest.startTest(this,handler);
                debug.logd("添加WIFI测试布局");
            }
        }
        if(blueToothTest != null){
            if(blueToothTest.getPosition() == nextTestNum){
                linearLayout.addView(blueToothTest.getScrollView());
                blueToothTest.startTest(this,handler);
                debug.logd("添加蓝牙测试布局");
            }
        }

        if(serial232Test != null){
            if(serial232Test.getPosition() == nextTestNum){
                linearLayout.addView(serial232Test.getLineout());
                serial232Test.startTest(this);
                debug.logd("添加232串口测试布局");
            }
        }
        if(serial485Odd !=null){
            if(serial485Odd.getPosition() == nextTestNum){
                linearLayout.addView(serial485Odd.getLineout());
                serial485Odd.startTest(this,handler);
                debug.logd("添加奇数485测试布局");
            }
        }
        if(serial485Even != null){
            if(serial485Even.getPosition() == nextTestNum){
                linearLayout.addView(serial485Even.getScrollView());
                serial485Even.startTest(this,handler);
                debug.logd("添加偶数485测试布局");
            }
        }
        if(usbTest != null){
            if(usbTest.getPosition() == nextTestNum){
                linearLayout.addView(usbTest.getLineout());
                usbTest.startTest(this,handler);
                debug.logd("添加USB测试布局");
            }
        }
        if(gpioTest != null){
            if(gpioTest.getPosition() == nextTestNum){
                linearLayout.addView(gpioTest.getScrollView());
                gpioTest.startTest(this,handler);
                debug.logd("添加GPIO测试布局");
            }
        }
        if(screenTets != null){
            if(screenTets.getPosition() == nextTestNum){
                linearLayout.addView(screenTets.getLineout());
                screenTets.startTest(this);
                debug.logd("添加屏幕测试布局");
            }
        }
        if(touchTest != null){
            if(touchTest.getPosition() == nextTestNum){
                linearLayout.addView(touchTest.getLineout());
                touchTest.startTest(this,handler);
                debug.logd("添加触摸测试布局");
            }
        }
        if(mobileNet != null){
            if(mobileNet.getPosition() == nextTestNum){
                linearLayout.addView(mobileNet.getLineout());
                mobileNet.startTest(this,handler);
                debug.logd("添加移动网络测试布局");
            }
        }
        if(gpsTest != null){
            if(gpsTest.getPosition() == nextTestNum){
                linearLayout.addView(gpsTest.getLineout());
                gpsTest.startTest(this,handler);
                debug.logd("添加GPS测试布局");
            }
        }
        if(rtcTest != null){
            if(rtcTest.getPosition() == nextTestNum){
                linearLayout.addView(rtcTest.getScrollView());
                rtcTest.startTest(this,handler);
                debug.logd("添加RTC测试布局");
            }
        }
        if(tfcardTest != null){
            if(tfcardTest.getPosition() == nextTestNum){
                linearLayout.addView(tfcardTest.getLineout());
                tfcardTest.startTest(this,handler);
                debug.logd("添加TF卡测试布局");
            }
        }
        linearLayout.addView(nextTest);
    }

    private void removeCurrentLayout(int currentTest){
        linearLayout.removeView(nextTest);
        if(speakTest != null){
            if(speakTest.getPosition() == currentTest){
                speakTest.finish();
                linearLayout.removeView(speakTest.getLineout());
                debug.logd("移除外放布局");
            }
        }
        if(headhhoneTest != null){
            if(headhhoneTest.getPosition() == currentTest){
                headhhoneTest.finish();
                linearLayout.removeView(headhhoneTest.getLineout());
                debug.logd("移除耳机布局");
            }
        }
        if(cameraOne != null){
            if(cameraOne.getPosition() == currentTest){
                cameraOne.finish();
                linearLayout.removeView(cameraOne.getLineout());
                debug.logd("移除相机布局");
            }
        }
        if(recordTest != null){
            if(recordTest.getPosition() == currentTest){
                recordTest.finish();
                linearLayout.removeView(recordTest.getLineout());
                debug.logd("移除录音布局");
            }
        }
        if(ethTest != null){
            if(ethTest.getPosition() == currentTest){
                ethTest.finish();
                linearLayout.removeView(ethTest.getLineout());
                debug.logd("移除以太网测试布局");
            }
        }
        if(wifiTest != null){
            if(wifiTest.getPosition() == currentTest){
                wifiTest._finish(this.getApplicationContext());
                linearLayout.removeView(wifiTest.getScrollView());
                debug.logd("移除WIFI测试布局");
            }
        }
        if(blueToothTest != null){
            if(blueToothTest.getPosition() == currentTest){
                blueToothTest.finish();
                linearLayout.removeView(blueToothTest.getScrollView());
                debug.logd("移除蓝牙测试布局");
            }
        }
        if(serial232Test != null){
            if(serial232Test.getPosition() == currentTest){
                serial232Test.finish();
                linearLayout.removeView(serial232Test.getLineout());
                debug.logd("移除232串口测试布局");
            }
        }
        if(serial485Odd != null){
            if(serial485Odd.getPosition() == currentTest){
                serial485Odd.finish();
                linearLayout.removeView(serial485Odd.getLineout());
                debug.logd("移除奇数485测试布局");
            }
        }
        if(serial485Even != null){
            if(serial485Even.getPosition() == currentTest){
                serial485Even.finish();
                linearLayout.removeView(serial485Even.getScrollView());
                debug.logd("移除偶数485测试布局");
            }
        }
        if(usbTest != null){
            if(usbTest.getPosition() == currentTest){
                usbTest.finish();
                linearLayout.removeView(usbTest.getLineout());
                debug.logd("移除USB测试布局");
            }
        }
        if(gpioTest != null){
            if(gpioTest.getPosition() == currentTest){
                gpioTest.finish();
                linearLayout.removeView(gpioTest.getScrollView());
                debug.logd("移除GPIO测试布局");
            }
        }
        if(screenTets != null){
            if(screenTets.getPosition() == currentTest){
                screenTets.finish();
                linearLayout.removeView(screenTets.getLineout());
                debug.logd("移除屏幕测试布局");
            }
        }
        if(touchTest != null){
            if(touchTest.getPosition() == currentTest){
                touchTest.finish();
                linearLayout.removeView(touchTest.getLineout());
                debug.logd("移除触摸测试布局");
            }
        }
        if(mobileNet != null){
            if(mobileNet.getPosition() == currentTest){
                mobileNet.finish();
                linearLayout.removeView(mobileNet.getLineout());
                debug.logd("移除移动网络测试布局");
            }
        }
        if(gpsTest != null){
            if(gpsTest.getPosition() == currentTest){
                gpsTest.finish();
                linearLayout.removeView(gpsTest.getLineout());
                debug.logd("移除GPS测试布局");
            }
        }
        if(rtcTest != null){
            if(rtcTest.getPosition() == currentTest){
                rtcTest.finish();
                linearLayout.removeView(rtcTest.getScrollView());
                debug.logd("移除RTC测试布局");
            }
        }
        if(tfcardTest != null){
            if(tfcardTest.getPosition() == currentTest){
                tfcardTest.finish();
                linearLayout.removeView(tfcardTest.getLineout());
                debug.logd("移除TF卡测试布局");
            }
        }
    }



    private void initAllTestProject(Context context){
        int i;
        for (i = 0; i< planTest.planStep.length; i++) {
            String string = planTest.planStep[i];
            if (string.equals(PreMachineInfo.SPEAK_TEST)) {
                speakTest = new Speak(context,"外放测试是否通过",handler);
                speakTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.HEADPHONE_TEST)) {
                headhhoneTest = new Headhhone(context, "耳机输出测试是否通过", handler);
                headhhoneTest.setPosition(i);
            }else if(string.equals(PreMachineInfo.CAMERA_ONE_TEST)){
                cameraOne = new CameraOne(context,"摄像头测试",handler);
                cameraOne.setPosition(i);
            } else if (string.equals(PreMachineInfo.RECORD_TEST)) {
                recordTest = new Record(context,"录音测试是否通过",handler);
                recordTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.WFIF_TEST)) {
                wifiTest = new Wifi(context,"WIFI 测试",handler);
                wifiTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.BLUETOOTH_TEST)) {
                blueToothTest = new BlueTooth(context,"蓝牙测试",handler);
                blueToothTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.MOBILE_NET_TEST)) {
                mobileNet = new MobileNet(context, "移动网络测试", handler);
                mobileNet.setPosition(i);
            } else if (string.endsWith(PreMachineInfo.GPS_TEST)){
                gpsTest = new GPS(context,"GPS功能测试",handler);
                gpsTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.ETH_A33_TEST) ||
                    string.equals(PreMachineInfo.ETH_H3_TEST) ||
                    string.equals(PreMachineInfo.ETH_RK3288_1_TEST) ||
                    string.equals(PreMachineInfo.ETH_RK3288_2_TEST) ||
                    string.equals(PreMachineInfo.ETH_RK3368_TEST)) {
                ethTest = new EthR(context,string,handler);
                ethTest.setPosition(i);
            }  else if (string.equals(PreMachineInfo.RS232_2_TEST) ||
                    string.equals(PreMachineInfo.RS232_3_TEST) ||
                    string.equals(PreMachineInfo.RS232_4_TEST) ) {
                serial232Test = new Serial232Control(context,-1,handler);
                serial232Test.setPosition(i);
            }  else if (string.equals(PreMachineInfo.RS485_1_TEST)) {
                serial485Odd = new Serial485Odd(context,"485测试是否通过",handler);
                serial485Odd.setPosition(i);
            } else if (string.equals(PreMachineInfo.RS485_2_TEST)) {
                serial485Even = new Serial485Even(context,"485测试",handler);
                serial485Even.setPosition(i);
            } else if (string.equals(PreMachineInfo.USB_TEST)) {
                usbTest = new USB(context,"USB测试是否通过",handler);
                usbTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.SCREEN_TEST)) {
                screenTets = new Screen(context,"屏幕测试是否通过",handler);
                screenTets.setPosition(i);
            } else if (string.equals(PreMachineInfo.TOUCH_SCREEN_TEST)) {
                touchTest = new Touch(context,"触摸测试是否通过",handler);
                touchTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.GPIO_A33_TEST)
                    || string.equals(PreMachineInfo.GPIO_RK3288_TEST)) {
                gpioTest = new GPIO(context,"GPIO测试",handler);
                gpioTest.setPosition(i);
            } else if (string.equals(PreMachineInfo.RTC_TEST)) {
                rtcTest = new RTC(context,"RTC测试",handler);
                rtcTest.setPosition(i);
            } else if(string.equals(PreMachineInfo.TFCARD_TEST)){
                tfcardTest = new TFCARD(context,"TF卡测试",handler);
                tfcardTest.setPosition(i);
            }
        }
        debug.logw("初始化完成");
    }


    private void syncStepView(int i){
        int j;
        for(j = 0; j < testProject.length; j++){
            if(j == i)
                testProject[j].setBackgroundColor(Color.BLUE);
            else
                testProject[j].setBackgroundColor(Color.WHITE);
        }

    }


    /*
    初始化，添加测试步骤布局
     */
    private void addStepView(LinearLayout layout,Context context){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);

        testProject = new TextView[planTest.planStep.length];
        int i;
        for(String string: planTest.planStep)
            debug.loge(string);
        debug.loge(String.valueOf(planTest.planStep.length));
        debug.logw(MachineInfoData.getMachineInfoData().showConfig()); //显示所有配置
        for(i = 0; i < planTest.planStep.length; i++){
            testProject[i] = new TextView(context);
            testProject[i].setText(planTest.planStep[i]);
            testProject[i].setLayoutParams(layoutParams);
            testProject[i].setPadding(padding,padding,padding,padding);
            testProject[i].setTextSize(waitSize);
            layout.addView(testProject[i]);
        }
        currentPosition = 0;
        syncStepView(currentPosition);
    }

    /*
    双击退出
     */
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 释放所有在用资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(speakTest != null) {
            if(currentPosition == speakTest.getPosition())
                speakTest.finish();
        }
        if( headhhoneTest != null) {
            if(currentPosition == headhhoneTest.getPosition())
                headhhoneTest.finish();
        }
        if(cameraOne != null){
            if(currentPosition == cameraOne.getPosition()){
                cameraOne.finish();
            }
        }
        if(ethTest != null) {
            if(currentPosition == ethTest.getPosition())
                ethTest.finish();
        }
        if( wifiTest != null) {
            if(currentPosition == wifiTest.getPosition()) {
                wifiTest._finish(this.getApplicationContext());
            }
        }
        if( serial232Test != null){
            if(currentPosition == serial232Test.getPosition())
                serial232Test.finish();
        }
        if( serial485Odd != null){
            if(currentPosition == serial485Odd.getPosition())
                serial485Odd.finish();
        }
        if( serial485Even != null){
            if(currentPosition == serial485Even.getPosition())
                serial485Even.finish();
        }
        if( usbTest != null){
            if(currentPosition == usbTest.getPosition())
                usbTest.finish();
        }
        if(gpioTest != null){
            if(currentPosition == gpioTest.getPosition())
                gpioTest.finish();
        }
        if(screenTets != null){
            if(currentPosition == screenTets.getPosition())
                screenTets.finish();
        }
        if(touchTest != null){
            if(currentPosition == touchTest.getPosition())
                touchTest.finish();
        }
        if(mobileNet != null){
            if(currentPosition == mobileNet.getPosition())
                mobileNet.finish();
        }
        if(gpsTest != null){
            if(currentPosition == gpsTest.getPosition()){
                gpsTest.finish();
            }
        }
        if(rtcTest != null){
            if(currentPosition == rtcTest.getPosition())
                rtcTest.finish();
        }
        if(tfcardTest != null){
            if(currentPosition == tfcardTest.getPosition()){
                tfcardTest.finish();
            }
        }


        if(result != null)
            result.finish();
        if(planTest != null)
            planTest.finish();
        MachineInfoData.getMachineInfoData().finish();

    }
}
