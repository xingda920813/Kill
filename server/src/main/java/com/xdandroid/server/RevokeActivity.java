package com.xdandroid.server;

import android.app.*;
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
        Revoke.invokeHack(this);
        finish();
    }
}
