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
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.PreMachineInfo;
import com.example.x6.androidmachinetest.Data.PreData;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.EthControl;
import com.example.x6.androidmachinetest.function.MobileNetControl;
import com.example.x6.androidmachinetest.function.SuCommand;
import com.example.x6.androidmachinetest.function.WifiControl;

import java.io.File;
import java.io.PipedReader;

public class EthR extends Common {
    private Debug debug;
    private Handler handler;
    private boolean isB701Eth2Pass = false;

    public EthR(final Context context, String title1, final Handler handler) {
        super(context, title1);
        debug = new Debug("CameraOne");
        this.handler = handler;
        getYes().setEnabled(false);
        getNo().setEnabled(false);
        /* B701 是双网口的*/
        getYes().setTextColor(Color.WHITE);
        getNo().setTextColor(Color.WHITE);

        getMessage().setText("点击\"重测\"跳转到设置界面，请到 \"设置->（更多->）->以太网\" 界面下进行以太网测试");

        if (MachineInfoData.getMachineInfoData().typeName.equals(PreMachineInfo.B701)) {
            getReTest().setEnabled(false);
            getMessage().setText("当前设备型号是B701，有两个网口，所以请等待\"以太网2\"自动测试完成再进行第二步\"以太网1\"手动测试\n点击\"重测\"跳转到设置界面，请到 \"设置->（更多->）->以太网\" 界面下进行以太网测试\n正在测试\"以太网2\"...");
        }
        getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getNo().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler, getPosition(), 1);
            }
        });
        getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getYes().setEnabled(false);
                getNo().setEnabled(false);
                getYes().setTextColor(Color.WHITE);
                handler.sendEmptyMessage(0);
                TestActivity.syncResultList(handler, getPosition(), 0);
            }
        });
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MachineInfoData.getMachineInfoData().typeName.equals(PreMachineInfo.B701)) {
                    if (isB701Eth2Pass) {
                        new EthControl().ethOpen();
                        getYes().setEnabled(true);
                        getNo().setEnabled(true);
                        getYes().setTextColor(Color.BLACK);
                        getNo().setTextColor(Color.BLACK);
                        OpenEthSetting(context);
                        handler.sendEmptyMessage(1);
                    } else {
                        startTest(context);
                    }
                }else{
                    new EthControl().ethOpen();
                    getYes().setEnabled(true);
                    getNo().setEnabled(true);
                    getYes().setTextColor(Color.BLACK);
                    getNo().setTextColor(Color.BLACK);
                    OpenEthSetting(context);
                    handler.sendEmptyMessage(1);
                }
            }
        });

    }

    @Override
    public void startTest(final Context context) {
        super.startTest(context);
        final Context context1 = context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                关闭其他网络
                 */
                new WifiControl(context1).WifiClose();
                new MobileNetControl().disableData(); //关闭移动网络

                MachineInfoData machineInfoData = MachineInfoData.getMachineInfoData();
                /*设备B701的网口2 测试*/
                if (machineInfoData.typeName.equals(PreMachineInfo.B701)) {
                    if (!isB701Eth2Pass) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                getMessage().setText("正在测试以太网2...");
                            }
                        });
                        if (machineInfoData.ETH_RK3288_2 != null && machineInfoData.ETH_RK3288_1 != null) {
                            SuCommand suCommand = new SuCommand();
                        /*
                        这里测试网口2
                         */
                            String cmd = null;
                            cmd = "netcfg " + machineInfoData.ETH_RK3288_2 + " down"; //先把网口down掉，再up起来，作为一个触发
                            if (0 == suCommand.execRootCmdSilent(cmd)) {
                                cmd = "netcfg " + machineInfoData.ETH_RK3288_2 + " up";
                                if (0 == suCommand.execRootCmdSilent(cmd)) {
                                    try {
                                        Thread.sleep(4 * 1000);//此处延时10 s 作为dhcp ，如果10s还无法拨号成功，则拨号失败
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    cmd = "busybox ping " + PreData.ETHgateWay + " -w 1 -I " + machineInfoData.ETH_RK3288_2;
                                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                                        syncTextView("以太网2测试通过", true);
                                        isB701Eth2Pass = true;
                                    } else {
                                        syncTextView("以太网2测试不通过，PING网关失败", false);
                                        isB701Eth2Pass = false;
                                    }
                                } else {
                                    syncTextView("以太网2测试不通过，以太网重启失败", false);
                                    isB701Eth2Pass = false;
                                }
                            } else {
                                syncTextView("以太网2测试不通过，以太网重启失败", false);
                                isB701Eth2Pass = false;
                            }
                        }
                    }
                }
                /*B701 网口2 测试结束*/
            }
        }).start();
    }

    private void syncTextView(final String message, final boolean isPass){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getMessage().setText(message);
                if(!isPass){
                    handler.sendEmptyMessage(0);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getYes().setEnabled(false);
                            getNo().setEnabled(false);
                            getReTest().setEnabled(true);
                        }
                    });
                    TestActivity.syncResultList(handler, getPosition(), 0);
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getYes().setEnabled(false);
                            getNo().setEnabled(false);
                            getReTest().setEnabled(true);
                            handler.sendEmptyMessage(1);
                            getMessage().setText("以太网2 测试通过\n点击\"重测\"跳转到设置界面，请到 \"设置->（更多->）->以太网\" 界面下进行以太网测试");
                        }
                    });
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private void OpenEthSetting(Context context) {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    /*
        这里作资源释放处理
         */
    public void finish() {

    }
}
