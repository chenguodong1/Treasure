package com.zhuoxin.treasure;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public class TreasureApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //一般在此处进行初始化操作
        UserPrefs.init(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
    }
}
