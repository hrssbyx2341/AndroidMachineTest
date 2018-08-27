package com.example.x6.androidmachinetest.Core;

import android.os.Environment;

import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.ExcelUtils;
import com.example.x6.androidmachinetest.function.SuCommand;

import java.io.File;

/**
 * Created by 24179 on 2018/7/11.
 */

public class Result {
    private static Result result = null;
    private Debug debug;
    private static String testReporterPtah = "/sdcard/TestResult/";
    private MachineInfoData machineInfoData;


    private Result(){
        debug = new Debug("Result");
        machineInfoData = MachineInfoData.getMachineInfoData();
    }
    public static Result getResult(){
        synchronized (Result.class){
            if(result == null)
                result = new Result();
        }
        return result;
    }

    public String getTestReporterPtah(){
        touchExceDir(); //生成目录
        return testReporterPtah;
    }
    private String touchExceDir(){
        File dir = new File(testReporterPtah);
        if(dir.exists())
            return dir.toString();
        else {
            dir.mkdirs();
            debug.loge("保存路径不存在");
            return dir.toString();
        }
    }

    /**
     * 保存结果，这个方法只能在最后RTC测试的时候调用
     * @return
     */
    public boolean saveResult(String filePath){
        boolean isOk = true;
        File file = new File(filePath);
        if(!file.exists()){
            debug.loge("表格文件不存在");
            return false;
        }
        ExcelUtils excelUtils = new ExcelUtils();

        if(!excelUtils.writeToExcel(filePath,"设备型号",machineInfoData.typeName))
            isOk = false;
        if(!excelUtils.writeToExcel(filePath,"设备识别号",machineInfoData.UUID))
            isOk = false;
        if(!excelUtils.writeToExcel(filePath,"CPU_ABI",android.os.Build.CPU_ABI))
            isOk = false;
        if(!excelUtils.writeToExcel(filePath,"Android版本",android.os.Build.VERSION.RELEASE))
            isOk = false;
        if(!excelUtils.writeToExcel(filePath,"固件更新日期",android.os.Build.VERSION.INCREMENTAL))
            isOk = false;
        if(!excelUtils.writeToExcel(filePath,"SDK",android.os.Build.VERSION.SDK_INT))
            isOk = false;
        if(!excelUtils.writeToExcel(filePath,"内核版本",new GetMachineInfo().getLinuxCore_Ver()))
            isOk = false;
        if(speakTestResult != null)
            if(!excelUtils.writeToExcel(filePath,"功放喇叭",speakTestResult))
                isOk = false;
        if(headPhoneTestResult != null)
            if(!excelUtils.writeToExcel(filePath,"耳机接口",headPhoneTestResult))
                isOk = false;
        if(recordTest != null)
           if(!excelUtils.writeToExcel(filePath,"录音功能",recordTest))
               isOk = false;
        if(wifiTest != null)
            if(!excelUtils.writeToExcel(filePath,"Wifi功能",wifiTest))
                isOk = false;
        if(blueToothTest != null)
            if(!excelUtils.writeToExcel(filePath,"蓝牙功能",blueToothTest))
                isOk = false;
        if(mobileNetTest !=null)
            if(!excelUtils.writeToExcel(filePath,"移动网络",mobileNetTest))
                isOk = false;
        if(gpsTest != null)
            if(!excelUtils.writeToExcel(filePath,"GPS功能",gpsTest))
                isOk = false;
        if(ethTest != null)
            if(!excelUtils.writeToExcel(filePath,"以太网",ethTest))
                isOk = false;
        if(RS232Test !=  null && RS232ReMark != null)
            if(!excelUtils.writeToExcel(filePath,"232串口",RS232Test,RS232ReMark))
                isOk = false;
        if(RS232Test !=  null && RS232ReMark == null)
            if(!excelUtils.writeToExcel(filePath,"232串口",RS232Test))
                isOk = false;
        if(RS485Test != null && RS485ReMark != null)
            if(!excelUtils.writeToExcel(filePath,"485串口",RS485Test,RS485ReMark))
                isOk = false;
        if(RS485Test != null && RS485ReMark == null)
            if(!excelUtils.writeToExcel(filePath,"485串口",RS485Test))
                isOk = false;
        if(USBTest != null)
            if(!excelUtils.writeToExcel(filePath,"USB功能",USBTest))
                isOk = false;
        if(TFCARDTest != null)
            if(!excelUtils.writeToExcel(filePath,"TF卡功能",TFCARDTest))
                isOk = false;
        if(ScreenTest != null)
            if(!excelUtils.writeToExcel(filePath,"屏幕显示",ScreenTest))
                isOk = false;
        if(TouchScreenTest != null)
            if(!excelUtils.writeToExcel(filePath,"触摸功能",TouchScreenTest))
                isOk = false;
        if(GpioTest != null)
            if(!excelUtils.writeToExcel(filePath,"GPIO接口",GpioTest))
                isOk = false;
        if(GpioReMark != null){}
        if(0 != new SuCommand().execRootCmdSilent("sync")) //确保文件刷入磁盘里面
            isOk = false;
        return isOk;
    }

