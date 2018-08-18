package com.example.x6.androidmachinetest.function;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.x6.androidmachinetest.R;


/**
 * Created by 24179 on 2018/7/19.
 */

public class ScreenTestActivity extends Activity {
    private Debug debug;
    private LinearLayout linearLayout;
    private Button checkscreen;
    private int pageNum = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug = new Debug("ScreenTestActivity");
        full_screen();
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        checkscreen = new Button(this);
        checkscreen.setBackgroundResource(R.mipmap.screen);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.addView(checkscreen, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        checkscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debug.logd("按一下");
                full_screen();
                switch (pageNum){
                    case 0:
                        ScreenTestActivity.this.finish();
                        break;
                    case 1:
                        checkscreen.setBackgroundResource(R.drawable.screen_test_o);
                        setLight(ScreenTestActivity.this,255);
                        break;
                    case 2:
                        checkscreen.setBackgroundResource(R.drawable.screen_test_t);
                        setLight(ScreenTestActivity.this,10);
                        break;
                    case 3:
                        checkscreen.setBackgroundColor(Color.parseColor("#ff0000"));
                        setLight(ScreenTestActivity.this,255);
                        break;
                    case 4:
                        checkscreen.setBackgroundColor(Color.parseColor("#00ff00"));
                        setLight(ScreenTestActivity.this,10);
                        break;
                    case 5:
                        checkscreen.setBackgroundColor(Color.parseColor("#0000ff"));
                        setLight(ScreenTestActivity.this,255);
                        break;
                    case 6:
                        checkscreen.setBackgroundColor(Color.parseColor("#ffffff"));
                        setLight(ScreenTestActivity.this,10);
                        break;
                    case 7:
                        checkscreen.setBackgroundColor(Color.parseColor("#000000"));
                        setLight(ScreenTestActivity.this,255);
                        break;
                    default:
                        break;
                }
                pageNum--;
            }
        });

        setContentView(linearLayout);
    }

    private void setLight(Activity context, int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }


    public void full_screen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }
}
