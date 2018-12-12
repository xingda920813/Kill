package com.xdandroid.server;

import android.app.*;
import android.os.*;
import android.widget.*;

import com.xdandroid.lib.*;

/**
 * uses-permission android:name="android.permission.UPDATE_APP_OPS_STATS"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class RevokeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Revoke", Toast.LENGTH_SHORT).show();
        Revoke.invokeHack(this);
        finish();
    }
}
