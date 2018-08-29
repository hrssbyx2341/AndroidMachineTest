package com.example.x6.androidmachinetest.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.x6.androidmachinetest.Activity.TestActivity;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.Result;
import com.example.x6.androidmachinetest.function.EthControl;
import com.example.x6.androidmachinetest.function.WifiControl;

/**
 * Created by 24179 on 2018/7/18.
 */

public class Eth extends AutoCommon {
    private Handler handler;
    private EthControl ethControl;
    private Context context;
    private MachineInfoData machineInfoData;
    public Eth(Context context, String Title, Handler handler) {
        super(context, Title);
        ethControl = new EthControl();
        getReTest().setEnabled(false);
        getTitle().setText(Title);
        getTitle().setTextColor(Color.BLACK);
        this.handler = handler;
        this.context = context;
        machineInfoData = MachineInfoData.getMachineInfoData();
        getReTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ethTestStart();
            }
        });
    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        ethTestStart();
    }

    private void ethReMark(String reMark){
        Result result = Result.getResult();
        result.ethTestReMark = "";
        result.ethTestReMark += reMark +"\n";
    }

    private void ethTestStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getReTest().setEnabled(false);
                        handler.sendEmptyMessage(1);
                    }
                });
                syncTextView("正在测试以太网...",Color.BLACK);
                switch (ethControl.ethTest(context)){
                    case EthControl.ETH_A33_SUCESSFUL:
                        syncTextView("以太网测试通过",Color.BLUE);
                        break;
                    case EthControl.ETH_A33_DHCP_FAILED:
                        syncTextView("以太网测试不通过\n原因：获取IP地址失败，请检查网线是否插入。",Color.RED);
                        break;
                    case EthControl.ETH_A33_UP_FAILED:
                        syncTextView("以太网测试不通过\n原因：以太网启动失败，请检查硬件。",Color.RED);
                        break;
                    case EthControl.ETH_A33_PING_FAILED:
                        syncTextView("以太网测试不通过\n原因：ping 网关失败",Color.RED);
                        break;
                    case EthControl.ETH_H3_SUCESSFUL:
                        syncTextView("以太网测试通过",Color.BLUE);
                        break;
                    case EthControl.ETH_H3_DOWN_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网复位失败", Color.RED);
                        break;
                    case EthControl.ETH_H3_UP_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网启动失败",Color.RED);
                        break;
                    case EthControl.ETH_H3_PING_FAILED:
                        syncTextView("以太网测试失败\n原因：ping 网关失败",Color.RED);
                        break;
                    case EthControl.ETH_RK3288_1_SUCESSFUL:
                        syncTextView("以太网测试通过",Color.BLUE);
                        break;
                    case EthControl.ETH_RK3288_2_SUCESSFUL:
                        syncTextView("以太网测试通过",Color.BLUE);
                        break;
                    case EthControl.ETH_RK3288_1_DOWN_FAILED:
                        if(machineInfoData.ETH_RK3288_2 == null && machineInfoData.ETH_RK3288_1 != null){
                            syncTextView("以太网测试失败\n原因：以太网重启失败",Color.RED);
                        }else if(machineInfoData.ETH_RK3288_2 != null && machineInfoData.ETH_RK3288_1 != null){
                            syncTextView("以太网测试失败\n原因：以太网 0 重启失败",Color.RED);
                        }
                        break;
                    case EthControl.ETH_RK3288_1_UP_FAILED:
                        if(machineInfoData.ETH_RK3288_2 == null && machineInfoData.ETH_RK3288_1 != null){
                            syncTextView("以太网测试失败\n原因：以太网启动失败",Color.RED);
                        }else if(machineInfoData.ETH_RK3288_2 != null && machineInfoData.ETH_RK3288_1 != null){
                            syncTextView("以太网测试失败\n原因：以太网 0 启动失败",Color.RED);
                        }
                        break;
                    case EthControl.ETH_RK3288_1_PING_FAILED:
                        if(machineInfoData.ETH_RK3288_2 == null && machineInfoData.ETH_RK3288_1 != null){
                            syncTextView("以太网测试失败\n原因：ping 网关失败",Color.RED);
                        }else if(machineInfoData.ETH_RK3288_2 != null && machineInfoData.ETH_RK3288_1 != null){
                            syncTextView("以太网测试失败\n原因：以太网 0 ping 网关失败",Color.RED);
                        }
                        break;
                    case EthControl.ETH_RK3288_2_DOWN_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网 1 重启失败",Color.RED);
                        break;
                    case EthControl.ETH_RK3288_2_UP_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网 1 启动失败",Color.RED);
                        break;
                    case EthControl.ETH_RK3288_2_PING_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网 1 ping 网关失败",Color.RED);
                        break;
                    case EthControl.ETH_RK3368_SUCESSFUL:
                        syncTextView("以太网测试通过",Color.BLUE);
                        break;
                    case EthControl.ETH_RK3368_DOWN_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网重启失败",Color.RED);
                        break;
                    case EthControl.ETH_RK3368_UP_FAILED:
                        syncTextView("以太网测试失败\n原因：以太网启动失败",Color.RED);
                        break;
                    case EthControl.ETH_RK3368_PING_FAILED:
                        syncTextView("以太网测试不通过\n原因：ping 网关失败",Color.RED);
                        break;
                    default:break;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getReTest().setEnabled(true);
                    }
                });
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    @Override
    public void finish() {
        super.finish();
        ethControl.ethClose();
    }

    private void syncTextView(final String message){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getTextView().setText(message);
            }
        });
    }
    private void syncTextView(final String message, final int color){
        handler.post(new Runnable() {
            @Override
            public void run() {
                getTextView().setText(message);
                getTextView().setTextColor(color);
                if(color == Color.RED)
                    TestActivity.syncResultList(handler,getPosition(),1);
                else if(color == Color.BLUE)
                    TestActivity.syncResultList(handler,getPosition(),0);
            }
        });
    }
}
