package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.function.Debug;

import java.io.File;

public class CameraOne extends Common {
    private Debug debug;



    public CameraOne(final Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("CameraOne");



        getMessage().setText("默认会打开相机，请测试摄像头能否正常工作，如果相机界面消失可以点击重新测试调出相机界面");
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
                getMessage().setText("默认会打开相机，请测试摄像头能否正常工作，如果相机界面消失可以点击重新测试调出相机界面");
                getYes().setEnabled(true);
                getNo().setEnabled(true);
                getYes().setTextColor(Color.BLACK);
                getNo().setTextColor(Color.BLACK);
                startTest(context,handler);
                handler.sendEmptyMessage(1);
            }
        });

    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        if(0 == Camera.getNumberOfCameras()){
            debug.loge("没有检测到摄像头");
            getMessage().setText("当前没有检测到摄像头的存在，请插上摄像头后点击\"重测\"进行测试");
            getYes().setEnabled(false);
            getNo().setEnabled(false);
            getYes().setTextColor(Color.WHITE);
            handler.sendEmptyMessage(0);
            TestActivity.syncResultList(handler,getPosition(),0);
        }else {
            debug.loge("检测到摄像头");
            OpenCamera(context);
        }
    }



    private void OpenCamera(Context context){
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File("/sdcard/testVideo.3gp");
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        context.startActivity(intent);
    }

    /*
        这里作资源释放处理
         */
    public void finish(){

    }

}
