package com.xdandroid.kill;

import android.app.*;
import android.os.*;

import com.xdandroid.lib.*;

import java.io.*;

/**
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class SystemActivity extends Activity {

    static final String XML_FILE_NAME = "privapp-permissions-kill.xml";
    static final String XML_SYSTEM_DEST = "/system/etc/permissions/" + XML_FILE_NAME;
    static final String APK_SYSTEM_DEST = "/system/priv-app/Kill.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplicationInfo().sourceDir.startsWith("/system/priv-app/")) {
            finish();
            return;
        }
        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o rw,remount,rw /system"}).waitFor();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                File f = new File(Environment.getExternalStorageDirectory(), XML_FILE_NAME);
                Utils.copyToFileOrThrow(getAssets().open(XML_FILE_NAME), f);
                Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f " + XML_SYSTEM_DEST}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + f.getAbsolutePath() + " " + XML_SYSTEM_DEST}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 0644 " + XML_SYSTEM_DEST}).waitFor();
            }
            Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f " + APK_SYSTEM_DEST}).waitFor();
            Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + getApplicationInfo().sourceDir + " " + APK_SYSTEM_DEST}).waitFor();
            Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 0644 " + APK_SYSTEM_DEST}).waitFor();
            Runtime.getRuntime().exec(new String[]{"su", "-c", "pm uninstall " + getPackageName()}).waitFor();
        } catch (InterruptedException | IOException e) {
            throw Utils.asUnchecked(e);
        }
        finish();
    }
}
