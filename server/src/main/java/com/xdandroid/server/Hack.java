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

    @SuppressWarnings("unchecked")
    static void hackTarget() throws Throwable {
        Map<String, Integer> whiteListApps = new HashMap<>();
        whiteListApps.put("com.tencent.mm", 23);
        PackageManagerService pms = (PackageManagerService) ServiceManager.getService("package");
        Field packagesField = PackageManagerService.class.getDeclaredField("mPackages");
        packagesField.setAccessible(true);
        ArrayMap<String, PackageParser.Package> packages = (ArrayMap<String, PackageParser.Package>) packagesField.get(pms);
        packages.values()
                .stream()
                .filter(Objects::nonNull)
                .map(pkg -> pkg.applicationInfo)
                .forEach(ai -> {
                    if (ai.targetSdkVersion >= Build.VERSION_CODES.M) ai.targetSdkVersion = Build.VERSION_CODES.CUR_DEVELOPMENT - 1;
                    if (ai.targetSdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1) ai.targetSdkVersion = Build.VERSION_CODES.LOLLIPOP_MR1;
                    ai.targetSdkVersion = whiteListApps.getOrDefault(ai.packageName, ai.targetSdkVersion);
                });
    }
}
