package com.patelheggere.poacollection.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Locale;


public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getName();

    public Context context;
    public Activity activity;
    private String currentLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       // setLocale();
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activity = this;
        setContentView(getContentView());
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
  /*  public void setLocale() {
        Locale myLocale = new Locale(SharedPrefsHelper.getInstance().get(LANGUAGE_SELECTED, "ka"));
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        Resources res = getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
*/
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    protected void onPause() {
        super.onPause();
    }
}
