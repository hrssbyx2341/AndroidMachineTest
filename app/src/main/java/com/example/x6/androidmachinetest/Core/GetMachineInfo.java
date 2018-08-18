package com.example.x6.androidmachinetest.Core;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.SuCommand;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 24179 on 2018/7/7.
 */

public class GetMachineInfo {
    private SuCommand suCommand;
    private final String TAG = "GetMachineInfo";
    private MachineInfoData machineInfoData = MachineInfoData.getMachineInfoData();

    Debug debug = new Debug(TAG);
    public GetMachineInfo(){
        suCommand = new SuCommand();
    };
    /**
     * 获取手机内部存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public  String getInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        return Formatter.formatFileSize(context, size);
    }
    /**
     * 获取手机内部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public  String getAvailableInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }
    /**
     * 获取手机外部存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public  String getExternalMemorySize(Context context) {
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return Formatter
                .formatFileSize(context, blockCountLong * blockSizeLong);
    }
    /**
     * 获取手机外部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public  String getAvailableExternalMemorySize(Context context) {
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }


    /**
     * 返回CPU型号名字，如果要添加新的平台此处需要作修改
     * @return
     */
    public String getCupName(){
        if(machineInfoData.cpuName == null){
            machineInfoData.cpuName = _getCupName();
        }
        return machineInfoData.cpuName;
    }

    private String _getCupName(){
        Vector<String> vector;
        String cmd = "cat /proc/cpuinfo";
        vector = suCommand.execRootCmd(cmd);
        String string = "";
        for (String str:vector){
            if(str.indexOf("Hardware") != -1) {
                string = str.substring(str.indexOf(":")).trim();
                break;
            }
        }
        if(string.indexOf("sun8i") != -1){
            if(Build.MODEL.equals("QUAD-CORE A33 y3") || Build.MODEL.equals("QUAD-CORE A33 d7")){
                string = PreMachineInfo.A33;
            }else if(Build.MODEL.equals("dolphin")){
                string = PreMachineInfo.H3;
            }
        }else if(string.indexOf("RK3288") != -1){
            string = PreMachineInfo.RK3288;
        }else if(string.indexOf("rk3368") != -1){
            string = PreMachineInfo.RK3368;
        }
        return string;
    }


    /**
     * 返回WFIF模块的型号，如果没有返回NULL 这里需要一个延时操作
     * @param context
     * @return
     * @throws InterruptedException
     */
    public String getWifiModuleName(Context context) throws InterruptedException {
        if (machineInfoData.wifiModule == null){
            String str = _getWifiModuleName(context);
            if(str == null){
                machineInfoData.wifiModule = PreMachineInfo.NOWIFI;
            }else{
                machineInfoData.wifiModule = str;
            }
        }
        return machineInfoData.wifiModule;
    }

    private String _getWifiModuleName(Context context) throws InterruptedException {
        String string = null;
        String cmd = "svc wifi enable";
        if (getCupName().equals(PreMachineInfo.RK3288)||getCupName().equals(PreMachineInfo.RK3368)){
            string = PreMachineInfo.AP6212A1;
            return string;
        }
        if(getCupName().equals(PreMachineInfo.A33)){
            Vector<String> vector;
            cmd = "busybox lsusb";
            vector = suCommand.execRootCmd(cmd);
            for(String str: vector){
                if(str.indexOf("0bda:8179") != -1){
                    string = PreMachineInfo.RTL8188EU;
                    break;
                }else if(str.indexOf("0bda:b720") != -1){
                    string = PreMachineInfo.RTL8723BU;
                    break;
                }
            }
        }
        if(getCupName().equals(PreMachineInfo.H3)){
            /*
        这里需要先打开wifi，因为H3平台必须打开wifi才能给模块供电
         */
            WifiManager mWifiManager = (WifiManager)
                    context.getSystemService(Context.WIFI_SERVICE);
            //调用WifiManager的setWifiEnabled方法设置wifi的打开或者关闭，只需把下面的state改为布尔值即可（true:打开 false:关闭）
            mWifiManager.setWifiEnabled(true);
            Thread.sleep(2*1000);

            Vector<String> vector;
            cmd = "busybox lsusb";
            vector = suCommand.execRootCmd(cmd);
            for(String str: vector){
                if(str.indexOf("0bda:8179") != -1){
                    string = PreMachineInfo.RTL8188EU;
                    break;
                }else if(str.indexOf("0bda:b720") != -1){
                    string = PreMachineInfo.RTL8723BU;
                    break;
                }
            }
        }
        return string;
    }

    /**
     * 返回4G模块型号，没有返回null
     * @return
     */
    public String getPhoneMoudle(){
        Vector<String> vector;
        String cmd = "busybox lsusb";
        String string = null;
        vector = suCommand.execRootCmd(cmd);
        for(String str: vector){
            if(str.indexOf("2c7c:0125") != -1){
                string = PreMachineInfo.EC20;
                break;
            }
        }
        return string;
    }


    public String getRAMSize(Context context){
        Vector<String> vector;
        String cmd = "cat /proc/meminfo";
        String sizeStr = "";
        vector = suCommand.execRootCmd(cmd);
        for(String str: vector){
            if(str.indexOf("MemTotal") != -1){
                String regex = "\\d*";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str);
                while (m.find()) {
                    sizeStr += m.group();
                }
                break;
            }
        }
        debug.logw(sizeStr);
        long size = Integer.parseInt(sizeStr);
        return Formatter
                .formatFileSize(context, size);
    }

    public void initMachineInfo(Context context) throws InterruptedException {
        getCupName();
        getWifiModuleName(context);
    }

    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */

    public String getBaseband_Ver(){
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] { String.class,String.class });
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
// System.out.println(">>>>>>><<<<<<<" +(String)result);
            Version = (String)result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CORE-VER
     * 内核版本
     * return String
     */

    public String getLinuxCore_Ver() {
        String result = "";
        String cmd = "cat /proc/version";
        Vector<String> vector = suCommand.execRootCmd(cmd);
        result = vector.get(0);
        return result;
    }
}
