package com.xdandroid.kill;

import android.app.*;
import android.os.*;

import java.io.*;

/**
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class SystemActivity extends Activity implements Utils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setPermissive();
        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o rw,remount,rw /system"}).waitFor();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String fileName = "privapp-permissions-kill.xml";
                File sdcardFile = new File(Environment.getExternalStorageDirectory(), fileName);
                FileUtils.copyToFileOrThrow(getAssets().open(fileName), sdcardFile);
                Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f /system/etc/permissions/" + fileName}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + sdcardFile.getAbsolutePath() + " /system/etc/permissions/" + fileName}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 0644 /system/etc/permissions/" + fileName}).waitFor();
            }
            if (!getApplicationInfo().sourceDir.startsWith("/system/priv-app/")) {
                Runtime.getRuntime().exec(new String[]{"su", "-c", "rm -f /system/priv-app/Kill.apk"}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + getApplicationInfo().sourceDir + " /system/priv-app/Kill.apk"}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 0644 /system/priv-app/Kill.apk"}).waitFor();
                Runtime.getRuntime().exec(new String[]{"su", "-c", "pm uninstall " + getPackageName()}).waitFor();
            }
        } catch (InterruptedException | IOException e) {
            throw asUnchecked(e);
        }
        finish();
    }
}
