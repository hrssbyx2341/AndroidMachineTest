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
import com.example.x6.gpioctl.GpioUtils;

/**
 * Created by 24179 on 2018/7/19.
 */

public class GPIO extends AutoCommon{
    private Handler handler;
    private MachineInfoData machineInfoData;
    private GpioUtils gpioUtils;
    private Debug debug;
    private TextView[] gpioTextViews;

    public GPIO(final Context context, String Title, final Handler handler) {
        super(context, Title);
        this.handler = handler;
        machineInfoData = MachineInfoData.getMachineInfoData();
        if(machineInfoData.cpuName.equals(PreMachineInfo.RK3288)||machineInfoData.cpuName.equals(PreMachineInfo.RK3368))
            gpioUtils = GpioUtils.getGpioUtils("/dev/rk_gpio");
        else
            gpioUtils = GpioUtils.getGpioUtils("/dev/sunxi_gpio");
        debug = new Debug("GPIO");

        getLineout().removeView(getTextView());
        gpioTextViews = new TextView[2];
        gpioTextViews[0] = new TextView(context);
        gpioTextViews[1] = new TextView(context);
        getLineout().addView(gpioTextViews[0], LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getLineout().addView(gpioTextViews[1], LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        getReTest().setEnabled(false);
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpioTextViews[0].setText("");
                gpioTextViews[1].setText("");
                gpioTextViews[0].setTextColor(Color.BLACK);
                gpioTextViews[1].setTextColor(Color.BLACK);
                startTest(context,handler);
            }
        });
    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        gpioTestInit();
        gpioTestStart();
    }

    private boolean gpioTestInit(){
        debug.logw("当前GPIO号码："+String.valueOf(machineInfoData.GPIO[0])+" "+String.valueOf(machineInfoData.GPIO[1])+" "
        +String.valueOf(machineInfoData.GPIO[2])+" "+String.valueOf(machineInfoData.GPIO[3])+" ");
        if(machineInfoData.GPIO != null){
            gpioUtils.setGpioDirection(machineInfoData.GPIO[0],GpioUtils.GPIO_DIRECTION_IN);
            gpioUtils.setGpioDirection(machineInfoData.GPIO[1],GpioUtils.GPIO_DIRECTION_IN);
            gpioUtils.setGpioDirection(machineInfoData.GPIO[2],GpioUtils.GPIO_DIRECTION_OUT);
            gpioUtils.setGpioDirection(machineInfoData.GPIO[3],GpioUtils.GPIO_DIRECTION_OUT);
            return true;
        }else{
            getTextView().setText("当前没有找到GPIO， GPIO测试失败");
            handler.sendEmptyMessage(0);
            TestActivity.syncResultList(handler,getPosition(),0);
            return false;
        }
    }

    private void gpioReMark(String reMark){
        Result result = Result.getResult();
        result.GpioReMark = "";
        result.GpioReMark += reMark;
    }

    private void gpioTestStart(){
        getReTest().setEnabled(false);
        boolean isOk = true;
        gpioUtils.gpioSetValue(machineInfoData.GPIO[2],GpioUtils.GPIO_VALUE_LOW);
        gpioUtils.gpioSetValue(machineInfoData.GPIO[3],GpioUtils.GPIO_VALUE_LOW);
        int gpio0_in_value = gpioUtils.gpioGetValue(machineInfoData.GPIO[0]);
        int gpio1_in_value = gpioUtils.gpioGetValue(machineInfoData.GPIO[1]);

        gpioUtils.gpioSetValue(machineInfoData.GPIO[2],GpioUtils.GPIO_VALUE_HIGH);
        gpioUtils.gpioSetValue(machineInfoData.GPIO[3],GpioUtils.GPIO_VALUE_HIGH);

        /*
        作反转判断
         */
        if(((gpio0_in_value == gpioUtils.GPIO_VALUE_HIGH)
                && (gpioUtils.gpioGetValue(machineInfoData.GPIO[0]) == gpioUtils.GPIO_VALUE_LOW))
                || ((gpio0_in_value == gpioUtils.GPIO_VALUE_LOW)
                && (gpioUtils.gpioGetValue(machineInfoData.GPIO[0]) == gpioUtils.GPIO_VALUE_HIGH))){
            gpioTextViews[0].setText("GPIO_0 和 GPIO_2 测试通过");
            gpioTextViews[0].setTextColor(Color.BLUE);
        }else{
            gpioTextViews[0].setText("GPIO_0 和 GPIO_2 测试不通过");
            gpioReMark("GPIO_0 和 GPIO_2 测试不通过");
            isOk = false;
            gpioTextViews[0].setTextColor(Color.RED);
        }

        if(((gpio1_in_value == gpioUtils.GPIO_VALUE_HIGH)
                && (gpioUtils.gpioGetValue(machineInfoData.GPIO[1]) == gpioUtils.GPIO_VALUE_LOW))
                || ((gpio1_in_value == gpioUtils.GPIO_VALUE_LOW)
                && (gpioUtils.gpioGetValue(machineInfoData.GPIO[1]) == gpioUtils.GPIO_VALUE_HIGH))){
            gpioTextViews[1].setText("GPIO_1 和 GPIO_3 测试通过");
            gpioTextViews[1].setTextColor(Color.BLUE);
        }else{
            gpioTextViews[1].setText("GPIO_1 和 GPIO_3 测试不通过");
            gpioReMark("GPIO_1 和 GPIO_3 测试不通过");
            gpioTextViews[1].setTextColor(Color.RED);
            isOk = false;
        }

        handler.sendEmptyMessage(0);
        if(isOk){
            TestActivity.syncResultList(handler,getPosition(),1);
        }else{
            TestActivity.syncResultList(handler,getPosition(),0);
        }
        getReTest().setEnabled(true);
    }


}
