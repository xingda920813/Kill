package com.xdandroid.server;

/**
 * coreApp="true"
 * android:sharedUserId="android.uid.system"
 * android:process="system"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class DebugActivity extends BaseActivity {

    @Override
    Object getToken() {
        return "Debug";
    }
}
