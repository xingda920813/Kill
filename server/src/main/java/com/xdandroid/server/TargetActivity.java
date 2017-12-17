package com.xdandroid.server;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * coreApp="true"
 * android:sharedUserId="android.uid.system"
 * android:process="system"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class TargetActivity extends Activity {

    static final Map<String, Integer> WHITE_LIST_APPS = new HashMap<>();
    static {
        WHITE_LIST_APPS.put("com.tencent.mm", 23);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(() -> {
            try {
                IPackageManager pm = (IPackageManager) ServiceManager.getService("package");
                Field packagesField = pm.getClass().getDeclaredField("mPackages");
                packagesField.setAccessible(true);
                final Field[] appInfoField = {null};
                ((ArrayMap<String, ?>) packagesField.get(pm))
                        .values()
                        .stream()
                        .map(pkg -> {
                            try {
                                if (appInfoField[0] == null) {
                                    appInfoField[0] = pkg.getClass().getDeclaredField("applicationInfo");
                                    appInfoField[0].setAccessible(true);
                                }
                                return (ApplicationInfo) appInfoField[0].get(pkg);
                            } catch (Exception e) { return null; }
                        })
                        .filter(Objects::nonNull)
                        .forEach(appInfo -> {
                            if (appInfo.targetSdkVersion >= Build.VERSION_CODES.M) appInfo.targetSdkVersion = Build.VERSION.SDK_INT;
                            if (appInfo.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) appInfo.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
                            if (WHITE_LIST_APPS.containsKey(appInfo.packageName)) appInfo.targetSdkVersion = WHITE_LIST_APPS.getOrDefault(appInfo.packageName, appInfo.targetSdkVersion);
                        });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
        finish();
    }
}
