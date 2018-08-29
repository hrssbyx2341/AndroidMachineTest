package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.ScreenTestActivity;

/**
 * Created by 24179 on 2018/7/19.
 */

public class Screen extends Common {
    private Debug debug;
    private Handler handler;
    public Screen(final Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("Screen");
        this.handler = handler;
        getMessage().setText("请检查屏幕显示和背光调节是否正常");
        getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getYes().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),0);
            }
        });
        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),1);
            }
        });
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                handler.sendEmptyMessage(1);
                startTest(context);
            }
        });
    }


    public void finish(){}
    @Override
    public void startTest(Context context) {
        super.startTest(context);
        Intent intent = new Intent(context, ScreenTestActivity.class);
        context.startActivity(intent);
    }
}
