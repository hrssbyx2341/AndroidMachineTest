package com.example.x6.androidmachinetest.function;

import android.content.Context;
import android.provider.Settings;

import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.Data.PreData;

/**
 * Created by 24179 on 2018/7/17.
 */

public class EthControl {
    private Debug debug = new Debug("EthControl");
    private boolean isOutTime = true;
    private SuCommand suCommand = new SuCommand();
    private MachineInfoData machineInfoData =MachineInfoData.getMachineInfoData();
    public EthControl(){

    }
    public boolean ethClose(){
        boolean result = true;
        if(machineInfoData.cpuName != null){
            String cmd = "";
            if(machineInfoData.cpuName.equals(PreMachineInfo.A33)){
                debug.logd("CPU型号A33");
                cmd = "netcfg "+machineInfoData.ETH_A33+" down";
                if(0 != suCommand.execRootCmdSilent(cmd)) {
                    debug.loge("A33关闭以太网失败");
                    result = false;
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.H3)){
                debug.logd("CPU型号H3");
                cmd = "netcfg "+machineInfoData.ETH_H3+" down";
                if(0 != suCommand.execRootCmdSilent(cmd)) {
                    result = false;
                    debug.loge("H3关闭以太网失败");
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.RK3368)){
                debug.logd("CPU型号RK3368");
                cmd = "netcfg "+machineInfoData.ETH_RK3368+" down";
                if(0 != suCommand.execRootCmdSilent(cmd)) {
                    result = false;
                    debug.loge("RK3368关闭以太网失败");
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.RK3288)){
                debug.logd("CPU型号是RK3288");
                if(machineInfoData.ETH_RK3288_2 == null && machineInfoData.ETH_RK3288_1 != null){
                    debug.logd("设备型号是B601");
                    cmd = "netcfg "+machineInfoData.ETH_RK3288_1+" down";
                    if(0 != suCommand.execRootCmdSilent(cmd)){
                        debug.loge("RK3288 B601 关闭以太网失败");
                        result = false;
                    }
                }else if(machineInfoData.ETH_RK3288_1 !=null && machineInfoData.ETH_RK3288_2 !=null){
                    debug.logd("设备型号是B701");
                    cmd = "netcfg "+machineInfoData.ETH_RK3288_1+" down";
                    if(0 != suCommand.execRootCmdSilent(cmd)) {
                        debug.loge("RK3288 B701 关闭以太网1失败");
                        return  false;
                    }
                    cmd = "netcfg "+machineInfoData.ETH_RK3288_2+" down";
                    if(0 != suCommand.execRootCmdSilent(cmd)){
                        debug.loge("RK3288 B701 关闭以太网2失败");
                        return false;
                    }
                }
            }
        }else{
            result = false;
            debug.loge("没有设备CPU型号？");
        }
        return result;
    }


    public boolean ethOpen(){
        boolean result = true;
        if(machineInfoData.cpuName != null){
            String cmd = "";
            if(machineInfoData.cpuName.equals(PreMachineInfo.A33)){
                debug.logd("CPU型号A33");
                cmd = "netcfg "+machineInfoData.ETH_A33+" up";
                if(0 != suCommand.execRootCmdSilent(cmd)) {
                    debug.loge("A33启动以太网失败");
                    result = false;
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.H3)){
                debug.logd("CPU型号H3");
                cmd = "netcfg "+machineInfoData.ETH_H3+" up";
                if(0 != suCommand.execRootCmdSilent(cmd)) {
                    result = false;
                    debug.loge("H3启动以太网失败");
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.RK3368)){
                debug.logd("CPU型号RK3368");
                cmd = "netcfg "+machineInfoData.ETH_RK3368+" up";
                if(0 != suCommand.execRootCmdSilent(cmd)) {
                    result = false;
                    debug.loge("RK3368启动以太网失败");
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.RK3288)){
                debug.logd("CPU型号是RK3288");
                if(machineInfoData.ETH_RK3288_2 == null && machineInfoData.ETH_RK3288_1 != null){
                    debug.logd("设备型号是B601");
                    cmd = "netcfg "+machineInfoData.ETH_RK3288_1+" up";
                    if(0 != suCommand.execRootCmdSilent(cmd)){
                        debug.loge("RK3288 B601 启动以太网失败");
                        result = false;
                    }
                }else if(machineInfoData.ETH_RK3288_1 !=null && machineInfoData.ETH_RK3288_2 !=null){
                    debug.logd("设备型号是B701");
                    cmd = "netcfg "+machineInfoData.ETH_RK3288_1+" up";
                    if(0 != suCommand.execRootCmdSilent(cmd)) {
                        debug.loge("RK3288 B701 启动以太网1失败");
                        return  false;
                    }
                    cmd = "netcfg "+machineInfoData.ETH_RK3288_2+" up";
                    if(0 != suCommand.execRootCmdSilent(cmd)){
                        debug.loge("RK3288 B701 启动以太网2失败");
                        return false;
                    }
                }
            }
        }else{
            result = false;
            debug.loge("没有设备CPU型号？");
        }
        return result;
    }



