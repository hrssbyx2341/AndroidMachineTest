package com.example.x6.androidmachinetest.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.Data.AfterShutDownData;
import com.example.x6.androidmachinetest.R;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.ExcelUtils;
import com.example.x6.androidmachinetest.function.SuCommand;

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
    private boolean isRtcPass = true;
    private ExcelUtils excelUtils;
    private SuCommand suCommand;
    private Handler handler = new Handler();
    private boolean isRtcWrited = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug = new Debug("EndActivity");
        excelUtils = new ExcelUtils();
        suCommand = new SuCommand();
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
        }else{
            display.setText("当前时间："+formatTime(currentTime)+"\n"
                    +"掉电前时间："+formatTime(afterShutDownData.getTimeBeforeShutDown())+"\n"
                    +"时间不超过\"一小时\"RTC测试通过");
            display.setTextColor(Color.BLUE);
            isRtcPass = true;
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
                String cmd = "mkdir /cache/recovery";
                suCommand.execRootCmdSilent(cmd);
                cmd = "echo --wipe_data > /cache/recovery/command";
                suCommand.execRootCmdSilent(cmd);
                cmd = "sync";
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                suCommand.execRootCmdSilent(cmd);
                cmd = "reboot recovery";
                suCommand.execRootCmdSilent(cmd);
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
                boolean temp;
                if(isRtcWrited)
                    temp = true;
                else
                     temp = writeRTC(isRtcPass);
                if(temp){
                    isRtcWrited = true;
                    debug.logd("RTC测试结果写入成功");
                    if(copyReporterFile(afterShutDownData.getTempReporterFilePath())){
                        syncDisplay("生成测试报告成功，请恢复出厂设置");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                reStroe.setEnabled(true);
                                reStroe.setTextColor(Color.BLACK);
                                appendReporter.setEnabled(false);
                            }
                        });
                    }else{
                        syncDisplay("生成测试报告失败");
                    }
                }else{
                    syncDisplay("RTC测试结果写入失败");
                    debug.loge("RTC测试结果写入失败");
                }
            }
        }).start();
    }

    private boolean copyReporterFile(String boardPath){
        boolean isOk = true;
        String cmd = "";
        String uDiskPath = getUdiskPath();
        if(sureUdiskPath(uDiskPath)) {
            cmd = "ls "+boardPath;
            if(0 == suCommand.execRootCmdSilent(cmd)){
                cmd = "cp " + boardPath + " " + uDiskPath;
                if (0 != suCommand.execRootCmdSilent(cmd)) {
                    isOk = false;
                    debug.loge("拷贝文件失败");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            display.append("拷贝文件失败...");
                        }
                    });
                }
                cmd = "sync";
                if (0 != suCommand.execRootCmdSilent(cmd))
                    isOk = false;
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        display.append("没有检测到测试报告，请卸载软件后重装软件重新测试");
                    }
                });
                debug.loge("没有检测到测试报告");
                isOk = false;
            }
        }else{
            debug.loge("没有检测到U盘的存在");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    display.append("没有检测到U盘的存在");
                }
            });

            isOk = false;
        }
        return isOk;
    }

    private String getUdiskPath(){
        String result = null;
        Vector<String> vector = null;
        if(afterShutDownData.getCpuName().equals(PreMachineInfo.RK3288)){
            result = "/mnt/usb_storage/USB_DISK2/udisk0/";
        }else if(afterShutDownData.getCpuName().equals(PreMachineInfo.RK3368)){
            String cmd = "ls /mnt/media_rw/";
            vector = suCommand.execRootCmd(cmd);
            result = "/mnt/media_rw/"+vector.get(0);
        }else if(afterShutDownData.getCpuName().equals(PreMachineInfo.A33)){
            result = "/mnt/usbhost1/";
        }else if(afterShutDownData.getCpuName().equals(PreMachineInfo.H3)){
            String cmd = "ls /mnt/usbhost/";
            vector = suCommand.execRootCmd(cmd);
            result = "/mnt/usbhost/"+vector.get(0);
        }
        if(result == null)
            debug.loge("没有分配路径");
        else
            debug.logw(result);
        return result;
    }
    private boolean sureUdiskPath(String uDiskpath){
        boolean isok = true;
        String cmd = "ls "+uDiskpath;
        if(0 != suCommand.execRootCmdSilent(cmd))
            isok = false;
        cmd = "ls /dev/block/sd*";
        if(0 != suCommand.execRootCmdSilent(cmd))
            isok = false;
        if(isok)
            debug.logd("U盘存在");
        else
            debug.loge("U盘不存在");
        return isok;
    }

    private boolean writeRTC(boolean isPass){
        boolean isok = true;
        if(isPass) {
           if(!excelUtils.writeToExcel(afterShutDownData.getTempReporterFilePath(), "RTC功能", "测试通过"))
               isok = false;
        }
        else {
            if(!excelUtils.writeToExcel(afterShutDownData.getTempReporterFilePath(), "RTC功能", "测试不通过"))
                isok = false;
        }
        if(!excelUtils.writeToExcel(afterShutDownData.getTempReporterFilePath(),"测试时间",formatTime(afterShutDownData.getTimeBeforeShutDown())))
            isok = false;
        if(0 != suCommand.execRootCmdSilent("sync"))
            isok = false;
        return isok;
    }

    private String formatTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

}
