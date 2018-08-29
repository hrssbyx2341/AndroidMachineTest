package com.example.x6.androidmachinetest.Core;

import android.content.Context;

import com.example.x6.androidmachinetest.function.Debug;

import java.util.Vector;

/**
 * Created by 24179 on 2018/7/7.
 */

public class PlanTest {
    private static PlanTest planTest = null;
    Debug debug = new Debug("PlanTest");
    private PlanTest(){

    }

    public static PlanTest getPlanTest(){
        synchronized (PlanTest.class){
            if (planTest == null)
                planTest = new PlanTest();
        }
        return planTest;
    }

    private MachineInfoData machineInfoData = MachineInfoData.getMachineInfoData();
    private GetMachineInfo getMachineInfo = new GetMachineInfo();


    /*************************************************************************************************************/
    /*      initMachineInfoData() 函数用于初始化数据，把要测试的项目写到一个集合里面，添加测试项目这里一定要加   */
    /*      setPlanStep() 函数获取添加好的集合，对测试项目进行一个计划排版，添加测试项目，这里也一定要改动       */
    /*************************************************************************************************************/

    public void initMachineInfoData(Context context,String typeName,String UUID,Boolean isRS485,Boolean isWifiTest,Boolean isPHONE){
        machineInfoData.cpuName = getMachineInfo.getCupName();
        machineInfoData.typeName = typeName;
        machineInfoData.UUID = UUID;
        machineInfoData.ISWIFI = isWifiTest;
        machineInfoData.ISPHONE = isPHONE;
        machineInfoData.RomSize = getMachineInfo.getExternalMemorySize(context);
        machineInfoData.RamSize = getMachineInfo.getRAMSize(context);
        try {
            machineInfoData.wifiModule = getMachineInfo.getWifiModuleName(context);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machineInfoData.mobileModule = getMachineInfo.getPhoneMoudle();
        machineInfoData.ISRS485 = isRS485;

        if(machineInfoData.typeName.equals(PreMachineInfo.Y7)||machineInfoData.typeName.equals(PreMachineInfo.Y8)
                ||machineInfoData.typeName.equals(PreMachineInfo.Y10)){
            /*
            串口必须从1开始赋值，不能1不赋值直接从2 赋值
             */
            machineInfoData.RS232_1 = PreMachineInfo.ttyS0;
            machineInfoData.RS232_2 = PreMachineInfo.ttyS1;
            machineInfoData.RS232_3 = PreMachineInfo.ttyS2;
            machineInfoData.RS232_4 = PreMachineInfo.ttyS3;
            machineInfoData.RS485_1 = PreMachineInfo.ttyS3;
            machineInfoData.ETH_A33 = PreMachineInfo.ETH1;
            machineInfoData.RECORD = true;
            machineInfoData.SPEAK = true;
            machineInfoData.HEADPHONE = true;
            machineInfoData.TFCARD = true;
            machineInfoData.USB = true;
            machineInfoData.SCREEN = true;
            machineInfoData.TOUCH_SCREEN = true;
            machineInfoData.RTC = true;
        }
        if(machineInfoData.typeName.equals(PreMachineInfo.Y5)){
            machineInfoData.RS232_1 = PreMachineInfo.ttyS1;
            machineInfoData.RS232_2 = PreMachineInfo.ttyS2;
            machineInfoData.RS485_1 = PreMachineInfo.ttyS3;
            machineInfoData.ETH_A33 = PreMachineInfo.ETH1;
            machineInfoData.SPEAK = true;
            machineInfoData.TFCARD = true;
            machineInfoData.USB = true;
            machineInfoData.SCREEN = true;
            machineInfoData.TOUCH_SCREEN = true;
            machineInfoData.RTC = true;
        }
        if(machineInfoData.typeName.equals(PreMachineInfo.K7)||machineInfoData.typeName.equals(PreMachineInfo.K10)){
            machineInfoData.RS232_1 = PreMachineInfo.ttyS0;
            machineInfoData.RS232_2 = PreMachineInfo.ttyS1;
            if(machineInfoData.ISRS485) {
                machineInfoData.RS485_1 = PreMachineInfo.ttyS2;
                machineInfoData.RS485_2 = PreMachineInfo.ttyS3;
            }else {
                machineInfoData.RS232_3 = PreMachineInfo.ttyS2;
                machineInfoData.RS232_4 = PreMachineInfo.ttyS3;
            }
            machineInfoData.ETH_A33 = PreMachineInfo.ETH1;
            machineInfoData.RECORD = true;
            machineInfoData.SPEAK = true;
//            machineInfoData.HEADPHONE = true;
            machineInfoData.TFCARD = true;
            machineInfoData.USB = true;
            machineInfoData.SCREEN = true;
            machineInfoData.TOUCH_SCREEN = true;
            machineInfoData.GPIONUM = 4;
            int[] gpio = new int[machineInfoData.GPIONUM];
            machineInfoData.GPIO = gpio;
            gpio[0] = ('E'-'A')*32 +2;//输入
            gpio[1] = ('E'-'A')*32 +0;//输入
            gpio[2] = ('E'-'A')*32 +3;//输出
            gpio[3] = ('E'-'A')*32 +1;//输出
            machineInfoData.GPIO = gpio;
            machineInfoData.RTC = true;
        }
        if(machineInfoData.typeName.equals(PreMachineInfo.B301) || machineInfoData.typeName.equals(PreMachineInfo.B301_LVDS)
                || machineInfoData.typeName.equals(PreMachineInfo.B301D_ELE01)){
            machineInfoData.RS232_1 = PreMachineInfo.ttyS0;
            machineInfoData.RS232_2 = PreMachineInfo.ttyS1;
            if(machineInfoData.ISRS485) {
                machineInfoData.RS485_1 = PreMachineInfo.ttyS2;
                machineInfoData.RS485_2 = PreMachineInfo.ttyS3;
            }else {
                machineInfoData.RS232_3 = PreMachineInfo.ttyS2;
                machineInfoData.RS232_4 = PreMachineInfo.ttyS3;
            }
            machineInfoData.ETH_H3 = PreMachineInfo.ETH0;
            machineInfoData.RECORD = true;
            machineInfoData.SPEAK = true;
            machineInfoData.HEADPHONE = true;
            machineInfoData.TFCARD = true;
            machineInfoData.USB = true;
            if(machineInfoData.typeName.equals(PreMachineInfo.B301_LVDS)){
                machineInfoData.SCREEN = true;
                machineInfoData.TOUCH_SCREEN = true;
            }
            if(machineInfoData.typeName.equals(PreMachineInfo.B301D_ELE01)){
                machineInfoData.SCREEN = true;
                machineInfoData.CAMERA_ONE = true;
            }
            machineInfoData.RTC = true;
        }
        if(machineInfoData.typeName.equals(PreMachineInfo.B701)||machineInfoData.typeName.equals(PreMachineInfo.B601)){
            machineInfoData.RS232_1 = PreMachineInfo.ttyS1;
            machineInfoData.RS232_2 = PreMachineInfo.ttyS2;
            if(isRS485){
                machineInfoData.RS485_1 = PreMachineInfo.ttyS3;
                machineInfoData.RS485_2 = PreMachineInfo.ttyS4;
            }else{
                machineInfoData.RS232_3 = PreMachineInfo.ttyS3;
                machineInfoData.RS232_4 = PreMachineInfo.ttyS4;
            }
            if(machineInfoData.typeName.equals(PreMachineInfo.B701)){
                machineInfoData.ETH_RK3288_1 = PreMachineInfo.ETH0;
                machineInfoData.ETH_RK3288_2 = PreMachineInfo.ETH1;
            }
            if(machineInfoData.typeName.equals(PreMachineInfo.B601)){
                machineInfoData.ETH_RK3288_1 = PreMachineInfo.ETH0;//等B601出来之后再更改
            }
            machineInfoData.RECORD = true;
            machineInfoData.SPEAK = true;
            machineInfoData.HEADPHONE = true;
            machineInfoData.TFCARD = true;
            machineInfoData.USB = true;
            /*
            这里如果需要测试屏幕和触摸就把注释打开
             */
//            machineInfoData.SCREEN = true;
//            machineInfoData.TOUCH_SCREEN = true;

            machineInfoData.GPIONUM = 4;
            int[] gpio = new int[machineInfoData.GPIONUM];
            gpio[0] = 32 +26;//输入
            gpio[1] = 32 +24;//输入
            gpio[2] = 32 +27;//输出
            gpio[3] = 32 +25;//输出
            machineInfoData.GPIO = gpio;
            machineInfoData.RTC = true;
        }
    }


    public String[] planStep = null;
    public void setPlanStep(){
        Vector<String> vector = new Vector<>();
        if(machineInfoData.SPEAK)
            vector.add(PreMachineInfo.SPEAK_TEST);
        if(machineInfoData.HEADPHONE)
            vector.add(PreMachineInfo.HEADPHONE_TEST);
        if(machineInfoData.CAMERA_ONE)
            vector.add(PreMachineInfo.CAMERA_ONE_TEST);
        if(machineInfoData.RECORD)
            vector.add(PreMachineInfo.RECORD_TEST);
        if(machineInfoData.wifiModule != null ) {
            if (machineInfoData.wifiModule.equals(PreMachineInfo.RTL8188EU) ||
                    machineInfoData.wifiModule.equals(PreMachineInfo.RTL8723BU) ||
                    machineInfoData.wifiModule.equals(PreMachineInfo.AP6212A1))
                vector.add(PreMachineInfo.WFIF_TEST);
            if (machineInfoData.wifiModule.equals(PreMachineInfo.AP6212A1) ||
                    machineInfoData.wifiModule.equals(PreMachineInfo.RTL8723BU))
                vector.add(PreMachineInfo.BLUETOOTH_TEST);
        }
//        if((machineInfoData.wifiModule.equals(PreMachineInfo.NOWIFI) && machineInfoData.ISWIFI)||
//                (!machineInfoData.wifiModule.equals(PreMachineInfo.NOWIFI)) && (!machineInfoData.ISWIFI)){
//            vector.add(PreMachineInfo.ERROR_WIFI_TEST);
//        }
        if(machineInfoData.mobileModule != null )
            if(machineInfoData.mobileModule.equals(PreMachineInfo.EC20)) {
                vector.add(PreMachineInfo.MOBILE_NET_TEST);
                vector.add(PreMachineInfo.GPS_TEST); //添加GPS测试
            }
//        if((machineInfoData.mobileModule == null && machineInfoData.ISPHONE)||
//                (machineInfoData.mobileModule != null) && (!machineInfoData.ISPHONE))
//            vector.add(PreMachineInfo.ERROR_PHONE_TEST);
        if(machineInfoData.ETH_A33 != null)
            vector.add(PreMachineInfo.ETH_A33_TEST);
        if(machineInfoData.ETH_H3 != null)
            vector.add(PreMachineInfo.ETH_H3_TEST);
        if(machineInfoData.ETH_RK3288_1 != null && machineInfoData.ETH_RK3288_2 != null)
            vector.add(PreMachineInfo.ETH_RK3288_2_TEST);
        if(machineInfoData.ETH_RK3288_1 != null && machineInfoData.ETH_RK3288_2 == null)
            vector.add(PreMachineInfo.ETH_RK3288_1_TEST);
        if(machineInfoData.ETH_RK3368 != null)
            vector.add(PreMachineInfo.ETH_RK3368_TEST);

        String rs232num = "";
        if(machineInfoData.RS232_1 != null) {
            if(machineInfoData.RS232_2 != null){
                rs232num = PreMachineInfo.RS232_2_TEST;
                if(machineInfoData.RS232_3 != null){
                    rs232num = PreMachineInfo.RS232_3_TEST;
                    if(machineInfoData.RS232_4 != null){
                        rs232num = PreMachineInfo.RS232_4_TEST;
                    }
                }
            }
        }
        if(!rs232num.equals(""))
        vector.add(rs232num);
        String rs485num = "";
        if(machineInfoData.RS485_1 != null){
            rs485num = PreMachineInfo.RS485_1_TEST;
            if(machineInfoData.RS485_2 != null){
                rs485num = PreMachineInfo.RS485_2_TEST;
            }
        }
        if(!rs485num.equals(""))
            vector.add(rs485num);

        if(machineInfoData.USB)
            vector.add(PreMachineInfo.USB_TEST);
        if(machineInfoData.TFCARD)
            vector.add(PreMachineInfo.TFCARD_TEST);
        if(machineInfoData.SCREEN)
            vector.add(PreMachineInfo.SCREEN_TEST);
        if(machineInfoData.TOUCH_SCREEN)
            vector.add(PreMachineInfo.TOUCH_SCREEN_TEST);
        if(machineInfoData.cpuName != null) {
            if (machineInfoData.GPIONUM != -1 && machineInfoData.cpuName.equals(PreMachineInfo.A33))
                vector.add(PreMachineInfo.GPIO_A33_TEST);
            if (machineInfoData.GPIONUM != -1 && machineInfoData.cpuName.equals(PreMachineInfo.RK3288))
                vector.add(PreMachineInfo.GPIO_RK3288_TEST);
        }
        if(machineInfoData.RTC)
            vector.add(PreMachineInfo.RTC_TEST);

        String[] tempStrings = new String[vector.size()];
        int i;
        for(i = 0; i < vector.size(); i++) {
            tempStrings[i] = vector.get(i);
        }
        planStep = tempStrings;
    }
    public void finish(){
        machineInfoData.cpuName = null;
        machineInfoData.typeName = null;
        machineInfoData.UUID = null;
        machineInfoData.RomSize = null;
        machineInfoData.RamSize = null;
        machineInfoData.wifiModule = null;
        machineInfoData.mobileModule = null;
        machineInfoData.RS232_1 = null;
        machineInfoData.RS232_2 = null;
        machineInfoData.RS232_3 = null;
        machineInfoData.RS232_4 = null;
        machineInfoData.RS485_1 = null;
        machineInfoData.RS485_2 = null;

        machineInfoData.ETH_A33 = null;
        machineInfoData.ETH_H3 = null;
        machineInfoData.ETH_RK3288_1 = null;
        machineInfoData.ETH_RK3288_2 = null;
        machineInfoData.ETH_RK3368 = null;

        machineInfoData.ISRS485 = false;
        machineInfoData.RECORD = false;
        machineInfoData.SPEAK = false;
        machineInfoData.HEADPHONE = false;
        machineInfoData.CAMERA_ONE = false;
        machineInfoData.TFCARD = false;
        machineInfoData.USB = false;
        machineInfoData.ISWIFI = false;
        machineInfoData.ISPHONE = false;
        machineInfoData.SCREEN = false;
        machineInfoData.TOUCH_SCREEN = false;
        machineInfoData.GPIO = null;
        machineInfoData.GPIONUM = -1;
        machineInfoData.RTC = false;
    }
}
