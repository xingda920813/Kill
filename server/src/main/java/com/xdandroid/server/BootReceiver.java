package com.xdandroid.server;

import android.annotation.*;
import android.content.*;

/**
 * uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"
 * action android:name="android.intent.action.BOOT_COMPLETED"
 */
public class BootReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(new TargetActivity()::invokeHack).start();
    }
}
