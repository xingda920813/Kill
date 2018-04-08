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
        Context c = context.getApplicationContext();
        new Thread(() -> {
            try {
                Intent i = new Intent();
                i.setComponent(new ComponentName("com.xdandroid.server", "com.xdandroid.server.TargetActivity"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
        new Thread(() -> {
            try {
                Intent i = new Intent();
                i.setComponent(new ComponentName("me.piebridge.brevent", "me.piebridge.brevent.ui.BreventActivity"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
        new Thread(new RevokeActivity()::invokeHackNoThrow).start();
    }
}
