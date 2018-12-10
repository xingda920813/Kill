package com.xdandroid.lib;

import android.app.*;
import android.content.*;

public final class App extends Application {

    @Override
    public final void onCreate() {
        super.onCreate();
        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_PACKAGE_ADDED);
        f.addAction(Intent.ACTION_PACKAGE_REPLACED);
        registerReceiver(new BootReceiver(), f);
    }
}
