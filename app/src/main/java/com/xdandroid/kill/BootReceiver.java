package com.xdandroid.kill;

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
        new Thread(() -> {
            try {
                Context c = context.getApplicationContext();
                Intent i = new Intent(c, RevokeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
                i = new Intent();
                i.setComponent(new ComponentName("com.xdandroid.server", "com.xdandroid.server.TargetActivity"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
                i = new Intent();
                i.setComponent(new ComponentName("me.piebridge.brevent", "me.piebridge.brevent.ui.BreventActivity"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}
