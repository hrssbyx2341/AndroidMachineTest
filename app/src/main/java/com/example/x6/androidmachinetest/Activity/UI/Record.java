package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.StartApk;

/**
 * Created by 24179 on 2018/7/13.
 */

public class Record extends Common {
    private Debug debug;
    private StartApk startApk;

    public Record(final Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("Record");
        startApk = new StartApk();


        getMessage().setText("默认会打开录音界面，请测试录音功能是否正常，如果录音界面消失可以点击重新测试调出录音界面");
        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),true);
            }
        });
        getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getYes().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler,getPosition(),false);
            }
        });
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                startApk.startPro("录音机",context);
                handler.sendEmptyMessage(1);
            }
        });

    }

    @Override
    public void startTest(Context context) {
        super.startTest(context);
        startApk.startPro("录音机",context);
    }

    /*
        这里作资源释放处理
         */
    public void finish(){

    }

}
