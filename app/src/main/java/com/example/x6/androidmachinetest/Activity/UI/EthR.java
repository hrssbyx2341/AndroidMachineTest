package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;

import java.io.File;

public class EthR extends Common {
    private Debug debug;


    public EthR(final Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("CameraOne");

        getYes().setEnabled(false);
        getNo().setEnabled(false);
        getYes().setTextColor(Color.WHITE);
        getNo().setTextColor(Color.WHITE);

        getMessage().setText("点击\"重测\"跳转到设置界面，请到 \"设置->（更多->）->以太网\" 界面下进行以太网测试");
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
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                OpenEthSetting(context);
                handler.sendEmptyMessage(1);
            }
        });

    }

    @Override
    public void startTest(Context context) {
        super.startTest(context);
        //以太网默认不打开设置,点击重测进入设置界面
//        OpenEthSetting(context);
    }

    private void OpenEthSetting(Context context){
        context. startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    /*
        这里作资源释放处理
         */
    public void finish(){

    }
}
