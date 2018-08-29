package com.example.x6.androidmachinetest.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.x6.androidmachinetest.Data.AfterShutDownData;
import com.example.x6.androidmachinetest.R;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.Core.GetMachineInfo;
import com.example.x6.androidmachinetest.Core.PlanTest;
import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.PreMachineInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartActivity extends Activity {
    private GetMachineInfo getMachineInfo ;
    private MachineInfoData machineInfoData;
    private PlanTest planTest;
    private Debug debug;
    private LinearLayout myLayout;
    private CheckBox[] checkBoxes;
    private int typeNum = 0;
    private int padding = 16;
    private ProgressDialog progressDialog;
    private boolean isDebug = false;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        这里进行判断是否进入RTC测试
         */
        SharedPreferences sharedPreferences = getSharedPreferences("dateBeforeShutDown", Context.MODE_PRIVATE);
        boolean isOk = true;
        String string = "";
        long tempTime = -1;
        string = sharedPreferences.getString("CpuName","null");
        if(string.equals("null"))
            isOk = false;
        AfterShutDownData.getAfterShutDownData().setCpuName(string);
        string = sharedPreferences.getString("MachineType","null");
        if(string.equals("null"))
            isOk = false;
        AfterShutDownData.getAfterShutDownData().setMachineType(string);
        string = sharedPreferences.getString("TempReporterPath","null");
        if(string.equals("null"))
            isOk = false;
        AfterShutDownData.getAfterShutDownData().setTempReporterFilePath(string);
        tempTime = sharedPreferences.getLong("TimeBeforeShutDown",-1);
        if(tempTime == -1)
            isOk = false;
        AfterShutDownData.getAfterShutDownData().setTimeBeforeShutDown(tempTime);

        if(isOk){
            Intent intent = new Intent(this,EndActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        /*
        验证RTC测试到此为止
         */

        myLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_start,null);
        myLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(myLayout);
        machineInfoData = MachineInfoData.getMachineInfoData();
        debug = new Debug();
        planTest = PlanTest.getPlanTest();
        getMachineInfo = new GetMachineInfo();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示：");
        progressDialog.setMessage("正在初始化数据...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getMachineInfo.initMachineInfo(StartActivity.this.getApplicationContext());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();

        /*
        显示CPU型号
         */
        TextView cpuNameDisp = new TextView(this);
        cpuNameDisp.setText("CPU型号："+getMachineInfo.getCupName()+"\n"
                            +"RAM大小："+getMachineInfo.getRAMSize(getApplicationContext())+"\n"
                            +"ROM大小："+getMachineInfo.getExternalMemorySize(getApplicationContext()));
        /*
        初始化设备型号
         */
        machineInfoData.cpuName = getMachineInfo.getCupName();
        machineInfoData.RamSize = getMachineInfo.getRAMSize(this);
        machineInfoData.RomSize = getMachineInfo.getExternalMemorySize(this);
        myLayout.addView(cpuNameDisp, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        /*
        这几个 if语句是根据CPU型号给出型号选择
         */
        if(getMachineInfo.getCupName().equals(PreMachineInfo.A33)){
            typeNum = 6;
            String[] strings = new String[typeNum];
            strings[0] = PreMachineInfo.Y5;
            strings[1] = PreMachineInfo.Y7;
            strings[2] = PreMachineInfo.Y8;
            strings[3] = PreMachineInfo.Y10;
            strings[4] = PreMachineInfo.K7;
            strings[5] = PreMachineInfo.K10;
            setTypeView(typeNum,strings);
        }else if (getMachineInfo.getCupName().equals(PreMachineInfo.H3)){
            typeNum = 3;
            String[] strings = new String[typeNum];
            strings[0] = PreMachineInfo.B301;
            strings[1] = PreMachineInfo.B301_LVDS;
            strings[2] = PreMachineInfo.B301D_ELE01;
            setTypeView(typeNum,strings);
        }else if(getMachineInfo.getCupName().equals(PreMachineInfo.RK3288)){
            typeNum = 2;
            String[] strings = new String[typeNum];
            strings[0] = PreMachineInfo.B701;
            strings[1] = PreMachineInfo.B601;
            setTypeView(typeNum,strings);
        }else if(getMachineInfo.getCupName().equals(PreMachineInfo.RK3368)){ //3368设备目前没有定型

        }
        final Button submit = new Button(this);
        submit.setText("确认");
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(machineInfoData.typeName == null){
                    Toast.makeText(StartActivity.this,"请选择设备型号",Toast.LENGTH_LONG).show();
                }else{
                    myLayout.removeAllViews();
                    setContentView(myLayout);
                    if(machineInfoData.typeName.equals(PreMachineInfo.Y5)
                            ||machineInfoData.typeName.equals(PreMachineInfo.Y7)
                            ||machineInfoData.typeName.equals(PreMachineInfo.Y8)
                            ||machineInfoData.typeName.equals(PreMachineInfo.Y10)
                            ||machineInfoData.typeName.equals(PreMachineInfo.B301D_ELE01)){
                        machineInfoData.ISRS485 = true;

                        /*
                        写界面
                         */
                        LinearLayout lineOne = new LinearLayout(StartActivity.this);
                        lineOne.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout lineTwo = new LinearLayout(StartActivity.this);
                        lineTwo.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
                        final CheckBox supportWifi = new CheckBox(StartActivity.this);
                        supportWifi.setText("支持WiFi模块");
                        supportWifi.setLayoutParams(lineParam);
                        supportWifi.setPadding(padding,padding,padding,padding);
                        final CheckBox notSupportWifi = new CheckBox(StartActivity.this);
                        notSupportWifi.setText("不支持WiFi模块");
                        notSupportWifi.setLayoutParams(lineParam);
                        notSupportWifi.setPadding(padding,padding,padding,padding);
                        lineOne.addView(supportWifi);
                        lineOne.addView(notSupportWifi);

                        final CheckBox supportPhone = new CheckBox(StartActivity.this);
                        final CheckBox notSupportPhone = new CheckBox(StartActivity.this);
                        supportPhone.setText("支持4G模块");
                        notSupportPhone.setText("不支持4G模块");
                        supportPhone.setLayoutParams(lineParam);
                        notSupportPhone.setLayoutParams(lineParam);
                        supportPhone.setPadding(padding,padding,padding,padding);
                        notSupportPhone.setPadding(padding,padding,padding,padding);
                        lineTwo.addView(supportPhone);
                        lineTwo.addView(notSupportPhone);

                        Button submitTwo = new Button(StartActivity.this);
                        submitTwo.setText("确认");

                        LinearLayout.LayoutParams lineParamTwo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        final EditText editText = new EditText(StartActivity.this);
                        editText.setLayoutParams(lineParamTwo);
                        editText.setHint("请录入设备识别号");




                        myLayout.addView(editText);
                        myLayout.addView(lineOne);
                        myLayout.addView(lineTwo);
                        myLayout.addView(submitTwo,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                        supportWifi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notSupportWifi.setChecked(false);
                            }
                        });
                        notSupportWifi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supportWifi.setChecked(false);
                            }
                        });

                        supportPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notSupportPhone.setChecked(false);
                            }
                        });
                        notSupportPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supportPhone.setChecked(false);
                            }
                        });

                        submitTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean isError = false;
                               if((!supportPhone.isChecked() && !notSupportPhone.isChecked())
                                       || (!supportWifi.isChecked() && !notSupportWifi.isChecked())){
                                   Toast.makeText(StartActivity.this,"请选择模块是否支持",Toast.LENGTH_LONG).show();
                               }else{
                                   /*
                                   4G模块的判断
                                    */
                                   String tempPhone = getMachineInfo.getPhoneMoudle();
                                   String tempWifi = null;
                                   try {
                                       tempWifi = getMachineInfo.getWifiModuleName(getApplicationContext());
                                   } catch (InterruptedException e) {
                                       e.printStackTrace();
                                   }
                                   if(supportPhone.isChecked() && tempPhone == null){
                                       showDialog("系统没有检测到4G模块，请检查硬件是否有故障");
                                       isError = true;
                                   }else if(supportPhone.isChecked() && tempPhone != null){
                                       machineInfoData.ISPHONE = true;
                                   }else if(notSupportPhone.isChecked() && tempPhone == null){
                                       machineInfoData.ISPHONE = false;
                                   }
                                   /*
                                   Wifi模块的判断
                                    */
                                       if(supportWifi.isChecked() && tempWifi.equals(PreMachineInfo.NOWIFI) /*这里做判断，前面一定要初始化*/){
                                           showDialog("系统没有检测到WiFi模块，请检查硬件是否有故障");
                                           isError = true;
                                       }else if(supportWifi.isChecked() && !tempWifi.equals(PreMachineInfo.NOWIFI)){
                                           machineInfoData.ISWIFI = true;
                                       }else if(notSupportWifi.isChecked() && tempWifi.equals(PreMachineInfo.NOWIFI)){
                                           machineInfoData.ISWIFI = false;
                                       }

                                    if(!isError){
                                        if(editText.getText().toString().equals("")){
                                            Toast.makeText(StartActivity.this,"请输入识别码",Toast.LENGTH_LONG).show();
                                        }else {
                                            planTest.initMachineInfoData(getApplicationContext(), machineInfoData.typeName,
                                                    replaceBlank(editText.getText().toString()), true, machineInfoData.ISWIFI, machineInfoData.ISPHONE);
                                            planTest.setPlanStep();
                                            if(!isDebug) {
                                                /*进入测试界面*/
                                                Intent intent = new Intent(StartActivity.this,TestActivity.class);
                                                startActivity(intent);
                                                StartActivity.this.finish();
                                            }else {
                                             /*Debug*/
                                                Intent intent1 = new Intent(StartActivity.this, DebugActivity.class);
                                                startActivity(intent1);
                                                StartActivity.this.finish();
                                            }
                                        }
                                    }
                               }
                            }
                        });
                        setContentView(myLayout);


                    }else if(machineInfoData.typeName.equals(PreMachineInfo.K7)
                            ||machineInfoData.typeName.equals(PreMachineInfo.K10)
                            ||machineInfoData.typeName.equals(PreMachineInfo.B301_LVDS)
                            ||machineInfoData.typeName.equals(PreMachineInfo.B301)
                            ||machineInfoData.typeName.equals(PreMachineInfo.B701)
                            ||machineInfoData.typeName.equals(PreMachineInfo.B601)){
                         /*
                        写界面
                         */
                        LinearLayout lineOne = new LinearLayout(StartActivity.this);
                        lineOne.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout lineTwo = new LinearLayout(StartActivity.this);
                        lineTwo.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout lineThree = new LinearLayout(StartActivity.this);
                        lineThree.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
                        final CheckBox supportWifi = new CheckBox(StartActivity.this);
                        supportWifi.setText("支持WiFi模块");
                        supportWifi.setLayoutParams(lineParam);
                        supportWifi.setPadding(padding,padding,padding,padding);
                        final CheckBox notSupportWifi = new CheckBox(StartActivity.this);
                        notSupportWifi.setText("不支持WiFi模块");
                        notSupportWifi.setLayoutParams(lineParam);
                        notSupportWifi.setPadding(padding,padding,padding,padding);
                        lineOne.addView(supportWifi);
                        lineOne.addView(notSupportWifi);

                        final CheckBox supportPhone = new CheckBox(StartActivity.this);
                        final CheckBox notSupportPhone = new CheckBox(StartActivity.this);
                        supportPhone.setText("支持4G模块");
                        notSupportPhone.setText("不支持4G模块");
                        supportPhone.setLayoutParams(lineParam);
                        notSupportPhone.setLayoutParams(lineParam);
                        supportPhone.setPadding(padding,padding,padding,padding);
                        notSupportPhone.setPadding(padding,padding,padding,padding);
                        lineTwo.addView(supportPhone);
                        lineTwo.addView(notSupportPhone);

                        final CheckBox supportRS485 = new CheckBox(StartActivity.this);
                        final CheckBox notSupportRS485 = new CheckBox(StartActivity.this);
                        supportRS485.setText("支持485");
                        notSupportRS485.setText("不支持485");
                        supportRS485.setLayoutParams(lineParam);
                        notSupportRS485.setLayoutParams(lineParam);
                        supportRS485.setPadding(padding,padding,padding,padding);
                        notSupportRS485.setPadding(padding,padding,padding,padding);
                        lineThree.addView(supportRS485);
                        lineThree.addView(notSupportRS485);

                        Button submitTwo = new Button(StartActivity.this);
                        submitTwo.setText("确认");

                        LinearLayout.LayoutParams lineParamTwo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        final EditText editText = new EditText(StartActivity.this);
                        editText.setLayoutParams(lineParamTwo);
                        editText.setHint("请录入设备识别号");


                        myLayout.addView(editText);
                        myLayout.addView(lineOne);
                        myLayout.addView(lineTwo);
                        myLayout.addView(lineThree);
                        myLayout.addView(submitTwo,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                        supportRS485.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notSupportRS485.setChecked(false);
                            }
                        });
                        notSupportRS485.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supportRS485.setChecked(false);
                            }
                        });

                        supportWifi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notSupportWifi.setChecked(false);
                            }
                        });
                        notSupportWifi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supportWifi.setChecked(false);
                            }
                        });

                        supportPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notSupportPhone.setChecked(false);
                            }
                        });
                        notSupportPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supportPhone.setChecked(false);
                            }
                        });

                        submitTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean isError = false;
                                if((!supportPhone.isChecked() && !notSupportPhone.isChecked())
                                        || (!supportWifi.isChecked() && !notSupportWifi.isChecked())
                                        || (!supportRS485.isChecked() && !notSupportRS485.isChecked())){
                                    Toast.makeText(StartActivity.this,"请选择模块是否支持",Toast.LENGTH_LONG).show();
                                }else{
                                   /*
                                   4G模块的判断
                                    */
                                    String tempPhone = getMachineInfo.getPhoneMoudle();
                                    String tempWifi = null;
                                    try {
                                        tempWifi = getMachineInfo.getWifiModuleName(getApplicationContext());
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if(supportPhone.isChecked() && tempPhone == null){
                                        showDialog("系统没有检测到4G模块，请检查硬件是否有故障");
                                        isError = true;
                                    }else if(supportPhone.isChecked() && tempPhone != null){
                                        machineInfoData.ISPHONE = true;
                                    }else if(notSupportPhone.isChecked() && tempPhone == null){
                                        machineInfoData.ISPHONE = false;
                                    }
                                   /*
                                   Wifi模块的判断
                                    */
                                    if(supportWifi.isChecked() && tempWifi.equals(PreMachineInfo.NOWIFI)){
                                        showDialog("系统没有检测到WiFi模块，请检查硬件是否有故障");
                                        isError = true;
                                    }else if(supportWifi.isChecked() && !tempWifi.equals(PreMachineInfo.NOWIFI)){
                                        machineInfoData.ISWIFI = true;
                                    }else if(notSupportWifi.isChecked() && tempWifi.equals(PreMachineInfo.NOWIFI)){
                                        machineInfoData.ISWIFI = false;
                                    }

                                    if(supportRS485.isChecked()){
                                        machineInfoData.ISRS485 = true;
                                    }else if(notSupportRS485.isChecked()){
                                        machineInfoData.ISRS485 = false;
                                    }

                                    if(!isError){
                                        if(editText.getText().toString().equals("")){
                                            Toast.makeText(StartActivity.this,"请输入识别码",Toast.LENGTH_LONG).show();
                                        }else {
                                            planTest.initMachineInfoData(getApplicationContext(), machineInfoData.typeName,
                                                    replaceBlank(editText.getText().toString()), machineInfoData.ISRS485, machineInfoData.ISWIFI, machineInfoData.ISPHONE);
                                            planTest.setPlanStep();

                                            if(!isDebug) {
                                                /*进入测试界面*/
                                                Intent intent = new Intent(StartActivity.this,TestActivity.class);
                                                startActivity(intent);
                                                StartActivity.this.finish();
                                            }else {
                                             /*Debug*/
                                                Intent intent1 = new Intent(StartActivity.this, DebugActivity.class);
                                                startActivity(intent1);
                                                StartActivity.this.finish();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        setContentView(myLayout);
                    }
                }
            }
        });
        myLayout.addView(submit,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        setContentView(myLayout);
    }

    /*
    去掉换行和制表符号
     */
    private String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private void setTypeView(int typeNum1, String[] typeList){
        typeNum = typeNum1;
        int i = 0;
        checkBoxes = new CheckBox[typeNum];
        for (i = 0; i < checkBoxes.length; i++){
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(typeList[i]);
        }
        for(i = 0; i < checkBoxes.length; i++){
            checkBoxes[i].setPadding(padding,padding,padding,padding);
            myLayout.addView(checkBoxes[i], LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            syncCheckBox(checkBoxes, i);
        }
    }
    /**
     * checkBoxs 同时只能选择一个型号 当没有勾选的时候，设备型号为空
     * @param checkBoxs
     * @param i
     */
    private void syncCheckBox(final CheckBox[] checkBoxs, final int i){
        checkBoxs[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int j = 0;
                for(j = 0; j < checkBoxs.length; j++){
                    if(j != i)
                        checkBoxs[j].setChecked(false);
                    if(j == i) {
                        if(checkBoxs[j].isChecked())
                            machineInfoData.typeName = checkBoxs[i].getText().toString();
                        else
                            machineInfoData.typeName = null;
                    }
                }
                debug.logw(machineInfoData.typeName);
            }
        });
    }

    private void showDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
