package com.xdandroid.lib;

import android.content.*;

public interface BootReceiverImpl {

    default void onReceive(Context restricted) {
        Context c = restricted.getApplicationContext();
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