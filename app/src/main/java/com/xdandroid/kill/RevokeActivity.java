package com.xdandroid.kill;

import android.app.*;
import android.content.*;
import android.os.*;

import com.xdandroid.lib.*;

/**
 * uses-permission android:name="android.permission.UPDATE_APP_OPS_STATS"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class RevokeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokeHack(this);
        finish();
    }

    static void invokeHack(Context c) {
        try {
            Intent i = new Intent();
            i.setComponent(new ComponentName("com.xdandroid.server", "com.xdandroid.server.RevokeActivity"));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
            return;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Revoke.invokeHack(c);
    }
}
