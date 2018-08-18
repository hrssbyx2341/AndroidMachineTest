package com.example.x6.androidmachinetest.function;

import android.app.Activity;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.CountryRecord;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * Created by 24179 on 2018/7/20.
 */

public class ExcelUtils {
    private WritableWorkbook writableWorkbook;
    private Debug debug;

    public ExcelUtils(){
        debug = new Debug("ExcelUtils");
    }


    /*
    创建Excel
     */
    public boolean createExcel(File  file){
        boolean isok = true;
        WritableSheet writableSheet = null;
        if(file.exists())
            file.delete();
            try {
                writableWorkbook = Workbook.createWorkbook(file);
                writableSheet = writableWorkbook.createSheet("sheet1",0);

                Label lbl1 = new Label(0,0,"项目");
                Label lbl2 = new Label(1,0,"结果");
                Label lbl3 = new Label(2,0,"备注");

                writableSheet.addCell(lbl1);
                writableSheet.addCell(lbl2);
                writableSheet.addCell(lbl3);

                writableWorkbook.write();
                writableWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
                isok = false;
            } catch (RowsExceededException e) {
                e.printStackTrace();
                isok = false;
            } catch (WriteException e) {
                e.printStackTrace();
                isok = false;
            }
            return isok;
    }

    public boolean writeToExcel(String excePath,Object... args){
        boolean isok =true;
        try {
            File exceFile = new File(excePath);
            Workbook oldwwb = Workbook.getWorkbook(exceFile);
            writableWorkbook = Workbook.createWorkbook(exceFile,oldwwb);
            WritableSheet writableSheet = writableWorkbook.getSheet(0);

            int row = writableSheet.getRows();
            Label label1 = new Label(0,row,args[0]+"");
            Label label2 = new Label(1,row,args[1]+"");
            if(args.length >= 3) {
                Label label3 = new Label(2, row, args[2] + "");
                writableSheet.addCell(label3);
            }

            writableSheet.addCell(label1);
            writableSheet.addCell(label2);


            writableWorkbook.write();
            writableWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            isok = false;
        } catch (BiffException e) {
            e.printStackTrace();
            isok = false;
        } catch (RowsExceededException e) {
            e.printStackTrace();
            isok = false;
        } catch (WriteException e) {
            e.printStackTrace();
            isok = false;
        }
        return isok;
    }

}
