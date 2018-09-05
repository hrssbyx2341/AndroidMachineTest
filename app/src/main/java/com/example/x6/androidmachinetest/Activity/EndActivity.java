package com.example.x6.androidmachinetest.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.Data.AfterShutDownData;
import com.example.x6.androidmachinetest.DataBase.TestDataBaseUtils;
import com.example.x6.androidmachinetest.R;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.EthControl;
import com.example.x6.androidmachinetest.function.ExcelUtils;
import com.example.x6.androidmachinetest.function.MobileNetControl;
import com.example.x6.androidmachinetest.function.SuCommand;
import com.example.x6.androidmachinetest.function.WifiControl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by 24179 on 2018/7/20.
 */

public class EndActivity extends Activity {
    private AfterShutDownData afterShutDownData;
    private Debug debug;
    private Button appendReporter,reStroe;
    private TextView display;
    private ProgressDialog progressDialog;
    private boolean isRtcPass = true;
    private SuCommand suCommand;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug = new Debug("EndActivity");
        suCommand = new SuCommand();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示：");
        progressDialog.setMessage("正在上传测试报告...");
        progressDialog.setCanceledOnTouchOutside(false);
        afterShutDownData = AfterShutDownData.getAfterShutDownData();
        setContentView(R.layout.activity_end);
        appendReporter = (Button) findViewById(R.id.appendResult);
        reStroe = (Button) findViewById(R.id.recovery);
        reStroe.setEnabled(false);
        reStroe.setTextColor(Color.WHITE);
        display = (TextView) findViewById(R.id.showRtcResult);

        long currentTime = System.currentTimeMillis();

        if(Math.abs(currentTime - afterShutDownData.getTimeBeforeShutDown()) >= 60*60*1000){
            display.setText("当前时间："+formatTime(currentTime)+"\n"
            +"掉电前时间："+formatTime(afterShutDownData.getTimeBeforeShutDown())+"\n"
            +"时间超过\"一小时\"RTC测试不通过");
            display.setTextColor(Color.RED);
            isRtcPass = false;
            //这里写入RTC测试结果数据库 并且统计整机测试是否通过
            TestDataBaseUtils.getTestDataBaseUtils(EndActivity.this.getApplicationContext()).updateRTC(0);
            TestDataBaseUtils.getTestDataBaseUtils(EndActivity.this.getApplicationContext()).updateISPass();
        }else{
            display.setText("当前时间："+formatTime(currentTime)+"\n"
                    +"掉电前时间："+formatTime(afterShutDownData.getTimeBeforeShutDown())+"\n"
                    +"时间不超过\"一小时\"RTC测试通过");
            display.setTextColor(Color.BLUE);
            isRtcPass = true;
            //这里写入RTC测试结果数据库 并且统计整机测试是否通过
            TestDataBaseUtils.getTestDataBaseUtils(EndActivity.this.getApplicationContext()).updateRTC(1);
            TestDataBaseUtils.getTestDataBaseUtils(EndActivity.this.getApplicationContext()).updateISPass();
        }

        appendReporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCopy();
            }
        });

        reStroe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reStroreStart();
            }
        });

    }

    private void reStroreStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               /***************************************************/
               /*  这里删除程序里面生成的所有文件，并且卸载程序   */
               /***************************************************/
               /*打开所有网络*/
                new WifiControl(EndActivity.this.getApplicationContext()).WifiOpen();
                new MobileNetControl().enableData(); //打开移动网络
                /* 删除录像文件 */
                File file = new File("/sdcard/testVideo.3gp");
                if(file.exists()){
                    file.delete();
                }

               Uri uri = Uri.fromParts("package","com.example.x6.androidmachinetest",null);
               Intent intent = new Intent(Intent.ACTION_DELETE,uri);
               EndActivity.this.startActivity(intent);
            }
        }).start();
    }

    private void syncDisplay(final String string){
        handler.post(new Runnable() {
            @Override
            public void run() {
                display.append("\n"+string);
            }
        });
    }

    private void startCopy(){
        display.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });
                    int temp = TestDataBaseUtils.getTestDataBaseUtils(EndActivity.this.getApplicationContext()).upLoad();
                    if(temp == 0){
                        syncDisplay("上传测试报告成功，请点击下方按钮，清除测试数据和卸载本程序");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(EndActivity.this,"上传测试报告成功",Toast.LENGTH_LONG).show();
                                reStroe.setEnabled(true);
                                reStroe.setTextColor(Color.BLACK);
                                appendReporter.setEnabled(false);
                            }
                        });
                    }else if(temp == -1){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(EndActivity.this,"上传测试报告失败",Toast.LENGTH_LONG).show();
                            }
                        });
                        syncDisplay("上传测试报告失败，请检查网线是否插入，以太网是否打开");
                    }else if(temp == -2){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(EndActivity.this,"上传测试报告失败",Toast.LENGTH_LONG).show();
                            }
                        });
                        syncDisplay("上传测试报告失败，服务端无法存储上传数据，请联系研发");
                    }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String formatTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

}
