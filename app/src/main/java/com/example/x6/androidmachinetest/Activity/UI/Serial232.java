package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.serialportlib.SerialPort;

/**
 * Created by 24179 on 2018/7/18.
 */

public class Serial232 {
    private Debug debug;
    private LinearLayout lineout;
    private int position = -1;
    private TextView[] textView;
    private int serialNum = -1;
    private Button reTest;


    public Serial232(Context context, int num, Handler handler){
        lineout = new LinearLayout(context);
        lineout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lineout.setLayoutParams(layoutParam);
        reTest = new Button(context);
        reTest.setText("重新测试");
        lineout.addView(reTest, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void startTest(Context context){}

    public Button getReTest(){
        return reTest;
    }

    public LinearLayout getLineout(){
        return lineout;
    }
    /**
     * 返回串口的TextView， 根据num进行数组的索引
     * @param num
     * @return
     */
    public TextView getSerialTextView(int num){
        if (num >= serialNum){
            return  null;
        }else{
            return textView[num];
        }
    }

    public int getSerialNum(){
        return serialNum;
    }
    public void setSerialNum(int i){
        serialNum = i;
    }
    public int getPosition(){
        return  position;
    }
    public void setPosition(int i){
        position = i;
    }
}