    public final static int ETH_A33_SUCESSFUL = 1;
    public final static int ETH_A33_UP_FAILED = 2;
    public final static int ETH_A33_DHCP_FAILED = 3;
    public final static int ETH_A33_PING_FAILED = 4;

    public final static int ETH_H3_SUCESSFUL = 5;
    public final static int ETH_H3_UP_FAILED = 6;
    public final static int ETH_H3_PING_FAILED =7;

    public final static int ETH_RK3288_1_SUCESSFUL = 8;
    public final static int ETH_RK3288_1_UP_FAILED = 9;
    public final static int ETH_RK3288_1_PING_FAILED = 10;
    public final static int ETH_RK3288_2_SUCESSFUL = 11;
    public final static int ETH_RK3288_2_UP_FAILED = 12;
    public final static int ETH_RK3288_2_PING_FAILED = 13;

    public final static int ETH_RK3368_SUCESSFUL = 14;
    public final static int ETH_RK3368_UP_FAILED = 15;
    public final static int ETH_RK3368_PING_FAILED = 16;

    public final static int ETH_H3_DOWN_FAILED = 17;
    public final static int ETH_RK3288_1_DOWN_FAILED = 18;
    public final static int ETH_RK3288_2_DOWN_FAILED = 19;
    public final static int ETH_RK3368_DOWN_FAILED = 20;

