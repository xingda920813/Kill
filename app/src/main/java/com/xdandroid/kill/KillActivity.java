package com.xdandroid.kill;

import android.app.*;
import android.content.pm.*;
import android.os.*;

import java.util.*;

/**
 * uses-permission android:name="android.permission.FORCE_STOP_PACKAGES"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class KillActivity extends Activity implements Utils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setPermissive();
        getPackageManager()
                .getInstalledPackages(0)
                .stream()
                .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                .filter(i -> (i.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
                .map(i -> i.packageName)
                .filter(n -> !WHITE_LIST_APPS.contains(n))
                .forEach(Objects.requireNonNull(getSystemService(ActivityManager.class))::forceStopPackage);
        finish();
    }
}
