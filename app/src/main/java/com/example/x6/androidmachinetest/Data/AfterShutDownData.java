package com.example.x6.androidmachinetest.Data;

import com.example.x6.androidmachinetest.Core.MachineInfoData;

/**
 * Created by 24179 on 2018/7/20.
 */

public class AfterShutDownData {
    private static AfterShutDownData afterShutDownData = null;
    private AfterShutDownData(){}

    public static AfterShutDownData getAfterShutDownData(){
        synchronized (AfterShutDownData.class){
            if(afterShutDownData == null)
                afterShutDownData = new AfterShutDownData();
            return afterShutDownData;
        }
    }

    private String CpuName = null;
    private String MachineType = null;
    private long TimeBeforeShutDown = -1;
    private String TempReporterFilePath = null;

    public void setCpuName(String string){
        CpuName = string;
    }
    public String getCpuName(){
        return CpuName;
    }
    public void setMachineType(String string){
        MachineType = string;
    }
    public String getMachineType(){
        return MachineType;
    }
    public void setTimeBeforeShutDown(long time){
        TimeBeforeShutDown = time;
    }
    public long getTimeBeforeShutDown(){
        return TimeBeforeShutDown;
    }
    public void setTempReporterFilePath(String string){
        TempReporterFilePath = string;
    }
    public String getTempReporterFilePath(){
        return TempReporterFilePath;
    }
}
