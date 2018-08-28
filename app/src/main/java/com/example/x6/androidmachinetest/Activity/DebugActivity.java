package com.example.x6.androidmachinetest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.x6.androidmachinetest.Activity.UI.Eth;
import com.example.x6.androidmachinetest.Activity.UI.GPIO;
import com.example.x6.androidmachinetest.Activity.UI.Wifi;
import com.example.x6.androidmachinetest.Core.GetMachineInfo;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.DataBase.TestDataBaseInfo;
import com.example.x6.androidmachinetest.DataBase.TestDataBaseUtils;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.EthControl;
import com.example.x6.androidmachinetest.function.ExcelUtils;
import com.example.x6.androidmachinetest.function.ScreenTestActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.zip.Inflater;




/**
 * Created by 24179 on 2018/7/13.
 */

public class DebugActivity extends Activity {
    private Debug debug;
//    private GPIO gpioTest;
//    private LinearLayout lineout;
////    private Eth ethTest;
//    private Handler handler = new Handler();
//    private Wifi wifiTest;
//    Handler handler = new Handler();
//    ExcelUtils excelUtils;


    private TestDataBaseUtils testDataBaseUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug = new Debug("测试界面");




        testDataBaseUtils = TestDataBaseUtils.getTestDataBaseUtils(this.getApplicationContext());
        testDataBaseUtils.updateType("B301");
        testDataBaseUtils.updateName("B301_TW99998888");
        testDataBaseUtils.updateCpuSerial("87689e7916e700000000");
        testDataBaseUtils.updateCpuType("H3");
        testDataBaseUtils.updateLastUpdateDate("2018-08-27");
        testDataBaseUtils.updateTestDateTime(System.currentTimeMillis());
        testDataBaseUtils.updateUuid("TW99998888");


//        testDataBaseUtils.updateSpk(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateHp(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateRecord(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateTF(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateUSB(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateEth(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateWifi(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateMobileNet(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateGPIO(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateGPS(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateBT(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateScreen(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateTS(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateRS485(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateRS232(TestDataBaseUtils.PASS);
//        testDataBaseUtils.updateRTC(TestDataBaseUtils.PASS);


        testDataBaseUtils.updateISPass();
        debug.loge(testDataBaseUtils.jsonEncodeFromDB());

        new Thread(new Runnable() {
            @Override
            public void run() {
                testDataBaseUtils.upLoad();
            }
        }).start();

//        lineout = new LinearLayout(this);
//        lineout.setOrientation(LinearLayout.VERTICAL);
////        ethTest = new Eth(DebugActivity.this,"以太网测试",handler);
//        wifiTest = new Wifi(this,"Wifi 测试",handler);
//        lineout.addView(wifiTest.getScrollView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        setContentView(lineout);
//        debug.logw(MachineInfoData.getMachineInfoData().showConfig());
//        wifiTest.startTest(this,handler);
//        long startTime = System.currentTimeMillis();
//        long endTime = startTime + 24*60*60*1000;
//        debug.loge(String .valueOf(startTime));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        Date date = new Date(startTime);
//        Date date1 = new Date(endTime);
//        debug.loge(simpleDateFormat.format(date));
//        debug.loge(simpleDateFormat.format(date1));


//        String phoneInfo = "";
//        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI + "\n";
//        phoneInfo += ", ANDROID.VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE+ "\n";
//        phoneInfo += ", INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL+ "\n";
//        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK_INT + "\n";
//        phoneInfo += ", KERNELVERSION:"+new GetMachineInfo().getLinuxCore_Ver();
//
//        debug.logw(phoneInfo);
//
//        String excelPtah = getExceDir()+ File.separator+"demo.xls";
//        debug.logd("当前文件："+excelPtah);
//        excelUtils = new ExcelUtils(excelPtah);
//        excelUtils.createExcel(new File(excelPtah));
//        excelUtils.writeToExcel("LED","PASS","N/A");
//        excelUtils.writeToExcel("背光","不通过","当前没有加背光测试");


    }

//    public String getExceDir(){
//        String sdCardPtah = Environment.getExternalStorageDirectory().toString();
//        File dir = new File(sdCardPtah+ File.separator +"Excel" + File.separator+"Person");
//        if(dir.exists())
//            return dir.toString();
//        else {
//            dir.mkdirs();
//            debug.loge("保存路径不存在");
//            return dir.toString();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MachineInfoData.getMachineInfoData().finish();
//        if(wifiTest != null){
//            wifiTest._finish(this);
//        }
    }
}
