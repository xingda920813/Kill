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

    static Field sAppInfoField;

    static ApplicationInfo mapToAppInfo(Object pkg) {
        if (sAppInfoField == null)
            for (Field f : pkg.getClass().getDeclaredFields())
                if (ApplicationInfo.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    sAppInfoField = f;
                    break;
                }
        try {
            return (ApplicationInfo) sAppInfoField.get(pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Map<String, Integer> whiteListApps = new HashMap<>();
            whiteListApps.put("com.tencent.mm", 23);
            IPackageManager.Stub pms = (IPackageManager.Stub) ServiceManager.getService("package");
            Field packagesField = pms.getClass().getDeclaredField("mPackages");
            packagesField.setAccessible(true);
            ArrayMap<String, ?> packages = (ArrayMap<String, ?>) packagesField.get(pms);
            packages.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(TargetActivity::mapToAppInfo)
                    .filter(Objects::nonNull)
                    .forEach(ai -> {
                        assert ai != null;
                        if (ai.targetSdkVersion >= Build.VERSION_CODES.M) ai.targetSdkVersion = Build.VERSION_CODES.CUR_DEVELOPMENT - 1;
                        if (ai.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) ai.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
                        ai.targetSdkVersion = whiteListApps.getOrDefault(ai.packageName, ai.targetSdkVersion);
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finish();
    }
}
