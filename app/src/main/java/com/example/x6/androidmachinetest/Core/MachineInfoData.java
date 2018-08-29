package com.example.x6.androidmachinetest.Core;

/**
 * Created by 24179 on 2018/7/7.
 */

public class MachineInfoData {
    private   MachineInfoData(){};

    private static MachineInfoData machineInfoData = null;

    public static MachineInfoData getMachineInfoData(){
        synchronized (MachineInfoData.class){
            if(machineInfoData == null)
                machineInfoData = new MachineInfoData();
        }
        return machineInfoData;
    }

    /**
     * CPU，emmc等相关信息存储变量
     */
    public String cpuName = null;
    public String typeName = null;
    public String UUID = null;
    public String RomSize = null;
    public String RamSize = null;

    /**
     * 设备的相关芯片
     */
    public String wifiModule = null;
    public String mobileModule = null;

    /**
     * 设备相关外设信息
     */
    public String RS232_1 = null;
    public String RS232_2 = null;
    public String RS232_3 = null;
    public String RS232_4 = null;
    public String RS485_1 = null;
    public String RS485_2 = null;

    public String ETH_A33 = null;
    public String ETH_H3 = null;
    public String ETH_RK3288_1 = null;
    public String ETH_RK3288_2 = null;
    public String ETH_RK3368 = null;

    public boolean ISRS485 = false;
    public boolean RECORD = false;
    public boolean SPEAK = false;
    public boolean HEADPHONE = false;
    public boolean TFCARD = false;
    public boolean USB = false;
    public boolean ISWIFI = false;
    public boolean ISPHONE = false;

    public boolean SCREEN = false;
    public boolean CAMERA_ONE = false;
    public boolean TOUCH_SCREEN = false;
    public int GPIONUM = -1;
    public int[] GPIO = null;
    public boolean RTC = false;


    public void finish(){
        cpuName = null;
        typeName = null;
        UUID = null;
        RomSize = null;
        RamSize = null;
        wifiModule = null;
        mobileModule = null;
        RS232_1 = null;
        RS232_2 = null;
        RS232_3 = null;
        RS232_4 = null;
        RS485_1 = null;
        RS485_2 = null;
        ETH_A33 = null;
        ETH_H3 = null;
        ETH_RK3288_1 = null;
        ETH_RK3288_2 = null;
        ETH_RK3368 = null;
        ISRS485 = false;
        RECORD = false;
        SPEAK = false;
        HEADPHONE = false;
        TFCARD = false;
        USB = false;
        ISWIFI = false;
        ISPHONE = false;
        SCREEN = false;
        CAMERA_ONE = false;
        TOUCH_SCREEN = false;
        GPIONUM = -1;
        GPIO = null;
        RTC = false;
    }

    public String showConfig(){
        String result = "";
        result += "CPU name:"+cpuName+"\n"+"Type name:"+typeName+"\n"+"Uuid:"+UUID+"\n"+"Rom size:"+RomSize+"\n"
        +"Ram size:"+RamSize+"\n"+"Wifi Module name:"+wifiModule+"\n"+"Phone Module name:"+mobileModule+"\n"
        +"RS232:"+RS232_1+","+RS232_2+","+RS232_3+","+RS232_4+"\nRS485:"+RS485_1+","+RS485_2+"\nEth_A33:"+ETH_A33+"\n"
        +"Eth_H3:"+ETH_H3+"\n"+"Eth_RK3288_1:"+ETH_RK3288_1+"\n"+"Eth_RK3288_2:"+ETH_RK3288_2+"\n"+"Eth_RK3368:"+ETH_RK3368
        +"\nISRS485:"+String.valueOf(ISRS485)+"\n"+"Record:"+String.valueOf(RECORD)+"\n"+"Speak:"+String.valueOf(SPEAK)+"\n"
        +"Headphone:"+String.valueOf(HEADPHONE)+"\n"+"TFcard:"+String.valueOf(TFCARD)+"\n"+"USB:"+String.valueOf(USB)+"\n"
        +"ISwifi:"+String.valueOf(ISWIFI)+"\n"+"ISPhone:"+String.valueOf(ISPHONE)+"\n"+"Screen:"+String.valueOf(SCREEN)+"\n"
        +"TouchScreen:"+String.valueOf(TOUCH_SCREEN)+"\n"+"RTC:"+String.valueOf(RTC);
        if (GPIONUM != -1)
            result += "\nGPIO:" +String.valueOf(GPIO[0])+","+String.valueOf(GPIO[1])+","+String.valueOf(GPIO[2])+","+String.valueOf(GPIO[3]);
        else
            result+= "\nGPIO:null";
        return result;
    }
}
