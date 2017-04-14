package com.stillwildman.lazyrunner;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by vincent.chang on 2017/4/11.
 */

public class AppController extends MultiDexApplication {

    private static AppController appInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    public static synchronized AppController getInstance() {
        return appInstance;
    }
}
