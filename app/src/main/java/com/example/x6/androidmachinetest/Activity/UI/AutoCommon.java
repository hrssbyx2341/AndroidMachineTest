package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.x6.androidmachinetest.function.Debug;

import org.w3c.dom.Text;

/**
 * Created by 24179 on 2018/7/13.
 */

public class AutoCommon {
    private Debug debug;
    private LinearLayout lineout;
    private ScrollView scrollView;
    private int position = -1;
    private TextView textView;
    private TextView title;
    private Button reTest;


    public AutoCommon(Context context, String Title){
        scrollView = new ScrollView(context);
        debug = new Debug("AutoCommon");
        lineout = new LinearLayout(context);
        lineout.setOrientation(LinearLayout.VERTICAL);
        ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        title = new TextView(context);
        title.setText(Title);
        reTest = new Button(context);
        reTest.setText("重新测试");
        textView = new TextView(context);
        lineout.addView(title, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineout.addView(reTest, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineout.addView(textView,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scrollView.addView(lineout);

    }


    /*
    添加显示信息
     */
    public void setMessage(String message){
        textView.setText(message);
        textView.setTextColor(Color.BLACK);
    }
    public void addMessage(String message){
        if(textView.getText() == null){
            textView.setText(message);
        }else {
            textView.setText(textView.getText().toString() + "\n" + message);
        }
    }
    public void addMessage(String message,int color){
        if(textView.getText() == null){
            textView.setText(message);
        }else {
            textView.setText(textView.getText().toString() + "\n" + message);
        }
        textView.setTextColor(color);
    }
    public void addMessage(String message,int color,int size){
        if(textView.getText() == null){
            textView.setText(message);
        }else {
            textView.setText(textView.getText().toString() + "\n" + message);
        }
        textView.setTextColor(color);
        textView.setTextSize(size);
    }
    /*
    获取控件
     */
    public ScrollView getScrollView(){
        return scrollView;
    }
    public Button getReTest(){
        return reTest;
    }
    public TextView getTextView(){
        return textView;
    }
    public TextView getTitle(){
        return title;
    }
    public LinearLayout getLineout(){
        return lineout;
    }
    /*
    开始测试
     */
    public void startTest(Context context){}
    public void startTest(Context context, Handler handler){};
    public void startTest(Context context,Handler handler, String title){}
    public void finish(){}

    public void setPosition(int i){
        this.position = i;
    }
    public int getPosition(){
        return this.position;
    }

}
