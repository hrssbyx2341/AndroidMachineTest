package com.example.x6.androidmachinetest.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;

import com.example.x6.androidmachinetest.Core.MachineInfoData;
import com.example.x6.androidmachinetest.Data.PreData;
import com.example.x6.androidmachinetest.function.Debug;

import junit.framework.Test;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDataBaseUtils {
    private TestDataBaseOpenHelper testDataBaseOpenHelper;
    private Context context;
    private Debug debug;
    private static TestDataBaseUtils testDataBaseUtils = null;

    public static TestDataBaseUtils getTestDataBaseUtils(Context context) {
        synchronized (TestDataBaseUtils.class){
            if(testDataBaseUtils == null)
                testDataBaseUtils = new TestDataBaseUtils(context);
        }
        return testDataBaseUtils;
    }

    private TestDataBaseUtils(Context context){
        this.context = context;
        debug = new Debug("TestDataBaseUtils");
        testDataBaseOpenHelper = new TestDataBaseOpenHelper(this.context);
        SQLiteDatabase dbw = testDataBaseOpenHelper.getWritableDatabase();
        SQLiteDatabase dbr = testDataBaseOpenHelper.getReadableDatabase();

        Cursor cursor = dbr.query(TestDataBaseInfo.tableName,null,null,null,null,null,null);
        if(!cursor.moveToFirst()) { //表格中没有数据
            dbw.execSQL("insert into " +TestDataBaseInfo.tableName+
                    "(Name,Type,CPUType,Uuid,LastUpdateDate,TestDateyyyy,TestDateMM,TestDatedd,TestTimeHH,TestTimemm,TestTimess)" +
                    " values('initdata','initdata','initdata','initdata','initdata','initdata','initdata','initdata','initdata','initdata','initdata')");
        }
        dbr.close();
        dbw.close();
    }

    private void execDBWriteCommand(String cmd){
        SQLiteDatabase dbw =  testDataBaseOpenHelper.getWritableDatabase();
        dbw.execSQL(cmd);
        dbw.close();
    }

    private void updateData(String Key,String values){
        String cmd = "update "+TestDataBaseInfo.tableName+" set "+Key+"='"+values+"' "+"where ID=1";
        execDBWriteCommand(cmd);
    }

    public void updateName(String Name){
        updateData("Name",Name);
    }
    public void updateType(String Type){
        updateData("Type",Type);
    }

    public void updateCpuType(String CPUType){
        updateData("CPUType",CPUType);
    }
    public void updateUuid(String Uuid){
        updateData("Uuid",Uuid);
    }
    public void updateLastUpdateDate(String LastUpdateDate){
        updateData("LastUpdateDate",LastUpdateDate);
    }
    public void updateCpuSerial(String CpuSerial){
        updateData("CpuSerial",CpuSerial);
    }
    public void updateTestDateTime(long date){
        Date date1 = new Date(date);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
        updateData("TestDateyyyy",simpleDateFormat1.format(date1));
        SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("MM");
        updateData("TestDateMM",simpleDateFormat5.format(date1));
        SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("dd");
        updateData("TestDatedd",simpleDateFormat6.format(date1));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH");
        updateData("TestTimeHH",simpleDateFormat2.format(date1));
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("mm");
        updateData("TestTimemm",simpleDateFormat3.format(date1));
        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("ss");
        updateData("TestTimess",simpleDateFormat4.format(date1));
    }

    /**************************************************************/
    /*                    上传测试结果到服务器                    */
    /**************************************************************/
    public int upLoad(){
        URL url;
        try {
            url = new URL(PreData.ServiceAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("ser-Agent", "Fiddler");
            connection.setRequestProperty("Content-Type","application/json");
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonEncodeFromDB().getBytes());
            outputStream.close();

            int code = connection.getResponseCode();
            debug.loge("服务器回应："+String.valueOf(code));
            if(code == 200){
                InputStream inputStream = connection.getInputStream();
                String res = readBytes(inputStream);
                debug.loge("服务器回应码："+res);
                if(res.equals("0")){
                    return 0;
                }else{
                    return -999;
                }
            }else{
                return -2;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String readBytes(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1){
            byteArrayOutputStream.write(buffer,0,len);
        }
        byteArrayOutputStream.close();
        return new String (byteArrayOutputStream.toByteArray());
    }

    /**************************************************************/
    /*              测试结果生成json字符串                        */
    /**************************************************************/

    public String jsonEncodeFromDB(){
        String result = null;
        SQLiteDatabase dbr = testDataBaseOpenHelper.getReadableDatabase();
        String sql = "select * from "+TestDataBaseInfo.tableName+" where ID=1";
        JSONObject jsonObject = new JSONObject();
        String[] res = new String[]{};
        Cursor cursor = dbr.rawQuery(sql,res);

        if(cursor.moveToFirst()){
                try {
                    jsonObject.put("USER",TestDataBaseInfo.USER);
                    jsonObject.put("PASSWORD",TestDataBaseInfo.PASSWORD);
                    jsonObject.put("Name",cursor.getString(cursor.getColumnIndex("Type"))+"_"+cursor.getString(cursor.getColumnIndex("Uuid")));
                    jsonObject.put("Type",cursor.getString(cursor.getColumnIndex("Type")));
                    jsonObject.put("CPUType",cursor.getString(cursor.getColumnIndex("CPUType")));
                    jsonObject.put("Uuid",cursor.getString(cursor.getColumnIndex("Uuid")));
                    jsonObject.put("LastUpdateDate",cursor.getString(cursor.getColumnIndex("LastUpdateDate")));
                    jsonObject.put("CpuSerial",cursor.getString(cursor.getColumnIndex("CpuSerial")));
                    jsonObject.put("SpkTestTimes",cursor.getString(cursor.getColumnIndex("SpkTestTimes")));
                    jsonObject.put("SpkPassTimes",cursor.getString(cursor.getColumnIndex("SpkPassTimes")));
                    jsonObject.put("SpkNotPassTimes",cursor.getString(cursor.getColumnIndex("SpkNotPassTimes")));
                    jsonObject.put("SpkEndResult",cursor.getString(cursor.getColumnIndex("SpkEndResult")));
                    jsonObject.put("HpTestTimes",cursor.getString(cursor.getColumnIndex("HpTestTimes")));
                    jsonObject.put("HpPassTimes",cursor.getString(cursor.getColumnIndex("HpPassTimes")));
                    jsonObject.put("HpNotPassTimes",cursor.getString(cursor.getColumnIndex("HpNotPassTimes")));
                    jsonObject.put("HpEndResult",cursor.getString(cursor.getColumnIndex("HpEndResult")));
                    jsonObject.put("CameraTestTimes",cursor.getString(cursor.getColumnIndex("CameraTestTimes")));
                    jsonObject.put("CameraPassTimes",cursor.getString(cursor.getColumnIndex("CameraPassTimes")));
                    jsonObject.put("CameraNotPassTimes",cursor.getString(cursor.getColumnIndex("CameraNotPassTimes")));
                    jsonObject.put("CameraEndResult",cursor.getString(cursor.getColumnIndex("CameraEndResult")));
                    jsonObject.put("RecordTestTimes",cursor.getString(cursor.getColumnIndex("RecordTestTimes")));
                    jsonObject.put("RecordPassTimes",cursor.getString(cursor.getColumnIndex("RecordPassTimes")));
                    jsonObject.put("RecordNotPassTimes",cursor.getString(cursor.getColumnIndex("RecordNotPassTimes")));
                    jsonObject.put("RecordEndResult",cursor.getString(cursor.getColumnIndex("RecordEndResult")));
                    jsonObject.put("TFTestTimes",cursor.getString(cursor.getColumnIndex("TFTestTimes")));
                    jsonObject.put("TFPassTimes",cursor.getString(cursor.getColumnIndex("TFPassTimes")));
                    jsonObject.put("TFNotPassTimes",cursor.getString(cursor.getColumnIndex("TFNotPassTimes")));
                    jsonObject.put("TFEndResult",cursor.getString(cursor.getColumnIndex("TFEndResult")));
                    jsonObject.put("USBTestTimes",cursor.getString(cursor.getColumnIndex("USBTestTimes")));
                    jsonObject.put("USBPassTimes",cursor.getString(cursor.getColumnIndex("USBPassTimes")));
                    jsonObject.put("USBNotPassTimes",cursor.getString(cursor.getColumnIndex("USBNotPassTimes")));
                    jsonObject.put("USBEndResult",cursor.getString(cursor.getColumnIndex("USBEndResult")));
                    jsonObject.put("EthTestTimes",cursor.getString(cursor.getColumnIndex("EthTestTimes")));
                    jsonObject.put("EthPassTimes",cursor.getString(cursor.getColumnIndex("EthPassTimes")));
                    jsonObject.put("EthNotPassTimes",cursor.getString(cursor.getColumnIndex("EthNotPassTimes")));
                    jsonObject.put("EthEndResult",cursor.getString(cursor.getColumnIndex("EthEndResult")));
                    jsonObject.put("WifiTestTimes",cursor.getString(cursor.getColumnIndex("WifiTestTimes")));
                    jsonObject.put("WifiPassTimes",cursor.getString(cursor.getColumnIndex("WifiPassTimes")));
                    jsonObject.put("WifiNotPassTimes",cursor.getString(cursor.getColumnIndex("WifiNotPassTimes")));
                    jsonObject.put("WifiEndResult",cursor.getString(cursor.getColumnIndex("WifiEndResult")));
                    jsonObject.put("MobileNetTestTimes",cursor.getString(cursor.getColumnIndex("MobileNetTestTimes")));
                    jsonObject.put("MobileNetPassTimes",cursor.getString(cursor.getColumnIndex("MobileNetPassTimes")));
                    jsonObject.put("MobileNetNotPassTimes",cursor.getString(cursor.getColumnIndex("MobileNetNotPassTimes")));
                    jsonObject.put("MobileNetEndResult",cursor.getString(cursor.getColumnIndex("MobileNetEndResult")));
                    jsonObject.put("GPSTestTimes",cursor.getString(cursor.getColumnIndex("GPSTestTimes")));
                    jsonObject.put("GPSPassTimes",cursor.getString(cursor.getColumnIndex("GPSPassTimes")));
                    jsonObject.put("GPSNotPassTimes",cursor.getString(cursor.getColumnIndex("GPSNotPassTimes")));
                    jsonObject.put("GPSEndResult",cursor.getString(cursor.getColumnIndex("GPSEndResult")));
                    jsonObject.put("BTTestTimes",cursor.getString(cursor.getColumnIndex("BTTestTimes")));
                    jsonObject.put("BTPassTimes",cursor.getString(cursor.getColumnIndex("BTPassTimes")));
                    jsonObject.put("BTNotPassTimes",cursor.getString(cursor.getColumnIndex("BTNotPassTimes")));
                    jsonObject.put("BTEndResult",cursor.getString(cursor.getColumnIndex("BTEndResult")));
                    jsonObject.put("ScreenTestTimes",cursor.getString(cursor.getColumnIndex("ScreenTestTimes")));
                    jsonObject.put("ScreenPassTimes",cursor.getString(cursor.getColumnIndex("ScreenPassTimes")));
                    jsonObject.put("ScreenNotPassTimes",cursor.getString(cursor.getColumnIndex("ScreenNotPassTimes")));
                    jsonObject.put("ScreenEndResult",cursor.getString(cursor.getColumnIndex("ScreenEndResult")));
                    jsonObject.put("TSTestTimes",cursor.getString(cursor.getColumnIndex("TSTestTimes")));
                    jsonObject.put("TSPassTimes",cursor.getString(cursor.getColumnIndex("TSPassTimes")));
                    jsonObject.put("TSNotPassTimes",cursor.getString(cursor.getColumnIndex("TSNotPassTimes")));
                    jsonObject.put("TSEndResult",cursor.getString(cursor.getColumnIndex("TSEndResult")));
                    jsonObject.put("RS232TestTimes",cursor.getString(cursor.getColumnIndex("RS232TestTimes")));
                    jsonObject.put("RS232PassTimes",cursor.getString(cursor.getColumnIndex("RS232PassTimes")));
                    jsonObject.put("RS232NotPassTimes",cursor.getString(cursor.getColumnIndex("RS232NotPassTimes")));
                    jsonObject.put("RS232EndResult",cursor.getString(cursor.getColumnIndex("RS232EndResult")));
                    jsonObject.put("RS485TestTimes",cursor.getString(cursor.getColumnIndex("RS485TestTimes")));
                    jsonObject.put("RS485PassTimes",cursor.getString(cursor.getColumnIndex("RS485PassTimes")));
                    jsonObject.put("RS485NotPassTimes",cursor.getString(cursor.getColumnIndex("RS485NotPassTimes")));
                    jsonObject.put("RS485EndResult",cursor.getString(cursor.getColumnIndex("RS485EndResult")));
                    jsonObject.put("GPIOTestTimes",cursor.getString(cursor.getColumnIndex("GPIOTestTimes")));
                    jsonObject.put("GPIOPassTimes",cursor.getString(cursor.getColumnIndex("GPIOPassTimes")));
                    jsonObject.put("GPIONotPassTimes",cursor.getString(cursor.getColumnIndex("GPIONotPassTimes")));
                    jsonObject.put("GPIOEndResult",cursor.getString(cursor.getColumnIndex("GPIOEndResult")));
                    jsonObject.put("RTCTestTimes",cursor.getString(cursor.getColumnIndex("RTCTestTimes")));
                    jsonObject.put("RTCPassTimes",cursor.getString(cursor.getColumnIndex("RTCPassTimes")));
                    jsonObject.put("RTCNotPassTimes",cursor.getString(cursor.getColumnIndex("RTCNotPassTimes")));
                    jsonObject.put("RTCEndResult",cursor.getString(cursor.getColumnIndex("RTCEndResult")));
                    jsonObject.put("ISPass",cursor.getString(cursor.getColumnIndex("ISPass")));
                    jsonObject.put("Test_Dateyyyy",cursor.getString(cursor.getColumnIndex("TestDateyyyy")));
                    jsonObject.put("Test_DateMM",cursor.getString(cursor.getColumnIndex("TestDateMM")));
                    jsonObject.put("Test_Datedd",cursor.getString(cursor.getColumnIndex("TestDatedd")));
                    jsonObject.put("Test_TimeHH",cursor.getString(cursor.getColumnIndex("TestTimeHH")));
                    jsonObject.put("Test_Timemm",cursor.getString(cursor.getColumnIndex("TestTimemm")));
                    jsonObject.put("Test_Timess",cursor.getString(cursor.getColumnIndex("TestTimess")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        dbr.close();
        result = jsonObject.toString();
        return result;
    }


    /**************************************************************/
    /*              测试结果总结数据库操作                        */
    /**************************************************************/

    public boolean isTotalPass(){
        SQLiteDatabase dbw = testDataBaseOpenHelper.getWritableDatabase();
        SQLiteDatabase dbr = testDataBaseOpenHelper.getReadableDatabase();
        String[] Times = new String[]{};
        boolean result = true;
        Cursor cursor = dbr.rawQuery("select * from "+TestDataBaseInfo.tableName+" where ID=1",Times);
        int[] endResults = new int[17];
        int i = 0;
        if(cursor.moveToFirst()){
                endResults[0] = cursor.getInt(cursor.getColumnIndex("SpkEndResult"));
                endResults[1] = cursor.getInt(cursor.getColumnIndex("HpEndResult"));
                endResults[2] = cursor.getInt(cursor.getColumnIndex("CameraEndResult"));
                endResults[3] = cursor.getInt(cursor.getColumnIndex("RecordEndResult"));
                endResults[4] = cursor.getInt(cursor.getColumnIndex("TFEndResult"));
                endResults[5] = cursor.getInt(cursor.getColumnIndex("USBEndResult"));
                endResults[6] = cursor.getInt(cursor.getColumnIndex("WifiEndResult"));
                endResults[7] = cursor.getInt(cursor.getColumnIndex("MobileNetEndResult"));
                endResults[8] = cursor.getInt(cursor.getColumnIndex("GPSEndResult"));
                endResults[9] = cursor.getInt(cursor.getColumnIndex("BTEndResult"));
                endResults[10] = cursor.getInt(cursor.getColumnIndex("ScreenEndResult"));
                endResults[11] = cursor.getInt(cursor.getColumnIndex("TSEndResult"));
                endResults[12] = cursor.getInt(cursor.getColumnIndex("RS232EndResult"));
                endResults[13] = cursor.getInt(cursor.getColumnIndex("RS485EndResult"));
                endResults[14] = cursor.getInt(cursor.getColumnIndex("GPIOEndResult"));
                endResults[15] = cursor.getInt(cursor.getColumnIndex("EthEndResult"));
                endResults[16] = cursor.getInt(cursor.getColumnIndex("RTCEndResult"));
        }
        for(int j : endResults){
            debug.loge(String.valueOf(j));
            if(j==0)
                result = false;
        }
        dbr.close();
        dbw.close();
        return result;
    }
    public void updateISPass(){
        String string = "";
        if(isTotalPass()){
            string = "update "+TestDataBaseInfo.tableName+" set ISPass=1"
                    +" where ID=1";
        }else{
            string = "update "+TestDataBaseInfo.tableName+" set ISPass=0"
                    +" where ID=1";
        }
        execDBWriteCommand(string);
    }

    /**************************************************************/
    /*              测试数据数据库操作                            */
    /**************************************************************/


    public static final int PASS = 1;
    public static final int NOTPASS = 0;
    public static final int UNSUPPORT = -1;
    /**
     * 返回测试次数数组
     * @param key
     * @return 索引值0 对应测试总次数，1 对应测试通过次数，2 对应测试不通过次数 为空操作不成功
     */
    private int[] getTimes(String key){
        SQLiteDatabase dbr = testDataBaseOpenHelper.getReadableDatabase();
        String[] Times = new String[]{};
        int[] result = null;
        Cursor cursor = dbr.rawQuery("select * from "+TestDataBaseInfo.tableName+" where ID=1",Times);
        if(cursor.moveToFirst()){
            int TestTimes = cursor.getInt(cursor.getColumnIndex(key+"TestTimes"));
            int PassTimes = cursor.getInt(cursor.getColumnIndex(key+"PassTimes"));
            int NotPassTimes = cursor.getInt(cursor.getColumnIndex(key+"NotPassTimes"));
            result = new int[]{TestTimes,PassTimes,NotPassTimes};
        }
        dbr.close();
        return result;
    }

    /**
     * 添加次数
     * @param Key 测试项目
     * @param ISPass 是否通过 0为不通过，1为通过，-1为不支持
     * @return 参数正确返回true 参数错误返回false
     */
    private boolean updateAddTestData(String Key, int ISPass){
        String TestTimesKey = Key+"TestTimes";
        String PassTimesKey = Key+"PassTimes";
        String NotPassTimesKey = Key+"NotPassTimes";
        String EndResultKey = Key+"EndResult";
        int TestTimesValue = 0;
        int NotPassTimesValue = 0;
        int PassTimesValue = 0;
        int[] Times = getTimes(Key);
        NotPassTimesValue = Times[2];
        PassTimesValue = Times[1];
        TestTimesValue = Times[0];
        if(ISPass == 0){
            NotPassTimesValue += 1;
            TestTimesValue += +1;
        }else if(ISPass == 1){
            PassTimesValue += 1;
            TestTimesValue += 1;
        }else if(ISPass == -1){
            PassTimesValue = 0;
            TestTimesValue = 0;
            NotPassTimesValue = 0;
        }else{
            return false;
        }

        String cmd = "update "+TestDataBaseInfo.tableName+" set "
                +TestTimesKey+"="+TestTimesValue+","
                +NotPassTimesKey+"="+NotPassTimesValue+","
                +PassTimesKey+"="+PassTimesValue+","
                +EndResultKey+"="+String.valueOf(ISPass)
                +" where ID=1";
        execDBWriteCommand(cmd);
        return true;
    }


    public void updateSpk(int isPass){
        updateAddTestData("Spk",isPass);
    }
    public void updateHp(int isPass){
        updateAddTestData("Hp",isPass);
    }
    public void updateCamera(int isPass){updateAddTestData("Camera",isPass);}
    public void updateRecord(int isPass){
        updateAddTestData("Record",isPass);
    }
    public void updateTF(int isPass){
        updateAddTestData("TF",isPass);
    }
    public void updateUSB(int isPass){
        updateAddTestData("USB",isPass);
    }
    public void updateEth(int isPass){
        updateAddTestData("Eth",isPass);
    }
    public void updateWifi(int isPass){
        updateAddTestData("Wifi",isPass);
    }
    public void updateMobileNet(int isPass){
        updateAddTestData("MobileNet",isPass);
    }
    public void updateGPS(int isPass){
        updateAddTestData("GPS",isPass);
    }
    public void updateBT(int isPass){
        updateAddTestData("BT",isPass);
    }
    public void updateScreen(int isPass){
        updateAddTestData("Screen",isPass);
    }
    public void updateTS(int isPass){
        updateAddTestData("TS",isPass);
    }
    public void updateRS232(int isPass){
        updateAddTestData("RS232",isPass);
    }
    public void updateRS485(int isPass){
        updateAddTestData("RS485",isPass);
    }
    public void updateGPIO(int isPass){
        updateAddTestData("GPIO",isPass);
    }
    public void updateRTC(int isPass){
        updateAddTestData("RTC",isPass);
    }
}
