package com.example.x6.androidmachinetest.Activity.UI;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Core.Result;
import com.example.x6.androidmachinetest.function.Debug;
import com.example.x6.androidmachinetest.function.EthControl;
import com.example.x6.androidmachinetest.function.ExcelUtils;
import com.example.x6.androidmachinetest.function.MobileNetControl;
import com.example.x6.androidmachinetest.function.TimeSetting.DateUtils;
import com.example.x6.androidmachinetest.function.TimeSetting.MyDatePickDialog;
import com.example.x6.androidmachinetest.function.TimeSetting.MyTimePickDialog;
import com.example.x6.androidmachinetest.function.WifiControl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;

/**
 * Created by 24179 on 2018/7/19.
 */

public class RTC extends AutoCommon{
    private Debug debug;
    private Button notSetTime;
    private Button setTime;
    private TextView explain;
    private TextView show;
    private String reporterFile = null;
    private Result result;
    private Handler handler;
    private Context context;
    private ProgressDialog progressDialog;
    private MyDatePickDialog datePickDialog;
    private MyTimePickDialog timePickDialog;
    private DateUtils dateUtils;
    private int Year = 0, Month = 0, Day = 0, Hour = 0, Minute = 0, Second = 0;


    public RTC(final Context context, String Title, final Handler handler) {
        super(context, Title);



        setReporterFileName();
        result = Result.getResult();
        debug = new Debug("RTC");
        dateUtils = new DateUtils();
        this.handler = handler;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示：");
        progressDialog.setMessage("正在生成临时测试报告...");
        getLineout().removeAllViews();
        notSetTime = new Button(context);
        setTime = new Button(context);
        explain = new TextView(context);
        show = new TextView(context);
        show.setTextColor(Color.BLACK);
        notSetTime.setText("测试中已经完成网络对时，直接点击这个按钮保存此次时间");
        setTime.setText("测试中未进行网络对时，点击这个按钮进行时间设置");

        explain.setText("RTC 测试步骤：\n①、设置正确的时间\n②、保存时间到文件中\n③、断电重启设备\n④、重启后启动本测试程序\n" +
                "程序重启后会把断电前保存的时间和当前时间进行比对，判断时间有没有正常被保存，如果时间相差不大则RTC测试通过，否则RTC测试不通过");

        getLineout().addView(notSetTime, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getLineout().addView(setTime, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getLineout().addView(explain,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getLineout().addView(show, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        notSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllTestResult();
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePick(true);
            }
        });

        datePickDialog = new MyDatePickDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                debug.logd(String.valueOf(i)+" "+String.valueOf(i1)+" "+String.valueOf(i2));
                Year = i;
                Month = i1;
                Day = i2;
                showTimePick(true);
            }
        },2018,0,0);
        datePickDialog.setCanceledOnTouchOutside(false);
        datePickDialog.setCancelable(false);

        timePickDialog = new MyTimePickDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Hour = i;
                Minute = i1;
                debug.logd(String.valueOf(Year)+"  "+String.valueOf(Month)+"  "+String.valueOf(Day)+
                        "  "+String.valueOf(Hour)+"  "+String.valueOf(Minute)+"  "+String.valueOf(Second));
                startManualTiming();
                show.setText("时间已经设置为："+getCurrentTime());
            }
        },0,0,true);
        timePickDialog.setCanceledOnTouchOutside(false);
        timePickDialog.setCancelable(false);
    }

    @Override
    public void startTest(Context context, Handler handler) {
        super.startTest(context, handler);
        show.setText("当前时间"+getCurrentTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startManualTiming(){
        dateUtils.setDate(Year,Month,Day);
        dateUtils.setTime(Hour,Minute);
    };
    private void showTimePick(boolean isShowTimePick){
        if(isShowTimePick)
            timePickDialog.show();
        else
            timePickDialog.dismiss();
    }
    private void showDatePick(boolean isShowDatePick){
        if(isShowDatePick)
            datePickDialog.show();
        else
            datePickDialog.dismiss();
    }


    private void saveAllTestResult(){
        notSetTime.setEnabled(false);
        setTime.setEnabled(false);
        new ExcelUtils().createExcel(new File(result.getTestReporterPtah()+reporterFile)); //先创建表格
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.show();
                    }
                });

                /*
                关闭所有网络
                 */
                new WifiControl(context).WifiClose();
                new EthControl().ethClose();
                new MobileNetControl().disableData(); //关闭移动网络

                if(result.saveResult(result.getTestReporterPtah()+reporterFile)){
                    debug.logd("生成临时测试报告成功");
                    if(saveDataBeforeShutDown()) {
                        debug.logd("生成掉点前文件成功");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(context, "生成临时测试文件成功", Toast.LENGTH_LONG).show();
                                show.setText("请断电重新启动设备并启动本程序" + "\n当前时间" + getCurrentTime());
                                show.setTextColor(Color.BLUE);
                            }
                        });
                    }else{
                        debug.loge("生成掉电前文件失败");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(context,"生成临时测试文件失败",Toast.LENGTH_LONG).show();
                                show.setText("生成临时测试文件失败，请重新生成"+"\n当前时间"+getCurrentTime());
                                show.setTextColor(Color.RED);
                                notSetTime.setEnabled(true);
                                setTime.setEnabled(true);
                            }
                        });
                    }
                }else{
                    debug.loge("生成临时文件失败");
                    progressDialog.dismiss();
                    Toast.makeText(context,"生成临时测试文件失败",Toast.LENGTH_LONG).show();
                    show.setText("生成临时测试文件失败，请重新生成"+"\n当前时间"+getCurrentTime());
                    show.setTextColor(Color.RED);
                    notSetTime.setEnabled(true);
                    setTime.setEnabled(true);
                }
            }
        }).start();
    }

    private String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return  simpleDateFormat.format(date);
    }

    private boolean saveDataBeforeShutDown(){
        boolean isok = true;
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("dateBeforeShutDown",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CpuName",MachineInfoData.getMachineInfoData().cpuName);
        editor.putString("MachineType",MachineInfoData.getMachineInfoData().typeName);
        editor.putLong("TimeBeforeShutDown",System.currentTimeMillis());
        editor.putString("TempReporterPath",result.getTestReporterPtah()+reporterFile);
        editor.commit();
        if(!sharedPreferences.getString("CpuName","null").equals(MachineInfoData.getMachineInfoData().cpuName))
            isok = false;
        if(!sharedPreferences.getString("MachineType","null").equals(MachineInfoData.getMachineInfoData().typeName))
            isok = false;
        if(!sharedPreferences.getString("TempReporterPath","null").equals(result.getTestReporterPtah()+reporterFile))
            isok = false;
        return isok;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void startTest(Context context) {
        super.startTest(context);
    }

    private void setReporterFileName(){
        reporterFile = MachineInfoData.getMachineInfoData().typeName+"_"+MachineInfoData.getMachineInfoData().UUID+".xls";
    }
}