    public int ethTest(Context context){
        int result = -1;
        String cmd = "";
        /*
        关闭其他网络
         */
        WifiControl wifiControl = new WifiControl(context);
        wifiControl.WifiClose();
        MobileNetControl mobileNetControl = new MobileNetControl();
        mobileNetControl.disableData();
        try {
            Thread.sleep(1*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(machineInfoData.cpuName != null){
            /*****A33 ETH TEST******/
            if(machineInfoData.cpuName.equals(PreMachineInfo.A33)) {
                if (machineInfoData.ETH_A33 != null){
                    cmd = "netcfg " + machineInfoData.ETH_A33 + " up";
                if (0 == suCommand.execRootCmdSilent(cmd)) {
                    cmd = "netcfg " + machineInfoData.ETH_A33 + " dhcp";
                    isOutTime = true;
                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                        isOutTime = false;
                        cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_A33;
                        if (0 == suCommand.execRootCmdSilent(cmd)) {
                            result = ETH_A33_SUCESSFUL;
                        }else{
                            result = ETH_A33_PING_FAILED;
                        }
                    }else{
                        result = ETH_A33_DHCP_FAILED;
                    }
                }else{
                    result = ETH_A33_UP_FAILED;
                }
            }
            /*****A33 ETH TEST******/
            /*****H3 ETH TEST*******/
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.H3)){
                if(machineInfoData.ETH_H3 != null) {
                    cmd = "netcfg " + machineInfoData.ETH_H3 + " down"; //先把网口down掉，再up起来，作为一个触发
                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                        cmd = "netcfg " + machineInfoData.ETH_H3 + " up";
                        if (0 == suCommand.execRootCmdSilent(cmd)) {
                            cmd = "netcfg " + machineInfoData.ETH_H3 + " dhcp";
                            if(0 == suCommand.execRootCmdSilent(cmd)) {
                                try {
                                    Thread.sleep(6 * 1000);//此处延时10 s 作为dhcp ，如果10s还无法拨号成功，则拨号失败
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_H3;
                                if (0 == suCommand.execRootCmdSilent(cmd)) {
                                    result = ETH_H3_SUCESSFUL;
                                } else {
                                    result = ETH_H3_PING_FAILED;
                                }
                            }else{
                                result = ETH_H3_UP_FAILED;
                            }
                        }else{
                            result = ETH_H3_UP_FAILED;
                        }
                    }else{
                        result = ETH_H3_DOWN_FAILED;
                    }
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.RK3288)){ //这里测试需要把两个网口都插上网线
                if(machineInfoData.ETH_RK3288_2 != null && machineInfoData.ETH_RK3288_1 != null){
                    /*
                    这里测试网口1
                     */
                    cmd = "netcfg " + machineInfoData.ETH_RK3288_1 + " down"; //先把网口down掉，再up起来，作为一个触发
                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                        cmd = "netcfg " + machineInfoData.ETH_RK3288_1 + " up";
                        if (0 == suCommand.execRootCmdSilent(cmd)) {
                            try {
                                Thread.sleep(6 * 1000);//此处延时10 s 作为dhcp ，如果10s还无法拨号成功，则拨号失败
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_RK3288_1;
                            if (0 == suCommand.execRootCmdSilent(cmd)) {
                                /*
                                这里测试网口2
                                 */
                                cmd = "netcfg " + machineInfoData.ETH_RK3288_2 + " down"; //先把网口down掉，再up起来，作为一个触发
                                if (0 == suCommand.execRootCmdSilent(cmd)) {
                                    cmd = "netcfg " + machineInfoData.ETH_RK3288_2 + " up";
                                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                                        try {
                                            Thread.sleep(10 * 1000);//此处延时10 s 作为dhcp ，如果10s还无法拨号成功，则拨号失败
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_RK3288_2;
                                        if (0 == suCommand.execRootCmdSilent(cmd)) {
                                            result = ETH_RK3288_2_SUCESSFUL;
                                        }else{
                                            result = ETH_RK3288_2_PING_FAILED;
                                        }
                                    }else{
                                        result = ETH_RK3288_2_UP_FAILED;
                                    }
                                }else{
                                    result = ETH_RK3288_2_DOWN_FAILED;
                                }
                            }else{
                                result = ETH_RK3288_1_PING_FAILED;
                            }
                        }else{
                            result = ETH_RK3288_1_UP_FAILED;
                        }
                    }else{
                        result = ETH_RK3288_1_DOWN_FAILED;
                    }
                }
                if(machineInfoData.ETH_RK3288_2 == null && machineInfoData.ETH_RK3288_1 != null){ //B601
                    /*
                    这里测试网口1
                     */
                    cmd = "netcfg " + machineInfoData.ETH_RK3288_1 + " down"; //先把网口down掉，再up起来，作为一个触发
                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                        cmd = "netcfg " + machineInfoData.ETH_RK3288_1 + " up";
                        if (0 == suCommand.execRootCmdSilent(cmd)) {
                            try {
                                Thread.sleep(6 * 1000);//此处延时10 s 作为dhcp ，如果10s还无法拨号成功，则拨号失败
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_RK3288_1;
                            if (0 == suCommand.execRootCmdSilent(cmd)) {
                                result = ETH_RK3288_1_SUCESSFUL;
                            }else{
                                result = ETH_RK3288_1_PING_FAILED;
                            }
                        }else{
                            result = ETH_RK3288_1_UP_FAILED;
                        }
                    }else{
                        result = ETH_RK3288_1_DOWN_FAILED;
                    }
                }
            }else if(machineInfoData.cpuName.equals(PreMachineInfo.RK3368)){
                /*
                这里测试网口1
                 */
                cmd = "netcfg " + machineInfoData.ETH_RK3368 + " down"; //先把网口down掉，再up起来，作为一个触发
                if (0 == suCommand.execRootCmdSilent(cmd)) {
                    cmd = "netcfg " + machineInfoData.ETH_RK3368 + " up";
                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                        try {
                            Thread.sleep(10 * 1000);//此处延时10 s 作为dhcp ，如果10s还无法拨号成功，则拨号失败
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_RK3368;
                        if (0 == suCommand.execRootCmdSilent(cmd)) {
                            result = ETH_RK3288_1_SUCESSFUL;
                        }else{
                            result = ETH_RK3288_1_PING_FAILED;
                        }
                    }else{
                        result = ETH_RK3288_1_UP_FAILED;
                    }
                }else{
                    result = ETH_RK3288_1_DOWN_FAILED;
                }
            }
        }
        return  result;
    }
}
