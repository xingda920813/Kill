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
public class TargetActivity extends Activity implements Utils {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Map<String, Integer> whiteListForTarget = new HashMap<>();
            whiteListForTarget.put("com.tencent.mm", 23);
            IPackageManager.Stub pms = (IPackageManager.Stub) ServiceManager.getService("package");
            Field packagesField = pms.getClass().getDeclaredField("mPackages");
            packagesField.setAccessible(true);
            ArrayMap<String, ?> packages = (ArrayMap<String, ?>) packagesField.get(pms);
            packages.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(this::fromPkgToAppInfo)
                    .forEach(ai -> {
                        if (ai.targetSdkVersion >= Build.VERSION_CODES.M) ai.targetSdkVersion = Build.VERSION_CODES.CUR_DEVELOPMENT - 1;
                        if (ai.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) ai.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
                        ai.targetSdkVersion = whiteListForTarget.getOrDefault(ai.packageName, ai.targetSdkVersion);
                    });
            packages.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .flatMap(this::fromPkgToSrvInfo)
                    .filter(si -> (si.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                    .filter(si -> (si.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)
                    .filter(si -> !WHITE_LIST_APPS.contains(si.packageName))
                    .forEach(si -> si.flags |= ServiceInfo.FLAG_STOP_WITH_TASK);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finish();
    }
}
