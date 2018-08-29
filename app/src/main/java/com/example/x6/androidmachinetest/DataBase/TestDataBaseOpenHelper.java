package com.example.x6.androidmachinetest.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.x6.androidmachinetest.Core.MachineInfoData;

public class TestDataBaseOpenHelper extends SQLiteOpenHelper {
    private Context context;
    /*
        这里测试结果用int 型， 不支持为-1，测试通过为1，测试不通过为0；
     */
    private static final String CREATE_TABLE = "create table if not exists " +
            TestDataBaseInfo.tableName+
            "(ID integer primary key autoincrement," +
            "Name varchar(64) not null," +
            "Type varchar(64) not null," +
            "CPUType varchar(64) not null," +
            "Uuid varchar(64) not null," +
            "LastUpdateDate varchar(64) not null," +
            "CpuSerial varchar (64)," +

            "SpkTestTimes integer default 0," +
            "SpkPassTimes integer DEFAULT 0," +
            "SpkNotPassTimes integer DEFAULT 0," +
            "SpkEndResult integer DEFAULT -1," +

            "HpTestTimes integer DEFAULT 0," +
            "HpPassTimes integer DEFAULT 0," +
            "HpNotPassTimes integer DEFAULT 0," +
            "HpEndResult integer DEFAULT -1," +

            "CameraTestTimes integer DEFAULT 0," +
            "CameraPassTimes integer DEFAULT 0," +
            "CameraNotPassTimes integer DEFAULT 0," +
            "CameraEndResult integer DEFAULT -1," +

            "RecordTestTimes integer DEFAULT 0," +
            "RecordPassTimes integer DEFAULT 0," +
            "RecordNotPassTimes integer DEFAULT 0," +
            "RecordEndResult integer DEFAULT -1," +

            "TFTestTimes integer DEFAULT  0," +
            "TFPassTimes integer DEFAULT 0," +
            "TFNotPassTimes integer DEFAULT 0," +
            "TFEndResult integer DEFAULT -1," +

            "USBTestTimes integer DEFAULT 0," +
            "USBPassTimes integer DEFAULT 0," +
            "USBNotPassTimes integer DEFAULT 0," +
            "USBEndResult integer DEFAULT -1," +

            "EthTestTimes integer DEFAULT 0," +
            "EthPassTimes integer DEFAULT 0," +
            "EthNotPassTimes integer DEFAULT 0," +
            "EthEndResult integer DEFAULT -1," +

            "WifiTestTimes integer DEFAULT 0," +
            "WifiPassTimes integer DEFAULT 0," +
            "WifiNotPassTimes integer DEFAULT 0," +
            "WifiEndResult integer DEFAULT -1," +

            "MobileNetTestTimes integer DEFAULT 0," +
            "MobileNetPassTimes integer DEFAULT 0," +
            "MobileNetNotPassTimes integer DEFAULT 0," +
            "MobileNetEndResult integer DEFAULT -1," +

            "GPSTestTimes integer DEFAULT 0," +
            "GPSPassTimes integer DEFAULT 0," +
            "GPSNotPassTimes integer DEFAULT 0," +
            "GPSEndResult integer DEFAULT -1," +


            "BTTestTimes  integer DEFAULT 0," +
            "BTPassTimes  integer DEFAULT 0," +
            "BTNotPassTimes  integer DEFAULT 0," +
            "BTEndResult  integer DEFAULT -1," +

            "ScreenTestTimes  integer DEFAULT 0," +
            "ScreenPassTimes  integer DEFAULT 0," +
            "ScreenNotPassTimes  integer DEFAULT 0," +
            "ScreenEndResult  integer DEFAULT -1," +

            "TSTestTimes  integer DEFAULT 0," +
            "TSPassTimes  integer DEFAULT 0," +
            "TSNotPassTimes integer DEFAULT 0," +
            "TSEndResult  integer DEFAULT -1," +

            "RS232TestTimes integer DEFAULT 0," +
            "RS232PassTimes integer DEFAULT 0," +
            "RS232NotPassTimes integer DEFAULT 0," +
            "RS232EndResult integer DEFAULT -1," +

            "RS485TestTimes integer DEFAULT 0," +
            "RS485PassTimes integer DEFAULT 0," +
            "RS485NotPassTimes integer DEFAULT 0," +
            "RS485EndResult integer DEFAULT -1," +

            "GPIOTestTimes integer DEFAULT 0," +
            "GPIOPassTimes integer DEFAULT 0," +
            "GPIONotPassTimes integer DEFAULT 0," +
            "GPIOEndResult integer DEFAULT -1," +

            "RTCTestTimes integer DEFAULT 0," +
            "RTCPassTimes integer DEFAULT 0," +
            "RTCNotPassTimes integer DEFAULT 0," +
            "RTCEndResult integer DEFAULT -1," +

            "ISPass integer DEFAULT 0," +

            "TestDateyyyy VARCHAR (4) NOT NULL," +
            "TestDateMM VARCHAR (2) NOT NULL," +
            "TestDatedd VARCHAR (2) NOT NULL," +
            "TestTimeHH VARCHAR (2) NOT NULL," +
            "TestTimemm VARCHAR (2) NOT NULL," +
            "TestTimess VARCHAR (2) NOT NULL" +
            ")";

    public TestDataBaseOpenHelper(Context context) {
        super(context,TestDataBaseInfo.databaseName,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
