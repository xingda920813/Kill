package com.xdandroid.kill;

import android.annotation.*;
import android.content.*;

import com.xdandroid.lib.*;

/**
 * uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"
 * action android:name="android.intent.action.BOOT_COMPLETED"
 */
public class BootReceiver extends BroadcastReceiver implements BootReceiverImpl {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        onReceive(context);
    }
}
