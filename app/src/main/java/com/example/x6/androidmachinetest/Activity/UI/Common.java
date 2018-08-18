package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.androidmachinetest.function.Debug;

/**
 * Created by 24179 on 2018/7/12.
 */

public class Common {
    private LinearLayout lineout;
    private LinearLayout lineH;
    private TextView title;
    private TextView message;
    private Button yes;
    private Button no;
    private Button reTest;
    private Debug debug;
    private int padding = 16;
    private int textSize = 25;

    private int position = -1;

    public Common(Context context,String title1){
        debug = new Debug("Common");
        lineout = new LinearLayout(context);
        lineH = new LinearLayout(context);
        lineH.setOrientation(LinearLayout.HORIZONTAL);
        lineout.setOrientation(LinearLayout.VERTICAL);
        lineout.setPadding(padding,padding,padding,padding);
        title = new TextView(context);
        title.setText(title1);
        message = new TextView(context);
        if(textSize != -1) {
            title.setTextSize(textSize);
            message.setTextSize(textSize);
        }
        yes = new Button(context);
        yes.setText("是");
        no = new Button(context);
        no.setText("否");
        reTest = new Button(context);
        reTest.setText("重测");
        LinearLayout.LayoutParams messageLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2f);
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);

        lineH.setLayoutParams(messageLayout);
        message.setLayoutParams(messageLayout);
        title.setLayoutParams(textLayout);
        yes.setLayoutParams(buttonLayout);
        no.setLayoutParams(buttonLayout);
        reTest.setLayoutParams(buttonLayout);
        yes.setTextColor(Color.BLACK);
        no.setTextColor(Color.BLACK);
        reTest.setTextColor(Color.BLACK);

        lineH.addView(title);
        lineH.addView(yes);
        lineH.addView(no);
        lineH.addView(reTest);

        lineout.addView(lineH);
        lineout.addView(message);

    }
    /*
    开始测试
     */
    public void startTest(Context context){}
    public void startTest(Context context, Handler handler){};
    public void startTest(Context context,Handler handler, String title){}

    public void setPosition(int i){
        this.position = i;
    }
    public int getPosition(){
        return this.position;
    }

    public Button getYes(){
        return yes;
    }
    public Button getNo(){
        return no;
    }
    public Button getReTest(){
        return reTest;
    }
    public TextView getMessage(){
        return message;
    }
    public LinearLayout getLineout(){
        return lineout;
    }

}
