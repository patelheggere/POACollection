package com.patelheggere.poacollection.activities.splash;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.patelheggere.poacollection.R;
import com.patelheggere.poacollection.activities.newlogin.NewLoginActivity;
import com.patelheggere.poacollection.base.BaseActivity;

import static com.patelheggere.poacollection.utils.AppUtils.Constants.THREE_SECOND;


public class Splash2Activity extends BaseActivity {

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash2;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                    Intent i = new Intent(Splash2Activity.this, NewLoginActivity.class);
                    startActivity(i);

                // close this activity
                finish();

            }



        }, THREE_SECOND);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
