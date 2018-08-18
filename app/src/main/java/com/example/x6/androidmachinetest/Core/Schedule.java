package com.example.x6.androidmachinetest.Core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.BoringLayout;
import android.text.Spannable;
import android.widget.LinearLayout;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.Activity.UI.Speak;


/**
 * Created by 24179 on 2018/7/9.
 */

public class Schedule {
    private Context context;
    private static Schedule schedule = null;
    private MachineInfoData machineInfoData = MachineInfoData.getMachineInfoData();
    private PlanTest SplanTest = PlanTest.getPlanTest();
    private GetMachineInfo getMachineInfo = new GetMachineInfo();


    /*
    所有测试项的实例化
     */
    Class[] testProject = null;


    private int testPosition = 0; //这个变量保存当前测试的位置

    private Schedule(){
        testProject = new Class[SplanTest.planStep.length];
    }
    public static Schedule getSchedule(Context context1){
        synchronized (Schedule.class){
            if(schedule == null){
                schedule = new Schedule();
            }
        }
        return schedule;
    }


    public int getTestPosition(){
        return testPosition;
    }

    public LinearLayout moveSchedule(Context context){
        LinearLayout linearLayout = null;
        if(testPosition == SplanTest.planStep.length){
            /* 这里做恢复出厂设置 */
            return linearLayout;
        }
        String string = SplanTest.planStep[testPosition];
        if(string.equals(PreMachineInfo.SPEAK_TEST)){
            linearLayout = new Speak(context,"喇叭是否有声音",new Handler()).getLineout();
        }else if(string.equals(PreMachineInfo.HEADPHONE_TEST)){

        }else if(string.equals(PreMachineInfo.RECORD_TEST)){

        }else if(string.equals(PreMachineInfo.WFIF_TEST)){

        }else if(string.equals(PreMachineInfo.BLUETOOTH_TEST)){

        }else if(string.equals(PreMachineInfo.MOBILE_NET_TEST)){

        }else if(string.equals(PreMachineInfo.ETH_A33_TEST)){

        }else if(string.equals(PreMachineInfo.ETH_H3_TEST)){

        }else if(string.equals(PreMachineInfo.ETH_RK3288_2_TEST)/*rk3288 B701*/){

        }else if(string.equals(PreMachineInfo.ETH_RK3288_1_TEST)){

        }else if(string.equals(PreMachineInfo.ETH_RK3368_TEST)){

        }else if(string.equals(PreMachineInfo.RS232_2_TEST)){

        }else if(string.equals(PreMachineInfo.RS232_3_TEST)){

        }else if(string.equals(PreMachineInfo.RS232_4_TEST)){

        }else if(string.equals(PreMachineInfo.RS485_1_TEST)){

        }else if(string.equals(PreMachineInfo.RS485_2_TEST)){

        }else if(string.equals(PreMachineInfo.USB_TEST)){

        }else if(string.equals(PreMachineInfo.SCREEN_TEST)){

        }else if(string.equals(PreMachineInfo.TOUCH_SCREEN_TEST)){

        }else if(string.equals(PreMachineInfo.GPIO_A33_TEST)){

        }else if(string.equals(PreMachineInfo.GPIO_RK3288_TEST)){

        }else if(string.equals(PreMachineInfo.RTC_TEST)){

        }else {
            linearLayout = new LinearLayout(context);
        }
        testPosition++;

        return linearLayout;
    }
    /*
    释放
     */
    public void finish(){
        testPosition = 0;
    }

}
