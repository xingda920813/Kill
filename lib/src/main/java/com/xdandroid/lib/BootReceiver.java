package com.xdandroid.lib;

import android.content.*;
import android.widget.*;

public final class BootReceiver extends BroadcastReceiver {

    @Override
    public final void onReceive(Context restricted, Intent intent) {
        Context c = restricted.getApplicationContext();
        Toast.makeText(c, "BootReceiver", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            try {
                Intent i = new Intent();
                i.setComponent(new ComponentName("com.xdandroid.server", "com.xdandroid.server.TargetActivity"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            } catch (Throwable e) {
                if (!(e instanceof ActivityNotFoundException)) e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                Intent i = new Intent();
                i.setComponent(new ComponentName("me.piebridge.brevent", "me.piebridge.brevent.ui.BreventActivity"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            } catch (Throwable e) {
                if (!(e instanceof ActivityNotFoundException)) e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                Revoke.invokeHack(c);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start();
    }
}
