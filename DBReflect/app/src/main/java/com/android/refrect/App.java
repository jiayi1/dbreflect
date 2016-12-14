package com.android.refrect;

import android.app.Application;
import android.content.Context;

/**
 * Created by yulong.liu on 2016/12/14.
 */

public class App extends Application {

    public static Context appContext;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appContext = this;
    }
}
