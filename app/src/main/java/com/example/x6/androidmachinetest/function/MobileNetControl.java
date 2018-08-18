package com.example.x6.androidmachinetest.function;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by 24179 on 2018/7/18.
 */

public class MobileNetControl {
    public MobileNetControl(){
        suCommand = new SuCommand();
    }

    public boolean disableData(){
        String cmd = "svc data disable";
        boolean isOk = true;
        if(suCommand.execRootCmdSilent(cmd) != 0)
            isOk = false;
        cmd = "settings put global mobile_data 0";
        if(suCommand.execRootCmdSilent(cmd) != 0)
            isOk = false;
        return isOk;
    }
    public boolean enableData(){
        String cmd = "svc data enable";
        boolean isOk = true;
        if(suCommand.execRootCmdSilent(cmd) != 0)
            isOk = false;
        cmd = "settings put global mobile_data 1";
        if(suCommand.execRootCmdSilent(cmd) != 0)
            isOk = false;
        return isOk;
    }

    private SuCommand suCommand;
    private final static String COMMAND_AIRPLANE_ON = "settings put global airplane_mode_on 1 \n" +
            "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n";

    private final static String COMMAND_AIRPLANE_OFF = "settings put global airplane_mode_on 0 \n" +
            " am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n ";
    public void OpenFlyMode(){
        suCommand.execRootCmdSilent(COMMAND_AIRPLANE_ON);
    }
    public void CloseFlaMode(){
        suCommand.execRootCmdSilent(COMMAND_AIRPLANE_OFF);
    }

    private static final int MOBIL_NET = 8888;
    private static final int WIFI_NET = 6666;
    private static final int ETH_NET = 6688;
    private static final int NO_LINK = 8866;
    private static int get_net_state(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(dataNetworkInfo.isConnected()){
            return MOBIL_NET;
        }else{
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo.isConnected()){
                return WIFI_NET;
            }else{
                try {
                    NetworkInfo netNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                    if(netNetworkInfo.isConnected()){
                        return ETH_NET;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return NO_LINK;
    }
    public static int isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            return NO_LINK;
        }
        else
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        Log.e("网络情况","网络可用");
                        return get_net_state(context);
                    }
                }
            }
        }
        Log.e("网络情况","网络不可用");
        return NO_LINK;
    }
}
