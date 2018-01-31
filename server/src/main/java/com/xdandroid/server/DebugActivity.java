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
public class DebugActivity extends Activity implements Utils {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            IPackageManager.Stub pms = (IPackageManager.Stub) ServiceManager.getService("package");
            Field packagesField = pms.getClass().getDeclaredField("mPackages");
            packagesField.setAccessible(true);
            ((ArrayMap<String, ?>) packagesField
                    .get(pms))
                    .values()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(this::fromPkgToAppInfo)
                    .forEach(ai -> ai.flags |= ApplicationInfo.FLAG_DEBUGGABLE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finish();
    }
}
