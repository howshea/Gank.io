package com.haipo.gankio;

import android.app.Application;

/**
 * Created by haipo on 2016/10/18.
 */

public class MyApp extends Application {

    public Application getContext(){
        return MyApp.this;
    }
}
