package com.xdandroid.server;

import android.content.pm.*;
import android.os.*;
import android.util.*;

import com.android.server.pm.*;

import java.lang.reflect.*;
import java.util.*;

public class Hack {

    public static void hack(Object token) throws Throwable {
        switch (token.toString()) {
            case "Target":
                hackTarget();
                break;
        }
    }

    static final Map<String, Integer> WHITE_LIST_APPS = new HashMap<>();
    static {
        WHITE_LIST_APPS.put("com.tencent.mm", 23);
    }

    @SuppressWarnings("unchecked")
    static void hackTarget() throws Throwable {
        PackageManagerService pms = (PackageManagerService) ServiceManager.getService("package");
        Field packagesField = PackageManagerService.class.getDeclaredField("mPackages");
        packagesField.setAccessible(true);
        ArrayMap<String, PackageParser.Package> packages = (ArrayMap<String, PackageParser.Package>) packagesField.get(pms);
        packages.values()
                .stream()
                .map(pkg -> pkg.applicationInfo)
                .filter(Objects::nonNull)
                .forEach(appInfo -> {
                    if (appInfo.targetSdkVersion >= Build.VERSION_CODES.M) appInfo.targetSdkVersion = Build.VERSION.SDK_INT;
                    if (appInfo.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) appInfo.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
                    appInfo.targetSdkVersion = WHITE_LIST_APPS.getOrDefault(appInfo.packageName, appInfo.targetSdkVersion);
                });
    }
}
