package com.example.x6.androidmachinetest.function;

import android.util.Log;

/**
 * Created by 24179 on 2018/7/7.
 */

public class Debug {
    private String TAG = null;
    private static final boolean DBG = true;

    public Debug(){
        this.TAG = "AndroidMachineTest";
    }
    public Debug(String TAG){
        this.TAG = TAG;
    }
    public void loge(String string){
        if(DBG){
            if(string != null)
                Log.e(TAG,string);
            else
                Log.e(TAG,"NULL");
        }
    }
    public void logd(String string){
        if(DBG){
            if(string != null)
                Log.d(TAG,string);
            else
                Log.d(TAG,"NULL");
        }
    }
    public void logi(String string){
        if(DBG){
            if(string != null)
                Log.i(TAG,string);
            else
                Log.i(TAG,"NULL");
        }
    }
    public void logw(String string){
        if(DBG){
            if(string != null)
                Log.w(TAG,string);
            else
                Log.w(TAG,"NULL");
        }
    }
}
