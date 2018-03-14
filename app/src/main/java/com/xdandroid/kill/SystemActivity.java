package com.xdandroid.kill;

import android.app.*;
import android.os.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

/**
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class SystemActivity extends Activity implements Utils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPermissive();
        try {
            Runtime.getRuntime().exec("su -c mount -o rw,remount,rw /system").waitFor();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Runtime.getRuntime().exec("su -c rm -f /sdcard/build.prop").waitFor();
                Runtime.getRuntime().exec("su -c cp /system/build.prop /sdcard/build.prop").waitFor();
                Path path = Paths.get("/sdcard/build.prop");
                Charset charset = StandardCharsets.UTF_8;
                String content = new String(Files.readAllBytes(path), charset);
                content = content.replaceAll("control_privapp_permissions=enforce", "control_privapp_permissions=log");
                Files.write(path, content.getBytes(charset));
                Runtime.getRuntime().exec("su -c rm -f /system/build.bak").waitFor();
                Runtime.getRuntime().exec("su -c cp /system/build.prop /system/build.bak").waitFor();
                Runtime.getRuntime().exec("su -c chmod 0600 /system/build.bak").waitFor();
                Runtime.getRuntime().exec("su -c rm -f /system/build.prop").waitFor();
                Runtime.getRuntime().exec("su -c cp /sdcard/build.prop /system/build.prop").waitFor();
                Runtime.getRuntime().exec("su -c chmod 0600 /system/build.prop").waitFor();
                Runtime.getRuntime().exec("su -c rm -f /sdcard/build.prop").waitFor();
            }
            if (!getApplicationInfo().sourceDir.startsWith("/system/priv-app/")) {
                Runtime.getRuntime().exec("su -c rm -f /system/priv-app/Kill.apk").waitFor();
                Runtime.getRuntime().exec("su -c cp " + getApplicationInfo().sourceDir + " /system/priv-app/Kill.apk").waitFor();
                Runtime.getRuntime().exec("su -c chmod 0644 /system/priv-app/Kill.apk").waitFor();
                Runtime.getRuntime().exec("su -c pm uninstall " + getPackageName()).waitFor();
            }
        } catch (InterruptedException | IOException e) {
            throw asUnchecked(e);
        }
        finish();
    }
}