    public String speakTestResult = null;
    public String headPhoneTestResult = null;
    public String recordTest = null;
    public String wifiTest = null;
    public String blueToothTest = null;
    public String mobileNetTest = null;
    public String gpsTest = null;

    public String ethTest = null;
    public String ethTestReMark = null;

    public String RS232Test = null;
    public String RS232ReMark = null;

    public String RS485Test = null;
    public String RS485ReMark = null;

    public String USBTest = null;
    public String ScreenTest = null;
    public String TouchScreenTest = null;

    public String GpioTest = null;
    public String GpioReMark = null;

    public String RtcTest = null;
    public String TFCARDTest = null;


    public String showResult(){
        String result = "";
        if(speakTestResult != null)
            result+="外放测试："+speakTestResult+"\n";
        if(headPhoneTestResult != null)
            result+="耳机测试："+headPhoneTestResult+"\n";
        if(recordTest != null)
            result+="录音测试："+recordTest+"\n";
        if(wifiTest != null)
            result+="Wifi测试："+wifiTest+"\n";
        if(blueToothTest != null)
            result +="蓝牙测试："+blueToothTest+"\n";
        if(mobileNetTest !=null)
            result+="移动网络测试："+mobileNetTest+"\n";
        if(gpsTest !=null)
            result+="GPS功能测试："+gpsTest+"\n";
        if(ethTest != null)
            result+="以太网测试："+ethTest+"\n";
        if(ethTestReMark != null)
            result+="以太网备注："+ethTestReMark+"\n";
        if(RS232Test != null)
            result+="232串口测试："+RS232Test+"\n";
        if(RS232ReMark != null)
            result+="RS232串口备注："+RS232ReMark+"\n";
        if(RS485Test != null)
            result+="485串口测试："+RS485Test+"\n";
        if(RS485ReMark != null)
            result+="RS485串口备注："+RS485ReMark+"\n";
        if(USBTest != null)
            result+="USB测试："+USBTest+"\n";
        if(TFCARDTest != null)
            result+="TF卡测试："+TFCARDTest+"\n";
        if(ScreenTest != null)
            result+= "屏幕测试："+ScreenTest+"\n";
        if(TouchScreenTest != null)
            result+= "触摸测试："+TouchScreenTest+"\n";
        if(GpioTest != null)
            result+="GPIO测试："+GpioTest+"\n";
        if(GpioReMark != null)
            result+="GPIO备注："+GpioReMark+"\n";
        return result;
    }


    public void finish(){
        speakTestResult = null;
        headPhoneTestResult = null;
        recordTest = null;
        wifiTest = null;
        blueToothTest = null;
        mobileNetTest = null;
        gpsTest = null;
        ethTest = null;
        ethTestReMark = null;
        RS232Test = null;
        RS232ReMark = null;
        RS485Test = null;
        RS485ReMark = null;
        USBTest = null;
        ScreenTest = null;
        TouchScreenTest = null;
        GpioTest = null;
        GpioReMark = null;
        RtcTest = null;
        TFCARDTest = null;
    }
}
